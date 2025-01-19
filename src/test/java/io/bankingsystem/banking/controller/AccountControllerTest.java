package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.AccountType;
import io.bankingsystem.banking.service.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountDto accountDto;
    private UUID accountId;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        accountDto = new AccountDto();
        accountDto.setCustomerId(UUID.randomUUID());
        accountDto.setAccountType(AccountType.CHECKING);
        accountDto.setAccountStatus(AccountStatus.ACTIVE);
        accountDto.setAccountCurrentBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void getAllAccounts() {
        when(accountService.getAllAccounts()).thenReturn(List.of(accountDto));
        ResponseEntity<List<AccountDto>> response = accountController.getAllAccounts();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getAccountById() {
        when(accountService.getAccountById(accountId)).thenReturn(accountDto);
        ResponseEntity<AccountDto> response = accountController.getAccountById(accountId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createAccount() {
        when(accountService.createAccount(accountDto)).thenReturn(accountDto);
        ResponseEntity<AccountDto> response = accountController.createAccount(accountDto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateAccount() {
        when(accountService.updateAccountById(accountId, accountDto)).thenReturn(accountDto);
        ResponseEntity<AccountDto> response = accountController.updateAccount(accountId, accountDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateAccountBalance() {
        ResponseEntity<Void> response = accountController.updateAccountBalance(accountId, BigDecimal.valueOf(1500));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateAccountStatus() {
        ResponseEntity<Void> response = accountController.updateAccountStatus(accountId, "CLOSED");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateAccountDateClosed() {
        ResponseEntity<Void> response = accountController.updateAccountDateClosed(accountId, LocalDateTime.now());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteAccount() {
        doNothing().when(accountService).deleteAccount(accountId);
        ResponseEntity<Void> response = accountController.deleteAccount(accountId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
