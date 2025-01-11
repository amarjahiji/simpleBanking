package io.bankingsystem.banking.model.dto;

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
    private String transactionType;
    private BigDecimal transactionAmount;
    private LocalDateTime transactionDate;
    private String transactionDescription;
    private UUID accountId;
}
