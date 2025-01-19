package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.TransactionEntity;
import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.TransactionRepository;
import io.bankingsystem.banking.service.mappings.TransactionMapping;
import io.bankingsystem.banking.service.validations.TransactionValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapping mappingService;
    private final TransactionValidation validationService;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, TransactionMapping mappingService, TransactionValidation validationService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.mappingService = mappingService;
        this.validationService = validationService;
    }

    public List<TransactionDto> getAllTransactions() {
        return transactionRepository.findAll().stream().map(mappingService::mapToTransactionDto).collect(Collectors.toList());
    }

    public TransactionDto getTransactionById(UUID id){
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        return mappingService.mapToTransactionDto(transaction);
    }

    @Transactional
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        AccountEntity sourceAccount = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + transactionDto.getAccountId()));

        if (sourceAccount.getAccountStatus() == AccountStatus.CLOSED) {
            throw new IllegalStateException("Cannot create transaction for closed account");
        }

        validationService.validateTransactionDto(transactionDto);

        switch (transactionDto.getTransactionType()) {
            case DEPOSIT:
                sourceAccount.setAccountCurrentBalance(
                        sourceAccount.getAccountCurrentBalance().add(transactionDto.getTransactionAmount())
                );
                break;

            case WITHDRAWAL:
                if (sourceAccount.getAccountCurrentBalance().compareTo(transactionDto.getTransactionAmount()) < 0) {
                    throw new IllegalStateException("Insufficient funds for withdrawal");
                }
                sourceAccount.setAccountCurrentBalance(
                        sourceAccount.getAccountCurrentBalance().subtract(transactionDto.getTransactionAmount())
                );
                break;

            case TRANSFER:
                if (sourceAccount.getAccountCurrentBalance().compareTo(transactionDto.getTransactionAmount()) < 0) {
                    throw new IllegalStateException("Insufficient funds for transfer");
                }

                AccountEntity destinationAccount = accountRepository.findById(
                                UUID.fromString(transactionDto.getTransactionDestination()))
                        .orElseThrow(() -> new EntityNotFoundException("Destination account not found"));

                if (destinationAccount.getAccountStatus() == AccountStatus.CLOSED) {
                    throw new IllegalStateException("Cannot transfer to closed account");
                }

                sourceAccount.setAccountCurrentBalance(
                        sourceAccount.getAccountCurrentBalance().subtract(transactionDto.getTransactionAmount())
                );
                destinationAccount.setAccountCurrentBalance(
                        destinationAccount.getAccountCurrentBalance().add(transactionDto.getTransactionAmount())
                );
                accountRepository.save(destinationAccount);
                break;
        }

        TransactionEntity transaction = mappingService.mapToTransactionEntity(transactionDto);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccount(sourceAccount);
        accountRepository.save(sourceAccount);
        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        return mappingService.mapToTransactionDto(savedTransaction);
    }
}
