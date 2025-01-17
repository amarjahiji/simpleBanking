package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.CardTypeDto;
import io.bankingsystem.banking.repository.CardTypeRepository;
import io.bankingsystem.banking.service.mappings.CardTypeMapping;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardTypeService {
    private final CardTypeRepository cardTypeRepository;
    private final CardTypeMapping mappingService;

    public CardTypeService(CardTypeRepository cardTypeRepository, CardTypeMapping mappingService) {
        this.cardTypeRepository = cardTypeRepository;
        this.mappingService = mappingService;
    }

    public List<CardTypeDto> getAllCardTypes() {
        return cardTypeRepository.findAll().stream().map(mappingService::mapToCardTypeDto).collect(Collectors.toList());
    }

}