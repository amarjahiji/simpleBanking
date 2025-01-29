package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enum_fields.CustomerRole;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerAccountsCardsDtoTest {

    @Test
    void getId() {
        CustomerAccountsCardsDto customer = new CustomerAccountsCardsDto();
        UUID id = UUID.randomUUID();
        customer.setId(id);
        assertEquals(id, customer.getId());
    }

    @Test
    void getCustomerFirstName() {
        CustomerAccountsCardsDto customer = new CustomerAccountsCardsDto();
        customer.setCustomerFirstName("John");
        assertEquals("John", customer.getCustomerFirstName());
    }

    @Test
    void getCustomerLastName() {
        CustomerAccountsCardsDto customer = new CustomerAccountsCardsDto();
        customer.setCustomerLastName("Doe");
        assertEquals("Doe", customer.getCustomerLastName());
    }

    @Test
    void getCustomerEmail() {
        CustomerAccountsCardsDto customer = new CustomerAccountsCardsDto();
        customer.setCustomerEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", customer.getCustomerEmail());
    }

    @Test
    void getCustomerPhoneNumber() {
        CustomerAccountsCardsDto customer = new CustomerAccountsCardsDto();
        customer.setCustomerPhoneNumber("123-456-7890");
        assertEquals("123-456-7890", customer.getCustomerPhoneNumber());
    }

    @Test
    void getCustomerAddress() {
        CustomerAccountsCardsDto customer = new CustomerAccountsCardsDto();
        customer.setCustomerAddress("123 Main St, Springfield, USA");
        assertEquals("123 Main St, Springfield, USA", customer.getCustomerAddress());
    }

    @Test
    void getCustomerRole() {
        CustomerAccountsCardsDto customer = new CustomerAccountsCardsDto();
        customer.setCustomerRole(CustomerRole.ADMIN);
        assertEquals(CustomerRole.ADMIN, customer.getCustomerRole());
    }

    @Test
    void getAccounts() {
        AccountCardsDto account1 = new AccountCardsDto();
        List<AccountCardsDto> accounts = Arrays.asList(account1);
        CustomerAccountsCardsDto customer = new CustomerAccountsCardsDto();
        customer.setAccounts(accounts);
        assertEquals(accounts, customer.getAccounts());
    }
}