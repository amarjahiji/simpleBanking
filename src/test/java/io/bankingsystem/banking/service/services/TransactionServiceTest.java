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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapping mappingService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountEntity accountEntity;

    @Mock private TransactionValidation validationService;


    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getAllTransactions_Success() {
        TransactionEntity transaction1 = new TransactionEntity();
        transaction1.setId(UUID.randomUUID());
        transaction1.setTransactionType(TransactionType.DEPOSIT);
        transaction1.setTransactionAmount(BigDecimal.valueOf(100));
        transaction1.setTransactionDate(LocalDateTime.now());
        transaction1.setTransactionDescription("Deposit");
        transaction1.setTransactionDestination("ATM");

        TransactionEntity transaction2 = new TransactionEntity();
        transaction2.setId(UUID.randomUUID());
        transaction2.setTransactionType(TransactionType.WITHDRAWAL);
        transaction2.setTransactionAmount(BigDecimal.valueOf(50));
        transaction2.setTransactionDate(LocalDateTime.now());
        transaction2.setTransactionDescription("Withdrawal");
        transaction2.setTransactionDestination("ATM");

        List<TransactionEntity> transactionEntities = List.of(transaction1, transaction2);

        TransactionDto transactionDto1 = new TransactionDto(transaction1.getId(), transaction1.getTransactionType(), transaction1.getTransactionAmount(), transaction1.getTransactionDate(), transaction1.getTransactionDescription(), transaction1.getTransactionDestination(), UUID.randomUUID());
        TransactionDto transactionDto2 = new TransactionDto(transaction2.getId(), transaction2.getTransactionType(), transaction2.getTransactionAmount(), transaction2.getTransactionDate(), transaction2.getTransactionDescription(), transaction2.getTransactionDestination(), UUID.randomUUID());

        when(transactionRepository.findAll()).thenReturn(transactionEntities);
        when(mappingService.mapToTransactionDto(transaction1)).thenReturn(transactionDto1);
        when(mappingService.mapToTransactionDto(transaction2)).thenReturn(transactionDto2);

        List<TransactionDto> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transactionDto1.getId(), result.get(0).getId());
        assertEquals(transactionDto2.getId(), result.get(1).getId());

        verify(transactionRepository).findAll();
        verify(mappingService).mapToTransactionDto(transaction1);
        verify(mappingService).mapToTransactionDto(transaction2);
    }

    @Test
    void getTransactionById_Success() {
        UUID transactionId = UUID.randomUUID();
        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(transactionId);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionAmount(BigDecimal.valueOf(100));
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setTransactionDescription("Deposit");
        transaction.setTransactionDestination("ATM");

        TransactionDto transactionDto = new TransactionDto(transaction.getId(), transaction.getTransactionType(), transaction.getTransactionAmount(), transaction.getTransactionDate(), transaction.getTransactionDescription(), transaction.getTransactionDestination(), UUID.randomUUID());

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(mappingService.mapToTransactionDto(transaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.getTransactionById(transactionId);

        assertNotNull(result);
        assertEquals(transactionDto.getId(), result.getId());
        assertEquals(transactionDto.getTransactionType(), result.getTransactionType());

        verify(transactionRepository).findById(transactionId);
        verify(mappingService).mapToTransactionDto(transaction);
    }

    @Test
    void getTransactionById_EntityNotFound() {
        UUID transactionId = UUID.randomUUID();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> transactionService.getTransactionById(transactionId));
        assertEquals("Transaction not found", exception.getMessage());

        verify(transactionRepository).findById(transactionId);
        verify(mappingService, never()).mapToTransactionDto(any());
    }

    @Test
    void createTransaction_Deposit_Success() {
        TransactionDto dto = new TransactionDto(
                UUID.randomUUID(),
                TransactionType.DEPOSIT,
                new BigDecimal("100"),
                LocalDateTime.now(),
                "test deposit",
                null,
                UUID.randomUUID()
        );

        AccountEntity account = new AccountEntity();
        account.setAccountCurrentBalance(new BigDecimal("500"));
        account.setAccountStatus(AccountStatus.ACTIVE);

        TransactionEntity transactionEntity = new TransactionEntity();

        when(accountRepository.findById(dto.getAccountId())).thenReturn(Optional.of(account));
        when(mappingService.mapToTransactionEntity(dto)).thenReturn(transactionEntity);
        when(transactionRepository.save(any())).thenReturn(transactionEntity);
        when(mappingService.mapToTransactionDto(transactionEntity)).thenReturn(dto);

        TransactionDto result = transactionService.createTransaction(dto);

        assertEquals(new BigDecimal("600"), account.getAccountCurrentBalance());
        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(TransactionEntity.class));
    }

    @Test
    void createTransaction_InsufficientFunds_ThrowsException() {

        TransactionDto dto = new TransactionDto(
                UUID.randomUUID(),
                TransactionType.WITHDRAWAL,
                new BigDecimal("1000"),
                LocalDateTime.now(),
                "test withdrawal",
                null,
                UUID.randomUUID()
        );

        AccountEntity account = new AccountEntity();
        account.setAccountCurrentBalance(new BigDecimal("500"));
        account.setAccountStatus(AccountStatus.ACTIVE);

        when(accountRepository.findById(dto.getAccountId())).thenReturn(Optional.of(account));

        assertThrows(IllegalStateException.class, () ->
                transactionService.createTransaction(dto)
        );
    }

    @Test
    void createTransaction_ClosedAccount_ThrowsException() {
        TransactionDto dto = new TransactionDto(
                UUID.randomUUID(),
                TransactionType.DEPOSIT,
                new BigDecimal("100"),
                LocalDateTime.now(),
                "test deposit",
                null,
                UUID.randomUUID()
        );

        AccountEntity account = new AccountEntity();
        account.setAccountStatus(AccountStatus.CLOSED);

        when(accountRepository.findById(dto.getAccountId())).thenReturn(Optional.of(account));

        assertThrows(IllegalStateException.class, () ->
                transactionService.createTransaction(dto)
        );
    }

}