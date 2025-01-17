package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.AccountType;
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
    private AccountType accountType;
    private BigDecimal accountCurrentBalance;
    private LocalDateTime accountDateOpened;
    private LocalDateTime accountDateClosed;
    private AccountStatus accountStatus;
    private UUID customerId;
}
