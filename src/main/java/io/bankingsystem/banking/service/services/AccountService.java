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
    private final AccountMapping mappingService;
    private final AccountValidation validationService;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, AccountMapping mappingService, AccountValidation validationService, CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.mappingService = mappingService;
        this.validationService = validationService;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }


    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(mappingService::mapToAccountDto).collect(Collectors.toList());
    }

    public AccountDto getAccountById(UUID id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id: " + id + " not found"));
        return mappingService.mapToAccountDto(account);
    }

    public List<AccountCardsDto> getAccountsWithCards() {
        List<AccountEntity> accounts = accountRepository.findAll();
        return accounts.stream().map(account -> {
            List<CardDto> cards = cardRepository.findByAccountId(account.getId())
                    .stream()
                    .map(card -> new CardDto(
                            card.getId(),
                            card.getCardNumber(),
                            card.getCardExpiryDate(),
                            card.getCardCvv(),
                            card.getCardType().getId(),
                            card.getAccount().getId()
                    ))
                    .toList();

            return new AccountCardsDto(
                    account.getId(),
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getAccountCurrentBalance(),
                    account.getAccountDateOpened(),
                    account.getAccountDateClosed(),
                    account.getAccountStatus(),
                    account.getCustomer().getId(),
                    cards
            );
        }).collect(Collectors.toList());
    }

    public AccountCardsDto getAccountWithCardsById(UUID accountId) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        List<CardDto> cards = cardRepository.findByAccountId(accountId)
                .stream()
                .map(card -> new CardDto(
                        card.getId(),
                        card.getCardNumber(),
                        card.getCardExpiryDate(),
                        card.getCardCvv(),
                        card.getCardType().getId(),
                        card.getAccount().getId()
                ))
                .toList();

        return new AccountCardsDto(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getAccountCurrentBalance(),
                account.getAccountDateOpened(),
                account.getAccountDateClosed(),
                account.getAccountStatus(),
                account.getCustomer().getId(),
                cards
        );
    }

    public List<AccountTransactionsDto> getAccountsWithTransactions() {
        List<AccountEntity> accounts = accountRepository.findAll();
        return accounts.stream().map(account -> {
            List<TransactionDto> transactions = transactionRepository.findByAccountId(account.getId())
                    .stream()
                    .map(transaction -> new TransactionDto(
                            transaction.getId(),
                            transaction.getTransactionType(),
                            transaction.getTransactionAmount(),
                            transaction.getTransactionDate(),
                            transaction.getTransactionDescription(),
                            transaction.getTransactionDestination(),
                            transaction.getAccount().getId()
                    ))
                    .toList();

            return new AccountTransactionsDto(
                    account.getId(),
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getAccountCurrentBalance(),
                    account.getAccountDateOpened(),
                    account.getAccountDateClosed(),
                    account.getAccountStatus(),
                    account.getCustomer().getId(),
                    transactions
            );
        }).collect(Collectors.toList());
    }

    public AccountTransactionsDto getAccountWithTransactionsById(UUID accountId) {
        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        List<TransactionDto> transactions = transactionRepository.findByAccountId(accountId)
                .stream()
                .map(transaction -> new TransactionDto(
                        transaction.getId(),
                        transaction.getTransactionType(),
                        transaction.getTransactionAmount(),
                        transaction.getTransactionDate(),
                        transaction.getTransactionDescription(),
                        transaction.getTransactionDestination(),
                        transaction.getAccount().getId()
                ))
                .toList();

        return new AccountTransactionsDto(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getAccountCurrentBalance(),
                account.getAccountDateOpened(),
                account.getAccountDateClosed(),
                account.getAccountStatus(),
                account.getCustomer().getId(),
                transactions
        );
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
