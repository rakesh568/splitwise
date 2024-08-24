package com.rakesh.splitwise.controller;

import com.rakesh.splitwise.dto.ExpenseRequest;
import com.rakesh.splitwise.model.Expense;
import com.rakesh.splitwise.model.Transaction;
import com.rakesh.splitwise.service.ExpenseService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<UUID> createExpense(@RequestBody ExpenseRequest expenseRequest) {
        Expense expense = expenseService.createExpense(expenseRequest);
        return ResponseEntity.ok(expense.getUuid());
    }
}