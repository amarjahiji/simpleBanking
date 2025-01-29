package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enum_fields.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDtoTest {

    @Test
    void getId() {
        TransactionDto transaction = new TransactionDto();
        UUID id = UUID.randomUUID();
        transaction.setId(id);
        assertEquals(id, transaction.getId());
    }

    @Test
    void getTransactionType() {
        TransactionDto transaction = new TransactionDto();
        transaction.setTransactionType(TransactionType.DEPOSIT);
        assertEquals(TransactionType.DEPOSIT, transaction.getTransactionType());
    }

    @Test
    void getTransactionAmount() {
        TransactionDto transaction = new TransactionDto();
        BigDecimal amount = new BigDecimal("100.50");
        transaction.setTransactionAmount(amount);
        assertEquals(amount, transaction.getTransactionAmount());
    }

    @Test
    void getTransactionDate() {
        TransactionDto transaction = new TransactionDto();
        LocalDateTime transactionDate = LocalDateTime.now();
        transaction.setTransactionDate(transactionDate);
        assertEquals(transactionDate, transaction.getTransactionDate());
    }

    @Test
    void getTransactionDescription() {
        TransactionDto transaction = new TransactionDto();
        String description = "Payment for services";
        transaction.setTransactionDescription(description);
        assertEquals(description, transaction.getTransactionDescription());
    }

    @Test
    void getTransactionDestination() {
        TransactionDto transaction = new TransactionDto();
        String destination = "Recipient Account";
        transaction.setTransactionDestination(destination);
        assertEquals(destination, transaction.getTransactionDestination());
    }

    @Test
    void getAccountId() {
        TransactionDto transaction = new TransactionDto();
        UUID accountId = UUID.randomUUID();
        transaction.setAccountId(accountId);
        assertEquals(accountId, transaction.getAccountId());
    }

}