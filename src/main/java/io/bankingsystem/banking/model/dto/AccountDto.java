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

public class AccountDto {
    private UUID id;
    private String accountNumber;
    private String accountType;
    private BigDecimal accountCurrentBalance;
    private LocalDateTime accountDateOpened;
    private LocalDateTime accountDateClosed;
    private String accountStatus;
    private UUID customerId;
}
