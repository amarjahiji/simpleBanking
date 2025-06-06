package io.bankingsystem.banking.service.validations;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountValidation {

    public void validateAccountDto(AccountDto accountDto) {
        if (accountDto.getAccountCurrentBalance() == null || accountDto.getAccountCurrentBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Account balance must be a positive value");
        }
        if (accountDto.getAccountType() == null || accountDto.getAccountStatus() == null) {
            throw new IllegalArgumentException("Account type and status cannot be null");
        }
        if (accountDto.getAccountNumber() == null || accountDto.getAccountNumber().trim().isEmpty() || accountDto.getAccountNumber().length() < 8 || accountDto.getAccountNumber().length() > 12) {
            throw new IllegalArgumentException("Account number format not valid");
        }
    }


    public void validateAccountStatus(String status) {
        try {
            AccountStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account status: " + status);
        }
    }

    public void validateNewBalance(BigDecimal newBalance) {
        if (newBalance == null || newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("New balance must be a positive value");
        }
    }

    public void validateDateClosed(LocalDateTime newDateClosed) {
        if (newDateClosed != null && newDateClosed.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date closed cannot be in the future");
        }
    }
}
