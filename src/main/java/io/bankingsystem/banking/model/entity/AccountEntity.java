package io.bankingsystem.banking.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")

public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "account_number", length = 20, nullable = false)
    private String accountNumber;

    @Column(name = "account_type", length = 20, nullable = false)
    private String accountType;

    @Column(name = "account_current_balance", nullable = false)
    private BigDecimal accountCurrentBalance = BigDecimal.ZERO;

    @Column(name = "account_date_opened", nullable = false)
    private Timestamp accountDateOpened = new Timestamp(System.currentTimeMillis());

    @Column(name = "account_date_closed")
    private Date accountDateClosed;

    @Column(name = "account_status", length = 20, nullable = false)
    private String accountStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;
}
