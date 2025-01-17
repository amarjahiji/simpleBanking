package io.bankingsystem.banking.service.mappings;

import io.bankingsystem.banking.model.dto.CustomerDto;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapping {

    private final PasswordEncoder passwordEncoder;

    public CustomerMapping(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Map CustomerDto to CustomerEntity, encoding the password
    public CustomerEntity mapToCustomerEntity(CustomerDto customerDto) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setCustomerFirstName(customerDto.getCustomerFirstName());
        customerEntity.setCustomerLastName(customerDto.getCustomerLastName());
        customerEntity.setCustomerEmail(customerDto.getCustomerEmail());
        customerEntity.setCustomerPhoneNumber(customerDto.getCustomerPhoneNumber());
        customerEntity.setCustomerAddress(customerDto.getCustomerAddress());
        customerEntity.setCustomerPassword(passwordEncoder.encode(customerDto.getCustomerPassword()));
        return customerEntity;
    }

    // Map CustomerEntity to CustomerDto
    public CustomerDto mapToCustomerDto(CustomerEntity customerEntity) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customerEntity.getId());
        customerDto.setCustomerFirstName(customerEntity.getCustomerFirstName());
        customerDto.setCustomerLastName(customerEntity.getCustomerLastName());
        customerDto.setCustomerEmail(customerEntity.getCustomerEmail());
        customerDto.setCustomerPhoneNumber(customerEntity.getCustomerPhoneNumber());
        customerDto.setCustomerAddress(customerEntity.getCustomerAddress());
        return customerDto;
    }
}
