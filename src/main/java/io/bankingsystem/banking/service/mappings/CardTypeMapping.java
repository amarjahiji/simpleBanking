package io.bankingsystem.banking.service.mappings;

import io.bankingsystem.banking.model.dto.CardTypeDto;
import io.bankingsystem.banking.model.entity.CardTypeEntity;
import org.springframework.stereotype.Service;

@Service
public class CardTypeMapping {

    public CardTypeDto mapToCardTypeDto(CardTypeEntity cardType) {
        return new CardTypeDto(
                cardType.getId(),
                cardType.getCardTypeName()
        );
    }
}
