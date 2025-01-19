package io.bankingsystem.banking.service.services;

import io.bankingsystem.banking.model.dto.CustomerAccountsDto;
import io.bankingsystem.banking.model.dto.CustomerDto;
import io.bankingsystem.banking.model.entity.AccountEntity;
import io.bankingsystem.banking.model.entity.CustomerEntity;
import io.bankingsystem.banking.model.enum_fields.AccountStatus;
import io.bankingsystem.banking.model.enum_fields.AccountType;
import io.bankingsystem.banking.repository.AccountRepository;
import io.bankingsystem.banking.repository.CustomerRepository;
import io.bankingsystem.banking.service.mappings.CustomerMapping;
import io.bankingsystem.banking.service.validations.CustomerValidation;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerMapping mappingService;
    @Mock
    private CustomerValidation validationService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomers_ShouldReturnMappedCustomers() {
        
        List<CustomerEntity> customers = List.of(new CustomerEntity());
        CustomerDto mappedDto = new CustomerDto();
        when(customerRepository.findAll()).thenReturn(customers);
        when(mappingService.mapToCustomerDto(any())).thenReturn(mappedDto);

        
        List<CustomerDto> result = customerService.getAllCustomers();

        
        assertEquals(1, result.size());
        verify(customerRepository).findAll();
        verify(mappingService).mapToCustomerDto(any());
    }

    @Test
    void getCustomerById_WhenCustomerExists_ShouldReturnMappedCustomer() {
        
        UUID id = UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity();
        CustomerDto mappedDto = new CustomerDto();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(mappingService.mapToCustomerDto(customer)).thenReturn(mappedDto);

        
        CustomerDto result = customerService.getCustomerById(id);

        
        assertNotNull(result);
        verify(customerRepository).findById(id);
        verify(mappingService).mapToCustomerDto(customer);
    }

    @Test
    void getCustomerById_WhenCustomerNotFound_ShouldThrowException() {
        
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomerById(id));
    }

    @Test
    void getCustomersWithAccounts_ShouldReturnCustomersWithTheirAccounts() {
        
        CustomerEntity customer = new CustomerEntity();
        customer.setId(UUID.randomUUID());
        List<CustomerEntity> customers = List.of(customer);
        List<AccountEntity> accounts = List.of(createAccountEntity(customer));

        when(customerRepository.findAll()).thenReturn(customers);
        when(accountRepository.findByCustomerId(customer.getId())).thenReturn(accounts);

        
        List<CustomerAccountsDto> result = customerService.getCustomersWithAccounts();

        
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getAccounts().size());
        verify(customerRepository).findAll();
        verify(accountRepository).findByCustomerId(customer.getId());
    }

    @Test
    void createCustomer_ShouldCreateAndReturnCustomer() {
        
        CustomerDto inputDto = createCustomerDto();
        CustomerEntity mappedEntity = new CustomerEntity();
        CustomerEntity savedEntity = new CustomerEntity();
        CustomerDto mappedDto = new CustomerDto();

        when(mappingService.mapToCustomerEntity(inputDto)).thenReturn(mappedEntity);
        when(customerRepository.save(any())).thenReturn(savedEntity);
        when(mappingService.mapToCustomerDto(savedEntity)).thenReturn(mappedDto);

        
        CustomerDto result = customerService.createCustomer(inputDto);

        
        assertNotNull(result);
        verify(validationService).validateCustomerDto(inputDto);
        verify(customerRepository).save(any());
    }

    @Test
    void updateCustomerById_WhenCustomerExists_ShouldUpdateAndReturnCustomer() {
        
        UUID id = UUID.randomUUID();
        CustomerDto inputDto = createCustomerDto();
        CustomerEntity existingCustomer = new CustomerEntity();
        CustomerEntity savedCustomer = new CustomerEntity();
        CustomerDto mappedDto = new CustomerDto();
        String encodedPassword = "encodedPassword";

        when(customerRepository.findById(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any())).thenReturn(savedCustomer);
        when(mappingService.mapToCustomerDto(savedCustomer)).thenReturn(mappedDto);
        when(passwordEncoder.encode(inputDto.getCustomerPassword())).thenReturn(encodedPassword);

        
        CustomerDto result = customerService.updateCustomerById(id, inputDto);

        
        assertNotNull(result);
        verify(validationService).validateCustomerDto(inputDto);
        verify(customerRepository).save(any());
    }

    @Test
    void updatePassword_ShouldUpdatePassword() {
        
        UUID id = UUID.randomUUID();
        String newPassword = "NewPassword123!";
        String encodedPassword = "encodedPassword";
        CustomerEntity customer = new CustomerEntity();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        
        customerService.updatePassword(id, newPassword);

        
        verify(validationService).validatePassword(newPassword);
        verify(customerRepository).save(customer);
        assertEquals(encodedPassword, customer.getCustomerPassword());
    }

    @Test
    void updateEmail_ShouldUpdateEmail() {
        
        UUID id = UUID.randomUUID();
        String newEmail = "new.email@example.com";
        CustomerEntity customer = new CustomerEntity();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        
        customerService.updateEmail(id, newEmail);

        
        verify(validationService).validateEmail(newEmail);
        verify(customerRepository).save(customer);
        assertEquals(newEmail, customer.getCustomerEmail());
    }

    @Test
    void deleteCustomer_WhenCustomerExists_ShouldDeleteCustomer() {
        
        UUID id = UUID.randomUUID();
        CustomerEntity customer = new CustomerEntity();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        
        customerService.deleteCustomer(id);

        
        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomer_WhenCustomerNotFound_ShouldThrowException() {
        
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(id));
    }

    private CustomerDto createCustomerDto() {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerFirstName("John");
        dto.setCustomerLastName("Doe");
        dto.setCustomerEmail("john.doe@example.com");
        dto.setCustomerPhoneNumber("+1234567890");
        dto.setCustomerAddress("123 Main St");
        dto.setCustomerPassword("Password123!");
        dto.setCustomerDateOfBirth(LocalDate.now().minusYears(20));
        return dto;
    }

    private AccountEntity createAccountEntity(CustomerEntity customer) {
        AccountEntity account = new AccountEntity();
        account.setId(UUID.randomUUID());
        account.setAccountNumber("123456789");
        account.setAccountType(AccountType.CHECKING);
        account.setAccountCurrentBalance(BigDecimal.valueOf(1000));
        account.setAccountDateOpened(LocalDateTime.now());
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);
        return account;
    }
}