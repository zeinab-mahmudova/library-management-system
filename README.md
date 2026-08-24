# Library Management System (Backend RESTful API)

This project is a high-performance, enterprise-grade Library Management System built using **Spring Boot 3.x** and **Java 21**. The application follows industry best practices, implementing strict architectural layering, comprehensive logging, secure stateless authentication, and automated database migrations.

---

## 🚀 Key Architectural & Technical Features

*   **Production-Ready Architecture:** Implements a decoupled layers model: `Controller ➡️ Service Interface ➡️ Service Implementation (impl) ➡️ Repository ➡️ Database`.
*   **Automated Database Migration:** Managed entirely via **Liquibase** change logs, tracking version control for the PostgreSQL database schema.
*   **Advanced Dynamic Search (JPA Specification):** Includes a dynamic filtering engine using the Criteria API. Allows complex querying across Books by `title`, `author name`, `price range`, and `discount range`.
*   **Robust Stateless Security:** Secured via **Spring Security** and custom **JWT (JSON Web Token)** layers, delivering both short-lived **Access Tokens** (15 mins) and long-lived **Refresh Tokens** (7 days).
*   **Compile-Time Mapping (MapStruct):** Eliminates manual boilerplates by utilizing MapStruct for automated high-performance DTO-to-Entity conversions.
*   **Unified Exception Management:** Centralized global exception routing utilizing `@RestControllerAdvice` to safely trap business violations, database integrity bugs, and bad credentials.
*   **Structured Enterprise Logging:** Powered by **SLF4J** to ensure comprehensive execution tracing across all production service components.
*   **Containerized Environments:** Fully containerized with an optimized **Dockerfile** and multi-container orchestration via **Docker Compose**.

---

## 📂 Structural Overview (Package Conventions)

The source directory follows the strict lowercase Java naming convention:
*   `config/` - Houses Spring Security, JWT Filters, OpenApi, and Caching configurations.
*   `controller/` - Exposes modern REST endpoints adhering to the `/api/v1/` versioning strategy.
*   `dto/` - Data Transfer Objects enforced with `jakarta.validation` annotations.
*   `entity/` - Declarative JPA Domain Models mapped to relational tables.
*   `enums/` - Isolated package for domain-specific static enumerations (e.g., `Role`).
*   `exception/` - Central framework for handling application errors and customer exceptions.
*   `mapper/` - MapStruct mapper definitions mapping presentation layers to underlying domains.
*   `repository/` - Layer interfacing directly with PostgreSQL via JpaRepositories.
*   `service/` - Clear domain abstraction separating core interfaces from concrete business implementations inside `impl/`.

---

## 🛠️ Multi-Profile Environment Configuration

The application implements structured configuration separation tailored for scalable software environments:
1.  **`application.yaml`**: The base bootstrap topology responsible for resolving active run-time environments.
2.  **`application-local.yml`**: Designed for standalone sandbox execution (targets localized PostgreSQL setups with detailed SQL tracing enabled).
3.  **`application-prod.yml`**: Architected for production/live deployments. It references environment abstraction variables (`${DB_URL}`, `${JWT_SECRET}`) for secured system execution.

---

## 🐋 DevOps & Deployment Guide

### 1. Multi-Container Infrastructure Bootup (Recommended)
To launch the backend application along with its PostgreSQL dependency inside isolated Docker containers, execute the following command at the project root directory:
```bash
docker-compose up --build
```
This builds the localized artifact, setups the network bridge, applies Liquibase schemas automatically, and exposes the app on port `8080`.

### 2. Standalone Sandbox Bootup (Local IDE)
1. Ensure a PostgreSQL instance is running and an empty database named `library_db` is present.
2. Verify that the active profile variable in `application.yaml` points to `local`.
3. Launch the Spring Boot main application class. Liquibase will trigger migration schemas automatically.

---

## 📝 API Documentation Interface (Swagger UI)

Interactive REST API documentation and live endpoint testing capability are securely configured via Swagger. Once the backend application instance runs successfully, access the gateway locally at:

```text
http://localhost:8080/swagger-ui/index.html
```

*Note: For invoking secured endpoints, click the **Authorize** lock emblem at the top right of the Swagger interface and append your authorization hash using the standard format: `Bearer <your_access_token>`.*
