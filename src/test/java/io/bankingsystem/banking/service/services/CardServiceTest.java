package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CardEntity;
import io.bankingsystem.banking.model.entity.CardTypeEntity;
import io.bankingsystem.banking.repository.CardRepository;
import io.bankingsystem.banking.repository.CardTypeRepository;
import io.bankingsystem.banking.service.mappings.CardMapping;
import io.bankingsystem.banking.service.validations.CardValidation;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapping cardMapping;

    @Mock
    private CardValidation validationService;

    @Mock
    private CardTypeRepository cardTypeRepository;
    

    @InjectMocks
    private CardService cardService;

    @Test
    void getAllCards() {
        CardEntity card1 = new CardEntity();
        card1.setId(UUID.randomUUID());
        card1.setCardNumber("123456789");
        card1.setCardExpiryDate(LocalDate.of(2023,1,1));
        card1.setCardCvv("123");

        CardEntity card2 = new CardEntity();
        card2.setId(UUID.randomUUID());
        card2.setCardNumber("123456789");
        card2.setCardExpiryDate(LocalDate.of(2023,1,1));
        card2.setCardCvv("123");

        CardDto cardDto1 = new CardDto(UUID.randomUUID(), "123456789" ,LocalDate.of(2023,1,1) , "123", null, null);
        CardDto cardDto2 = new CardDto(UUID.randomUUID(), "123456789" ,LocalDate.of(2023,1,1) , "123", null, null);

        when(cardRepository.findAll()).thenReturn(Arrays.asList(card1, card2));
        when(cardMapping.mapToCardDto(card1)).thenReturn(cardDto1);
        when(cardMapping.mapToCardDto(card2)).thenReturn(cardDto2);

        List<CardDto> cardDtos = cardService.getAllCards();

        assertNotNull(cardDtos);
        assertEquals(2, cardDtos.size());


        verify(cardRepository).findAll();
        verify(cardMapping).mapToCardDto(card1);
        verify(cardMapping).mapToCardDto(card2);

    }

    @Test
    void getCardById() throws Exception {

        UUID cardId = UUID.randomUUID();
        CardEntity card = new CardEntity();
        card.setId(cardId);
        card.setCardNumber("123456789");
        card.setCardExpiryDate(LocalDate.of(2023,1,1));
        card.setCardCvv("123");

        CardDto cardDto = new CardDto(UUID.randomUUID(), "123456789" ,LocalDate.of(2023,1,1) , "123", null, null);



        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardMapping.mapToCardDto(card)).thenReturn(cardDto);

        CardDto result = cardService.getCardById(cardId);
        assertNotNull(result);

        verify(cardRepository).findById(cardId);
        verify(cardMapping).mapToCardDto(card);
    }


    @Test
    void createCard() {
        UUID accountId = UUID.randomUUID();
        Integer cardTypeId = 1;
        CardDto cardDto = new CardDto(UUID.randomUUID(), "1234567891234567", LocalDate.of(2025, 1, 1), "123", 1, accountId);

        AccountEntity account = new AccountEntity();
        account.setId(accountId);

        CardTypeEntity cardType = new CardTypeEntity();
        cardType.setId(cardTypeId);

        CardEntity card = new CardEntity();
        card.setId(UUID.randomUUID());
        card.setCardNumber("1234567891234567");
        card.setCardExpiryDate(LocalDate.of(2025, 1, 1));
        card.setCardCvv("123");
    }

    @Test
    void updateCardById() {
        UUID cardId = UUID.randomUUID();
        Integer cardTypeId = 1;
        UUID accountId = UUID.randomUUID();
        CardDto cardDto = new CardDto(cardId, "9876543219876543", LocalDate.of(2025, 1, 1), "321", 1, accountId);

        CardEntity existingCard = new CardEntity();
        existingCard.setId(cardId);
        existingCard.setCardNumber("1234567891234567");
        existingCard.setCardExpiryDate(LocalDate.of(2025, 1, 1));
        existingCard.setCardCvv("123");

        CardTypeEntity cardType = new CardTypeEntity();
        cardType.setId(cardTypeId);

        CardDto updatedCardDto = new CardDto(cardId, cardDto.getCardNumber(), cardDto.getCardExpiryDate(), cardDto.getCardCvv(), cardDto.getCardTypeId(), cardDto.getAccountId());

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(existingCard));
        when(cardTypeRepository.findById(cardTypeId)).thenReturn(Optional.of(cardType));
        doNothing().when(validationService).validateCardDto(cardDto);
        when(cardMapping.mapToCardDto(existingCard)).thenReturn(updatedCardDto);
        when(cardRepository.save(existingCard)).thenReturn(existingCard);

        CardDto result = cardService.updateCardById(cardId, cardDto);

        assertNotNull(result);
        assertEquals(cardDto.getCardNumber(), result.getCardNumber());
        assertEquals(cardDto.getCardCvv(), result.getCardCvv());

        verify(cardRepository).findById(cardId);
        verify(cardTypeRepository).findById(cardTypeId);
        verify(validationService).validateCardDto(cardDto);
        verify(cardRepository).save(existingCard);
        verify(cardMapping).mapToCardDto(existingCard);
    }

    @Test
    void deleteCard() {
        CardEntity card1 = new CardEntity();
        UUID cardId = UUID.randomUUID();
        card1.setId(cardId);
        card1.setCardNumber("123456789");
        card1.setCardExpiryDate(LocalDate.of(2023,1,1));
        card1.setCardCvv("123");

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card1));

        cardService.deleteCard(cardId);

        verify(cardRepository).findById(cardId);
        verify(cardRepository).delete(card1);
    }

    @Test
    void deleteCard_NotFound() {
        UUID cardId = UUID.randomUUID();

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardService.deleteCard(cardId));

        verify(cardRepository).findById(cardId);
        verify(cardRepository, times(0)).delete(any());
    }
}