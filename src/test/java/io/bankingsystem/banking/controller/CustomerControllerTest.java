package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CustomerDto;
import io.bankingsystem.banking.service.services.CustomerService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void getAllCustomers() {
        List<CustomerDto> customerDtos = new ArrayList<>();
        customerDtos.add(new CustomerDto());
        when(customerService.getAllCustomers()).thenReturn(customerDtos);

        ResponseEntity<List<CustomerDto>> response = customerController.getAllCustomers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDtos, response.getBody());
        verify(customerService).getAllCustomers();
    }

    @Test
    void getCustomersYoungerThan24() {
        List<CustomerDto> customerDtos = new ArrayList<>();
        customerDtos.add(new CustomerDto());
        when(customerService.getCustomersYoungerThan24()).thenReturn(customerDtos);

        ResponseEntity<List<CustomerDto>> response = customerController.getCustomersYoungerThan24();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDtos, response.getBody());
        verify(customerService).getCustomersYoungerThan24();
    }

    @Test
    void getCustomersOlderThan64() {
        List<CustomerDto> customerDtos = new ArrayList<>();
        customerDtos.add(new CustomerDto());
        when(customerService.getCustomersOlderThan64()).thenReturn(customerDtos);

        ResponseEntity<List<CustomerDto>> response = customerController.getCustomersOlderThan64();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDtos, response.getBody());
        verify(customerService).getCustomersOlderThan64();
    }

    @Test
    void getCustomerById_Success() {
        UUID id = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto();
        when(customerService.getCustomerById(id)).thenReturn(customerDto);

        ResponseEntity<CustomerDto> response = customerController.getCustomerById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDto, response.getBody());
        verify(customerService).getCustomerById(id);
    }

    @Test
    void getCustomerById_NotFound() {
        UUID id = UUID.randomUUID();
        when(customerService.getCustomerById(id)).thenThrow(new EntityNotFoundException("Customer not found"));


        ResponseEntity<CustomerDto> response = customerController.getCustomerById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(customerService).getCustomerById(id);
    }

    @Test
    void createCustomer_Success() {
        CustomerDto customerDto = new CustomerDto();
        CustomerDto createdCustomer = new CustomerDto();
        when(customerService.createCustomer(customerDto)).thenReturn(createdCustomer);

        ResponseEntity<CustomerDto> response = customerController.createCustomer(customerDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCustomer, response.getBody());
        verify(customerService).createCustomer(customerDto);
    }

    @Test
    void createCustomer_BadRequest() {
        CustomerDto customerDto = new CustomerDto();
        when(customerService.createCustomer(customerDto)).thenThrow(new IllegalArgumentException("Invalid customer"));

        ResponseEntity<CustomerDto> response = customerController.createCustomer(customerDto);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(customerService).createCustomer(customerDto);
    }

    @Test
    void updateCustomer_Success() {
        UUID id = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto();
        CustomerDto updatedCustomer = new CustomerDto();
        when(customerService.updateCustomerById(id, customerDto)).thenReturn(updatedCustomer);

        ResponseEntity<CustomerDto> response = customerController.updateCustomer(id, customerDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCustomer, response.getBody());
        verify(customerService).updateCustomerById(id, customerDto);
    }

    @Test
    void updateCustomer_NotFound() {
        UUID id = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto();
        when(customerService.updateCustomerById(id, customerDto)).thenThrow(new EntityNotFoundException("Customer not found"));

        ResponseEntity<CustomerDto> response = customerController.updateCustomer(id, customerDto);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(customerService).updateCustomerById(id, customerDto);
    }

    @Test
    void deleteCustomer_Success() {
        UUID id = UUID.randomUUID();
        doNothing().when(customerService).deleteCustomer(id);

        ResponseEntity<Void> response = customerController.deleteCustomer(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService).deleteCustomer(id);
    }

    @Test
    void deleteCustomer_NotFound() {
        UUID id = UUID.randomUUID();
        doThrow(new EntityNotFoundException("Customer not found")).when(customerService).deleteCustomer(id);

        ResponseEntity<Void> response = customerController.deleteCustomer(id);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(customerService).deleteCustomer(id);
    }

}