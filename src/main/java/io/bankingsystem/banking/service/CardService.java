package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CardEntity;
import io.bankingsystem.banking.model.entity.CardTypeEntity;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CardRepository;
import io.bankingsystem.banking.repository.CardTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CardTypeRepository cardTypeRepository;
    private final MappingService mappingService;
    private final ValidationService validationService;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository, CardTypeRepository cardTypeRepository, MappingService mappingService, ValidationService validationService) {
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

    @Transactional
    public void updateExpiryDate(UUID cardId, Date expiryDate) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setCardExpiryDate(expiryDate);
        Repository.save(card);
    }

    public void deleteCard(UUID id) throws Exception {
        if(!cardRepository.existsById(id)) {
            throw new Exception("Card not found");
        }
        cardRepository.deleteById(id);
    }
}
