package com.example.SolvaTest.repository;

import com.example.SolvaTest.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findByRateDateAndCurrencyPair(LocalDate rateDate, String currencyPair);


}
