package com.example.SolvaTest.repository;

import com.example.SolvaTest.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByLimitExceededTrue();

    @Query(value = "SELECT SUM(t.amount_inusd) " +
            "FROM transaction t " +
            "JOIN (SELECT category, start_date, end_date, amount FROM expense_limit) e " +
            "ON t.category = e.category " +
            "WHERE t.category = :category " +
            "AND EXTRACT(MONTH FROM t.transaction_date) = :month " +
            "AND EXTRACT(YEAR FROM t.transaction_date) = :year " +
            "AND t.transaction_date BETWEEN e.start_date AND e.end_date",
            nativeQuery = true)
    BigDecimal calculateTotalExpenses(@Param("category") String category, @Param("month") int month, @Param("year") int year);
}
