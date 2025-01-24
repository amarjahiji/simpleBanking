package io.bankingsystem.banking.model.entity;

import io.bankingsystem.banking.model.enum_fields.CardTypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTypeEntityTest {
    private CardTypeEntity cardType;
    private final Integer TEST_ID = 1;
    private final CardTypeName TEST_TYPE = CardTypeName.CREDIT_CARD;

    @BeforeEach
    void setUp() {
        cardType = new CardTypeEntity();
        cardType.setId(TEST_ID);
        cardType.setCardTypeName(TEST_TYPE);
    }

    @Test
    @DisplayName("Test ID getter and setter")
    void testId() {
        cardType.setId(2);
        assertAll("ID Handling",
                () -> assertEquals(2, cardType.getId()),
                () -> assertNotEquals(TEST_ID, cardType.getId())
        );
    }

    @Test
    @DisplayName("Test all enum values are handled")
    void testAllEnumValues() {
        for(CardTypeName type : CardTypeName.values()) {
            cardType.setCardTypeName(type);
            assertEquals(type, cardType.getCardTypeName());
        }
    }

    @Test
    @DisplayName("Test constructor initialization")
    void testConstructor() {
        CardTypeEntity constructorType = new CardTypeEntity();
        constructorType.setId(3);
        constructorType.setCardTypeName(CardTypeName.DEBIT_CARD);

        assertAll("Constructor Setup",
                () -> assertEquals(3, constructorType.getId()),
                () -> assertEquals(CardTypeName.DEBIT_CARD, constructorType.getCardTypeName())
        );
    }
}