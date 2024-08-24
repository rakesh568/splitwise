package com.rakesh.splitwise.service;

import com.rakesh.splitwise.dto.ExpenseRequest;
import com.rakesh.splitwise.exceptions.UserWithUuidNotFoundException;
import com.rakesh.splitwise.model.Expense;
import com.rakesh.splitwise.model.User;
import com.rakesh.splitwise.model.SplitType;
import com.rakesh.splitwise.repository.ExpenseRepository;
import com.rakesh.splitwise.repository.GroupRepository;
import com.rakesh.splitwise.repository.TransactionRepository;
import com.rakesh.splitwise.repository.UserRepository;
import com.rakesh.splitwise.strategy.EqualSplitStrategy;
import com.rakesh.splitwise.strategy.ExactSplitStrategy;
import com.rakesh.splitwise.strategy.SplitStrategy;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

import java.util.UUID;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EqualSplitStrategy equalSplitStrategy;

    @Autowired
    private ExactSplitStrategy exactSplitStrategy;

    private Map<SplitType, SplitStrategy> strategyMap;

    @PostConstruct
    public void init() {
        strategyMap = new EnumMap<>(SplitType.class);
        strategyMap.put(SplitType.EQUAL, equalSplitStrategy);
        strategyMap.put(SplitType.EXACT, exactSplitStrategy);
    }

    @Transactional
    public Expense createExpense(ExpenseRequest expenseRequest) {
        Expense expense = new Expense();
        User paidByUser = userRepository.findByUuid(expenseRequest.getPaidBy()).orElseThrow(() -> new UserWithUuidNotFoundException(expenseRequest.getPaidBy()));
        expense.setUuid(UUID.randomUUID());
        expense.setExpenseName(expenseRequest.getExpenseName());
        expense.setAmount(expenseRequest.getTotalAmount());
        expense.setGroup(groupRepository.findByUuid(expenseRequest.getGroupId()).orElseThrow(() -> new UserWithUuidNotFoundException(expenseRequest.getGroupId())));
        expense.setSplitType(SplitType.valueOf(expenseRequest.getSplitType()));
        expense.setPaidBy(paidByUser);
        expense.setCreatedBy(paidByUser);
        expenseRepository.save(expense);

        // Select and apply the split strategy
        SplitStrategy splitStrategy = strategyMap.get(expense.getSplitType());
        if (splitStrategy == null) {
            throw new UnsupportedOperationException("Unsupported split type: " + expense.getSplitType());
        }
        splitStrategy.apply(expense, paidByUser, expenseRequest);

        return expense;
    }

    public Expense getExpenseByUuid(UUID uuid) {
        return expenseRepository.findByUuid(uuid);
    }
}
