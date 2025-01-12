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
    private final MappingService mappingService;

    public CardTypeService(CardTypeRepository cardTypeRepository, MappingService mappingService) {
        this.cardTypeRepository = cardTypeRepository;
        this.mappingService = mappingService;
    }

    public List<CardTypeDto> getAllCardTypes() {
        return cardTypeRepository.findAll().stream().map(mappingService::mapToCardTypeDto).collect(Collectors.toList());
    }

    public CardTypeDto getCardTypeById(Integer id) throws Exception {
        CardTypeEntity cardType = cardTypeRepository.findById(id)
                .orElseThrow(() -> new Exception("Card type not found"));
        return mappingService.mapToCardTypeDto(cardType);
    }

    public CardTypeDto createCardType(CardTypeDto cardTypeDto) {
        CardTypeEntity cardType = mappingService.mapToCardTypeEntity(cardTypeDto);
        CardTypeEntity savedCardType = cardTypeRepository.save(cardType);
        return mappingService.mapToCardTypeDto(savedCardType);
    }

    public CardTypeDto updateCardType(Integer id, CardTypeDto cardTypeDto) throws Exception {
        CardTypeEntity cardType = cardTypeRepository.findById(id)
                .orElseThrow(() -> new Exception("Card type not found"));

        cardType.setCardTypeName(cardTypeDto.getCardTypeName());

        CardTypeEntity updatedCardType = cardTypeRepository.save(cardType);
        return mappingService.mapToCardTypeDto(updatedCardType);
    }

    public void deleteCardType(Integer id) throws Exception {
        if(!cardTypeRepository.existsById(id)) {
            throw new Exception("Card type not found");
        }
        cardTypeRepository.deleteById(id);
    }
}