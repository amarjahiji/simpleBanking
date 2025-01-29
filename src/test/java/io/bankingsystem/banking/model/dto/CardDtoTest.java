package io.bankingsystem.banking.model.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CardDtoTest {

    @Test
    void getId() {
        CardDto card = new CardDto();
        UUID id = UUID.randomUUID();
        card.setId(id);
        assertEquals(id, card.getId());
    }

    @Test
    void getCardNumber() {
        CardDto card = new CardDto();
        String cardNumber = "1234 5678 9012 3456";
        card.setCardNumber(cardNumber);
        assertEquals(cardNumber, card.getCardNumber());
    }

    @Test
    void getCardExpiryDate() {
        CardDto card = new CardDto();
        LocalDate expiryDate = LocalDate.of(2025, 12, 31);
        card.setCardExpiryDate(expiryDate);
        assertEquals(expiryDate, card.getCardExpiryDate());
    }

    @Test
    void getCardCvv() {
        CardDto card = new CardDto();
        String cvv = "123";
        card.setCardCvv(cvv);
        assertEquals(cvv, card.getCardCvv());
    }

    @Test
    void getCardTypeId() {
        CardDto card = new CardDto();
        Integer cardTypeId = 1;
        card.setCardTypeId(cardTypeId);
        assertEquals(cardTypeId, card.getCardTypeId());
    }

    @Test
    void getAccountId() {
        CardDto card = new CardDto();
        UUID accountId = UUID.randomUUID();
        card.setAccountId(accountId);
        assertEquals(accountId, card.getAccountId());
    }
}