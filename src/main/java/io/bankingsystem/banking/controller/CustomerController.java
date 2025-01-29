package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.CustomerAccountsCardsDto;
import io.bankingsystem.banking.model.dto.CustomerAccountsDto;
import io.bankingsystem.banking.model.dto.CustomerDto;
import io.bankingsystem.banking.service.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @GetMapping("/young")
    public ResponseEntity<List<CustomerDto>> getCustomersYoungerThan24() {
        List<CustomerDto> customers = customerService.getCustomersYoungerThan24();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/old")
    public ResponseEntity<List<CustomerDto>> getCustomersOlderThan64() {
        List<CustomerDto> customers = customerService.getCustomersOlderThan64();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable UUID id) {
        try {
            CustomerDto customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<CustomerAccountsDto>> getCustomersWithAccounts() {
        try {
            List<CustomerAccountsDto> customers = customerService.getCustomersWithAccounts();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<CustomerAccountsDto> getCustomerWithAccountsById(@PathVariable UUID id) {
        try {
            CustomerAccountsDto customerAccounts = customerService.getCustomerWithAccountsById(id);
            return ResponseEntity.ok(customerAccounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/accounts/cards")
    public ResponseEntity<List<CustomerAccountsCardsDto>> getCustomersAccountsCards() {
        try{
            List<CustomerAccountsCardsDto> customers = customerService.getCustomersAccountsCards();
        return ResponseEntity.ok(customers);
    }catch (Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
    }


    @GetMapping("/accounts-cards/{id}")
    public ResponseEntity<CustomerAccountsCardsDto> getCustomerAccountsCardsById(@PathVariable UUID id) {
        try {
            CustomerAccountsCardsDto customer = customerService.getCustomerAccountsCardsById(id);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/create")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        try {
            CustomerDto createdCustomer = customerService.createCustomer(customerDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable UUID id, @RequestBody CustomerDto customerDto) {
        try {
            CustomerDto updatedCustomer = customerService.updateCustomerById(id, customerDto);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/address/{id}")
    public ResponseEntity<CustomerDto> updateCustomerAddress(
            @PathVariable UUID id,
            @RequestBody Map<String, String> addressUpdate
    ) {
        try {
            String newAddress = addressUpdate.get("customerAddress");
            CustomerDto updatedCustomer = customerService.updateCustomerAddress(id, newAddress);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PatchMapping("/phone-number/{id}")
    public ResponseEntity<CustomerDto> updateCustomerPhoneNumber(
            @PathVariable UUID id,
            @RequestBody Map<String, String> phoneNumberUpdate
    ) {
        try {
            String newPhoneNumber = phoneNumberUpdate.get("customerPhoneNumber");
            CustomerDto updatedCustomer = customerService.updateCustomerPhoneNumber(id, newPhoneNumber);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/email/{id}")
    public ResponseEntity<CustomerDto> updateCustomerEmail(
            @PathVariable UUID id,
            @RequestBody Map<String, String> emailUpdate
    ) {
        try {
            String newEmail = emailUpdate.get("customerEmail");
            CustomerDto updatedCustomer = customerService.updateCustomerEmail(id, newEmail);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<CustomerDto> updateCustomerPassword(
            @PathVariable UUID id,
            @RequestBody Map<String, String> passwordUpdate
    ) {
        try {
            String newPassword = passwordUpdate.get("password");
            CustomerDto updatedCustomer = customerService.updateCustomerPassword(id, newPassword);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
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
