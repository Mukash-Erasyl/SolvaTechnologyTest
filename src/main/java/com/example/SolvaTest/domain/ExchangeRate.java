package com.example.SolvaTest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String currencyPair;
    private LocalDate rateDate;
    private BigDecimal closeRate;
    private BigDecimal previousCloseRate;

    // Default constructor
    public ExchangeRate() {
    }

    // Constructor with parameters
    public ExchangeRate(LocalDate rateDate, String currencyPair, BigDecimal closeRate, BigDecimal previousCloseRate) {
        this.rateDate = rateDate;
        this.currencyPair = currencyPair;
        this.closeRate = closeRate;
        this.previousCloseRate = previousCloseRate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public LocalDate getRateDate() {
        return rateDate;
    }

    public void setRateDate(LocalDate rateDate) {
        this.rateDate = rateDate;
    }

    public BigDecimal getCloseRate() {
        return closeRate;
    }

    public void setCloseRate(BigDecimal closeRate) {
        this.closeRate = closeRate;
    }

    public BigDecimal getPreviousCloseRate() {
        return previousCloseRate;
    }

    public void setPreviousCloseRate(BigDecimal previousCloseRate) {
        this.previousCloseRate = previousCloseRate;
    }

    // toString
    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", currencyPair='" + currencyPair + '\'' +
                ", rateDate=" + rateDate +
                ", closeRate=" + closeRate +
                ", previousCloseRate=" + previousCloseRate +
                '}';
    }

    // equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(currencyPair, that.currencyPair) &&
                Objects.equals(rateDate, that.rateDate) &&
                Objects.equals(closeRate, that.closeRate) &&
                Objects.equals(previousCloseRate, that.previousCloseRate);
    }

    // hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, currencyPair, rateDate, closeRate, previousCloseRate);
    }
}
