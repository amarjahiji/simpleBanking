package io.bankingsystem.banking.model.entity;

import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AccountEntityTest {

    private AccountEntity account;
    private UUID testId;
    private CustomerEntity testCustomer;
    private final LocalDateTime OPEN_DATE = LocalDateTime.of(2023, 1, 1, 9, 0);
    private final LocalDateTime CLOSE_DATE = LocalDateTime.of(2024, 1, 1, 9, 0);

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testCustomer = new CustomerEntity();

        account = new AccountEntity();
        account.setId(testId);
        account.setAccountNumber("ACC-1234567890");
        account.setAccountType(AccountType.SAVINGS);
        account.setAccountCurrentBalance(new BigDecimal("1000.00"));
        account.setAccountDateOpened(OPEN_DATE);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setCustomer(testCustomer);
    }

    @Test
    @DisplayName("Entity ID management")
    void testId() {
        UUID newId = UUID.randomUUID();
        account.setId(newId);
        assertAll("ID",
                () -> assertEquals(newId, account.getId()),
                () -> assertNotEquals(testId, account.getId())
        );
    }

    @Test
    @DisplayName("Account number validation")
    void testAccountNumber() {
        account.setAccountNumber("ACC-0987654321");
        assertAll("Account Number",
                () -> assertEquals("ACC-0987654321", account.getAccountNumber()),
                () -> assertTrue(account.getAccountNumber().length() <= 20,
                        "Should respect maximum length constraint")
        );
    }

    @Test
    @DisplayName("All account type enum values")
    void testAccountTypeEnum() {
        for (AccountType type : AccountType.values()) {
            account.setAccountType(type);
            assertEquals(type, account.getAccountType(),
                    "Should handle " + type + " correctly");
        }
    }

    @Test
    @DisplayName("Balance precision handling")
    void testAccountBalance() {
        account.setAccountCurrentBalance(new BigDecimal("1234.5678"));
        assertEquals(0, new BigDecimal("1234.57").compareTo(
                        account.getAccountCurrentBalance().setScale(2, BigDecimal.ROUND_HALF_UP)),
                "Should maintain monetary precision"
        );
    }

    @Test
    @DisplayName("Date opened temporal handling")
    void testDateOpened() {
        LocalDateTime newDate = OPEN_DATE.plusMonths(6);
        account.setAccountDateOpened(newDate);
        assertEquals(newDate, account.getAccountDateOpened());
    }

    @Test
    @DisplayName("Date closed nullable field")
    void testDateClosed() {
        assertNull(account.getAccountDateClosed());
        account.setAccountDateClosed(CLOSE_DATE);
        assertEquals(CLOSE_DATE, account.getAccountDateClosed());
    }

    @Test
    @DisplayName("Account status transitions")
    void testAccountStatus() {
        account.setAccountStatus(AccountStatus.INACTIVE);
        assertEquals(AccountStatus.INACTIVE, account.getAccountStatus());
        LocalDateTime closeTime = LocalDateTime.now();
        account.setAccountStatus(AccountStatus.CLOSED);
        account.setAccountDateClosed(closeTime);

        assertAll("Closed Status",
                () -> assertEquals(AccountStatus.CLOSED, account.getAccountStatus()),
                () -> assertEquals(closeTime, account.getAccountDateClosed())
        );
    }

    @Test
    @DisplayName("Customer relationship integrity")
    void testCustomerRelationship() {
        CustomerEntity newCustomer = new CustomerEntity();
        account.setCustomer(newCustomer);

        assertAll("Customer",
                () -> assertEquals(newCustomer, account.getCustomer()),
                () -> assertNotEquals(testCustomer, account.getCustomer())
        );
    }

    @Test
    @DisplayName("All-args constructor validation")
    void testAllArgsConstructor() {
        AccountEntity fullAccount = new AccountEntity(
                testId,
                "ACC-1122334455",
                AccountType.FIXED_DEPOSIT,
                new BigDecimal("50000.00"),
                OPEN_DATE.minusDays(5),
                CLOSE_DATE.plusYears(1),
                AccountStatus.INACTIVE,
                testCustomer
        );

        assertAll("Constructor Initialization",
                () -> assertEquals(testId, fullAccount.getId()),
                () -> assertEquals(AccountType.FIXED_DEPOSIT, fullAccount.getAccountType()),
                () -> assertEquals(0, new BigDecimal("50000.00").compareTo(fullAccount.getAccountCurrentBalance())),
                () -> assertEquals(AccountStatus.INACTIVE, fullAccount.getAccountStatus()),
                () -> assertEquals(OPEN_DATE.minusDays(5), fullAccount.getAccountDateOpened()),
                () -> assertEquals(CLOSE_DATE.plusYears(1), fullAccount.getAccountDateClosed()),
                () -> assertEquals(testCustomer, fullAccount.getCustomer())
        );
    }

    @Test
    @DisplayName("Status-date consistency check")
    void testStatusDateConsistency() {
        account.setAccountStatus(AccountStatus.CLOSED);
        account.setAccountDateClosed(LocalDateTime.now().minusDays(1));

        assertAll("Closed Account Validation",
                () -> assertEquals(AccountStatus.CLOSED, account.getAccountStatus()),
                () -> assertTrue(account.getAccountDateClosed().isBefore(LocalDateTime.now()),
                        "Close date should be in the past")
        );
    }
}
