package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.AccountDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class ValidationService {

    public void validateAccountDto(AccountDto accountDto) {
        if (accountDto.getAccountCurrentBalance() == null || accountDto.getAccountCurrentBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Account balance must be a positive value");
        }
        if (accountDto.getAccountType() == null || accountDto.getAccountStatus() == null) {
            throw new IllegalArgumentException("Account type and status cannot be null");
        }
    }
}
