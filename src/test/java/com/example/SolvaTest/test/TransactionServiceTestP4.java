package com.example.SolvaTest.test;

import com.example.SolvaTest.domain.ExpenseLimit;
import com.example.SolvaTest.domain.Transaction;
import com.example.SolvaTest.repository.TransactionRepository;
import com.example.SolvaTest.repository.ExpenseLimitRepository;
import com.example.SolvaTest.service.ConcurrentExchangeRateService;
import com.example.SolvaTest.service.ExpenseLimitService;
import com.example.SolvaTest.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransactionServiceTestP4 {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExpenseLimitRepository expenseLimitRepository;

    @Mock
    private ConcurrentExchangeRateService concurrentExchangeRateService;

    @Mock
    private ExpenseLimitService expenseLimitService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransactionFlags() {
        // Define limits
        ExpenseLimit limit1 = new ExpenseLimit();
        limit1.setStartDate(LocalDate.of(2022, 1, 1));
        limit1.setEndDate(LocalDate.of(2022, 1, 10));
        limit1.setAmount(new BigDecimal("1000"));
        limit1.setCategory("Food");

        ExpenseLimit limit2 = new ExpenseLimit();
        limit2.setStartDate(LocalDate.of(2022, 1, 11));
        limit2.setEndDate(null);
        limit2.setAmount(new BigDecimal("2000"));
        limit2.setCategory("Food");

        List<ExpenseLimit> limits = Arrays.asList(limit1, limit2);

        // Define transactions
        Transaction tx1 = new Transaction();
        tx1.setTransactionDate(LocalDate.of(2022, 1, 2));
        tx1.setAmount(new BigDecimal("500"));
        tx1.setCategory("Food");

        Transaction tx2 = new Transaction();
        tx2.setTransactionDate(LocalDate.of(2022, 1, 3));
        tx2.setAmount(new BigDecimal("600"));
        tx2.setCategory("Food");

        Transaction tx3 = new Transaction();
        tx3.setTransactionDate(LocalDate.of(2022, 1, 12));
        tx3.setAmount(new BigDecimal("100"));
        tx3.setCategory("Food");

        // Mocking repository and service methods
        when(expenseLimitRepository.findActiveLimits()).thenReturn(limits);
        when(concurrentExchangeRateService.calculateTotalInUSD(any(Transaction.class)))
                .thenReturn(Mono.just(new BigDecimal("500")));

        when(expenseLimitService.isLimitExceeded(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction transaction = invocation.getArgument(0);
            System.out.println("Checking limit for transaction: " + transaction);
            if (transaction.getTransactionDate().isBefore(LocalDate.of(2022, 1, 11))) {
                return Mono.just(false);
            } else {
                return Mono.just(true);
            }
        });

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Save transactions and check flags
        transactionService.saveTransaction(tx1).block();
        transactionService.saveTransaction(tx2).block();
        transactionService.saveTransaction(tx3).block();

        // Validate if the transactions have correct flags
        System.out.println("TX1 Limit Exceeded: " + tx1.isLimitExceeded());
        System.out.println("TX2 Limit Exceeded: " + tx2.isLimitExceeded());
        System.out.println("TX3 Limit Exceeded: " + tx3.isLimitExceeded());

        assertEquals(false, tx1.isLimitExceeded());
        assertEquals(false, tx2.isLimitExceeded());
        assertEquals(true, tx3.isLimitExceeded());
    }

}
