package com.example.SolvaTest.controller;

import com.example.SolvaTest.domain.Transaction;
import com.example.SolvaTest.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transactions")
@Api(value = "Transaction Controller", tags = { "Transaction Management" })
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ApiOperation(value = "Add a new transaction")
    public Mono<ResponseEntity<Transaction>> addTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction)
                .map(tx -> ResponseEntity.ok(tx));
    }


    @GetMapping("/exceeded")
    @ApiOperation(value = "Get transactions exceeding limit")
    public Flux<Transaction> getExceededTransactions() {
        return transactionService.getTransactionsExceedingLimit();
    }
}
