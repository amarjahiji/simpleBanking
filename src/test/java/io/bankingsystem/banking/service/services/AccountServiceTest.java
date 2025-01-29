package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.AccountType;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapping accountMapping;

    @Mock
    private AccountValidation validationService;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getAllAccounts() {
        AccountEntity account1 = new AccountEntity();
        account1.setId(UUID.randomUUID());
        account1.setAccountNumber("123456789");
        account1.setAccountType(AccountType.CHECKING);
        account1.setAccountCurrentBalance(new BigDecimal("1000.00"));
        account1.setAccountDateOpened(LocalDateTime.of(2023,1,1,12,30));
        account1.setAccountStatus(AccountStatus.ACTIVE);

        AccountEntity account2 = new AccountEntity();
        account2.setId(UUID.randomUUID());
        account2.setAccountNumber("987654321");
        account2.setAccountType(AccountType.SAVINGS);
        account2.setAccountCurrentBalance(new BigDecimal("1000.00"));
        account2.setAccountDateOpened(LocalDateTime.of(2023, 1,1,12,30));
        account2.setAccountStatus(AccountStatus.ACTIVE);

        AccountDto accountDto1 = new AccountDto(UUID.randomUUID(), "123456789", AccountType.CHECKING, new BigDecimal("1000.00"), LocalDateTime.of(2023,1,1,12,30), null, AccountStatus.ACTIVE, null);
        AccountDto accountDto2 = new AccountDto(UUID.randomUUID(), "987654321", AccountType.SAVINGS, new BigDecimal("1000.00"), LocalDateTime.of(2023,1,1,12,30), null, AccountStatus.ACTIVE, null);


        when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));
        when(accountMapping.mapToAccountDto(account1)).thenReturn(accountDto1);
        when(accountMapping.mapToAccountDto(account2)).thenReturn(accountDto2);


        List<AccountDto> accountDtos = accountService.getAllAccounts();


        assertNotNull(accountDtos);
        assertEquals(2, accountDtos.size());
        assertEquals("123456789", accountDtos.get(0).getAccountNumber());
        assertEquals("CHECKING", accountDtos.get(0).getAccountType().toString());
        assertEquals(new BigDecimal("1000.00") , accountDtos.get(0).getAccountCurrentBalance());
        assertEquals("987654321", accountDtos.get(1).getAccountNumber());
        assertEquals("SAVINGS", accountDtos.get(1).getAccountType().toString());
        assertEquals(new BigDecimal("1000.00"), accountDtos.get(1).getAccountCurrentBalance());


        verify(accountRepository).findAll();
        verify(accountMapping).mapToAccountDto(account1);
        verify(accountMapping).mapToAccountDto(account2);
    }

    @Test
    void getAccountById_AccountFound() {

        UUID accountId = UUID.randomUUID();
        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.CHECKING);
        account.setAccountCurrentBalance(new BigDecimal("1000.00"));
        account.setAccountDateOpened(LocalDateTime.of(2023, 1, 1, 12, 30));
        account.setAccountStatus(AccountStatus.ACTIVE);

        AccountDto accountDto = new AccountDto(accountId, "123456789", AccountType.CHECKING, new BigDecimal("1000.00"), LocalDateTime.of(2023, 1, 1, 12, 30), null, AccountStatus.ACTIVE, null);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountMapping.mapToAccountDto(account)).thenReturn(accountDto);
        AccountDto result = accountService.getAccountById(accountId);
        assertNotNull(result);
        assertEquals("123456789", result.getAccountNumber());
        assertEquals("CHECKING", result.getAccountType().toString());
        assertEquals(new BigDecimal("1000.00"), result.getAccountCurrentBalance());
        verify(accountRepository).findById(accountId);
        verify(accountMapping).mapToAccountDto(account);
    }

    @Test
    void getAccountById_AccountNotFound() {
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountById(accountId);
        });
        assertEquals("Account with id: " + accountId + " not found", thrown.getMessage());
        verify(accountRepository).findById(accountId);
    }

    @Test
    void getAccountsWithCards() {
    }

    @Test
    void createAccount_Success() {
        UUID customerId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        AccountDto accountDto = new AccountDto(accountId, "123456789", AccountType.CHECKING, new BigDecimal("1000.00"), LocalDateTime.now(), null, AccountStatus.ACTIVE, customerId);
        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.CHECKING);
        account.setAccountCurrentBalance(new BigDecimal("1000.00"));
        account.setAccountDateOpened(LocalDateTime.now());
        account.setAccountStatus(AccountStatus.ACTIVE);
        AccountDto expectedAccountDto = new AccountDto(accountId, "123456789", AccountType.CHECKING, new BigDecimal("1000.00"), LocalDateTime.now(), null, AccountStatus.ACTIVE, customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountMapping.mapToAccountEntity(accountDto)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapping.mapToAccountDto(account)).thenReturn(expectedAccountDto);
        AccountDto result = accountService.createAccount(accountDto);

        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals("123456789", result.getAccountNumber());
        assertEquals(AccountType.CHECKING, result.getAccountType());
        assertEquals(new BigDecimal("1000.00"), result.getAccountCurrentBalance());
        assertEquals(AccountStatus.ACTIVE, result.getAccountStatus());

        verify(customerRepository).findById(customerId);
        verify(validationService).validateAccountDto(accountDto);
        verify(accountRepository).save(account);
        verify(accountMapping).mapToAccountDto(account);
    }

    @Test
    void createAccount_CustomerNotFound() {
        UUID customerId = UUID.randomUUID();
        AccountDto accountDto = new AccountDto(null, "123456789", AccountType.CHECKING, new BigDecimal("1000.00"), LocalDateTime.now(), null, AccountStatus.ACTIVE, customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            accountService.createAccount(accountDto);
        });
        assertEquals("Customer not found with ID: " + customerId, thrown.getMessage());
        verify(customerRepository).findById(customerId);
        verify(validationService, never()).validateAccountDto(accountDto);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void createAccount_InvalidAccount() {
        UUID customerId = UUID.randomUUID();
        AccountDto accountDto = new AccountDto(null, "123456789", AccountType.CHECKING, new BigDecimal("-1000.00"), LocalDateTime.now(), null, AccountStatus.ACTIVE, customerId);
        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(accountDto);
        });
        assertEquals("Account balance must be a positive value", thrown.getMessage());
        verify(customerRepository).findById(customerId);
        verify(validationService).validateAccountDto(accountDto);
        verify(accountRepository, never()).save(any());
    }


    @Test
    void updateAccountById_Success() {
        UUID accountId = UUID.randomUUID();
        AccountDto accountDto = new AccountDto(accountId, "123456789", AccountType.CHECKING, new BigDecimal("1500.00"), LocalDateTime.now(), null, AccountStatus.ACTIVE, UUID.randomUUID());
        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.CHECKING);
        account.setAccountCurrentBalance(new BigDecimal("1000.00"));
        account.setAccountDateOpened(LocalDateTime.now());
        account.setAccountStatus(AccountStatus.ACTIVE);

        AccountDto expectedAccountDto = new AccountDto(accountId, "123456789", AccountType.CHECKING, new BigDecimal("1500.00"), LocalDateTime.now(), null, AccountStatus.ACTIVE, UUID.randomUUID());
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapping.mapToAccountDto(account)).thenReturn(expectedAccountDto);

        AccountDto result = accountService.updateAccountById(accountId, accountDto);
        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals("123456789", result.getAccountNumber());
        assertEquals(AccountType.CHECKING, result.getAccountType());
        assertEquals(new BigDecimal("1500.00"), result.getAccountCurrentBalance());
        assertEquals(AccountStatus.ACTIVE, result.getAccountStatus());

        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(account);
        verify(accountMapping).mapToAccountDto(account);
    }

    @Test
    void updateAccountStatus() {
        UUID accountId = UUID.randomUUID();
        String newStatus = "CLOSED";

        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.CHECKING);
        account.setAccountCurrentBalance(new BigDecimal("1000.00"));
        account.setAccountDateOpened(LocalDateTime.now());
        account.setAccountStatus(AccountStatus.ACTIVE);
        AccountDto expectedAccountDto = new AccountDto(accountId, "123456789", AccountType.CHECKING, new BigDecimal("1000.00"), LocalDateTime.now(), null, AccountStatus.CLOSED, UUID.randomUUID());

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapping.mapToAccountDto(account)).thenReturn(expectedAccountDto);

        AccountDto result = accountService.updateAccountStatus(accountId, newStatus);

        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals(newStatus, result.getAccountStatus().toString());


        verify(validationService).validateAccountStatus(newStatus);
        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(account);
        verify(accountMapping).mapToAccountDto(account);
    }


    @Test
    void deleteAccount() {
        UUID accountId = UUID.randomUUID();
        AccountEntity account = new AccountEntity();
        account.setId(accountId);
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.CHECKING);
        account.setAccountCurrentBalance(new BigDecimal("1000.00"));
        account.setAccountDateOpened(LocalDateTime.now());
        account.setAccountStatus(AccountStatus.ACTIVE);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.deleteAccount(accountId);

        verify(accountRepository).findById(accountId);
        verify(accountRepository).delete(account);
    }

    @Test
    void deleteAccount_NotFound() {
        UUID accountId = UUID.randomUUID();

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accountService.deleteAccount(accountId));

        verify(accountRepository).findById(accountId);
        verify(accountRepository, times(0)).delete(any());
    }
    }
