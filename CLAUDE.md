# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commit and Versioning Standards

Follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) and [Semantic Versioning 2.0.0](https://semver.org/) specifications.

**See [COMMIT_AND_VERSION_GUIDE.md](COMMIT_AND_VERSION_GUIDE.md) for complete rules and examples.**

**Quick Reference:**
- Commit format: `<type>[scope]: <description>`
- Types: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `build`, `ci`, `chore`
- Breaking changes: Add `!` after type or use `BREAKING CHANGE:` footer
- Versioning: MAJOR.MINOR.PATCH (breaking.feature.bugfix)

## Build and Development Commands

**Build and Package:**
```bash
mvn clean package
```

**Database Operations:**
```bash
# Run database migrations
mvn liquibase:update

# Rollback last migration
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

**Running the Application:**
```bash
# Local development
java -jar target/social-network-1.0.0.jar

# Docker (recommended)
docker-compose up

# Application runs on http://localhost:8080
```

**Load Testing:**
```bash
# Run JMeter load tests
jmeter -n -t load-test-plan.jmx -l results.jtl
```

## Architecture Overview

This is a **hexagonal architecture** (clean architecture) Java application with custom HTTP server implementation:

### Layer Structure
- **Application Layer** (`com.rootbr.network.application`): Core business logic, commands, ports (interfaces)
- **Inbound Adapters** (`com.rootbr.network.adapter.in.rest`): REST API handlers, filters, custom HTTP server
- **Outbound Adapters** (`com.rootbr.network.adapter.out.db`): Database implementations using raw JDBC

### Key Architectural Patterns
- **Command Pattern**: All business operations are implemented as commands in `application/command/`
- **Visitor Pattern**: Data processing and transformation in `application/visitor/`
- **Port-Adapter Pattern**: Interfaces in `application/port/`, implementations in `adapter/`

### Technology Stack
- **Java 21** with virtual threads for high concurrency
- **Custom HTTP Server** using `com.sun.net.httpserver` (no Spring Framework)
- **PostgreSQL** with raw JDBC and HikariCP connection pooling
- **Liquibase** for database migrations
- **JWT** for authentication (JJWT 0.12.6)
- **BCrypt** for password hashing
- **Jackson 2.19.0** for JSON processing
- **Commons Configuration2** for flexible configuration management

## Database Schema

Core tables:
- `users`: User profiles (first_name, last_name, birth_date, biography, city)
- `principals`: Authentication data (id, encoded_password)
- Additional tables for chats, messages, posts, and friendships

## API Structure

All endpoints follow REST principles with JWT authentication where required. See `openapi.json` for complete API specification.

**Authentication Flow:**
1. `POST /user/register` - Register new user
2. `POST /login` - Get JWT token
3. Use `Authorization: Bearer {token}` header for authenticated endpoints

## Configuration

- Main config: `src/main/resources/config.yml`
- Environment variable overrides: Any config.yml property can be overridden using uppercase with underscores
  - Examples: `DATASOURCE_JDBCURL`, `SERVER_PORT`, `LOGGING_LEVEL_ROOT`
- Key environment variables:
  - `DATASOURCE_JDBCURL`: Database connection URL
  - `DATASOURCE_USERNAME`: Database username
  - `DATASOURCE_PASSWORD`: Database password
  - `JWT_SECRET`: JWT signing secret
  - `JWT_EXPIRATION`: JWT token expiration time
- HikariCP connection pool configured via `minimumIdle` and other pool settings

## Development Notes

**Custom HTTP Server Implementation:**
- Located in `adapter/in/rest/server/`
- Uses Java's built-in `com.sun.net.httpserver.HttpServer`
- Request routing in `RouterHttpHandler`
- Filter chain for CORS, authentication, error handling

**Business Logic:**
- All operations are commands that extend abstract class `Command`
- Commands are executed through `Principal` and created through `SocialNetworkApplication`
- Commands have `authorize()` method that returns boolean for access control
- Database operations use visitor pattern for result processing
- REST handlers support path variables and query parameters through updated interface

**Error Handling:**
- Custom error interceptor in `filter/ErrorInterceptor`
- Standard HTTP status codes with JSON error responses
- Limited exception handling - area for improvement

**Security:**
- JWT tokens for authentication
- BCrypt password hashing with cost factor 7
- CORS enabled for cross-origin requests
- Command-level authorization with boolean return flags

**Logging:**
- Dual configuration: `logback.xml` (production) and `logback-local.xml` (development)
- Production: File logging with daily rotation, 100MB max files, 30-day retention
- Development: Console logging only
- Logs written to `logs/` directory (Docker volume mounted)

## Testing Gap

**Important:** This codebase currently has no automated tests. When adding features:
1. Consider adding unit tests for command implementations
2. Integration tests for database operations
3. API endpoint tests using the custom HTTP server

Test framework recommendations: JUnit 5