package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.model.enums.AccountStatus;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CustomerRepository;
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
    private final MappingService mappingService;
    private final ValidationService validationService;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, MappingService mappingService, ValidationService validationService) {
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

        if (AccountStatus.CLOSED.name().equals(account.getAccountStatus())) {
            throw new IllegalStateException("Cannot update balance of a closed account");
        }

        account.setAccountCurrentBalance(newBalance);
    }

    @Transactional
    public void updateDateClosed(UUID accountId, LocalDateTime newDateClosed) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + accountId));

        if (AccountStatus.CLOSED.name().equals(account.getAccountStatus())) {
            throw new IllegalStateException("Cannot update date closed of a closed account");
        }

        account.setAccountDateClosed(newDateClosed);
    }

    @Transactional
    public void updateStatus(UUID id, String status) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));

        try {
            AccountStatus accountStatus = AccountStatus.valueOf(status.toUpperCase());
            account.setAccountStatus(accountStatus);
            accountRepository.save(account);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account status: " + status);
        }
    }

    public void deleteAccount(UUID id) {
        accountRepository.findById(id).ifPresentOrElse(
                accountRepository::delete, () -> {
                    throw new EntityNotFoundException("Account not found with ID: " + id);
                });
    }
}
