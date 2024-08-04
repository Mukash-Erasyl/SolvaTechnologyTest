package com.example.SolvaTest.service;

import com.example.SolvaTest.domain.ExchangeRate;
import com.example.SolvaTest.dto.ExchangeRateResponse;
import com.example.SolvaTest.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private final WebClient webClient = WebClient.create("https://v6.exchangerate-api.com/v6/2592e4cf4c14e52df82c809e");

    public Mono<BigDecimal> getExchangeRate(String fromCurrency, String toCurrency, LocalDate date) {
        String currencyPair = fromCurrency + "/" + toCurrency;
        return Mono.fromCallable(() -> exchangeRateRepository.findByRateDateAndCurrencyPair(date, currencyPair))
                .flatMap(optionalRate -> optionalRate
                        .map(rate -> Mono.just(rate.getCloseRate()))
                        .orElseGet(() -> fetchAndSaveExchangeRate(fromCurrency, toCurrency, date, currencyPair)));
    }

    private Mono<BigDecimal> fetchAndSaveExchangeRate(String fromCurrency, String toCurrency, LocalDate date, String currencyPair) {
        return Mono.zip(
                fetchExchangeRateFromApi(fromCurrency, toCurrency),
                getPreviousExchangeRate(fromCurrency, toCurrency, date)
        ).flatMap(tuple -> {
            BigDecimal rate = tuple.getT1();
            BigDecimal previousRate = tuple.getT2();

            System.out.println("Fetched Rate: " + rate);
            System.out.println("Previous Rate: " + previousRate);

            ExchangeRate exchangeRate = new ExchangeRate(date, currencyPair, rate, previousRate);
            System.out.println("ExchangeRate to Save: " + exchangeRate);

            return Mono.fromCallable(() -> {
                ExchangeRate savedRate = exchangeRateRepository.save(exchangeRate);
                System.out.println("Saved Rate: " + savedRate);
                return savedRate;
            }).flatMap(savedRate -> Mono.just(rate));
        });
    }

    private Mono<BigDecimal> fetchExchangeRateFromApi(String fromCurrency, String toCurrency) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest/" + fromCurrency)
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRateResponse.class)
                .map(response -> {
                    Map<String, BigDecimal> rates = response.getConversionRates();
                    if (rates == null || !rates.containsKey(toCurrency)) {
                        throw new RuntimeException("Курс обмена недоступен");
                    }
                    return rates.get(toCurrency);
                });
    }

    private Mono<BigDecimal> getPreviousExchangeRate(String fromCurrency, String toCurrency, LocalDate date) {
        LocalDate previousDate = date.minusDays(1);
        String currencyPair = fromCurrency + "/" + toCurrency;
        return Mono.fromCallable(() -> exchangeRateRepository.findByRateDateAndCurrencyPair(previousDate, currencyPair))
                .map(rate -> rate.map(ExchangeRate::getCloseRate).orElse(null));
    }
}
