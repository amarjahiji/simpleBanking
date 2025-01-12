package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final MappingService mappingService;

    public AccountService(AccountRepository accountRepository, CustomerRepository customerRepository, MappingService mappingService) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.mappingService = mappingService;
    }

    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(mappingService::mapToAccountDto).collect(Collectors.toList());
    }

    public AccountDto getAccountById(UUID id) throws Exception {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new Exception("Account not found"));
        return mappingService.mapToAccountDto(account);
    }

    public AccountDto createAccount(AccountDto accountDto) throws Exception {
        CustomerEntity customer = customerRepository.findById(accountDto.getCustomerId())
                .orElseThrow(() -> new Exception("Customer not found"));

        AccountEntity account = mappingService.mapToAccountEntity(accountDto);
        account.setCustomer(customer);

        AccountEntity savedAccount = accountRepository.save(account);
        return mappingService.mapToAccountDto(savedAccount);
    }

    public AccountDto updateAccountById(UUID id, AccountDto accountDto) throws Exception {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new Exception("Account not found"));

        account.setAccountType(accountDto.getAccountType());
        account.setAccountStatus(accountDto.getAccountStatus());
        account.setAccountCurrentBalance(accountDto.getAccountCurrentBalance());

        AccountEntity updatedAccount = accountRepository.save(account);
        return mappingService.mapToAccountDto(updatedAccount);
    }

    public void deleteAccount(UUID id) throws Exception {
        if(!accountRepository.existsById(id)) {
            throw new Exception("Account not found");
        }
        accountRepository.deleteById(id);
    }
}
