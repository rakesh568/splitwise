package com.rakesh.splitwise.repository;

import com.rakesh.splitwise.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Custom query methods, if needed

    // Find all transactions by expense UUID
    List<Transaction> findByExpense_Uuid(UUID expenseUuid);

    // Find all transactions by user UUID
    List<Transaction> findByUser_Uuid(UUID userUuid);

    // Find all transactions by expense UUID and user UUID
    List<Transaction> findByExpense_UuidAndUser_Uuid(UUID expenseUuid, UUID userUuid);

    @Query("SELECT t FROM Transaction t WHERE t.user.uuid = :userUuid AND t.expense.group.uuid = :groupId AND t.createdAt BETWEEN :startDate AND :endDate")
    List<Transaction> findByUserAndGroupAndDateRange(UUID userUuid, UUID groupId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE t.expense.group.uuid = :groupUuid AND t.expense.paidBy.id = :paidById AND t.user.id = :userId AND t.isSettled = false")
    List<Transaction> findByGroupAndPaidByAndUserAndUnSettled(UUID groupUuid, Long paidById, Long userId);
}
