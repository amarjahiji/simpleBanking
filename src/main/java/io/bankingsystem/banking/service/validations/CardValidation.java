package io.bankingsystem.banking.service.validations;

import io.bankingsystem.banking.model.dto.CardDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class CardValidation {

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
