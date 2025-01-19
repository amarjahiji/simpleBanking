package io.bankingsystem.banking.repository;

import io.bankingsystem.banking.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    List<AccountEntity> findByCustomerId(UUID customerId);
}
