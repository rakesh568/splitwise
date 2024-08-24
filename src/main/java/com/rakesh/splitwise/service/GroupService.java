package com.rakesh.splitwise.service;

import com.rakesh.splitwise.dto.TransactionResponse;
import com.rakesh.splitwise.exceptions.GroupNotFoundException;
import com.rakesh.splitwise.exceptions.UserNotBelongToGroupException;
import com.rakesh.splitwise.exceptions.UserWithPhoneNotFoundException;
import com.rakesh.splitwise.exceptions.UserWithUuidNotFoundException;
import com.rakesh.splitwise.model.Group;
import com.rakesh.splitwise.model.Transaction;
import com.rakesh.splitwise.model.TransactionType;
import com.rakesh.splitwise.model.User;
import com.rakesh.splitwise.repository.GroupRepository;
import com.rakesh.splitwise.repository.TransactionRepository;
import com.rakesh.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Group createGroup(String name, List<String> userPhones) {
        Group group = new Group();
        group.setName(name);
        group.setUuid(UUID.randomUUID());

        List<User> users = userPhones.stream()
                .map(phone -> userRepository.findByPhone(phone)
                        .orElseThrow(() -> new UserWithPhoneNotFoundException(phone)))
                .toList();

        group.setUsers(new HashSet<>(users));
        return groupRepository.save(group);
    }

    public Optional<Group> getGroupByUuid(UUID uuid) {
        return groupRepository.findByUuid(uuid);
    }

    public List<TransactionResponse> getTransactionsForUserAndGroup(UUID userUuid, UUID groupId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByUserAndGroupAndDateRange(userUuid, groupId, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getExpense))
                .values().stream()
                .map(transactionList -> {
                    double sumAmount = transactionList.stream()
                            .mapToDouble(t -> t.getTransactionType().equals(TransactionType.CREDIT) ? t.getAmount() : -t.getAmount())
                            .sum();
                    Transaction firstTransaction = transactionList.get(0);
                    return new TransactionResponse(
                            firstTransaction.getCreatedAt().toLocalDate(),
                            firstTransaction.getExpense().getGroup().getName(),
                            firstTransaction.getExpense().getExpenseName(),
                            firstTransaction.getExpense().getAmount(),
                            sumAmount > 0 ? "+" + sumAmount : String.valueOf(sumAmount)

                    );
                })
                .collect(Collectors.toList());
    }

    public void settleTransactionsBetweenUsers(UUID groupUuid, UUID user1Uuid, UUID user2Uuid) {
        // Get User1 and User2 entities
        User user1 = userRepository.findByUuid(user1Uuid).orElseThrow(() -> new UserWithUuidNotFoundException(user1Uuid));
        User user2 = userRepository.findByUuid(user2Uuid).orElseThrow(() -> new UserWithUuidNotFoundException(user2Uuid));

        Group group = groupRepository.findByUuid(groupUuid).orElseThrow(() -> new GroupNotFoundException(groupUuid));

        //Check if both users belong to same group
        if (!group.getUsers().contains(user1)) {
            throw new UserNotBelongToGroupException(user1Uuid, groupUuid);
        }
        if (!group.getUsers().contains(user2)) {
            throw new UserNotBelongToGroupException(user2Uuid, groupUuid);
        }

        // Settle transactions where User1 paid for User2
        List<Transaction> transactionsUser1PaidForUser2 = transactionRepository.findByGroupAndPaidByAndUserAndUnSettled(groupUuid, user1.getId(), user2.getId());
        transactionsUser1PaidForUser2.forEach(transaction -> transaction.setSettled(true));
        transactionRepository.saveAll(transactionsUser1PaidForUser2);

        // Settle transactions where User2 paid for User1
        List<Transaction> transactionsUser2PaidForUser1 = transactionRepository.findByGroupAndPaidByAndUserAndUnSettled(groupUuid, user2.getId(), user1.getId());
        transactionsUser2PaidForUser1.forEach(transaction -> transaction.setSettled(true));
        transactionRepository.saveAll(transactionsUser2PaidForUser1);
    }


    // Other methods...
}