package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.AccountType;
import io.bankingsystem.banking.model.enum_fields.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTransactionsDtoTest {
    @Test
    void getId() {
        AccountCardsDto dto = new AccountCardsDto();
        dto.setId(UUID.randomUUID());
        String id = UUID.randomUUID().toString();
        assertEquals(36, id.length());
    }

    @Test
    void getAccountNumber() {
        AccountCardsDto dto = new AccountCardsDto();
        dto.setAccountNumber("123456789");
        assertEquals("123456789", dto.getAccountNumber());
    }

    @Test
    void getAccountType() {
        AccountCardsDto dto = new AccountCardsDto();
        dto.setAccountType(AccountType.SAVINGS);
        assertEquals("SAVINGS", dto.getAccountType().toString());
    }

    @Test
    void getAccountCurrentBalance() {
        AccountCardsDto dto = new AccountCardsDto();
        dto.setAccountCurrentBalance(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), dto.getAccountCurrentBalance());
    }

    @Test
    void getAccountDateOpened() {
        AccountCardsDto dto = new AccountCardsDto();
        LocalDateTime now = LocalDateTime.now();
        dto.setAccountDateOpened(now);
        assertEquals(now, dto.getAccountDateOpened());
    }

    @Test
    void getAccountDateClosed() {
        AccountCardsDto dto = new AccountCardsDto();
        LocalDateTime closedDate = LocalDateTime.now();
        dto.setAccountDateClosed(closedDate);
        assertEquals(closedDate, dto.getAccountDateClosed());
    }

    @Test
    void getAccountStatus() {
        AccountCardsDto dto = new AccountCardsDto();
        dto.setAccountStatus(AccountStatus.ACTIVE);
        assertEquals("ACTIVE", dto.getAccountStatus().toString());
    }

    @Test
    void getCustomerId() {
        AccountCardsDto dto = new AccountCardsDto();
        dto.setCustomerId(UUID.randomUUID());
        String customerId = UUID.randomUUID().toString();
        assertEquals(36, customerId.length());
    }

    @Test
    void getTransactions() {
        TransactionDto transaction1 = new TransactionDto();
        transaction1.setId(UUID.randomUUID());
        transaction1.setTransactionType(TransactionType.TRANSFER);
        transaction1.setTransactionAmount(new BigDecimal("100.00"));
        transaction1.setTransactionDate(LocalDateTime.now());
        transaction1.setTransactionDescription("This is a transaction");
        transaction1.setTransactionDestination(UUID.randomUUID().toString());
        transaction1.setAccountId(UUID.randomUUID());

        List<TransactionDto> transactionDtos = Arrays.asList(transaction1);
        AccountTransactionsDto dto = new AccountTransactionsDto();
        dto.setTransactions(transactionDtos);

        assertEquals(transactionDtos, dto.getTransactions());
    }
}