package io.bankingsystem.banking.model.entity;

import io.bankingsystem.banking.model.enum_fields.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class TransactionEntityTest {

    private TransactionEntity transaction;
    private UUID testId;
    private AccountEntity testAccount;
    private final LocalDateTime TEST_DATE = LocalDateTime.of(2023, 10, 1, 14, 30);

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testAccount = new AccountEntity();

        transaction = new TransactionEntity();
        transaction.setId(testId);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionAmount(new BigDecimal("1500.50"));
        transaction.setTransactionDate(TEST_DATE);
        transaction.setTransactionDescription("Initial deposit");
        transaction.setTransactionDestination("External Bank");
        transaction.setAccount(testAccount);
    }

    @Test
    @DisplayName("Transaction ID management")
    void testId() {
        UUID newId = UUID.randomUUID();
        transaction.setId(newId);
        assertAll("ID",
                () -> assertEquals(newId, transaction.getId()),
                () -> assertNotEquals(testId, transaction.getId())
        );
    }

    @Test
    @DisplayName("Transaction type enum validation")
    void testTransactionType() {
        for (TransactionType type : TransactionType.values()) {
            transaction.setTransactionType(type);
            assertEquals(type, transaction.getTransactionType());
        }
    }

    @Test
    @DisplayName("Transaction amount handling")
    void testTransactionAmount() {
        transaction.setTransactionAmount(new BigDecimal("99.99"));
        assertEquals(0, new BigDecimal("99.99").compareTo(transaction.getTransactionAmount()));
    }

    @Test
    @DisplayName("Transaction date temporal handling")
    void testTransactionDate() {
        LocalDateTime newDate = TEST_DATE.plusDays(5);
        transaction.setTransactionDate(newDate);
        assertEquals(newDate, transaction.getTransactionDate());
    }

    @Test
    @DisplayName("Transaction description handling")
    void testTransactionDescription() {
        transaction.setTransactionDescription("Monthly savings");
        assertEquals("Monthly savings", transaction.getTransactionDescription());

        // Test maximum length
        String maxDesc = "A".repeat(100);
        transaction.setTransactionDescription(maxDesc);
        assertEquals(100, transaction.getTransactionDescription().length());
    }

    @Test
    @DisplayName("Transaction destination handling")
    void testTransactionDestination() {
        transaction.setTransactionDestination("Merchant Payment");
        assertEquals("Merchant Payment", transaction.getTransactionDestination());

        // Test null handling
        transaction.setTransactionDestination(null);
        assertNull(transaction.getTransactionDestination());
    }

    @Test
    @DisplayName("Account relationship validation")
    void testAccountRelationship() {
        AccountEntity newAccount = new AccountEntity();
        transaction.setAccount(newAccount);

        assertAll("Account",
                () -> assertEquals(newAccount, transaction.getAccount()),
                () -> assertNotEquals(testAccount, transaction.getAccount())
        );
    }

    @Test
    @DisplayName("All-args constructor verification")
    void testAllArgsConstructor() {
        TransactionEntity fullTransaction = new TransactionEntity(
                testId,
                TransactionType.TRANSFER,
                new BigDecimal("500.00"),
                TEST_DATE.plusHours(2),
                "Funds transfer",
                "External Account 123",
                testAccount
        );

        assertAll("Constructor Initialization",
                () -> assertEquals(testId, fullTransaction.getId()),
                () -> assertEquals(TransactionType.TRANSFER, fullTransaction.getTransactionType()),
                () -> assertEquals(0, new BigDecimal("500.00").compareTo(fullTransaction.getTransactionAmount())),
                () -> assertEquals(TEST_DATE.plusHours(2), fullTransaction.getTransactionDate()),
                () -> assertEquals("Funds transfer", fullTransaction.getTransactionDescription()),
                () -> assertEquals("External Account 123", fullTransaction.getTransactionDestination()),
                () -> assertEquals(testAccount, fullTransaction.getAccount())
        );
    }

    @Test
    @DisplayName("Zero amount handling")
    void testZeroAmount() {
        transaction.setTransactionAmount(BigDecimal.ZERO);
        assertEquals(0, BigDecimal.ZERO.compareTo(transaction.getTransactionAmount()));
    }
}