package io.bankingsystem.banking.service.validations;

import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.TransactionType;
import io.bankingsystem.banking.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionValidation {
    private final AccountRepository accountRepository;

    public TransactionValidation(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void validateTransactionDto(TransactionDto transactionDto) {
        validateAmount(transactionDto.getTransactionAmount());
        validateType(transactionDto.getTransactionType());
        validateDate(transactionDto.getTransactionDate());
        validateSourceAccount(transactionDto.getAccountId());
        validateDescriptionAndDestination(transactionDto);

        if (transactionDto.getTransactionType() == TransactionType.TRANSFER) {
            validateTransferDestination(transactionDto.getTransactionDestination());
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
    }

    private void validateType(TransactionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Transaction type must not be null");
        }
    }

    private void validateDate(LocalDateTime date) {
        if (date != null && date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Transaction date cannot be in the future");
        }
    }

    private void validateSourceAccount(UUID accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID must not be null");
        }

        AccountEntity account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Source account not found"));

        if (account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new IllegalStateException("Source account is closed");
        }
    }

    private void validateDescriptionAndDestination(TransactionDto dto) {
        if (dto.getTransactionDescription() != null &&
                dto.getTransactionDescription().length() > 100) {
            throw new IllegalArgumentException("Description exceeds 100 characters");
        }

        if (dto.getTransactionDestination() != null &&
                dto.getTransactionDestination().length() > 100) {
            throw new IllegalArgumentException("Destination exceeds 100 characters");
        }
    }

    private void validateTransferDestination(String destinationId) {
        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transfer destination required");
        }

        try {
            UUID destId = UUID.fromString(destinationId);
            AccountEntity destAccount = accountRepository.findById(destId)
                    .orElseThrow(() -> new EntityNotFoundException("Destination account not found"));

            if (destAccount.getAccountStatus() == AccountStatus.CLOSED) {
                throw new IllegalStateException("Destination account is closed");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid destination account ID format");
        }
    }

    public void validateSufficientFunds(AccountEntity account, BigDecimal amount) {
        if (account.getAccountCurrentBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
    }
}