package io.bankingsystem.banking.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")

public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "customer_first_name", length = 25, nullable = false)
    private String customerFirstName;

    @Column(name = "customer_last_name", length = 25, nullable = false)
    private String customerLastName;

    @Column(name = "customer_date_of_birth", nullable = false)
    private Date customerDateOfBirth;

    @Column(name = "customer_email", length = 25, nullable = false, unique = true)
    private String customerEmail;

    @Column(name = "customer_phone_number", length = 25, nullable = false)
    private String customerPhoneNumber;

    @Column(name = "customer_address", length = 50, nullable = false)
    private String customerAddress;

    @Column(name = "customer_password", length = 20, nullable = false)
    private String customerPassword;
}
