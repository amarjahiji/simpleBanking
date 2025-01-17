package io.bankingsystem.banking.model.dto;

import io.bankingsystem.banking.model.enums.CardTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CardTypeDto {
    private Integer id;
    private CardTypeName cardTypeName;
}
