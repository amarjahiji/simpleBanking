package io.bankingsystem.banking.model.entity;

import io.bankingsystem.banking.model.enum_fields.CustomerRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class CustomerEntityTest {

    private CustomerEntity customer;
    private UUID testId;
    private final LocalDate TEST_DOB = LocalDate.of(1985, 5, 15);
    private final String TEST_PASSWORD = "$2a$12$xyz1234567890123456789012345678901234567890123456789012";

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

        customer = new CustomerEntity();
        customer.setId(testId);
        customer.setCustomerFirstName("John");
        customer.setCustomerLastName("Doe");
        customer.setCustomerDateOfBirth(TEST_DOB);
        customer.setCustomerEmail("john.doe@example.com");
        customer.setCustomerPhoneNumber("+47 123 45 678");
        customer.setCustomerAddress("123 Main Street, Oslo, Norway");
        customer.setCustomerPassword(TEST_PASSWORD);
        customer.setCustomerRole(CustomerRole.CUSTOMER);
    }

    @Test
    @DisplayName("Customer ID management")
    void testId() {
        UUID newId = UUID.randomUUID();
        customer.setId(newId);
        assertAll("ID",
                () -> assertEquals(newId, customer.getId()),
                () -> assertNotEquals(testId, customer.getId())
        );
    }

    @Test
    @DisplayName("Name field handling")
    void testNames() {
        customer.setCustomerFirstName("Alice");
        customer.setCustomerLastName("Smith");

        assertAll("Names",
                () -> assertEquals("Alice", customer.getCustomerFirstName()),
                () -> assertEquals("Smith", customer.getCustomerLastName())
        );
    }

    @Test
    @DisplayName("Date of birth validation")
    void testDateOfBirth() {
        LocalDate newDob = LocalDate.of(1990, 1, 1);
        customer.setCustomerDateOfBirth(newDob);
        assertEquals(newDob, customer.getCustomerDateOfBirth());
    }

    @Test
    @DisplayName("Contact information integrity")
    void testContactInfo() {
        customer.setCustomerEmail("alice.smith@test.com");
        customer.setCustomerPhoneNumber("+1 555-123-4567");
        customer.setCustomerAddress("456 Oak Road, Bergen");

        assertAll("Contact Info",
                () -> assertEquals("alice.smith@test.com", customer.getCustomerEmail()),
                () -> assertEquals("+1 555-123-4567", customer.getCustomerPhoneNumber()),
                () -> assertEquals("456 Oak Road, Bergen", customer.getCustomerAddress())
        );
    }

    @Test
    @DisplayName("Password storage handling")
    void testPassword() {
        String newPassword = "$2a$12$newHashedPassword123456789012345678901234567890123456";
        customer.setCustomerPassword(newPassword);
        assertEquals(newPassword, customer.getCustomerPassword());
    }

    @Test
    @DisplayName("Customer role enum validation")
    void testCustomerRole() {
        customer.setCustomerRole(CustomerRole.ADMIN);
        assertEquals(CustomerRole.ADMIN, customer.getCustomerRole());
    }

    @Test
    @DisplayName("All-args constructor verification")
    void testAllArgsConstructor() {
        CustomerEntity fullCustomer = new CustomerEntity(
                testId,
                "Jane",
                "Smith",
                LocalDate.of(1990, 6, 30),
                "jane.smith@company.com",
                "+44 7911 123456",
                "789 Pine Ave, London",
                "$2a$12$anotherHashedPwd12345678901234567890123456789012",
                CustomerRole.ADMIN
        );

        assertAll("Constructor Initialization",
                () -> assertEquals(testId, fullCustomer.getId()),
                () -> assertEquals("Jane", fullCustomer.getCustomerFirstName()),
                () -> assertEquals("Smith", fullCustomer.getCustomerLastName()),
                () -> assertEquals(LocalDate.of(1990, 6, 30), fullCustomer.getCustomerDateOfBirth()),
                () -> assertEquals("jane.smith@company.com", fullCustomer.getCustomerEmail()),
                () -> assertEquals("+44 7911 123456", fullCustomer.getCustomerPhoneNumber()),
                () -> assertEquals("789 Pine Ave, London", fullCustomer.getCustomerAddress()),
                () -> assertEquals("$2a$12$anotherHashedPwd12345678901234567890123456789012",
                        fullCustomer.getCustomerPassword()),
                () -> assertEquals(CustomerRole.ADMIN, fullCustomer.getCustomerRole())
        );
    }


    @Test
    @DisplayName("Role enum coverage")
    void testAllRoles() {
        for (CustomerRole role : CustomerRole.values()) {
            customer.setCustomerRole(role);
            assertEquals(role, customer.getCustomerRole());
        }
    }
}