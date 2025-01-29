package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.AccountCardsDto;
import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.dto.AccountTransactionsDto;
import io.bankingsystem.banking.service.services.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void getAllAccounts() {

        List<AccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(new AccountDto());
        when(accountService.getAllAccounts()).thenReturn(accountDtos);


        ResponseEntity<List<AccountDto>> response = accountController.getAllAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDtos, response.getBody());
        verify(accountService).getAllAccounts();
    }

    @Test
    void getAccountById_Success() {
        UUID accountId = UUID.randomUUID();
        AccountDto accountDto = new AccountDto();
        when(accountService.getAccountById(accountId)).thenReturn(accountDto);

        ResponseEntity<AccountDto> response = accountController.getAccountById(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDto, response.getBody());
        verify(accountService).getAccountById(accountId);
    }

    @Test
    void getAccountById_NotFound() {
        UUID accountId = UUID.randomUUID();
        when(accountService.getAccountById(accountId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<AccountDto> response = accountController.getAccountById(accountId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService).getAccountById(accountId);
    }

    @Test
    void getAccountWithCards() {
        List<AccountCardsDto> accountCardsDtos = new ArrayList<>();
        accountCardsDtos.add(new AccountCardsDto());
        when(accountService.getAccountsWithCards()).thenReturn(accountCardsDtos);


        List<AccountCardsDto> response = accountController.getAccountWithCards();

        assertEquals(accountCardsDtos, response);
        verify(accountService).getAccountsWithCards();
    }

    @Test
    void getAccountWithCardsById_Success() {
        UUID accountId = UUID.randomUUID();
        AccountCardsDto accountCardsDto = new AccountCardsDto();
        when(accountService.getAccountWithCardsById(accountId)).thenReturn(accountCardsDto);

        ResponseEntity<AccountCardsDto> response = accountController.getAccountWithCardsById(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountCardsDto, response.getBody());
        verify(accountService).getAccountWithCardsById(accountId);
    }

    @Test
    void getAccountWithCardsById_NotFound() {
        UUID accountId = UUID.randomUUID();
        when(accountService.getAccountWithCardsById(accountId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<AccountCardsDto> response = accountController.getAccountWithCardsById(accountId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService).getAccountWithCardsById(accountId);
    }

    @Test
    void getAccountWithTransactions() {
        List<AccountTransactionsDto> accountTransactionsDtos = new ArrayList<>();
        accountTransactionsDtos.add(new AccountTransactionsDto());
        when(accountService.getAccountsWithTransactions()).thenReturn(accountTransactionsDtos);

        List<AccountTransactionsDto> response = accountController.getAccountWithTransactions();

        assertEquals(accountTransactionsDtos, response);
        verify(accountService).getAccountsWithTransactions();
    }

    @Test
    void getAccountWithTransactionsById_Success() {
        UUID accountId = UUID.randomUUID();
        AccountTransactionsDto accountTransactionsDto = new AccountTransactionsDto();
        when(accountService.getAccountWithTransactionsById(accountId)).thenReturn(accountTransactionsDto);

        ResponseEntity<AccountTransactionsDto> response = accountController.getAccountWithTransactionsById(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountTransactionsDto, response.getBody());
        verify(accountService).getAccountWithTransactionsById(accountId);
    }

    @Test
    void getAccountWithTransactionsById_NotFound() {
        UUID accountId = UUID.randomUUID();
        when(accountService.getAccountWithTransactionsById(accountId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<AccountTransactionsDto> response = accountController.getAccountWithTransactionsById(accountId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService).getAccountWithTransactionsById(accountId);
    }

    @Test
    void createAccount() {
        AccountDto accountDto = new AccountDto();
        AccountDto createdAccountDto = new AccountDto();
        when(accountService.createAccount(accountDto)).thenReturn(createdAccountDto);

        ResponseEntity<AccountDto> response = accountController.createAccount(accountDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdAccountDto, response.getBody());
        verify(accountService).createAccount(accountDto);
    }

    @Test
    void updateAccount_Success() {
        UUID accountId = UUID.randomUUID();
        AccountDto accountDto = new AccountDto();
        AccountDto updatedAccountDto = new AccountDto();
        when(accountService.updateAccountById(accountId, accountDto)).thenReturn(updatedAccountDto);

        ResponseEntity<AccountDto> response = accountController.updateAccount(accountId, accountDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAccountDto, response.getBody());
        verify(accountService).updateAccountById(accountId, accountDto);
    }

    @Test
    void updateAccount_NotFound() {
        UUID accountId = UUID.randomUUID();
        AccountDto accountDto = new AccountDto();
        when(accountService.updateAccountById(accountId, accountDto)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<AccountDto> response = accountController.updateAccount(accountId, accountDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(accountService).updateAccountById(accountId, accountDto);
    }

    @Test
    void updateAccountCurrentBalance_Success() {
        UUID accountId = UUID.randomUUID();
        BigDecimal newBalance = new BigDecimal("1000");
        AccountDto updatedAccountDto = new AccountDto();
        when(accountService.updateAccountCurrentBalance(accountId, newBalance)).thenReturn(updatedAccountDto);

        ResponseEntity<AccountDto> response = accountController.updateAccountCurrentBalance(accountId, Map.of("accountCurrentBalance", newBalance));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAccountDto, response.getBody());
        verify(accountService).updateAccountCurrentBalance(accountId, newBalance);
    }

    @Test
    void updateAccountDateClosed_Success() {
        UUID accountId = UUID.randomUUID();
        LocalDateTime newDateClosed = LocalDateTime.now();
        AccountDto updatedAccountDto = new AccountDto();
        when(accountService.updateAccountDateClosed(accountId, newDateClosed)).thenReturn(updatedAccountDto);

        ResponseEntity<AccountDto> response = accountController.updateAccountDateClosed(accountId, Map.of("accountClosedDate", newDateClosed));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAccountDto, response.getBody());
        verify(accountService).updateAccountDateClosed(accountId, newDateClosed);
    }

    @Test
    void updateAccountStatus_Success() {
        UUID accountId = UUID.randomUUID();
        String newStatus = "ACTIVE";
        AccountDto updatedAccountDto = new AccountDto();
        when(accountService.updateAccountStatus(accountId, newStatus)).thenReturn(updatedAccountDto);

        ResponseEntity<AccountDto> response = accountController.updateAccountStatus(accountId, Map.of("accountStatus", newStatus));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAccountDto, response.getBody());
        verify(accountService).updateAccountStatus(accountId, newStatus);
    }

    @Test
    void deleteAccount_Success() {
        UUID accountId = UUID.randomUUID();
        doNothing().when(accountService).deleteAccount(accountId);

        ResponseEntity<Void> response = accountController.deleteAccount(accountId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(accountService).deleteAccount(accountId);
    }

    @Test
    void deleteAccount_NotFound() {
        UUID accountId = UUID.randomUUID();
        doThrow(EntityNotFoundException.class).when(accountService).deleteAccount(accountId);

        ResponseEntity<Void> response = accountController.deleteAccount(accountId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(accountService).deleteAccount(accountId);
    }
}