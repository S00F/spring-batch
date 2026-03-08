# Spring Batch Demo Project

## 📌 Project Overview

This is a **Spring Boot Batch Processing Demo** application that demonstrates
core Spring Batch concepts for processing data from CSV files.

The application showcases:

-   Reading data from CSV files
-   Processing business data with item processors
-   Persisting results into a database using JPA
-   Job execution with listeners
-   Multi-step job flows with conditional branching

---

## 🏗️ Architecture

The project follows a layered single-module structure:

-   **Config** - Spring Batch job and step configuration
-   **Model** - Domain entities (User, UserDto)
-   **Batch** - ItemReader, ItemProcessor, ItemWriter, Tasklets
-   **Service** - Business logic and mapping
-   **Repository** - Data persistence

---

## 📂 Project Structure

    Spring-batch (single-module)
    │
    ├── src/main/java/com/batch/demo/
    │   ├── BatchApplication.java          # Main application entry
    │   ├── config/
    │   │   └── BatchConfig.java           # Job & Step configuration
    │   ├── model/
    │   │   ├── User.java                  # JPA Entity
    │   │   └── UserDto.java               # Data Transfer Object
    │   ├── batch/
    │   │   ├── UserReader.java            # CSV ItemReader
    │   │   ├── UserItemProcessor.java     # Business processing
    │   │   ├── UserDtoWriter.java         # JPA ItemWriter
    │   │   ├── CleanupTasklet.java         # Pre-processing tasklet
    │   │   ├── SummaryTasklet.java        # Post-processing tasklet
    │   │   └── JobCompletionListener.java # Job lifecycle listener
    │   ├── service/
    │   │   └── UserService.java           # Mapping service
    │   ├── mapper/
    │   │   └── UserMapper.java            # MapStruct mapper
    │   └── repository/
    │       └── UserRepository.java        # JPA Repository
    │
    └── src/main/resources/
        ├── application.yml               # Application configuration
        └── users.csv                      # Sample input data

---

## ⚙️ Technologies Used

### Backend

-   Java 17
-   Spring Boot 3.2.5
-   Spring Batch
-   Spring Data JPA
-   Hibernate
-   MapStruct

### Database

-   H2 Database (in-memory)

### Build

-   Maven

---

## 🔄 Batch Processing Flow

    users.csv
       ↓
    UserReader (FlatFileItemReader)
       ↓
    UserItemProcessor (business logic)
       ↓
    UserDtoWriter (JpaItemWriter)
       ↓
    H2 Database

Job execution flow:

1.  **CleanupStep** - Clears existing data from users table
2.  **userMappingStep** - Reads CSV, processes, writes to database (chunk size: 3)
3.  **summaryStep** - Logs job completion summary

---

## 🚀 Features

✅ Chunk-oriented batch processing\
✅ Restartable jobs\
✅ Conditional job flows using BatchStatus\
✅ Job incrementer support (RunIdIncrementer)\
✅ Multi-step jobs with tasklets\
✅ Database persistence using JPA\
✅ Job execution listeners\
✅ Clean separation of concerns

---

## ▶️ Running the Application

### Build project

```bash
mvn clean install
```

### Run application

```bash
mvn spring-boot:run
```

The batch job will automatically execute on startup via CommandLineRunner.

---

## 🌐 Access H2 Console

    http://localhost:8080/h2-console

Datasource configuration:

    JDBC URL: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    User: admin
    Password: admin

---

## 📊 Example Use Case

The batch job imports users from a CSV file, processes the data, and stores
it into the database.

Example CSV (`users.csv`):

    id,name,email
    1,Alice,alice@example.com
    2,Bob,bob@example.com
    3,Charlie,charlie@example.com

---

## 🧠 Design Principles

-   Single-module simplicity
-   Separation of concerns (Reader/Processor/Writer)
-   Declarative job configuration via @Configuration
-   Transactional batch operations
-   Listener pattern for job lifecycle

---

## 📈 Future Improvements

-   Multi-module Maven structure
-   REST API for job triggering
-   Job scheduling with @Scheduled
-   Error handling and skip policy
-   Parallel step execution
-   Unit and integration tests
-   PostgreSQL/MySQL production profile

---

## 📄 License

This project is for learning and demonstration purposes.
