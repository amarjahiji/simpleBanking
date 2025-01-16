package io.bankingsystem.banking.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CardDto {
    private UUID id;
    private String cardNumber;
    private LocalDate cardExpiryDate;
    private String cardCvv;
    private Integer cardTypeId;
    private UUID accountId;
}
