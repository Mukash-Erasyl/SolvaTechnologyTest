package com.example.SolvaTest.service;

import com.example.SolvaTest.domain.Transaction;
import com.example.SolvaTest.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ConcurrentExchangeRateService concurrentExchangeRateService;

    @Autowired
    private ExpenseLimitService expenseLimitService;

    @Transactional
    public Mono<Transaction> saveTransaction(Transaction transaction) {
        return concurrentExchangeRateService.calculateTotalInUSD(transaction)
                .flatMap(amountInUSD -> {
                    transaction.setAmountInUSD(amountInUSD);
                    return expenseLimitService.isLimitExceeded(transaction)
                            .map(limitExceeded -> {
                                transaction.setLimitExceeded(limitExceeded);
                                return transaction;
                            });
                })
                .flatMap(tx -> Mono.fromCallable(() -> transactionRepository.save(tx)));
    }

    public Flux<Transaction> getTransactionsExceedingLimit() {
        return Flux.fromIterable(transactionRepository.findByLimitExceededTrue());
    }

}
