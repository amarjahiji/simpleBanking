package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }
        private AccountDto mapToDto(AccountEntity account) {
            return new AccountDto(
                    account.getId(),
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getAccountCurrentBalance(),
                    account.getAccountDateOpened().toLocalDateTime(),
                    account.getAccountDateClosed() != null ?
                            account.getAccountDateClosed().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null,
                    account.getAccountStatus(),
                    account.getCustomer().getId()
            );
        }
    }
