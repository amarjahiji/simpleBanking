package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.TransactionEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public TransactionDto getTransactionById(UUID id) throws Exception {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new Exception("Transaction not found"));
        return mapToDto(transaction);
    }

    public TransactionDto createTransaction(TransactionDto transactionDto) throws Exception {
        AccountEntity account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new Exception("Account not found"));

        TransactionEntity transaction = mapToEntity(transactionDto);
        transaction.setAccount(account);

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        return mapToDto(savedTransaction);
    }

    private TransactionDto mapToDto(TransactionEntity transaction) {
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

    private TransactionEntity mapToEntity(TransactionDto dto) {
        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionType(dto.getTransactionType());
        entity.setTransactionAmount(dto.getTransactionAmount());
        entity.setDescription(dto.getTransactionDescription());
        return entity;
    }



}
