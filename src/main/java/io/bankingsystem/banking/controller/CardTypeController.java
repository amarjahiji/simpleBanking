package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CardTypeDto;
import io.bankingsystem.banking.service.CardTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardtypes")
public class CardTypeController {
    private final CardTypeService cardTypeService;

    public CardTypeController(CardTypeService cardTypeService) {
        this.cardTypeService = cardTypeService;
    }

    @GetMapping
    public List<CardTypeDto> getAllCardTypes() {
        return cardTypeService.getAllCardTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardTypeDto> getCardTypeById(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(cardTypeService.getCardTypeById(id));
    }

    @PostMapping
    public ResponseEntity<CardTypeDto> createCardType(@RequestBody CardTypeDto cardTypeDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cardTypeService.createCardType(cardTypeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardTypeDto> updateCardType(@PathVariable Integer id, @RequestBody CardTypeDto cardTypeDto) throws Exception {
        return ResponseEntity.ok(cardTypeService.updateCardType(id, cardTypeDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardType(@PathVariable Integer id) throws Exception {
        cardTypeService.deleteCardType(id);
        return ResponseEntity.noContent().build();
    }
}
