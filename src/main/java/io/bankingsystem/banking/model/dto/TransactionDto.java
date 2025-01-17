package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enum_fields.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TransactionDto {
    private UUID id;
    private TransactionType transactionType;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    private String transactionDescription;
    private String transactionDestination;
    private UUID accountId;
}
