package io.bankingsystem.banking.service.mappings;

import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.model.entity.CardEntity;
import org.springframework.stereotype.Service;

@Service
public class CardMapping {

    public CardDto mapToCardDto(CardEntity card) {
        return new CardDto(
                card.getId(),
                card.getCardNumber(),
                card.getCardExpiryDate(),
                card.getCardCvv(),
                card.getCardType().getId(),
                card.getAccount().getId()
        );
    }

    public CardEntity mapToCardEntity(CardDto dto) {
        CardEntity entity = new CardEntity();
        entity.setCardNumber(dto.getCardNumber());
        entity.setCardExpiryDate(dto.getCardExpiryDate());
        entity.setCardCvv(dto.getCardCvv());
        return entity;
    }
}
