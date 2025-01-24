package io.bankingsystem.banking.repository;

import io.bankingsystem.banking.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    List<CustomerEntity> findByCustomerDateOfBirthAfter(LocalDate date);
    List<CustomerEntity> findByCustomerDateOfBirthBefore(LocalDate date);
}
