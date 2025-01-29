package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CardTypeDto;
import io.bankingsystem.banking.service.services.CardTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardTypeControllerTest {
    @Mock
    private CardTypeService cardTypeService;

    @InjectMocks
    private CardTypeController cardTypeController;

    @Test
    void getAllCardTypes() {
        List<CardTypeDto> cardTypeDtos = new ArrayList<>();
        cardTypeDtos.add(new CardTypeDto());
        when(cardTypeService.getAllCardTypes()).thenReturn(cardTypeDtos);

        List<CardTypeDto> response = cardTypeController.getAllCardTypes();

        assertNotNull(response);
        assertEquals(cardTypeDtos, response);
        verify(cardTypeService).getAllCardTypes();
    }

}