package io.bankingsystem.banking.repository;

import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.smartcardio.Card;
import java.util.List;
import java.util.UUID;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    List<CardEntity> findByAccountId(UUID accountId);
    void deleteByAccountId(UUID accountId);
}
