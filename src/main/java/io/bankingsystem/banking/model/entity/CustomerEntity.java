package io.bankingsystem.banking.model.entity;

import io.bankingsystem.banking.model.enum_fields.CustomerRole;
import jakarta.persistence.*;
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
@Entity
@Table(name = "customers")

public class CustomerEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "customer_first_name", length = 25, nullable = false)
    private String customerFirstName;

    @Column(name = "customer_last_name", length = 25, nullable = false)
    private String customerLastName;

    @Column(name = "customer_date_of_birth", nullable = false)
    private LocalDate customerDateOfBirth;

    @Column(name = "customer_email", length = 50, nullable = false, unique = true)
    private String customerEmail;

    @Column(name = "customer_phone_number", length = 25, nullable = false)
    private String customerPhoneNumber;

    @Column(name = "customer_address", length = 100, nullable = false)
    private String customerAddress;

    @Column(name = "customer_password", length = 60, nullable = false)
    private String customerPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_role", length = 20, nullable = false)
    private CustomerRole customerRole;
}
