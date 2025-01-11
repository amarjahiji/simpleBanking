package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.CardTypeDto;
import io.bankingsystem.banking.model.entity.CardTypeEntity;
import io.bankingsystem.banking.repository.CardTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardTypeService {
    private final CardTypeRepository cardTypeRepository;

    public CardTypeService(CardTypeRepository cardTypeRepository) {
        this.cardTypeRepository = cardTypeRepository;
    }

    public List<CardTypeDto> getAllCardTypes() {
        return cardTypeRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public CardTypeDto mapToDto(CardTypeEntity cardType){
        return new CardTypeDto(
                cardType.getId(),
                cardType.getCardTypeName()
        );
    }
}