package com.example.SolvaTest.service;

import com.example.SolvaTest.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConcurrentExchangeRateService {

    @Autowired
    private ExchangeRateService exchangeRateService;

    public Mono<BigDecimal> calculateTotalInUSD(Transaction transaction) {
        return exchangeRateService.getExchangeRate(transaction.getCurrency(), "KZT", transaction.getTransactionDate())
                .map(rate -> transaction.getAmount().multiply(rate));
    }

    public Mono<List<BigDecimal>> calculateTotalsInUSD(List<Transaction> transactions) {
        // Создаем список Mono<BigDecimal> для каждого транзакции
        List<Mono<BigDecimal>> monos = transactions.stream()
                .map(this::calculateTotalInUSD)
                .collect(Collectors.toList());

        // Применяем Mono.zip и преобразуем результаты в List<BigDecimal>
        return Mono.zip(monos, results -> Arrays.stream(results)
                .map(result -> (BigDecimal) result) // Приведение типа
                .collect(Collectors.toList())
        );
    }

}
