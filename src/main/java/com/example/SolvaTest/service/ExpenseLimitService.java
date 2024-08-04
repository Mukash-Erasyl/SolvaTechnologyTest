package com.example.SolvaTest.service;
import com.example.SolvaTest.domain.ExpenseLimit;
import com.example.SolvaTest.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.SolvaTest.repository.ExpenseLimitRepository;
import com.example.SolvaTest.repository.TransactionRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Service
public class ExpenseLimitService {

    @Autowired
    private ExpenseLimitRepository expenseLimitRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public ExpenseLimit setNewLimit(ExpenseLimit newLimit) {
        // Проверка даты начала
        if (newLimit.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }

        // Проверка существования лимита с такой же датой начала
        boolean limitExists = expenseLimitRepository.findAll().stream()
                .anyMatch(limit -> limit.getStartDate().equals(newLimit.getStartDate()));

        if (limitExists) {
            throw new IllegalArgumentException("Cannot update existing limits.");
        }

        // Установка суммы по умолчанию, если не указана
        if (newLimit.getAmount() == null) {
            newLimit.setAmount(BigDecimal.valueOf(1000)); // Устанавливаем лимит по умолчанию
        }

        return expenseLimitRepository.save(newLimit);
    }


    public Mono<Boolean> isLimitExceeded(Transaction transaction) {
        return Mono.fromCallable(() -> {
            BigDecimal totalExpenses = transactionRepository.calculateTotalExpenses(
                    transaction.getCategory(),
                    transaction.getTransactionDate().getMonthValue(),
                    transaction.getTransactionDate().getYear()
            );
            totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;

            List<ExpenseLimit> limits = expenseLimitRepository.findAll();

            BigDecimal finalTotalExpenses = totalExpenses; // Используйте финальную или эффективно финальную переменную
            return limits.stream().anyMatch(limit ->
                    transaction.getCategory().equals(limit.getCategory()) &&
                            !transaction.getTransactionDate().isBefore(limit.getStartDate()) &&
                            (limit.getEndDate() == null || !transaction.getTransactionDate().isAfter(limit.getEndDate())) &&
                            finalTotalExpenses.add(transaction.getAmountInUSD()).compareTo(limit.getAmount()) > 0
            );
        });
    }




    public List<ExpenseLimit> getAllLimits() {
        return expenseLimitRepository.findAll();
    }
}
