package io.bankingsystem.banking.service;

import io.bankingsystem.banking.model.dto.CustomerDto;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {
private final CustomerRepository customerRepository;
private final MappingService mappingService;

    public CustomerService(CustomerRepository customerRepository, MappingService mappingService) {
        this.customerRepository = customerRepository;
        this.mappingService = mappingService;
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
        CustomerEntity customer = mappingService.mapToCustomerEntity(customerDto);
        CustomerEntity savedCustomer = customerRepository.save(customer);
        return mappingService.mapToCustomerDto(savedCustomer);
    }

    public CustomerDto updateCustomer(UUID id, CustomerDto customerDto) throws Exception {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new Exception("Customer not found"));

        customer.setCustomerFirstName(customerDto.getCustomerFirstName());
        customer.setCustomerLastName(customerDto.getCustomerLastName());
        customer.setCustomerEmail(customerDto.getCustomerEmail());
        customer.setCustomerPhoneNumber(customerDto.getCustomerPhoneNumber());
        customer.setCustomerAddress(customerDto.getCustomerAddress());

        CustomerEntity updatedCustomer = customerRepository.save(customer);
        return mappingService.mapToCustomerDto(updatedCustomer);
    }

    public void deleteCustomer(UUID id) throws Exception {
        if(!customerRepository.existsById(id)) {
            throw new Exception("Customer not found");
        }
        customerRepository.deleteById(id);
    }
}
