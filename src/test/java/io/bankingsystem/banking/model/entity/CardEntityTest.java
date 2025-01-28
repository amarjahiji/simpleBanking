package io.bankingsystem.banking.model.entity;

import io.bankingsystem.banking.model.enum_fields.CardTypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class CardEntityTest {

    private CardEntity card;
    private UUID testId;
    private CardTypeEntity testCardType;
    private AccountEntity testAccount;
    private final LocalDate TEST_EXPIRY = LocalDate.now().plusYears(2);

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testCardType = new CardTypeEntity(1, CardTypeName.CREDIT_CARD);
        testAccount = new AccountEntity();

        card = new CardEntity();
        card.setId(testId);
        card.setCardNumber("4111111111111111");
        card.setCardExpiryDate(TEST_EXPIRY);
        card.setCardCvv("123");
        card.setCardType(testCardType);
        card.setAccount(testAccount);
    }

    @Test
    @DisplayName("Card ID management")
    void testId() {
        UUID newId = UUID.randomUUID();
        card.setId(newId);
        assertAll("ID",
                () -> assertEquals(newId, card.getId()),
                () -> assertNotEquals(testId, card.getId())
        );
    }

    @Test
    @DisplayName("Card number handling")
    void testCardNumber() {
        String newNumber = "5500000000000004";
        card.setCardNumber(newNumber);
        assertEquals(newNumber, card.getCardNumber());
    }

    @Test
    @DisplayName("Expiry date temporal handling")
    void testCardExpiryDate() {
        LocalDate newDate = TEST_EXPIRY.plusMonths(6);
        card.setCardExpiryDate(newDate);
        assertEquals(newDate, card.getCardExpiryDate());
    }

    @Test
    @DisplayName("CVV code handling")
    void testCardCvv() {
        card.setCardCvv("987");
        assertEquals("987", card.getCardCvv());
    }


    @Test
    @DisplayName("Account relationship validation")
    void testAccountRelationship() {
        AccountEntity newAccount = new AccountEntity();
        card.setAccount(newAccount);

        assertAll("Account",
                () -> assertEquals(newAccount, card.getAccount()),
                () -> assertNotEquals(testAccount, card.getAccount())
        );
    }

    @Test
    @DisplayName("All-args constructor verification")
    void testAllArgsConstructor() {
        CardEntity fullCard = new CardEntity(
                testId,
                "378282246310005",
                TEST_EXPIRY.plusYears(1),
                "456",
                testCardType,
                testAccount
        );

        assertAll("Constructor Initialization",
                () -> assertEquals(testId, fullCard.getId()),
                () -> assertEquals("378282246310005", fullCard.getCardNumber()),
                () -> assertEquals(TEST_EXPIRY.plusYears(1), fullCard.getCardExpiryDate()),
                () -> assertEquals("456", fullCard.getCardCvv()),
                () -> assertEquals(testCardType, fullCard.getCardType()),
                () -> assertEquals(testAccount, fullCard.getAccount())
        );
    }

    @Test
    @DisplayName("Expiry date edge cases")
    void testExpiryEdgeCases() {
        card.setCardExpiryDate(LocalDate.MIN);
        assertEquals(LocalDate.MIN, card.getCardExpiryDate());

        LocalDate leapDate = LocalDate.of(2024, 2, 29);
        card.setCardExpiryDate(leapDate);
        assertEquals(29, card.getCardExpiryDate().getDayOfMonth());
    }
}