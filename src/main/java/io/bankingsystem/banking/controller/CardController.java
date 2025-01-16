package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CardDto;
import io.bankingsystem.banking.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public List<CardDto> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDto> getCardById(@PathVariable UUID id) throws Exception {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody CardDto cardDto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cardService.createCard(cardDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(@PathVariable UUID id, @RequestBody CardDto cardDto) throws Exception {
        return ResponseEntity.ok(cardService.updateCard(id, cardDto));
    }

    @PutMapping("/{id}/date")
    public ResponseEntity<Void> updateExpiryDate(
            @PathVariable UUID id,
            @RequestParam BigDecimal newExpiryDate) {
        cardService.updateExpiryDate(id, newExpiryDate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable UUID id) throws Exception {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }


}

