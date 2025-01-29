package io.bankingsystem.banking.service.mappings;

import io.bankingsystem.banking.model.dto.*;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.repository.CardRepository;
import io.bankingsystem.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountMapping {

    private final CardMapping cardMapping;
    private final TransactionMapping transactionMapping;

    public AccountMapping(CardMapping cardMapping, TransactionMapping transactionMapping) {
        this.cardMapping = cardMapping;
        this.transactionMapping = transactionMapping;
    }

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
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        return entity;
    }

    public AccountCardsDto mapToAccountCardsDto(AccountEntity account, CardRepository cardRepository) {
        AccountCardsDto dto = new AccountCardsDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setAccountCurrentBalance(account.getAccountCurrentBalance());
        dto.setAccountDateOpened(account.getAccountDateOpened());
        dto.setAccountDateClosed(account.getAccountDateClosed());
        dto.setAccountStatus(account.getAccountStatus());

        List<CardDto> cardDtos = cardRepository.findByAccountId(account.getId()).stream()
                .map(cardMapping::mapToCardDto)
                .toList();
        dto.setCards(cardDtos);

        return dto;
    }

    public AccountTransactionsDto mapToAccountTransactionsDto(AccountEntity account, TransactionRepository transactionRepository) {
        AccountTransactionsDto dto = new AccountTransactionsDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setAccountCurrentBalance(account.getAccountCurrentBalance());
        dto.setAccountDateOpened(account.getAccountDateOpened());
        dto.setAccountDateClosed(account.getAccountDateClosed());
        dto.setAccountStatus(account.getAccountStatus());

        List<TransactionDto> transactionDtos = transactionRepository.findByAccountId(account.getId()).stream()
                .map(transactionMapping::mapToTransactionDto)
                .toList();
        dto.setTransactions(transactionDtos);

        return dto;
    }
}
