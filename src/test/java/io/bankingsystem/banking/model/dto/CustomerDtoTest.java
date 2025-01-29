package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.AccountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerDtoTest {

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
}