package com.rakesh.splitwise.strategy;

import com.rakesh.splitwise.dto.ExpenseRequest;
import com.rakesh.splitwise.exceptions.UserWithUuidNotFoundException;
import com.rakesh.splitwise.model.Expense;
import com.rakesh.splitwise.model.Transaction;
import com.rakesh.splitwise.model.TransactionType;
import com.rakesh.splitwise.model.User;
import com.rakesh.splitwise.repository.TransactionRepository;
import com.rakesh.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExactSplitStrategy implements SplitStrategy {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void apply(Expense expense, User paidByUser, ExpenseRequest expenseRequest) {
        // Create a CREDIT transaction for the paidBy user
        Transaction creditTransaction = new Transaction();
        creditTransaction.setExpense(expense);
        creditTransaction.setUser(paidByUser);
        creditTransaction.setAmount(expense.getAmount());
        creditTransaction.setTransactionType(TransactionType.CREDIT);
        creditTransaction.setSettled(true);
        transactionRepository.save(creditTransaction);

        // Create DEBIT transactions for each user based on their exact share
        for (ExpenseRequest.Share share : expenseRequest.getSplitBreakup()) {
            User user = userRepository.findByUuid(share.getUserUuid()).orElseThrow(() -> new UserWithUuidNotFoundException(share.getUserUuid()));

            Transaction debitTransaction = new Transaction();
            debitTransaction.setExpense(expense);
            debitTransaction.setUser(user);
            if (user.equals(paidByUser)) {
                debitTransaction.setSettled(true);
            }
            debitTransaction.setAmount(share.getExactShare());
            debitTransaction.setTransactionType(TransactionType.DEBIT);
            transactionRepository.save(debitTransaction);
        }
    }
}
