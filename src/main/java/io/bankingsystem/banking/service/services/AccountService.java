package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.*;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CardRepository;
import io.bankingsystem.banking.repository.CustomerRepository;
import io.bankingsystem.banking.repository.TransactionRepository;
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
    private final AccountMapping accountMapping;
    private final AccountValidation validationService;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, AccountMapping accountMapping, AccountValidation validationService, CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountMapping = accountMapping;
        this.validationService = validationService;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }


    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(accountMapping::mapToAccountDto).collect(Collectors.toList());
    }

    public AccountDto getAccountById(UUID id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id: " + id + " not found"));
        return accountMapping.mapToAccountDto(account);
    }

    public List<AccountCardsDto> getAccountsWithCards() {
        List<AccountEntity> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(account -> accountMapping.mapToAccountCardsDto(account, cardRepository))
                .collect(Collectors.toList());
    }

    public AccountCardsDto getAccountWithCardsById(UUID accountId) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));
        return accountMapping.mapToAccountCardsDto(account, cardRepository);
    }

    public List<AccountTransactionsDto> getAccountsWithTransactions() {
        List<AccountEntity> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(account -> accountMapping.mapToAccountTransactionsDto(account, transactionRepository))
                .collect(Collectors.toList());
    }

    public AccountTransactionsDto getAccountWithTransactionsById(UUID accountId) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + accountId));
        return accountMapping.mapToAccountTransactionsDto(account, transactionRepository);
    }

    public AccountDto createAccount(AccountDto accountDto) {
        CustomerEntity customer = customerRepository.findById(accountDto.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + accountDto.getCustomerId()));
        validationService.validateAccountDto(accountDto);
        AccountEntity account = accountMapping.mapToAccountEntity(accountDto);
        account.setCustomer(customer);
        account.setAccountDateOpened(LocalDateTime.now());
        AccountEntity savedAccount = accountRepository.save(account);
        return accountMapping.mapToAccountDto(savedAccount);
    }

    public AccountDto updateAccountById(UUID id, AccountDto accountDto) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));
        validationService.validateAccountDto(accountDto);
        account.setAccountType(accountDto.getAccountType());
        account.setAccountStatus(accountDto.getAccountStatus());
        account.setAccountCurrentBalance(accountDto.getAccountCurrentBalance());
        AccountEntity updatedAccount = accountRepository.save(account);
        return accountMapping.mapToAccountDto(updatedAccount);
    }

    @Transactional
    public AccountDto updateAccountCurrentBalance(UUID id, BigDecimal newCurrentBalance) {
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        validationService.validateNewBalance(newCurrentBalance);
        accountEntity.setAccountCurrentBalance(newCurrentBalance);
        AccountEntity savedEntity = accountRepository.save(accountEntity);
        return accountMapping.mapToAccountDto(savedEntity);
    }

    @Transactional
    public AccountDto updateAccountDateClosed(UUID id, LocalDateTime newDateClosed) {
        validationService.validateDateClosed(newDateClosed);
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        accountEntity.setAccountDateClosed(newDateClosed);
        AccountEntity savedEntity = accountRepository.save(accountEntity);
        return accountMapping.mapToAccountDto(savedEntity);
    }

    @Transactional
    public AccountDto updateAccountStatus(UUID id, String newStatus) {
        validationService.validateAccountStatus(newStatus);
        AccountEntity accountEntity = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        accountEntity.setAccountStatus(AccountStatus.valueOf(newStatus));
        AccountEntity savedEntity = accountRepository.save(accountEntity);
        return accountMapping.mapToAccountDto(savedEntity);
    }

    @Transactional
    public void deleteAccount(UUID accountId) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
        cardRepository.deleteByAccountId(accountId);
        transactionRepository.deleteByAccountId(accountId);
        accountRepository.delete(account);
    }
}
