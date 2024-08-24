package com.rakesh.splitwise.repository;

import com.rakesh.splitwise.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Expense findByUuid(UUID uuid);
}
