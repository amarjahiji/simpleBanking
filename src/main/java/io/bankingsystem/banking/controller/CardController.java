package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cards")
public class CardController {
   private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<List<CardDto>> getAllCards() {
        List<CardDto> cards = cardService.getAllCards();
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCardById(@PathVariable UUID id) {
        try {
            CardDto card = cardService.getCardById(id);
            return ResponseEntity.ok(card);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto cardDto) {
        try {
            CardDto createdCard = cardService.createCard(cardDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(@PathVariable UUID id, @RequestBody CardDto cardDto) {
        try {
            CardDto updatedCard = cardService.updateCardById(id, cardDto);
            return ResponseEntity.ok(updatedCard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/{id}/expirydate")
    public ResponseEntity<Void> updateAccountDateClosed(@PathVariable UUID id, @RequestParam LocalDate cardExpiryDate) {
        try {
            cardService.updateExpiryDate(id, cardExpiryDate);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) {
        try {
            cardService.deleteCard(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

