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

- **Java**  17
- **Spring Boot**  3.4.1
- **J-Unit**  5
- **MySQL**  5


- **Spring Security** - For access control.
- **Spring Data JPA** - For database interactions.
- **Hibernate**       - For ORM persistence.
- **Bcrypt**          - For secure password handling.
- **HikariCP**        - For connection pooling.
- **Maven**           - For building and dependency management.

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
- **GET** `/customers` - Retrieve all customers.
- **GET** `/customers/young` - Retrieve all customers younger than 25.
- **GET** `/customers/old` - Retrieve all customers older than 64.
- **GET** `/customers/{id}` - Retrieve a specific customer by ID.
- **GET** `/customers/accounts` - Retrieve all customers and their accounts.
- **GET** `/customers/accounts/{id}` - Retrieve a specific customer by ID and their associated accounts.
- **GET** `/customers/accounts/cards` - Retrieve all customers and their accounts and cards associated to their accounts.
- **GET** `/customers/accounts/cards/{id}` - Retrieve a specific customer by ID and their associated accounts and cards associated to their accounts.
- **POST** `/customers/create` - Create a new customer.
- **PUT** `/customers/{id}` - Update customer details.
- **PATCH** `/customers/update/address{id}` - Update customer address.
- **PATCH** `/customers/update/email/{id}` - Update customer email.
- **PATCH** `/customers/update/password/{id}` - Update customer password.
- **DELETE** `/customers/delete/{id}` - Delete a customer.

### **Account Endpoints:**
- **GET** `/accounts` - Retrieve all accounts.
- **GET** `/accounts/{id}` - Retrieve a specific account by ID.
- **GET** `/accounts/cards` - Retrieve all accounts and their cards.
- **GET** `/accounts/cards/{id}` - Retrieve a specific account by ID and their associated cards.
- **GET** `/accounts/transactions` - Retrieve all accounts and their transactions.
- **GET** `/accounts/transactions/{id}` - Retrieve a specific account by ID and their associated transactions.
- **POST** `/accounts` - Create a new account.
- **PUT** `/accounts/{id}` - Update account details.
- **PATCH** `/accounts/balance/{id}` - Update account current balance.
- **PATCH** `/accounts/status/{id}` - Update account status.
- **PATCH** `/accounts/date-closed/{id}` - Update account date closed.
- **DELETE** `/accounts/{id}` - Delete an account.

> Similar endpoints exist for **Card**, **CardType**, and **Transaction** entities.

---

## 🔒 Security

- **Basic Authentication**: Secure Authentication.
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

