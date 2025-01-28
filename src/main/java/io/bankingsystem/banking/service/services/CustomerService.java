package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.*;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CardRepository;
import io.bankingsystem.banking.repository.CustomerRepository;
import io.bankingsystem.banking.service.mappings.CustomerMapping;
import io.bankingsystem.banking.service.validations.CustomerValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {
private final CustomerRepository customerRepository;
private final AccountRepository accountRepository;
private final CustomerMapping customerMapping;
private final CustomerValidation validationService;
private final PasswordEncoder passwordEncoder;
private final CardRepository cardRepository;

    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository, CustomerMapping customerMapping, CustomerValidation validationService, PasswordEncoder passwordEncoder, CardRepository cardRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.customerMapping = customerMapping;
        this.validationService = validationService;
        this.passwordEncoder = passwordEncoder;
        this.cardRepository = cardRepository;
    }
    
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapping::mapToCustomerDto).collect(Collectors.toList());
    }

    public List<CustomerDto> getCustomersYoungerThan24() {
        LocalDate twentyFourYearsAgo = LocalDate.now().minusYears(24);
        List<CustomerEntity> customers = customerRepository.findByCustomerDateOfBirthAfter(twentyFourYearsAgo);
        return customers.stream()
                .map(customerMapping::mapToCustomerDto)
                .collect(Collectors.toList());
    }

    public List<CustomerDto> getCustomersOlderThan64() {
        LocalDate sixtyFourYearsAgo = LocalDate.now().minusYears(64);
        List<CustomerEntity> customers = customerRepository.findByCustomerDateOfBirthBefore(sixtyFourYearsAgo);
        return customers.stream()
                .map(customerMapping::mapToCustomerDto)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(UUID id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return customerMapping.mapToCustomerDto(customer);
    }

    public List<CustomerAccountsDto> getCustomersWithAccounts() {
        List<CustomerEntity> customers = customerRepository.findAll();
        return customers.stream().map(customer -> {
            List<AccountDto> accounts = accountRepository.findByCustomerId(customer.getId())
                    .stream()
                    .map(account -> new AccountDto(
                            account.getId(),
                            account.getAccountNumber(),
                            account.getAccountType(),
                            account.getAccountCurrentBalance(),
                            account.getAccountDateOpened(),
                            account.getAccountDateClosed(),
                            account.getAccountStatus(),
                            account.getCustomer().getId()
                    ))
                    .collect(Collectors.toList());

            return new CustomerAccountsDto(
                    customer.getId(),
                    customer.getCustomerFirstName(),
                    customer.getCustomerLastName(),
                    customer.getCustomerEmail(),
                    customer.getCustomerPhoneNumber(),
                    customer.getCustomerAddress(),
                    customer.getCustomerRole(),
                    accounts
            );
        }).collect(Collectors.toList());
    }

    public CustomerAccountsDto getCustomerWithAccountsById(UUID customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

        List<AccountDto> accounts = accountRepository.findByCustomerId(customerId)
                .stream()
                .map(account -> new AccountDto(
                        account.getId(),
                        account.getAccountNumber(),
                        account.getAccountType(),
                        account.getAccountCurrentBalance(),
                        account.getAccountDateOpened(),
                        account.getAccountDateClosed(),
                        account.getAccountStatus(),
                        account.getCustomer().getId()
                ))
                .collect(Collectors.toList());

        return new CustomerAccountsDto(
                customer.getId(),
                customer.getCustomerFirstName(),
                customer.getCustomerLastName(),
                customer.getCustomerEmail(),
                customer.getCustomerPhoneNumber(),
                customer.getCustomerAddress(),
                customer.getCustomerRole(),
                accounts
        );
    }
    public List<CustomerAccountsDto> getCustomersAccountsCards() {
        List<CustomerEntity> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> customerMapping.mapToCustomerAccountsDto(customer, accountRepository, cardRepository))
                .toList();
    }

    public CustomerAccountsDto getCustomerAccountsCardsById(UUID customerId) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return customerMapping.mapToCustomerAccountsDto(customer, accountRepository, cardRepository);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        validationService.validateCustomerDto(customerDto);
        CustomerEntity customer = customerMapping.mapToCustomerEntity(customerDto);
        CustomerEntity savedCustomer = customerRepository.save(customer);
        return customerMapping.mapToCustomerDto(savedCustomer);
    }

    public CustomerDto updateCustomerById(UUID id, CustomerDto customerDto) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
        validationService.validateCustomerDto(customerDto);
        customer.setCustomerFirstName(customerDto.getCustomerFirstName());
        customer.setCustomerLastName(customerDto.getCustomerLastName());
        customer.setCustomerDateOfBirth(customerDto.getCustomerDateOfBirth());
        customer.setCustomerEmail(customerDto.getCustomerEmail());
        customer.setCustomerPhoneNumber(customerDto.getCustomerPhoneNumber());
        customer.setCustomerAddress(customerDto.getCustomerAddress());
        if (customerDto.getCustomerPassword() != null && !customerDto.getCustomerPassword().isEmpty()) {
            customer.setCustomerPassword(passwordEncoder.encode(customerDto.getCustomerPassword()));
        }
        CustomerEntity updatedCustomer = customerRepository.save(customer);
        return customerMapping.mapToCustomerDto(updatedCustomer);
    }

    @Transactional
    public CustomerDto updateCustomerAddress(UUID id, String newAddress) {
        validationService.validateCustomerAddress(newAddress);
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customerEntity.setCustomerAddress(newAddress);
        CustomerEntity savedEntity = customerRepository.save(customerEntity);
        return customerMapping.mapToCustomerDto(savedEntity);
    }

    @Transactional
    public CustomerDto updateCustomerPhoneNumber(UUID id, String newPhoneNumber) {
        validationService.validatePhoneNumber(newPhoneNumber);
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customerEntity.setCustomerPhoneNumber(newPhoneNumber);
        CustomerEntity savedEntity = customerRepository.save(customerEntity);
        return  customerMapping.mapToCustomerDto(savedEntity);
    }

    @Transactional
    public CustomerDto updateCustomerEmail(UUID id, String newEmail) {
        validationService.validateEmail(newEmail);
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customerEntity.setCustomerEmail(newEmail);
        CustomerEntity savedEntity = customerRepository.save(customerEntity);
        return  customerMapping.mapToCustomerDto(savedEntity);
    }

    @Transactional
    public CustomerDto updateCustomerPassword(UUID id, String newPassword) {
        validationService.validatePassword(newPassword);
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customerEntity.setCustomerPassword(passwordEncoder.encode(newPassword));
        CustomerEntity savedEntity = customerRepository.save(customerEntity);
        return
               customerMapping.mapToCustomerDto(savedEntity);
    }

    public void deleteCustomer(UUID id) {
        customerRepository.findById(id).ifPresentOrElse(
                customerRepository::delete, () -> {
                    throw new EntityNotFoundException("Customer not found with ID: " + id);
                });
    }
}

