package io.bankingsystem.banking.service.mappings;

import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.model.entity.TransactionEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionMapping {

    public TransactionDto mapToTransactionDto(TransactionEntity transactionEntity) {
        return new TransactionDto(
                transactionEntity.getId(),
                transactionEntity.getTransactionType(),
                transactionEntity.getTransactionAmount(),
                transactionEntity.getTransactionDate(),
                transactionEntity.getTransactionDescription(),
                transactionEntity.getTransactionDestination(),
                transactionEntity.getAccount().getId()
        );
    }

    public TransactionEntity mapToTransactionEntity(TransactionDto transactionDto) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionType(transactionDto.getTransactionType());
        transactionEntity.setTransactionAmount(transactionDto.getTransactionAmount());
        transactionEntity.setTransactionDate(transactionDto.getTransactionDate());
        transactionEntity.setTransactionDescription(transactionDto.getTransactionDescription());
        transactionEntity.setTransactionDestination(transactionDto.getTransactionDestination());
        return transactionEntity;
    }
}
