package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>>getAllTransactions() {
        List<TransactionDto> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable UUID id) {
        try {
            TransactionDto transaction = transactionService.getTransactionById(id);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        try {
            TransactionDto createdTransaction = transactionService.createTransaction(transactionDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
