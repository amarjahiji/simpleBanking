package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.CustomerDto;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.repository.CustomerRepository;
import io.bankingsystem.banking.service.mappings.CustomerMapping;
import io.bankingsystem.banking.service.validations.CustomerValidation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {
private final CustomerRepository customerRepository;
private final CustomerMapping mappingService;
private final CustomerValidation validationService;
private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, CustomerMapping mappingService, CustomerValidation validationService, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.mappingService = mappingService;
        this.validationService = validationService;
        this.passwordEncoder = passwordEncoder;
    }
    
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(mappingService::mapToCustomerDto).collect(Collectors.toList());
    }

    public CustomerDto getCustomerById(UUID id) throws Exception {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new Exception("Customer not found"));
        return mappingService.mapToCustomerDto(customer);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        validationService.validateCustomerDto(customerDto);
        CustomerEntity customer = mappingService.mapToCustomerEntity(customerDto);
        CustomerEntity savedCustomer = customerRepository.save(customer);
        return mappingService.mapToCustomerDto(savedCustomer);
    }

    public CustomerDto updateCustomerById(UUID id, CustomerDto customerDto) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
        validationService.validateCustomerDto(customerDto);
        customer.setCustomerFirstName(customerDto.getCustomerFirstName());
        customer.setCustomerLastName(customerDto.getCustomerLastName());
        customer.setCustomerEmail(customerDto.getCustomerEmail());
        customer.setCustomerPhoneNumber(customerDto.getCustomerPhoneNumber());
        customer.setCustomerAddress(customerDto.getCustomerAddress());
        if (customerDto.getCustomerPassword() != null && !customerDto.getCustomerPassword().isEmpty()) {
            customer.setCustomerPassword(customerDto.getCustomerPassword());
        }
        CustomerEntity updatedCustomer = customerRepository.save(customer);
        return mappingService.mapToCustomerDto(updatedCustomer);
    }

    @Transactional
    public void updatePassword(UUID customerId, String newPassword) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
        validationService.validatePassword(newPassword);
        customer.setCustomerPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
    }

    @Transactional
    public void updateAddress(UUID customerId, String newAddress) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
        if (newAddress == null || newAddress.isEmpty()) {
            throw new IllegalArgumentException("New address cannot be null or empty");
        }
        customer.setCustomerAddress(newAddress);
        customerRepository.save(customer);
    }

    @Transactional
    public void updatePhoneNumber(UUID customerId, String newPhoneNumber) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
        validationService.validatePhoneNumber(newPhoneNumber);
        customer.setCustomerPhoneNumber(newPhoneNumber);
        customerRepository.save(customer);
    }

    @Transactional
    public void updateEmail(UUID customerId, String newEmail) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
       validationService.validateEmail(newEmail);
        customer.setCustomerEmail(newEmail);
        customerRepository.save(customer);
    }

    public void deleteCustomer(UUID id) {
        customerRepository.findById(id).ifPresentOrElse(
                customerRepository::delete, () -> {
                    throw new EntityNotFoundException("Customer not found with ID: " + id);
                });
    }
}

