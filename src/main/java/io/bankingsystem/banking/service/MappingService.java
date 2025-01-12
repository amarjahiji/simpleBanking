package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.*;
import io.bankingsystem.banking.model.entity.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.ZoneId;

@Service
public class MappingService {
    public AccountDto mapToAccountDto(AccountEntity account) {
        return new AccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getAccountCurrentBalance(),
                account.getAccountDateOpened().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                account.getAccountDateClosed() != null ?
                        new Timestamp(account.getAccountDateClosed().getTime()).toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime() : null,
                account.getAccountStatus(),
                account.getCustomer().getId()
        );
    }

    public AccountEntity mapToAccountEntity(AccountDto dto) {
        AccountEntity entity = new AccountEntity();
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setAccountType(dto.getAccountType());
        entity.setAccountCurrentBalance(dto.getAccountCurrentBalance());
        entity.setAccountStatus(dto.getAccountStatus());
        return entity;
    }

    public CardDto mapToCardDto(CardEntity card) {
        return new CardDto(
                card.getId(),
                card.getCardNumber(),
                card.getCardExpiryDate(),
                card.getCardCvv(),
                card.getCardType().getId(),
                card.getAccount().getId()
        );
    }

    public CardEntity mapToCardEntity(CardDto dto) {
        CardEntity entity = new CardEntity();
        entity.setCardNumber(dto.getCardNumber());
        entity.setCardExpiryDate(dto.getCardExpiryDate());
        entity.setCardCvv(dto.getCardCvv());
        return entity;
    }

    public CardTypeDto mapToCardTypeDto(CardTypeEntity cardType) {
        return new CardTypeDto(
                cardType.getId(),
                cardType.getCardTypeName()
        );
    }

    public CardTypeEntity mapToCardTypeEntity(CardTypeDto dto) {
        CardTypeEntity entity = new CardTypeEntity();
        entity.setCardTypeName(dto.getCardTypeName());
        return entity;
    }

    public CustomerDto mapToCustomerDto(CustomerEntity customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getCustomerFirstName(),
                customer.getCustomerLastName(),
                customer.getCustomerEmail(),
                customer.getCustomerPhoneNumber(),
                customer.getCustomerAddress()
        );
    }

    public CustomerEntity mapToCustomerEntity(CustomerDto dto) {
        CustomerEntity entity = new CustomerEntity();
        entity.setCustomerFirstName(dto.getCustomerFirstName());
        entity.setCustomerLastName(dto.getCustomerLastName());
        entity.setCustomerEmail(dto.getCustomerEmail());
        entity.setCustomerPhoneNumber(dto.getCustomerPhoneNumber());
        entity.setCustomerAddress(dto.getCustomerAddress());
        return entity;
    }

    public TransactionDto mapToTransactionDto(TransactionEntity transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getTransactionAmount(),
                transaction.getTransactionDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                transaction.getDescription(),
                transaction.getAccount().getId()
        );
    }

    public TransactionEntity mapToTransactionEntity(TransactionDto dto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionType(dto.getTransactionType());
        entity.setTransactionAmount(dto.getTransactionAmount());
        entity.setDescription(dto.getTransactionDescription());
        return entity;
    }



}
