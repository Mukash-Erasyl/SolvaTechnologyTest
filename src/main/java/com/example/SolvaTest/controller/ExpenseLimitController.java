package com.example.SolvaTest.controller;

import com.example.SolvaTest.domain.ExpenseLimit;
import com.example.SolvaTest.service.ExpenseLimitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/limits")
@Api(value = "Expense Limit Controller", tags = { "Expense Limit Management" })
public class ExpenseLimitController {
    private final ExpenseLimitService limitService;

    public ExpenseLimitController(ExpenseLimitService limitService) {
        this.limitService = limitService;
    }

    @PostMapping
    @ApiOperation(value = "Set a new expense limit")
    public ResponseEntity<?> setNewLimit(@RequestBody ExpenseLimit limit) {
        try {
            ExpenseLimit savedLimit = limitService.setNewLimit(limit);
            return ResponseEntity.ok(savedLimit);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @ApiOperation(value = "Get all expense limits")
    public ResponseEntity<List<ExpenseLimit>> getAllLimits() {
        return ResponseEntity.ok(limitService.getAllLimits());
    }
}
