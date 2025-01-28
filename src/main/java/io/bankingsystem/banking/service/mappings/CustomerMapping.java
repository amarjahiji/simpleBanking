package io.bankingsystem.banking.service.mappings;

import io.bankingsystem.banking.model.dto.AccountDto;
import io.bankingsystem.banking.model.dto.CustomerAccountsDto;
import io.bankingsystem.banking.model.dto.CustomerDto;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CardRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerMapping {

    private final PasswordEncoder passwordEncoder;
    private final AccountMapping accountMapping;


    public CustomerMapping(PasswordEncoder passwordEncoder, AccountMapping accountMapping) {
        this.passwordEncoder = passwordEncoder;
        this.accountMapping = accountMapping;
    }

    public CustomerEntity mapToCustomerEntity(CustomerDto customerDto) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setCustomerFirstName(customerDto.getCustomerFirstName());
        customerEntity.setCustomerLastName(customerDto.getCustomerLastName());
        customerEntity.setCustomerDateOfBirth(customerDto.getCustomerDateOfBirth());
        customerEntity.setCustomerEmail(customerDto.getCustomerEmail());
        customerEntity.setCustomerPhoneNumber(customerDto.getCustomerPhoneNumber());
        customerEntity.setCustomerAddress(customerDto.getCustomerAddress());
        customerEntity.setCustomerRole(customerDto.getCustomerRole());
        customerEntity.setCustomerPassword(passwordEncoder.encode(customerDto.getCustomerPassword()));
        return customerEntity;
    }

    public CustomerDto mapToCustomerDto(CustomerEntity customerEntity) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customerEntity.getId());
        customerDto.setCustomerFirstName(customerEntity.getCustomerFirstName());
        customerDto.setCustomerLastName(customerEntity.getCustomerLastName());
        customerDto.setCustomerDateOfBirth(customerEntity.getCustomerDateOfBirth());
        customerDto.setCustomerEmail(customerEntity.getCustomerEmail());
        customerDto.setCustomerPhoneNumber(customerEntity.getCustomerPhoneNumber());
        customerDto.setCustomerAddress(customerEntity.getCustomerAddress());
        customerDto.setCustomerPassword(customerEntity.getCustomerPassword());
        customerDto.setCustomerRole(customerEntity.getCustomerRole());
        return customerDto;
    }

    public CustomerAccountsDto mapToCustomerAccountsDto(CustomerEntity customer, AccountRepository accountRepository, CardRepository cardRepository) {
        CustomerAccountsDto dto = new CustomerAccountsDto();
        dto.setId(customer.getId());
        dto.setCustomerFirstName(customer.getCustomerFirstName());
        dto.setCustomerLastName(customer.getCustomerLastName());
        dto.setCustomerEmail(customer.getCustomerEmail());
        dto.setCustomerPhoneNumber(customer.getCustomerPhoneNumber());
        dto.setCustomerAddress(customer.getCustomerAddress());
        dto.setCustomerRole(customer.getCustomerRole());

        List<AccountDto> accountDtos = accountRepository.findByCustomerId(customer.getId()).stream()
                .map(account -> accountMapping.mapToAccountCardsDto(account, cardRepository))
                .toList();
        dto.setAccounts(accountDtos);

        return dto;
    }

}