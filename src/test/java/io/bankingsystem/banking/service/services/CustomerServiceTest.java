package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.CustomerAccountsCardsDto;
import io.bankingsystem.banking.model.dto.CustomerAccountsDto;
import io.bankingsystem.banking.model.dto.CustomerDto;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.model.enum_fields.CustomerRole;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CardRepository;
import io.bankingsystem.banking.repository.CustomerRepository;
import io.bankingsystem.banking.service.mappings.CustomerMapping;
import io.bankingsystem.banking.service.validations.CustomerValidation;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapping customerMapping;

    @Mock
    private CustomerValidation customerValidation;

    @Mock
    private CardRepository cardRepository;

    @Mock private AccountRepository accountRepository;

    @InjectMocks
    private CustomerService customerService;


    @Test
    void getAllCustomers() {
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(UUID.randomUUID());
        customer1.setCustomerFirstName("John");
        customer1.setCustomerLastName("Doe");
        customer1.setCustomerDateOfBirth(LocalDate.of(1990, 1, 1));
        customer1.setCustomerEmail("john.doe@example.com");
        customer1.setCustomerPhoneNumber("1234567890");
        customer1.setCustomerAddress("123 Main St");
        customer1.setCustomerPassword("password123");
        customer1.setCustomerRole(CustomerRole.CUSTOMER);

        CustomerEntity customer2 = new CustomerEntity();
        customer2.setId(UUID.randomUUID());
        customer2.setCustomerFirstName("Jane");
        customer2.setCustomerLastName("Doe");
        customer2.setCustomerDateOfBirth(LocalDate.of(1985, 2, 15));
        customer2.setCustomerEmail("jane.doe@example.com");
        customer2.setCustomerPhoneNumber("0987654321");
        customer2.setCustomerAddress("456 Oak St");
        customer2.setCustomerPassword("securepassword");
        customer2.setCustomerRole(CustomerRole.ADMIN);

        List<CustomerEntity> customerEntities = Arrays.asList(customer1, customer2);

        when(customerRepository.findAll()).thenReturn(customerEntities);
        when(customerMapping.mapToCustomerDto(any(CustomerEntity.class))).thenAnswer(invocation -> {
            CustomerEntity entity = invocation.getArgument(0);
            return new CustomerDto(
                    entity.getId(),
                    entity.getCustomerFirstName(),
                    entity.getCustomerLastName(),
                    entity.getCustomerDateOfBirth(),
                    entity.getCustomerEmail(),
                    entity.getCustomerPhoneNumber(),
                    entity.getCustomerAddress(),
                    entity.getCustomerPassword(),
                    entity.getCustomerRole()
            );
        });

        List<CustomerDto> result = customerService.getAllCustomers();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(customerRepository).findAll();
        verify(customerMapping, times(2)).mapToCustomerDto(any(CustomerEntity.class));
    }

    @Test
    void getCustomersYoungerThan24() {
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(UUID.randomUUID());
        customer1.setCustomerFirstName("John");
        customer1.setCustomerLastName("Doe");
        customer1.setCustomerDateOfBirth(LocalDate.of(2000, 5, 20));  // younger than 24
        customer1.setCustomerEmail("john.doe@example.com");
        customer1.setCustomerPhoneNumber("1234567890");
        customer1.setCustomerAddress("123 Main St");
        customer1.setCustomerPassword("password123");
        customer1.setCustomerRole(CustomerRole.CUSTOMER);

        CustomerEntity customer2 = new CustomerEntity();
        customer2.setId(UUID.randomUUID());
        customer2.setCustomerFirstName("Jane");
        customer2.setCustomerLastName("Doe");
        customer2.setCustomerDateOfBirth(LocalDate.of(2005, 7, 15));  // younger than 24
        customer2.setCustomerEmail("jane.doe@example.com");
        customer2.setCustomerPhoneNumber("0987654321");
        customer2.setCustomerAddress("456 Oak St");
        customer2.setCustomerPassword("securepassword");
        customer2.setCustomerRole(CustomerRole.ADMIN);

        CustomerEntity customer3 = new CustomerEntity();
        customer3.setId(UUID.randomUUID());
        customer3.setCustomerFirstName("Alice");
        customer3.setCustomerLastName("Smith");
        customer3.setCustomerDateOfBirth(LocalDate.of(1990, 3, 10));  // older than 24
        customer3.setCustomerEmail("alice.smith@example.com");
        customer3.setCustomerPhoneNumber("1122334455");
        customer3.setCustomerAddress("789 Birch St");
        customer3.setCustomerPassword("mypassword");
        customer3.setCustomerRole(CustomerRole.CUSTOMER);

        List<CustomerEntity> customerEntities = Arrays.asList(customer1, customer2, customer3);

        when(customerRepository.findByCustomerDateOfBirthAfter(any(LocalDate.class))).thenReturn(Arrays.asList(customer1, customer2));

        when(customerMapping.mapToCustomerDto(customer1)).thenReturn(new CustomerDto(
                customer1.getId(),
                customer1.getCustomerFirstName(),
                customer1.getCustomerLastName(),
                customer1.getCustomerDateOfBirth(),
                customer1.getCustomerEmail(),
                customer1.getCustomerPhoneNumber(),
                customer1.getCustomerAddress(),
                customer1.getCustomerPassword(),
                customer1.getCustomerRole()
        ));

        when(customerMapping.mapToCustomerDto(customer2)).thenReturn(new CustomerDto(
                customer2.getId(),
                customer2.getCustomerFirstName(),
                customer2.getCustomerLastName(),
                customer2.getCustomerDateOfBirth(),
                customer2.getCustomerEmail(),
                customer2.getCustomerPhoneNumber(),
                customer2.getCustomerAddress(),
                customer2.getCustomerPassword(),
                customer2.getCustomerRole()
        ));

        List<CustomerDto> result = customerService.getCustomersYoungerThan24();

        assertNotNull(result);
        assertEquals(2, result.size());


        verify(customerRepository).findByCustomerDateOfBirthAfter(any(LocalDate.class));
        verify(customerMapping, times(2)).mapToCustomerDto(any(CustomerEntity.class));
    }

    @Test
    void getCustomerById() {
        UUID customerId = UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        customer.setCustomerFirstName("John");
        customer.setCustomerLastName("Doe");
        customer.setCustomerDateOfBirth(LocalDate.of(1995, 5, 15));
        customer.setCustomerEmail("john.doe@example.com");
        customer.setCustomerPhoneNumber("1234567890");
        customer.setCustomerAddress("123 Main St");
        customer.setCustomerPassword("password123");
        customer.setCustomerRole(CustomerRole.CUSTOMER);

        CustomerDto customerDto = new CustomerDto(
                customer.getId(),
                customer.getCustomerFirstName(),
                customer.getCustomerLastName(),
                customer.getCustomerDateOfBirth(),
                customer.getCustomerEmail(),
                customer.getCustomerPhoneNumber(),
                customer.getCustomerAddress(),
                customer.getCustomerPassword(),
                customer.getCustomerRole()
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        when(customerMapping.mapToCustomerDto(customer)).thenReturn(customerDto);

        CustomerDto result = customerService.getCustomerById(customerId);

        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals("John", result.getCustomerFirstName());
        assertEquals("Doe", result.getCustomerLastName());

        verify(customerRepository).findById(customerId);
        verify(customerMapping).mapToCustomerDto(customer);
    }

    @Test
    void getCustomerById_customerNotFound() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            customerService.getCustomerById(customerId);
        });

        assertEquals("Customer not found", thrown.getMessage());

        verify(customerRepository).findById(customerId);
    }

    @Test
    void createCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerFirstName("Jane");
        customerDto.setCustomerLastName("Smith");
        customerDto.setCustomerDateOfBirth(LocalDate.of(1990, 6, 20));
        customerDto.setCustomerEmail("jane.smith@example.com");
        customerDto.setCustomerPhoneNumber("0987654321");
        customerDto.setCustomerAddress("456 Another St");
        customerDto.setCustomerPassword("securepassword");
        customerDto.setCustomerRole(CustomerRole.ADMIN);

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setCustomerFirstName("Jane");
        customerEntity.setCustomerLastName("Smith");
        customerEntity.setCustomerDateOfBirth(LocalDate.of(1990, 6, 20));
        customerEntity.setCustomerEmail("jane.smith@example.com");
        customerEntity.setCustomerPhoneNumber("0987654321");
        customerEntity.setCustomerAddress("456 Another St");
        customerEntity.setCustomerPassword("securepassword");
        customerEntity.setCustomerRole(CustomerRole.ADMIN);

        CustomerDto expectedCustomerDto = new CustomerDto(
                customerEntity.getId(),
                customerEntity.getCustomerFirstName(),
                customerEntity.getCustomerLastName(),
                customerEntity.getCustomerDateOfBirth(),
                customerEntity.getCustomerEmail(),
                customerEntity.getCustomerPhoneNumber(),
                customerEntity.getCustomerAddress(),
                customerEntity.getCustomerPassword(),
                customerEntity.getCustomerRole()
        );

        doNothing().when(customerValidation).validateCustomerDto(customerDto);
        when(customerMapping.mapToCustomerEntity(customerDto)).thenReturn(customerEntity);
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        when(customerMapping.mapToCustomerDto(customerEntity)).thenReturn(expectedCustomerDto);
        CustomerDto result = customerService.createCustomer(customerDto);
        assertNotNull(result);
        assertEquals(customerDto.getCustomerFirstName(), result.getCustomerFirstName());
        assertEquals(customerDto.getCustomerLastName(), result.getCustomerLastName());
        assertEquals(customerDto.getCustomerEmail(), result.getCustomerEmail());
        assertEquals(customerDto.getCustomerRole(), result.getCustomerRole());

        verify(customerValidation).validateCustomerDto(customerDto);
        verify(customerMapping).mapToCustomerEntity(customerDto);
        verify(customerRepository).save(customerEntity);
        verify(customerMapping).mapToCustomerDto(customerEntity);
    }

    @Test
    void createCustomer_validationFails() {
        CustomerDto invalidCustomerDto = new CustomerDto();
        invalidCustomerDto.setCustomerEmail("invalid-email");

        doThrow(new IllegalArgumentException("Invalid email format")).when(customerValidation).validateCustomerDto(invalidCustomerDto);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            customerService.createCustomer(invalidCustomerDto);
        });

        assertEquals("Invalid email format", thrown.getMessage());

        verify(customerValidation).validateCustomerDto(invalidCustomerDto);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void updateCustomerById() {
        UUID customerId = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerFirstName("Updated First Name");
        customerDto.setCustomerLastName("Updated Last Name");
        customerDto.setCustomerEmail("updated.email@example.com");

        CustomerEntity existingCustomer = new CustomerEntity();
        existingCustomer.setId(customerId);
        existingCustomer.setCustomerFirstName("Old First Name");
        existingCustomer.setCustomerLastName("Old Last Name");
        existingCustomer.setCustomerEmail("old.email@example.com");

        CustomerEntity updatedCustomerEntity = new CustomerEntity();
        updatedCustomerEntity.setId(customerId);
        updatedCustomerEntity.setCustomerFirstName(customerDto.getCustomerFirstName());
        updatedCustomerEntity.setCustomerLastName(customerDto.getCustomerLastName());
        updatedCustomerEntity.setCustomerEmail(customerDto.getCustomerEmail());

        CustomerDto expectedCustomerDto = new CustomerDto(
                updatedCustomerEntity.getId(),
                updatedCustomerEntity.getCustomerFirstName(),
                updatedCustomerEntity.getCustomerLastName(),
                updatedCustomerEntity.getCustomerDateOfBirth(),
                updatedCustomerEntity.getCustomerEmail(),
                updatedCustomerEntity.getCustomerPhoneNumber(),
                updatedCustomerEntity.getCustomerAddress(),
                updatedCustomerEntity.getCustomerPassword(),
                updatedCustomerEntity.getCustomerRole()
        );

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        doNothing().when(customerValidation).validateCustomerDto(customerDto);
        when(customerMapping.updateCustomerEntityFromDto(existingCustomer, customerDto)).thenReturn(updatedCustomerEntity);
        when(customerRepository.save(updatedCustomerEntity)).thenReturn(updatedCustomerEntity);
        when(customerMapping.mapToCustomerDto(updatedCustomerEntity)).thenReturn(expectedCustomerDto);

        CustomerDto result = customerService.updateCustomerById(customerId, customerDto);

        assertNotNull(result);
        assertEquals(customerDto.getCustomerFirstName(), result.getCustomerFirstName());
        assertEquals(customerDto.getCustomerLastName(), result.getCustomerLastName());
        assertEquals(customerDto.getCustomerEmail(), result.getCustomerEmail());

        verify(customerRepository).findById(customerId);
        verify(customerValidation).validateCustomerDto(customerDto);
        verify(customerMapping).updateCustomerEntityFromDto(existingCustomer, customerDto);
        verify(customerRepository).save(updatedCustomerEntity);
        verify(customerMapping).mapToCustomerDto(updatedCustomerEntity);
    }

    @Test
    void updateCustomerById_customerNotFound() {
        UUID customerId = UUID.randomUUID();
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerFirstName("Updated First Name");

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            customerService.updateCustomerById(customerId, customerDto);
        });

        assertEquals("Customer not found with ID: " + customerId, thrown.getMessage());

        verify(customerRepository).findById(customerId);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void deleteAccount_NotFound() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(customerId));

        verify(customerRepository).findById(customerId);
        verify(customerRepository, times(0)).delete(any());
    }

    @Test
    void getCustomerWithAccountsById_customerNotFound() {
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            customerService.getCustomerWithAccountsById(customerId);
        });

        assertEquals("Customer not found with id: " + customerId, thrown.getMessage());

        verify(customerRepository).findById(customerId);
        verify(accountRepository, never()).findByCustomerId(any());
        verify(customerMapping, never()).mapToCustomerAccountsDto(any(), any());
    }

    @Test
    void getCustomersWithAccounts_ReturnsListOfCustomers() {
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(UUID.randomUUID());
        CustomerEntity customer2 = new CustomerEntity();
        customer2.setId(UUID.randomUUID());
        List<CustomerEntity> customers = Arrays.asList(customer1, customer2);

        CustomerAccountsDto dto1 = new CustomerAccountsDto();
        CustomerAccountsDto dto2 = new CustomerAccountsDto();

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapping.mapToCustomerAccountsDto(customer1, accountRepository)).thenReturn(dto1);
        when(customerMapping.mapToCustomerAccountsDto(customer2, accountRepository)).thenReturn(dto2);

        List<CustomerAccountsDto> result = customerService.getCustomersWithAccounts();

        assertEquals(2, result.size());
        verify(customerRepository).findAll();
        verify(customerMapping, times(2)).mapToCustomerAccountsDto(any(), any());
    }

    @Test
    void getCustomerWithAccountsById_CustomerExists_ReturnsDto() {
        UUID customerId = UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        CustomerAccountsDto expectedDto = new CustomerAccountsDto();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapping.mapToCustomerAccountsDto(customer, accountRepository)).thenReturn(expectedDto);

        CustomerAccountsDto result = customerService.getCustomerWithAccountsById(customerId);

        assertNotNull(result);
        verify(customerRepository).findById(customerId);
        verify(customerMapping).mapToCustomerAccountsDto(customer, accountRepository);
    }


    @Test
    void getCustomersAccountsCards_ReturnsListOfCustomers() {

        CustomerEntity customer1 = new CustomerEntity();
        CustomerEntity customer2 = new CustomerEntity();
        List<CustomerEntity> customers = Arrays.asList(customer1, customer2);

        CustomerAccountsCardsDto dto1 = new CustomerAccountsCardsDto();
        CustomerAccountsCardsDto dto2 = new CustomerAccountsCardsDto();

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapping.mapToCustomerAccountsCardsDto(customer1, accountRepository, cardRepository)).thenReturn(dto1);
        when(customerMapping.mapToCustomerAccountsCardsDto(customer2, accountRepository, cardRepository)).thenReturn(dto2);

        List<CustomerAccountsCardsDto> result = customerService.getCustomersAccountsCards();

        assertEquals(2, result.size());
        verify(customerRepository).findAll();
        verify(customerMapping, times(2)).mapToCustomerAccountsCardsDto(any(), any(), any());
    }

    @Test
    void getCustomerAccountsCardsById_CustomerExists_ReturnsDto() {
        UUID customerId = UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity();
        CustomerAccountsCardsDto expectedDto = new CustomerAccountsCardsDto();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerMapping.mapToCustomerAccountsCardsDto(customer, accountRepository, cardRepository)).thenReturn(expectedDto);

        CustomerAccountsCardsDto result = customerService.getCustomerAccountsCardsById(customerId);

        assertNotNull(result);
        verify(customerRepository).findById(customerId);
        verify(customerMapping).mapToCustomerAccountsCardsDto(customer, accountRepository, cardRepository);
    }


}

