package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.model.enum_fields.TransactionType;
import io.bankingsystem.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class ValidationService {
    private final AccountRepository accountRepository;

    public ValidationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void validateAccountDto(AccountDto accountDto) {
        if (accountDto.getAccountCurrentBalance() == null || accountDto.getAccountCurrentBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Account balance must be a positive value");
        }
        if (accountDto.getAccountType() == null || accountDto.getAccountStatus() == null) {
            throw new IllegalArgumentException("Account type and status cannot be null");
        }
    }

    public void validateCardDto(CardDto cardDto) {
        if (cardDto.getCardCvv().length() != 3) {
            throw new IllegalArgumentException("Card cvv must be a 3 digit number");
        }
        if (cardDto.getCardNumber().length() != 16) {
            throw new IllegalArgumentException("Card number must be a 16 digit number");
        }
        if (cardDto.getCardExpiryDate().isBefore(LocalDate.now().plusYears(1))) {
            throw new IllegalArgumentException("Card expiry date must be at least a year after today");
        }
    }

    public void validateExpiryDate(LocalDate expiryDate) {
        if (expiryDate.isBefore(LocalDate.now().plusYears(1))) {
            throw new IllegalArgumentException("Expiry date must be in the future.");
        }
    }

    // In ValidationService.java
    public void validateTransactionDto(TransactionDto transactionDto) {
        if (transactionDto.getTransactionAmount() == null || transactionDto.getTransactionAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero.");
        }

        if (transactionDto.getTransactionType() == null) {
            throw new IllegalArgumentException("Transaction type must not be null.");
        }

        if (transactionDto.getAccountId() == null) {
            throw new IllegalArgumentException("Account ID must not be null.");
        }

        if (transactionDto.getTransactionDescription() != null && transactionDto.getTransactionDescription().length() > 100) {
            throw new IllegalArgumentException("Transaction description must not exceed 100 characters.");
        }

        if (transactionDto.getTransactionDestination() != null && transactionDto.getTransactionDestination().length() > 100) {
            throw new IllegalArgumentException("Transaction destination must not exceed 100 characters.");
        }

        if (transactionDto.getTransactionType() == TransactionType.TRANSFER) {
            if (transactionDto.getTransactionDestination() == null || transactionDto.getTransactionDestination().isEmpty()) {
                throw new IllegalArgumentException("Transaction destination must not be null or empty for TRANSFER transactions.");
            }

            boolean destinationExists = accountRepository.existsById(UUID.fromString(transactionDto.getTransactionDestination()));
            if (!destinationExists) {
                throw new IllegalArgumentException("Transaction destination account does not exist in the database.");
            }
        }
    }


}
