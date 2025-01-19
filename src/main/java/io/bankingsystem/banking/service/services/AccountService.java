package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CustomerRepository;
import io.bankingsystem.banking.service.mappings.AccountMapping;
import io.bankingsystem.banking.service.validations.AccountValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountMapping mappingService;
    private final AccountValidation validationService;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, AccountMapping mappingService, AccountValidation validationService) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.mappingService = mappingService;
        this.validationService = validationService;
    }


    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(mappingService::mapToAccountDto).collect(Collectors.toList());
    }

    public AccountDto getAccountById(UUID id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id: " + id + " not found"));
        return mappingService.mapToAccountDto(account);
    }

    public AccountDto createAccount(AccountDto accountDto) {
        CustomerEntity customer = customerRepository.findById(accountDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + accountDto.getCustomerId()));
        validationService.validateAccountDto(accountDto);
        AccountEntity account = mappingService.mapToAccountEntity(accountDto);
        account.setCustomer(customer);
        account.setAccountDateOpened(LocalDateTime.now());
        AccountEntity savedAccount = accountRepository.save(account);
        return mappingService.mapToAccountDto(savedAccount);
    }

    public AccountDto updateAccountById(UUID id, AccountDto accountDto) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));
        validationService.validateAccountDto(accountDto);
        account.setAccountType(accountDto.getAccountType());
        account.setAccountStatus(accountDto.getAccountStatus());
        account.setAccountCurrentBalance(accountDto.getAccountCurrentBalance());
        AccountEntity updatedAccount = accountRepository.save(account);
        return mappingService.mapToAccountDto(updatedAccount);
    }

    @Transactional
    public void updateBalance(UUID accountId, BigDecimal newBalance) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
        validationService.validateAccountNotClosed(account);
        validationService.validateNewBalance(newBalance);
        account.setAccountCurrentBalance(newBalance);
        accountRepository.save(account);
    }

    @Transactional
    public void updateDateClosed(UUID accountId, LocalDateTime newDateClosed) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
        validationService.validateAccountNotClosed(account);
        validationService.validateDateClosed(newDateClosed);
        account.setAccountDateClosed(newDateClosed);
        accountRepository.save(account);
    }

    @Transactional
    public void updateStatus(UUID accountId, String status) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));
        validationService.validateAccountStatus(status);
        AccountStatus accountStatus = AccountStatus.valueOf(status.toUpperCase());
        account.setAccountStatus(accountStatus);
        accountRepository.save(account);
    }

    public void deleteAccount(UUID id) {
        accountRepository.findById(id).ifPresentOrElse(
                accountRepository::delete, () -> {
                    throw new EntityNotFoundException("Account not found with ID: " + id);
                });
    }
}
