# Social Network

This project is a social network application built for the Highload Architect course. It implements a REST API for user registration, authentication, friend management, post creation, and messaging.

## Tech Stack

- Java 21
- Spring Boot
- PostgreSQL
- JDBC Driver
- HikariCP (Connection Pool)
- jOOQ (SQL Query Builder)
- Flyway (Database Migrations)
- Spring Cache with Caffeine
- Docker & Docker Compose
- JMeter (Load Testing)
- OpenAPI Documentation (SpringDoc)

## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker & Docker Compose

### Running with Docker

The easiest way to start the application is using Docker Compose:

```bash
docker-compose up --build
```

This will start the PostgreSQL database and the application. The application will be available at http://localhost:8080.

### Running Locally

1. Start PostgreSQL:

```bash
docker-compose up postgres
```

2. Build and run the application:

```bash
./mvnw clean package
java -jar target/highload-1.0.0.jar
```

## API Documentation

Once the application is running, you can access the OpenAPI documentation at:

http://localhost:8080/swagger-ui.html

## Database Schema

The database schema consists of the following tables:

- `users` - User profiles
- `friends` - Friend relationships
- `posts` - User posts
- `dialog_messages` - Messages between users

## Project Structure

- `config` - Configuration classes
- `controller` - REST controllers
- `dto` - Data Transfer Objects
- `exception` - Exception handling
- `model` - Domain models
- `repository` - Data access layer
- `service` - Business logic

## Load Testing

A JMeter load test plan is provided in the `load-tests` directory. You can use it to test the performance of the application.

## Features

1. User Management
   - User registration
   - Authentication
   - Profile viewing
   - User search

2. Friend Management
   - Add friends
   - Remove friends

3. Posts
   - Create posts
   - Update posts
   - Delete posts
   - View post feed

4. Messaging
   - Send messages
   - View dialog history

## Highload Optimizations

The project includes several optimizations for high-load scenarios:

1. **Connection Pooling**
   - HikariCP is used for efficient database connection management

2. **Caching**
   - User profiles and posts are cached to reduce database load
   - Caffeine cache with configurable TTL and size limits

3. **Database Indexes**
   - Optimized indexes for common query patterns
   - Indexes on foreign keys for join operations

4. **Pagination**
   - Post feed API supports pagination to limit response size

5. **Error Handling**
   - Comprehensive error handling with request IDs for traceability

6. **Performance Monitoring**
   - SQL query logging for performance analysis
   - JMeter load test plan for performance testing

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
