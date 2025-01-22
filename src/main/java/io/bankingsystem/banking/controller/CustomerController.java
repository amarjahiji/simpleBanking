package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CustomerAccountsDto;
import io.bankingsystem.banking.model.dto.CustomerDto;
import io.bankingsystem.banking.service.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable UUID id) {
        try {
            CustomerDto customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/accounts")
    public List<CustomerAccountsDto> getCustomersWithAccounts() {
        return customerService.getCustomersWithAccounts();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<CustomerAccountsDto> getCustomerWithAccountsById(@PathVariable UUID id) {
        try {
            CustomerAccountsDto customerAccounts = customerService.getCustomerWithAccountsById(id);
            return ResponseEntity.ok(customerAccounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        try {
            CustomerDto createdCustomer = customerService.createCustomer(customerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable UUID id, @RequestBody CustomerDto customerDto) {
        try {
            CustomerDto updatedCustomer = customerService.updateCustomerById(id, customerDto);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/address/{id}")
    public ResponseEntity<Void> updateCustomerAddress(@PathVariable UUID id, @RequestParam String address) {
        try {
            customerService.updateAddress(id, address);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<Void> updateCustomerEmail(@PathVariable UUID id, @RequestParam String email) {
        try {
            customerService.updateEmail(id, email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/phone-number")
    public ResponseEntity<Void> updateCustomerPhoneNumber(@PathVariable UUID id, @RequestParam String phoneNumber) {
        try {
            customerService.updatePhoneNumber(id, phoneNumber);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> updateCustomerPassword(@PathVariable UUID id, @RequestParam String password) {
        try {
            customerService.updatePassword(id, password);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
