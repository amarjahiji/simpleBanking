package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CardEntity;
import io.bankingsystem.banking.model.entity.CardTypeEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CardRepository;
import io.bankingsystem.banking.repository.CardTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CardTypeRepository cardTypeRepository;
    private final MappingService mappingService;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository, CardTypeRepository cardTypeRepository, MappingService mappingService) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.cardTypeRepository = cardTypeRepository;
        this.mappingService = mappingService;
    }
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream().map(mappingService::mapToCardDto).collect(Collectors.toList());
    }

    public CardDto getCardById(UUID id) throws Exception {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new Exception("Card not found"));
        return mappingService.mapToCardDto(card);
    }

    public CardDto createCard(CardDto cardDto) throws Exception {
        AccountEntity account = accountRepository.findById(cardDto.getAccountId())
                .orElseThrow(() -> new Exception("Account not found"));
        CardTypeEntity cardType = cardTypeRepository.findById(cardDto.getCardTypeId())
                .orElseThrow(() -> new Exception("Card type not found"));

        CardEntity card = mappingService.mapToCardEntity(cardDto);
        card.setAccount(account);
        card.setCardType(cardType);

        CardEntity savedCard = cardRepository.save(card);
        return mappingService.mapToCardDto(savedCard);
    }

    public CardDto updateCard(UUID id, CardDto cardDto) throws Exception {
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new Exception("Card not found"));

        card.setCardNumber(cardDto.getCardNumber());
        card.setCardExpiryDate(cardDto.getCardExpiryDate());
        card.setCardCvv(cardDto.getCardCvv());

        CardEntity updatedCard = cardRepository.save(card);
        return mappingService.mapToCardDto(updatedCard);
    }

    public void deleteCard(UUID id) throws Exception {
        if(!cardRepository.existsById(id)) {
            throw new Exception("Card not found");
        }
        cardRepository.deleteById(id);
    }
}
