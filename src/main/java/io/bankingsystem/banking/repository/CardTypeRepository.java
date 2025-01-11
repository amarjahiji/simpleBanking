package io.bankingsystem.banking.repository;

import io.bankingsystem.banking.model.entity.CardTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTypeRepository extends JpaRepository<CardTypeEntity,Integer> {
}
