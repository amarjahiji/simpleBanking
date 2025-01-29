package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enum_fields.CustomerRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class CustomerAccountsCardsDto {
        private UUID id;
        private String customerFirstName;
        private String customerLastName;
        private String customerEmail;
        private String customerPhoneNumber;
        private String customerAddress;
        private CustomerRole customerRole;
        private List<AccountCardsDto> accounts;
    }

