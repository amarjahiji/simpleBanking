package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CardEntity;
import io.bankingsystem.banking.model.entity.CardTypeEntity;
import io.bankingsystem.banking.repository.AccountRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CardTypeRepository cardTypeRepository;
    @Mock
    private CardMapping mappingService;
    @Mock
    private CardValidation validationService;
    @InjectMocks
    private CardService cardService;

    @Test
    void getAllCards_ShouldReturnMappedCards() {
        
        List<CardEntity> cards = List.of(new CardEntity());
        CardDto mappedDto = new CardDto();
        when(cardRepository.findAll()).thenReturn(cards);
        when(mappingService.mapToCardDto(any())).thenReturn(mappedDto);

        
        List<CardDto> result = cardService.getAllCards();
        assertEquals(1, result.size());
        verify(cardRepository).findAll();
        verify(mappingService).mapToCardDto(any());
    }

    @Test
    void getCardById_WhenCardExists_ShouldReturnMappedCard() throws Exception {
        
        UUID id = UUID.randomUUID();
        CardEntity card = new CardEntity();
        CardDto mappedDto = new CardDto();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(mappingService.mapToCardDto(card)).thenReturn(mappedDto);

        
        CardDto result = cardService.getCardById(id);


        assertNotNull(result);
        verify(cardRepository).findById(id);
        verify(mappingService).mapToCardDto(card);
    }

    @Test
    void getCardById_WhenCardNotFound_ShouldThrowException() {
        
        UUID id = UUID.randomUUID();
        when(cardRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(Exception.class, () -> cardService.getCardById(id));
    }

    @Test
    void createCard_ShouldCreateAndReturnCard() {
        
        CardDto inputDto = new CardDto();
        inputDto.setAccountId(UUID.randomUUID());
        inputDto.setCardTypeId(1);

        AccountEntity account = new AccountEntity();
        CardTypeEntity cardType = new CardTypeEntity();
        CardEntity mappedEntity = new CardEntity();
        CardEntity savedEntity = new CardEntity();
        CardDto mappedDto = new CardDto();

        when(accountRepository.findById(inputDto.getAccountId())).thenReturn(Optional.of(account));
        when(cardTypeRepository.findById(inputDto.getCardTypeId())).thenReturn(Optional.of(cardType));
        when(mappingService.mapToCardEntity(inputDto)).thenReturn(mappedEntity);
        when(cardRepository.save(any())).thenReturn(savedEntity);
        when(mappingService.mapToCardDto(savedEntity)).thenReturn(mappedDto);

        
        CardDto result = cardService.createCard(inputDto);


        assertNotNull(result);
        verify(validationService).validateCardDto(inputDto);
        verify(cardRepository).save(any());
    }

    @Test
    void updateCardById_WhenCardExists_ShouldUpdateAndReturnCard() {
        
        UUID id = UUID.randomUUID();
        CardDto inputDto = new CardDto();
        inputDto.setCardTypeId(1);

        CardEntity existingCard = new CardEntity();
        CardTypeEntity cardType = new CardTypeEntity();
        CardEntity savedCard = new CardEntity();
        CardDto mappedDto = new CardDto();

        when(cardRepository.findById(id)).thenReturn(Optional.of(existingCard));
        when(cardTypeRepository.findById(inputDto.getCardTypeId())).thenReturn(Optional.of(cardType));
        when(cardRepository.save(any())).thenReturn(savedCard);
        when(mappingService.mapToCardDto(savedCard)).thenReturn(mappedDto);

        
        CardDto result = cardService.updateCardById(id, inputDto);

        assertNotNull(result);
        verify(validationService).validateCardDto(inputDto);
        verify(cardRepository).save(any());
    }

    @Test
    void updateExpiryDate_ShouldUpdateExpiryDate() {
        
        UUID id = UUID.randomUUID();
        LocalDate newDate = LocalDate.now().plusYears(2);
        CardEntity card = new CardEntity();

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));

        
        cardService.updateExpiryDate(id, newDate);

        verify(validationService).validateExpiryDate(newDate);
        verify(cardRepository).save(card);
        assertEquals(newDate, card.getCardExpiryDate());
    }

    @Test
    void deleteCard_WhenCardExists_ShouldDeleteCard() {
        
        UUID id = UUID.randomUUID();
        CardEntity card = new CardEntity();
        when(cardRepository.findById(id)).thenReturn(Optional.of(card));

        
        cardService.deleteCard(id);

        
        verify(cardRepository).delete(card);
    }

    @Test
    void deleteCard_WhenCardNotFound_ShouldThrowException() {
        
        UUID id = UUID.randomUUID();
        when(cardRepository.findById(id)).thenReturn(Optional.empty());

        
        assertThrows(EntityNotFoundException.class, () -> cardService.deleteCard(id));
    }
}