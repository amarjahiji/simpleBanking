package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.TransactionDto;
import io.bankingsystem.banking.service.services.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void getAllTransactions() {
        List<TransactionDto> transactionDtos = new ArrayList<>();
        transactionDtos.add(new TransactionDto());
        when(transactionService.getAllTransactions()).thenReturn(transactionDtos);

        ResponseEntity<List<TransactionDto>> response = transactionController.getAllTransactions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionDtos, response.getBody());
        verify(transactionService).getAllTransactions();
    }

    @Test
    void getTransactionById_Success() {
        UUID id = UUID.randomUUID();
        TransactionDto transactionDto = new TransactionDto();
        when(transactionService.getTransactionById(id)).thenReturn(transactionDto);

        ResponseEntity<TransactionDto> response = transactionController.getTransactionById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactionDto, response.getBody());
        verify(transactionService).getTransactionById(id);
    }

    @Test
    void getTransactionById_NotFound() {
        UUID id = UUID.randomUUID();
        when(transactionService.getTransactionById(id)).thenThrow(new EntityNotFoundException("Transaction not found"));

        ResponseEntity<TransactionDto> response = transactionController.getTransactionById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(transactionService).getTransactionById(id);
    }

    @Test
    void createTransaction_Success() {
        TransactionDto transactionDto = new TransactionDto();
        TransactionDto createdTransaction = new TransactionDto();
        when(transactionService.createTransaction(transactionDto)).thenReturn(createdTransaction);

        ResponseEntity<TransactionDto> response = (ResponseEntity<TransactionDto>) transactionController.createTransaction(transactionDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdTransaction, response.getBody());
        verify(transactionService).createTransaction(transactionDto);
    }

    @Test
    void createTransaction_BadRequest() {
        TransactionDto transactionDto = new TransactionDto();
        when(transactionService.createTransaction(transactionDto)).thenThrow(new IllegalArgumentException("Invalid transaction"));

        ResponseEntity<TransactionDto> response = (ResponseEntity<TransactionDto>) transactionController.createTransaction(transactionDto);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(transactionService).createTransaction(transactionDto);
    }

}