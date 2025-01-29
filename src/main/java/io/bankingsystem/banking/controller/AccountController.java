package io.bankingsystem.banking.controller;

import io.bankingsystem.banking.model.dto.AccountCardsDto;
import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.dto.AccountTransactionsDto;
import io.bankingsystem.banking.service.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        try{
        List<AccountDto> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }catch (Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable UUID id) {
        try {
            AccountDto account = accountService.getAccountById(id);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/cards")
    public ResponseEntity<List<AccountCardsDto>> getAccountWithCards() {
        try {
            List<AccountCardsDto> accountCardsDto = accountService.getAccountsWithCards();
            return ResponseEntity.ok(accountCardsDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/cards/{id}")
    public ResponseEntity<AccountCardsDto> getAccountWithCardsById(@PathVariable UUID id) {
        try {
            AccountCardsDto accountCards = accountService.getAccountWithCardsById(id);
            return ResponseEntity.ok(accountCards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<AccountTransactionsDto>> getAccountWithTransactions() {
        try{
        List<AccountTransactionsDto> accountTransactionsDtos = accountService.getAccountsWithTransactions();
        return ResponseEntity.ok(accountTransactionsDtos);
    }catch (Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<AccountTransactionsDto> getAccountWithTransactionsById(@PathVariable UUID id) {
        try {
            AccountTransactionsDto accountTransactions = accountService.getAccountWithTransactionsById(id);
            return ResponseEntity.ok(accountTransactions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        try {
            AccountDto createdAccount = accountService.createAccount(accountDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable UUID id, @RequestBody AccountDto accountDto) {
        try {
            AccountDto updatedAccount = accountService.updateAccountById(id, accountDto);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/balance/{id}")
    public ResponseEntity<AccountDto> updateAccountCurrentBalance(
            @PathVariable UUID id,
            @RequestBody Map<String, BigDecimal> currentBalanceUpdate
    ) {
        try {
            BigDecimal newCurrentBalance = currentBalanceUpdate.get("accountCurrentBalance");
            AccountDto updatedAccount = accountService.updateAccountCurrentBalance(id, newCurrentBalance);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/date-closed/{id}")
    public ResponseEntity<AccountDto> updateAccountDateClosed(
            @PathVariable UUID id,
            @RequestBody Map<String, LocalDateTime> dateClosedUpdate
    ) {
        try {
            LocalDateTime newDateClosed = dateClosedUpdate.get("accountClosedDate");
            AccountDto updatedAccount = accountService.updateAccountDateClosed(id, newDateClosed);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<AccountDto> updateAccountStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, String> statusUpdate
    ) {
        try {
            String newStatus = statusUpdate.get("accountStatus");
            AccountDto updatedAccount = accountService.updateAccountStatus(id, newStatus);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
