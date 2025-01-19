package io.bankingsystem.banking.service.validations;

import io.bankingsystem.banking.model.dto.CustomerDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerValidation {

    public void validateCustomerDto(CustomerDto customerDto) {
        if (customerDto.getCustomerFirstName() == null || customerDto.getCustomerFirstName().isEmpty()) {
            throw new IllegalArgumentException("Customer first name is required");
        }
        if (customerDto.getCustomerLastName() == null || customerDto.getCustomerLastName().isEmpty()) {
            throw new IllegalArgumentException("Customer last name is required");
        }
        if (customerDto.getCustomerDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }
        if (customerDto.getCustomerDateOfBirth().isAfter(LocalDate.now().minusYears(15))) {
            throw new IllegalArgumentException("Customer must be at least 15 years old");
        }
        if (customerDto.getCustomerEmail() == null || customerDto.getCustomerEmail().isEmpty()) {
            throw new IllegalArgumentException("Customer email is required");
        }
        validateEmail(customerDto.getCustomerEmail());
        if (customerDto.getCustomerPhoneNumber() == null || customerDto.getCustomerPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Customer phone number is required");
        }
        validatePhoneNumber(customerDto.getCustomerPhoneNumber());
        if (customerDto.getCustomerAddress() == null || customerDto.getCustomerAddress().isEmpty()) {
            throw new IllegalArgumentException("Customer address is required");
        }
        if (customerDto.getCustomerPassword() == null || customerDto.getCustomerPassword().isEmpty()) {
            throw new IllegalArgumentException("Customer password is required");
        }
        validatePassword(customerDto.getCustomerPassword());
    }

    public void validatePassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one number, and one special character.");
        }
    }

    public void validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public void validatePhoneNumber(String phoneNumber) {

        String phoneRegex = "^\\+?[1-9]\\d{1,14}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
    }
}
