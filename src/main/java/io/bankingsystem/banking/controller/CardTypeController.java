package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CardTypeDto;
import io.bankingsystem.banking.service.services.CardTypeService;
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
    public List<CardTypeDto> getAllCardTypes() {
        return cardTypeService.getAllCardTypes();
    }

}
