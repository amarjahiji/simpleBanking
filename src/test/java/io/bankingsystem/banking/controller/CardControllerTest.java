package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.service.services.CardService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {
    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @Test
    void getAllCards() {
        List<CardDto> cardDtos = new ArrayList<>();
        cardDtos.add(new CardDto());
        when(cardService.getAllCards()).thenReturn(cardDtos);

        ResponseEntity<List<CardDto>> response = cardController.getAllCards();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cardDtos, response.getBody());
        verify(cardService).getAllCards();
    }

    @Test
    void getCardById_Success() throws Exception {
        UUID cardId = UUID.randomUUID();
        CardDto cardDto = new CardDto();
        when(cardService.getCardById(cardId)).thenReturn(cardDto);

        ResponseEntity<CardDto> response = cardController.getCardById(cardId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cardDto, response.getBody());
        verify(cardService).getCardById(cardId);
    }

    @Test
    void getCardById_NotFound() throws Exception {
        UUID cardId = UUID.randomUUID();
        when(cardService.getCardById(cardId)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<CardDto> response = cardController.getCardById(cardId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(cardService).getCardById(cardId);
    }

    @Test
    void createCard() {
        CardDto cardDto = new CardDto();
        CardDto createdCardDto = new CardDto();
        when(cardService.createCard(cardDto)).thenReturn(createdCardDto);

        ResponseEntity<CardDto> response = cardController.createCard(cardDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCardDto, response.getBody());
        verify(cardService).createCard(cardDto);
    }

    @Test
    void updateCard_Success() {
        UUID cardId = UUID.randomUUID();
        CardDto cardDto = new CardDto();
        CardDto updatedCardDto = new CardDto();
        when(cardService.updateCardById(cardId, cardDto)).thenReturn(updatedCardDto);

        ResponseEntity<CardDto> response = cardController.updateCard(cardId, cardDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCardDto, response.getBody());
        verify(cardService).updateCardById(cardId, cardDto);
    }

    @Test
    void updateCard_NotFound() {
        UUID cardId = UUID.randomUUID();
        CardDto cardDto = new CardDto();
        when(cardService.updateCardById(cardId, cardDto)).thenThrow(EntityNotFoundException.class);

        ResponseEntity<CardDto> response = cardController.updateCard(cardId, cardDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(cardService).updateCardById(cardId, cardDto);
    }

    @Test
    void deleteCard_Success() {
        UUID cardId = UUID.randomUUID();
        doNothing().when(cardService).deleteCard(cardId);

        ResponseEntity<Void> response = cardController.deleteCard(cardId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(cardService).deleteCard(cardId);
    }

    @Test
    void deleteCard_NotFound() {
        UUID cardId = UUID.randomUUID();
        doThrow(EntityNotFoundException.class).when(cardService).deleteCard(cardId);

        ResponseEntity<Void> response = cardController.deleteCard(cardId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(cardService).deleteCard(cardId);
    }
}