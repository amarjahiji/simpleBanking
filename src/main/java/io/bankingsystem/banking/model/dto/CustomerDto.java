package io.bankingsystem.banking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CustomerDto {
    private UUID id;
    private String customerFirstName;
    private String customerLastName;
    private LocalDate customerDateOfBirth;
    private String customerEmail;
    private String customerPhoneNumber;
    private String customerAddress;
    private String customerPassword;

}
