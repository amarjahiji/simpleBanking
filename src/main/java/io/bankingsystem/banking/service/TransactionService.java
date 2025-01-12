package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.TransactionEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final MappingService mappingService;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, MappingService mappingService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.mappingService = mappingService;
    }

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream().map(mappingService::mapToTransactionDto).collect(Collectors.toList());
    }

    public TransactionDto getTransactionById(UUID id) throws Exception {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new Exception("Transaction not found"));
        return mappingService.mapToTransactionDto(transaction);
    }

    public TransactionDto createTransaction(TransactionDto transactionDto) throws Exception {
        AccountEntity account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new Exception("Account not found"));

        TransactionEntity transaction = mappingService.mapToTransactionEntity(transactionDto);
        transaction.setAccount(account);

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        return mappingService.mapToTransactionDto(savedTransaction);
    }
}
