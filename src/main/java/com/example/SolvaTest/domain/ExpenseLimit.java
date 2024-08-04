package com.example.SolvaTest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class ExpenseLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amount;
    private String category;

    // Default constructor
    public ExpenseLimit() {
    }

    // Parameterized constructor
    public ExpenseLimit(LocalDate startDate, LocalDate endDate, BigDecimal amount, String category) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // toString
    @Override
    public String toString() {
        return "ExpenseLimit{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                '}';
    }

    // equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseLimit that = (ExpenseLimit) o;
        return id.equals(that.id) &&
                startDate.equals(that.startDate) &&
                endDate.equals(that.endDate) &&
                amount.equals(that.amount) &&
                category.equals(that.category);
    }

    // hashCode
    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, amount, category);
    }
}
