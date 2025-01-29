package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.CardTypeDto;
import io.bankingsystem.banking.model.entity.CardTypeEntity;
import io.bankingsystem.banking.model.enum_fields.CardTypeName;
import io.bankingsystem.banking.repository.CardTypeRepository;
import io.bankingsystem.banking.service.mappings.CardTypeMapping;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardTypeTest {

    @Mock
    private CardTypeRepository cardTypeRepository;

    @Mock
    private CardTypeMapping cardTypeMapping;


    @InjectMocks
    private CardTypeService cardTypeService;

    @Test
    void getAllCards() {
        CardTypeEntity cardType1 = new CardTypeEntity();
        cardType1.setId(1);
        cardType1.setCardTypeName(CardTypeName.DEBIT_CARD);

        CardTypeEntity cardType2 = new CardTypeEntity();
        cardType2.setId(2);
        cardType2.setCardTypeName(CardTypeName.DEBIT_CARD);

        CardTypeDto cardTypeDto1 = new CardTypeDto(1,CardTypeName.DEBIT_CARD);
        CardTypeDto cardTypeDto2 = new CardTypeDto(2, CardTypeName.DEBIT_CARD);

        when(cardTypeRepository.findAll()).thenReturn(Arrays.asList(cardType1, cardType2));
        when(cardTypeMapping.mapToCardTypeDto(cardType1)).thenReturn(cardTypeDto1);
        when(cardTypeMapping.mapToCardTypeDto(cardType2)).thenReturn(cardTypeDto2);

        List<CardTypeDto> cardTypeDtos = cardTypeService.getAllCardTypes();

        assertNotNull(cardTypeDtos);
        assertEquals(2, cardTypeDtos.size());


        verify(cardTypeRepository).findAll();
        verify(cardTypeMapping).mapToCardTypeDto(cardType1);
        verify(cardTypeMapping).mapToCardTypeDto(cardType2);

    }
}