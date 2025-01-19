package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.TransactionEntity;
import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.TransactionType;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.TransactionRepository;
import io.bankingsystem.banking.service.mappings.TransactionMapping;
import io.bankingsystem.banking.service.validations.TransactionValidation;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionMapping mappingService;
    @Mock
    private TransactionValidation validationService;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getAllTransactions_ShouldReturnMappedTransactions() {
        
        List<TransactionEntity> transactions = List.of(new TransactionEntity());
        TransactionDto mappedDto = new TransactionDto();
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(mappingService.mapToTransactionDto(any())).thenReturn(mappedDto);

        
        List<TransactionDto> result = transactionService.getAllTransactions();

        
        assertEquals(1, result.size());
        verify(transactionRepository).findAll();
        verify(mappingService).mapToTransactionDto(any());
    }

    @Test
    void getTransactionById_WhenTransactionExists_ShouldReturnMappedTransaction() {
        
        UUID id = UUID.randomUUID();
        TransactionEntity transaction = new TransactionEntity();
        TransactionDto mappedDto = new TransactionDto();
        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
        when(mappingService.mapToTransactionDto(transaction)).thenReturn(mappedDto);

        
        TransactionDto result = transactionService.getTransactionById(id);

        
        assertNotNull(result);
        verify(transactionRepository).findById(id);
        verify(mappingService).mapToTransactionDto(transaction);
    }

    @Test
    void getTransactionById_WhenTransactionNotFound_ShouldThrowException() {
        
        UUID id = UUID.randomUUID();
        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

         
        assertThrows(EntityNotFoundException.class, () -> transactionService.getTransactionById(id));
    }

    @Test
    void createTransaction_Deposit_ShouldSucceed() {
        
        TransactionDto inputDto = createTransactionDto(TransactionType.DEPOSIT);
        AccountEntity account = createAccount(BigDecimal.valueOf(100));
        TransactionEntity mappedEntity = new TransactionEntity();
        TransactionEntity savedEntity = new TransactionEntity();
        TransactionDto mappedDto = new TransactionDto();

        when(accountRepository.findById(inputDto.getAccountId())).thenReturn(Optional.of(account));
        when(mappingService.mapToTransactionEntity(inputDto)).thenReturn(mappedEntity);
        when(transactionRepository.save(any())).thenReturn(savedEntity);
        when(mappingService.mapToTransactionDto(savedEntity)).thenReturn(mappedDto);

        
        TransactionDto result = transactionService.createTransaction(inputDto);

        
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150), account.getAccountCurrentBalance());
        verify(validationService).validateTransactionDto(inputDto);
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any());
    }

    @Test
    void createTransaction_Withdrawal_ShouldSucceed() {
        
        TransactionDto inputDto = createTransactionDto(TransactionType.WITHDRAWAL);
        AccountEntity account = createAccount(BigDecimal.valueOf(100));
        TransactionEntity mappedEntity = new TransactionEntity();
        TransactionEntity savedEntity = new TransactionEntity();
        TransactionDto mappedDto = new TransactionDto();

        when(accountRepository.findById(inputDto.getAccountId())).thenReturn(Optional.of(account));
        when(mappingService.mapToTransactionEntity(inputDto)).thenReturn(mappedEntity);
        when(transactionRepository.save(any())).thenReturn(savedEntity);
        when(mappingService.mapToTransactionDto(savedEntity)).thenReturn(mappedDto);

        
        TransactionDto result = transactionService.createTransaction(inputDto);

        
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50), account.getAccountCurrentBalance());
        verify(validationService).validateTransactionDto(inputDto);
        verify(accountRepository).save(account);
    }

    @Test
    void createTransaction_Transfer_ShouldSucceed() {
        
        TransactionDto inputDto = createTransactionDto(TransactionType.TRANSFER);
        AccountEntity sourceAccount = createAccount(BigDecimal.valueOf(100));
        AccountEntity destAccount = createAccount(BigDecimal.valueOf(100));
        TransactionEntity mappedEntity = new TransactionEntity();
        TransactionEntity savedEntity = new TransactionEntity();
        TransactionDto mappedDto = new TransactionDto();

        when(accountRepository.findById(inputDto.getAccountId())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(UUID.fromString(inputDto.getTransactionDestination()))).thenReturn(Optional.of(destAccount));
        when(mappingService.mapToTransactionEntity(inputDto)).thenReturn(mappedEntity);
        when(transactionRepository.save(any())).thenReturn(savedEntity);
        when(mappingService.mapToTransactionDto(savedEntity)).thenReturn(mappedDto);

        
        TransactionDto result = transactionService.createTransaction(inputDto);

        
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50), sourceAccount.getAccountCurrentBalance());
        assertEquals(BigDecimal.valueOf(150), destAccount.getAccountCurrentBalance());
        verify(validationService).validateTransactionDto(inputDto);
        verify(accountRepository).save(sourceAccount);
        verify(accountRepository).save(destAccount);
    }

    @Test
    void createTransaction_WithInsufficientFunds_ShouldThrowException() {
        
        TransactionDto inputDto = createTransactionDto(TransactionType.WITHDRAWAL);
        AccountEntity account = createAccount(BigDecimal.valueOf(20));

        when(accountRepository.findById(inputDto.getAccountId())).thenReturn(Optional.of(account));

         
        assertThrows(IllegalStateException.class, () -> transactionService.createTransaction(inputDto));
    }

    @Test
    void createTransaction_WithClosedAccount_ShouldThrowException() {
        
        TransactionDto inputDto = createTransactionDto(TransactionType.DEPOSIT);
        AccountEntity account = createAccount(BigDecimal.valueOf(100));
        account.setAccountStatus(AccountStatus.CLOSED);

        when(accountRepository.findById(inputDto.getAccountId())).thenReturn(Optional.of(account));

         
        assertThrows(IllegalStateException.class, () -> transactionService.createTransaction(inputDto));
    }

    private TransactionDto createTransactionDto(TransactionType type) {
        TransactionDto dto = new TransactionDto();
        dto.setAccountId(UUID.randomUUID());
        dto.setTransactionAmount(BigDecimal.valueOf(50));
        dto.setTransactionType(type);
        dto.setTransactionDate(LocalDateTime.now());
        if (type == TransactionType.TRANSFER) {
            dto.setTransactionDestination(UUID.randomUUID().toString());
        }
        return dto;
    }

    private AccountEntity createAccount(BigDecimal balance) {
        AccountEntity account = new AccountEntity();
        account.setId(UUID.randomUUID());
        account.setAccountCurrentBalance(balance);
        account.setAccountStatus(AccountStatus.ACTIVE);
        return account;
    }
}