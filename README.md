# SimpleBanking

A **Spring Boot** backend application providing REST APIs for a comprehensive banking system dashboard. This system efficiently manages customers, accounts, cards, and transactions through a secure and scalable architecture.

---

## 🚀 Features

- **Customer Management**: CRUD operations for customers.
- **Account Operations**: Manage accounts seamlessly.
- **Card Management**: Support for various card types.
- **Transaction Tracking**: Monitor and analyze transactions.
- **Secure Authentication**: Basic authentication.
- **RESTful API Endpoints**: Clean and efficient API design.

---

## 🛠️ Technologies

- **Java**: 17
- **Spring Boot**: 3.4.1
- **Spring Security**: Role-based access control.
- **Spring Data JPA**: For database interactions.
- **Hibernate**: ORM for persistence.
- **MySQL**: Relational database.
- **Bcrypt**: Secure password handling.
- **HikariCP**: High-performance connection pooling.
- **Maven**: Build and dependency management.

---

## 🗂️ Database Schema

The application uses five main entities:

1. **Customer**
2. **Account**
3. **Card**
4. **CardType**
5. **Transaction**

---

## 📡 API Endpoints

### **Customer Endpoints:**
- **GET** `/api/customers` - Retrieve all customers.
- **GET** `/api/customers/{id}` - Retrieve a specific customer by ID.
- **GET** `/api/customers/accounts` - Retrieve customers and their accounts.
- **GET** `/api/customers/{id}/accounts` - Retrieve a specific customer by ID and their associated accounts.
- **POST** `/api/customers` - Create a new customer.
- **PUT** `/api/customers/{id}` - Update customer details.
- **PATCH** `/api/customers/address{id}` - Update customer address.
- **PATCH** `/api/customers/email/{id}` - Update customer email.
- **PATCH** `/api/customers/password/{id}` - Update customer password.
- **DELETE** `/api/customers/{id}` - Delete a customer.

### **Account Endpoints:**
- **GET** `/api/accounts` - Retrieve all accounts.
- **GET** `/api/accounts/{id}` - Retrieve a specific account by ID.
- **POST** `/api/accounts` - Create a new account.
- **PUT** `/api/accounts/{id}` - Update account details.
- **PATCH** `/api/accounts/balance/{id}` - Update account current balance.
- **PATCH** `/api/accounts/status/{id}` - Update account status.
- **PATCH** `/api/accounts/date-closed/{id}` - Update account date closed.
- **DELETE** `/api/accounts/{id}` - Delete an account.

> Similar endpoints exist for **Card**, **CardType**, and **Transaction** entities.

---

## 🔒 Security

- **Basic Authentication**: Secure Authentication.
- **Role-Based Access Control**: Granular permissions.
- **Secure Password Handling**: Passwords hashed with **Bcrypt**.
- **Protected Endpoints**: Ensures only authorized access.

---

## ⚙️ Setup and Installation

### 1. Clone the Repository
```bash
git clone [https://github.com/amarjahiji/simpleBanking]
```

### 2. Configure Database Properties
Edit the application.properties file:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/db_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build the project
```bash
mvn clean install
```

### 3. Run the application
```bash
mvn spring-boot:run
```

