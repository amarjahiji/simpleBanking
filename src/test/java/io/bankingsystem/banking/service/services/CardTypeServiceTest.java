package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.CardTypeDto;
import io.bankingsystem.banking.model.entity.CardTypeEntity;
import io.bankingsystem.banking.repository.CardTypeRepository;
import io.bankingsystem.banking.service.mappings.CardTypeMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardTypeServiceTest {

    @Mock
    private CardTypeRepository cardTypeRepository;

    @Mock
    private CardTypeMapping mappingService;

    private CardTypeService cardTypeService;

    @BeforeEach
    void setUp() {
        cardTypeService = new CardTypeService(cardTypeRepository, mappingService);
    }

    @Test
    void getAllCardTypes_ShouldReturnMappedDtos() {

        CardTypeEntity cardType1 = new CardTypeEntity();
        CardTypeEntity cardType2 = new CardTypeEntity();
        List<CardTypeEntity> cardTypes = Arrays.asList(cardType1, cardType2);

        CardTypeDto dto1 = new CardTypeDto();
        CardTypeDto dto2 = new CardTypeDto();

        when(cardTypeRepository.findAll()).thenReturn(cardTypes);
        when(mappingService.mapToCardTypeDto(cardType1)).thenReturn(dto1);
        when(mappingService.mapToCardTypeDto(cardType2)).thenReturn(dto2);


        List<CardTypeDto> result = cardTypeService.getAllCardTypes();


        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));

        verify(cardTypeRepository, times(1)).findAll();
        verify(mappingService, times(1)).mapToCardTypeDto(cardType1);
        verify(mappingService, times(1)).mapToCardTypeDto(cardType2);
    }

    @Test
    void getAllCardTypes_ShouldReturnEmptyList_WhenNoCardTypesExist() {

        when(cardTypeRepository.findAll()).thenReturn(List.of());


        List<CardTypeDto> result = cardTypeService.getAllCardTypes();


        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(cardTypeRepository, times(1)).findAll();
        verify(mappingService, never()).mapToCardTypeDto(any());
    }
}