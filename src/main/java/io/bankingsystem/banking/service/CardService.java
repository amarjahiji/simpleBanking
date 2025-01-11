package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.model.entity.CardEntity;
import io.bankingsystem.banking.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public CardDto mapToDto(CardEntity card) {
        return new CardDto(
                card.getId(),
                card.getCardNumber(),
                card.getCardExpiryDate(),
                card.getCardCvv(),
                card.getCardType().getId(),
                card.getAccount().getId()
        );
    }
}
