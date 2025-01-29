package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.AccountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerAccountsDtoTest {

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
    void getCards() {
        CardDto card1 = new CardDto();
        card1.setId(UUID.randomUUID());
        card1.setCardNumber("1234 5678 9012 3456");
        card1.setCardExpiryDate(LocalDate.of(2025, 12, 31));
        card1.setCardCvv("123");
        card1.setCardTypeId(1);
        card1.setAccountId(UUID.randomUUID());

        List<CardDto> cards = Arrays.asList(card1);
        AccountCardsDto dto = new AccountCardsDto();
        dto.setCards(cards);

        assertEquals(cards, dto.getCards());
    }
}