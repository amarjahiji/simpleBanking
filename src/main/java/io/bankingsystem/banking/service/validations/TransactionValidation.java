package io.bankingsystem.banking.service.validations;

import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.model.enum_fields.TransactionType;
import io.bankingsystem.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class TransactionValidation {

    private final AccountRepository accountRepository;

    public TransactionValidation(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

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
