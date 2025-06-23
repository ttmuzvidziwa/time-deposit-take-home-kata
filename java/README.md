
# XA Bank – Time Deposit Refactoring Kata (Java)

This repository contains the Java-based solution to the XA Bank Time Deposit Refactoring Kata. The goal is to refactor existing logic while building a clean, extensible, and testable API that supports time deposit operations.

---

## 🧩 Features Implemented

- ✅ REST API to **retrieve all time deposit accounts**
- ✅ REST API to **update balances** for all accounts
- ✅ Interest logic for all three plans:
    - **Basic Plan**: 1% monthly interest (after 30 days)
    - **Student Plan**: 3% monthly interest (but none after 1 year)
    - **Premium Plan**: 5% monthly interest (only after 45 days)
- ✅ Domain-driven design with clearly separated service and controller layers
- ✅ Swagger/OpenAPI documentation with descriptive annotations
- ✅ In-memory H2 database for testing and development

---

## 📦 Tech Stack

- Java 17
- Spring Boot 3.4.6
- Maven
- JPA (Hibernate)
- H2 (in-memory DB)
- Swagger (springdoc-openapi)
- JUnit 5 + Mockito

---

## 🚀 How to Run

1. **Clone the repo**:
   ```bash
   git clone https://github.com/ttmuzvidziwa/time-deposit-take-home-kata.git
   cd java
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Swagger UI**:
   Visit [http://localhost:8080/api/v1/xa-time-deposits/swagger-ui.html](http://localhost:8080/api/v1/xa-time-deposits/swagger-ui.html) to explore the API.

---

## 📡 API Endpoints

### `GET /api/v1/time-deposit`
Returns all time deposits with full withdrawal history.

### `POST /api/v1/time-deposit/update`
Triggers interest calculation and updates all balances in the system.

---

## 🧪 Running Tests

```bash
mvn test
```

Tests are written using JUnit 5 and Mockito. You can run them directly from IntelliJ or your terminal.

---

## 📁 Project Structure

```
src/
├── api/controller       # REST controllers
├── domain/model         # Entities and DTOs
├── domain/service       # Business logic
├── repository           # JPA repositories
└── TimeDepositApplication.java
```

---

## 🛠️ Developer Notes

- `TimeDepositCalculator#updateBalance()` was preserved with no signature changes.
- Refactored logic adheres to SOLID principles and encourages future plan expansion.
- Swagger annotations help document and test the API quickly.
- Optional: implement TestContainers or external DB for future robustness.

---

## ✉️ Submission

This solution satisfies the provided challenge requirements. See `/java` for source.