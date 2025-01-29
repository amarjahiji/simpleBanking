package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enum_fields.CardTypeName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTypeDtoTest {
    @Test
    void getId() {
        CardTypeDto cardType = new CardTypeDto();
        cardType.setId(1);
        assertEquals(1, cardType.getId());
    }

    @Test
    void getCardTypeName() {
        CardTypeDto cardType = new CardTypeDto();
        cardType.setCardTypeName(CardTypeName.CREDIT_CARD);
        assertEquals("CREDIT_CARD", cardType.getCardTypeName().toString());
    }
}
