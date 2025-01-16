package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.*;
import io.bankingsystem.banking.model.entity.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class MappingService {

    // Maps AccountEntity to AccountDto
    public AccountDto mapToAccountDto(AccountEntity account) {
        return new AccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getAccountCurrentBalance(),
                account.getAccountDateOpened(),
                account.getAccountDateClosed(),
                account.getAccountStatus(),
                account.getCustomer().getId()
        );
    }

    // Maps AccountDto to AccountEntity
    public AccountEntity mapToAccountEntity(AccountDto dto) {
        AccountEntity entity = new AccountEntity();
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setAccountType(dto.getAccountType());
        entity.setAccountCurrentBalance(dto.getAccountCurrentBalance());
        entity.setAccountStatus(dto.getAccountStatus());
        if (dto.getAccountDateOpened() != null) {
            entity.setAccountDateOpened(dto.getAccountDateOpened());
        }
        if (dto.getAccountDateClosed() != null) {
            entity.setAccountDateClosed(dto.getAccountDateClosed());
        }
        return entity;
    }

    // Maps CardEntity to CardDto
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

    // Maps CardDto to CardEntity
    public CardEntity mapToCardEntity(CardDto dto) {
        CardEntity entity = new CardEntity();
        entity.setCardNumber(dto.getCardNumber());
        entity.setCardExpiryDate(dto.getCardExpiryDate());
        entity.setCardCvv(dto.getCardCvv());
        return entity;
    }

    // Maps CardTypeEntity to CardTypeDto
    public CardTypeDto mapToCardTypeDto(CardTypeEntity cardType) {
        return new CardTypeDto(
                cardType.getId(),
                cardType.getCardTypeName()
        );
    }

    // Maps CardTypeDto to CardTypeEntity
    public CardTypeEntity mapToCardTypeEntity(CardTypeDto dto) {
        CardTypeEntity entity = new CardTypeEntity();
        entity.setCardTypeName(dto.getCardTypeName());
        return entity;
    }

    // Maps CustomerEntity to CustomerDto
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

    // Maps CustomerDto to CustomerEntity
    public CustomerEntity mapToCustomerEntity(CustomerDto dto) {
        CustomerEntity entity = new CustomerEntity();
        entity.setCustomerFirstName(dto.getCustomerFirstName());
        entity.setCustomerLastName(dto.getCustomerLastName());
        entity.setCustomerEmail(dto.getCustomerEmail());
        entity.setCustomerPhoneNumber(dto.getCustomerPhoneNumber());
        entity.setCustomerAddress(dto.getCustomerAddress());
        return entity;
    }

    // Maps TransactionEntity to TransactionDto
    public TransactionDto mapToTransactionDto(TransactionEntity transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getTransactionAmount(),
                convertTimestampToLocalDateTime(transaction.getTransactionDate()),
                transaction.getDescription(),
                transaction.getAccount().getId()
        );
    }

    // Maps TransactionDto to TransactionEntity
    public TransactionEntity mapToTransactionEntity(TransactionDto dto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionType(dto.getTransactionType());
        entity.setTransactionAmount(dto.getTransactionAmount());
        entity.setDescription(dto.getTransactionDescription());
        if (dto.getTransactionDate() != null) {
            entity.setTransactionDate(convertLocalDateTimeToTimestamp(dto.getTransactionDate()));
        }
        return entity;
    }

    // Helper: Convert Timestamp to LocalDateTime
    private LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp) {
        return timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // Helper: Convert LocalDateTime to Timestamp
    private Timestamp convertLocalDateTimeToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
