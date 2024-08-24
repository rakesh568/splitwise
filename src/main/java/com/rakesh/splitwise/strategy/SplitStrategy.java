package com.rakesh.splitwise.strategy;

import com.rakesh.splitwise.dto.ExpenseRequest;
import com.rakesh.splitwise.model.Expense;
import com.rakesh.splitwise.model.User;

public interface SplitStrategy {
    void apply(Expense expense, User paidByUser, ExpenseRequest expenseRequest);
}