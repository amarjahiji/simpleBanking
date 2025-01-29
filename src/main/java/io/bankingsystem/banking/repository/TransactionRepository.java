package io.bankingsystem.banking.repository;

import io.bankingsystem.banking.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findByAccountId(UUID accountId);
    void deleteByAccountId(UUID accountId);

}
