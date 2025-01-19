package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CustomerRepository;
import io.bankingsystem.banking.service.mappings.AccountMapping;
import io.bankingsystem.banking.service.validations.AccountValidation;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountMapping mappingService;

    @Mock
    private AccountValidation validationService;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getAllAccounts() {
        AccountEntity account = new AccountEntity();
        AccountDto accountDto = new AccountDto();
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(mappingService.mapToAccountDto(account)).thenReturn(accountDto);

        List<AccountDto> accounts = accountService.getAllAccounts();

        assertEquals(1, accounts.size());
        verify(accountRepository).findAll();
        verify(mappingService).mapToAccountDto(account);
    }

    @Test
    void getAccountById() {
        UUID accountId = UUID.randomUUID();
        AccountEntity account = new AccountEntity();
        AccountDto accountDto = new AccountDto();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(mappingService.mapToAccountDto(account)).thenReturn(accountDto);

        AccountDto result = accountService.getAccountById(accountId);

        assertEquals(accountDto, result);
        verify(accountRepository).findById(accountId);
        verify(mappingService).mapToAccountDto(account);
    }

    @Test
    void getAccountById_ThrowsException_WhenNotFound() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> accountService.getAccountById(accountId));

        assertEquals("Account with id: " + accountId + " not found", exception.getMessage());
    }

    @Test
    void createAccount() {
        AccountDto accountDto = new AccountDto();
        accountDto.setCustomerId(UUID.randomUUID());
        CustomerEntity customer = new CustomerEntity();
        AccountEntity account = new AccountEntity();
        AccountEntity savedAccount = new AccountEntity();
        AccountDto resultDto = new AccountDto();

        when(customerRepository.findById(accountDto.getCustomerId())).thenReturn(Optional.of(customer));
        doNothing().when(validationService).validateAccountDto(accountDto);
        when(mappingService.mapToAccountEntity(accountDto)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(savedAccount);
        when(mappingService.mapToAccountDto(savedAccount)).thenReturn(resultDto);

        AccountDto result = accountService.createAccount(accountDto);

        assertEquals(resultDto, result);
        verify(customerRepository).findById(accountDto.getCustomerId());
        verify(validationService).validateAccountDto(accountDto);
        verify(mappingService).mapToAccountEntity(accountDto);
        verify(accountRepository).save(account);
    }

    @Test
    void updateAccountById() {
        UUID accountId = UUID.randomUUID();
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountCurrentBalance(BigDecimal.TEN);
        AccountEntity account = new AccountEntity();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        doNothing().when(validationService).validateAccountDto(accountDto);
        when(accountRepository.save(account)).thenReturn(account);
        when(mappingService.mapToAccountDto(account)).thenReturn(accountDto);

        AccountDto result = accountService.updateAccountById(accountId, accountDto);

        assertEquals(accountDto, result);
        verify(accountRepository).findById(accountId);
        verify(validationService).validateAccountDto(accountDto);
        verify(accountRepository).save(account);
        verify(mappingService).mapToAccountDto(account);
    }

    @Test
    void updateBalance() {
        UUID accountId = UUID.randomUUID();
        BigDecimal newBalance = BigDecimal.TEN;
        AccountEntity account = new AccountEntity();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        doNothing().when(validationService).validateAccountNotClosed(account);
        doNothing().when(validationService).validateNewBalance(newBalance);

        accountService.updateBalance(accountId, newBalance);

        verify(accountRepository).findById(accountId);
        verify(validationService).validateAccountNotClosed(account);
        verify(validationService).validateNewBalance(newBalance);
        verify(accountRepository).save(account);
        assertEquals(newBalance, account.getAccountCurrentBalance());
    }

    @Test
    void deleteAccount() {
        UUID accountId = UUID.randomUUID();
        AccountEntity account = new AccountEntity();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.deleteAccount(accountId);

        verify(accountRepository).findById(accountId);
        verify(accountRepository).delete(account);
    }

    @Test
    void deleteAccount_ThrowsException_WhenNotFound() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> accountService.deleteAccount(accountId));

        assertEquals("Account not found with ID: " + accountId, exception.getMessage());
    }
}
