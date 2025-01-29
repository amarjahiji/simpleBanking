package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CardTypeDto;
import io.bankingsystem.banking.service.services.CardTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cardtypes")
public class CardTypeController {
    private final CardTypeService cardTypeService;

    public CardTypeController(CardTypeService cardTypeService) {
        this.cardTypeService = cardTypeService;
    }

    @GetMapping
    public ResponseEntity<List<CardTypeDto>> getAllCardTypes() {
        try {
            List<CardTypeDto> cardTypes = cardTypeService.getAllCardTypes();
            return ResponseEntity.ok(cardTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
