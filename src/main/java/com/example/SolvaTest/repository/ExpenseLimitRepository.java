package com.example.SolvaTest.repository;

import com.example.SolvaTest.domain.ExpenseLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseLimitRepository extends JpaRepository<ExpenseLimit, Long> {
    @Query("SELECT e FROM ExpenseLimit e WHERE e.startDate <= CURRENT_DATE AND (e.endDate IS NULL OR e.endDate >= CURRENT_DATE)")
    List<ExpenseLimit> findActiveLimits();
}
