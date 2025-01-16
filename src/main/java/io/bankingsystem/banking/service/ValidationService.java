package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.dto.CardDto;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

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

}
