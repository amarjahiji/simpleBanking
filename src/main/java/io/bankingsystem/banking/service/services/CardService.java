package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CardEntity;
import io.bankingsystem.banking.model.entity.CardTypeEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CardRepository;
import io.bankingsystem.banking.repository.CardTypeRepository;
import io.bankingsystem.banking.service.mappings.CardMapping;
import io.bankingsystem.banking.service.validations.CardValidation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardMapping mappingService;
    private final CardValidation validationService;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository, CardTypeRepository cardTypeRepository, CardMapping mappingService, CardValidation validationService) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.mappingService = mappingService;
        this.validationService = validationService;
    }
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream().map(mappingService::mapToCardDto).collect(Collectors.toList());
    }

    public CardDto getCardById(UUID id) throws Exception {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new Exception("Card not found"));
        return mappingService.mapToCardDto(card);
    }

    public CardDto createCard(CardDto cardDto) {
        AccountEntity account = accountRepository.findById(cardDto.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + cardDto.getAccountId()));
        CardTypeEntity cardType = cardTypeRepository.findById(cardDto.getCardTypeId())
                .orElseThrow(() -> new EntityNotFoundException("CardType not found with ID: " + cardDto.getCardTypeId()));
        validationService.validateCardDto(cardDto);
        CardEntity card = mappingService.mapToCardEntity(cardDto);
        card.setAccount(account);
        card.setCardType(cardType);
        CardEntity savedCard = cardRepository.save(card);
        return mappingService.mapToCardDto(savedCard);
    }

    public CardDto updateCardById(UUID id, CardDto cardDto) {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + id));
        CardTypeEntity cardType = cardTypeRepository.findById(cardDto.getCardTypeId())
                .orElseThrow(() -> new EntityNotFoundException("CardType not found with ID: " + cardDto.getCardTypeId()));
        validationService.validateCardDto(cardDto);
        card.setCardCvv(cardDto.getCardCvv());
        card.setCardNumber(cardDto.getCardNumber());
        card.setCardType(cardType);
        CardEntity updatedCard = cardRepository.save(card);
        return mappingService.mapToCardDto(updatedCard);
    }

    public void deleteCard(UUID id) {
        cardRepository.findById(id).ifPresentOrElse(
                cardRepository::delete, () -> {
                    throw new EntityNotFoundException("Card not found with ID: " + id);
                });
    }
}
