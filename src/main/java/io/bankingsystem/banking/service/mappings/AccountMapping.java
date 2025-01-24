package io.bankingsystem.banking.service.mappings;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountMapping {

    public AccountDto mapToAccountDto(AccountEntity account) {
        return new AccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getAccountCurrentBalance(),
                account.getAccountDateOpened(),
                account.getAccountDateClosed(),
                account.getAccountStatus(),
                account.getCustomer().getId()
        );
    }

    public AccountEntity mapToAccountEntity(AccountDto dto) {
        AccountEntity entity = new AccountEntity();
        entity.setAccountNumber(dto.getAccountNumber());
        entity.setAccountType(dto.getAccountType());
        entity.setAccountCurrentBalance(dto.getAccountCurrentBalance());
        entity.setAccountStatus(dto.getAccountStatus());
        if (dto.getAccountDateOpened() != null) {
            entity.setAccountDateOpened(dto.getAccountDateOpened());
        }
        if (dto.getAccountDateClosed() != null) {
            entity.setAccountDateClosed(dto.getAccountDateClosed());
        }
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        return entity;
    }
}
