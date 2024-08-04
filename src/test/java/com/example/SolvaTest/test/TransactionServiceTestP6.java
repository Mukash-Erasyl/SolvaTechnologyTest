package com.example.SolvaTest.test;

import com.example.SolvaTest.domain.ExpenseLimit;
import com.example.SolvaTest.domain.Transaction;
import com.example.SolvaTest.repository.ExpenseLimitRepository;
import com.example.SolvaTest.repository.TransactionRepository;
import com.example.SolvaTest.service.ExpenseLimitService;
import com.example.SolvaTest.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TransactionServiceTestP6 {

    @Mock
    private ExpenseLimitRepository expenseLimitRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private ExpenseLimitService expenseLimitService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTransactionsExceedingLimit_Case1() {
        // Setup test data
        Transaction tx1 = new Transaction();
        tx1.setId(1L);
        tx1.setAmount(BigDecimal.valueOf(500));
        tx1.setCurrency("KZT");
        tx1.setTransactionDate(LocalDate.of(2022, 1, 2));
        tx1.setLimitExceeded(false);
        tx1.setAmountInUSD(BigDecimal.valueOf(500));
        tx1.setCategory("Food");

        Transaction tx2 = new Transaction();
        tx2.setId(2L);
        tx2.setAmount(BigDecimal.valueOf(600));
        tx2.setCurrency("KZT");
        tx2.setTransactionDate(LocalDate.of(2022, 1, 3));
        tx2.setLimitExceeded(true); // важно
        tx2.setAmountInUSD(BigDecimal.valueOf(600));
        tx2.setCategory("Food");

        Transaction tx3 = new Transaction();
        tx3.setId(3L);
        tx3.setAmount(BigDecimal.valueOf(100));
        tx3.setCurrency("KZT");
        tx3.setTransactionDate(LocalDate.of(2022, 1, 12));
        tx3.setLimitExceeded(false);
        tx3.setAmountInUSD(BigDecimal.valueOf(100));
        tx3.setCategory("Food");

        // Mock repository methods
        when(transactionRepository.findByLimitExceededTrue()).thenReturn(Arrays.asList(tx2));

        // Execute service method
        List<Transaction> result = transactionService.getTransactionsExceedingLimit().collectList().block();

        // Verify the result
        assertEquals(1, result.size());
        assertEquals(tx2, result.get(0));
    }
}
