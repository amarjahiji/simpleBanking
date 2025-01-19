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
import java.time.LocalDateTime;
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
    private CustomerRepository customerRepository;
    @Mock
    private AccountMapping mappingService;
    @Mock
    private AccountValidation validationService;
    @InjectMocks
    private AccountService accountService;

    @Test
    void getAllAccounts_ShouldReturnMappedAccounts() {

        List<AccountEntity> accounts = List.of(new AccountEntity());
        AccountDto mappedDto = new AccountDto();
        when(accountRepository.findAll()).thenReturn(accounts);
        when(mappingService.mapToAccountDto(any())).thenReturn(mappedDto);


        List<AccountDto> result = accountService.getAllAccounts();


        assertEquals(1, result.size());
        verify(accountRepository).findAll();
        verify(mappingService).mapToAccountDto(any());
    }

    @Test
    void getAccountById_WhenAccountExists_ShouldReturnMappedAccount() {

        UUID id = UUID.randomUUID();
        AccountEntity account = new AccountEntity();
        AccountDto mappedDto = new AccountDto();
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(mappingService.mapToAccountDto(account)).thenReturn(mappedDto);


        AccountDto result = accountService.getAccountById(id);


        assertNotNull(result);
        verify(accountRepository).findById(id);
        verify(mappingService).mapToAccountDto(account);
    }

    @Test
    void getAccountById_WhenAccountNotFound_ShouldThrowException() {

        UUID id = UUID.randomUUID();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> accountService.getAccountById(id));
    }

    @Test
    void createAccount_ShouldCreateAndReturnAccount() {

        AccountDto inputDto = new AccountDto();
        inputDto.setCustomerId(UUID.randomUUID());
        CustomerEntity customer = new CustomerEntity();
        AccountEntity mappedEntity = new AccountEntity();
        AccountEntity savedEntity = new AccountEntity();
        AccountDto mappedDto = new AccountDto();

        when(customerRepository.findById(inputDto.getCustomerId())).thenReturn(Optional.of(customer));
        when(mappingService.mapToAccountEntity(inputDto)).thenReturn(mappedEntity);
        when(accountRepository.save(any())).thenReturn(savedEntity);
        when(mappingService.mapToAccountDto(savedEntity)).thenReturn(mappedDto);


        AccountDto result = accountService.createAccount(inputDto);


        assertNotNull(result);
        verify(validationService).validateAccountDto(inputDto);
        verify(accountRepository).save(any());
    }

    @Test
    void updateAccountById_WhenAccountExists_ShouldUpdateAndReturnAccount() {

        UUID id = UUID.randomUUID();
        AccountDto inputDto = new AccountDto();
        AccountEntity existingAccount = new AccountEntity();
        AccountEntity savedAccount = new AccountEntity();
        AccountDto mappedDto = new AccountDto();

        when(accountRepository.findById(id)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any())).thenReturn(savedAccount);
        when(mappingService.mapToAccountDto(savedAccount)).thenReturn(mappedDto);


        AccountDto result = accountService.updateAccountById(id, inputDto);


        assertNotNull(result);
        verify(validationService).validateAccountDto(inputDto);
        verify(accountRepository).save(any());
    }

    @Test
    void updateBalance_ShouldUpdateBalance() {

        UUID id = UUID.randomUUID();
        BigDecimal newBalance = BigDecimal.TEN;
        AccountEntity account = new AccountEntity();

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));


        accountService.updateBalance(id, newBalance);


        verify(validationService).validateAccountNotClosed(account);
        verify(validationService).validateNewBalance(newBalance);
        verify(accountRepository).save(account);
        assertEquals(newBalance, account.getAccountCurrentBalance());
    }

    @Test
    void updateDateClosed_ShouldUpdateDateClosed() {

        UUID id = UUID.randomUUID();
        LocalDateTime newDate = LocalDateTime.now();
        AccountEntity account = new AccountEntity();

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));


        accountService.updateDateClosed(id, newDate);


        verify(validationService).validateAccountNotClosed(account);
        verify(validationService).validateDateClosed(newDate);
        verify(accountRepository).save(account);
        assertEquals(newDate, account.getAccountDateClosed());
    }
}
