# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.


## Build and Development Commands

```bash
mvn clean package
# Run database migrations
mvn liquibase:update
# Run application via jlink
jdeps --multi-release 21 --ignore-missing-deps --print-module-deps ./social-network-1.0.0.jar > /tmp/modules.txt && \
    jlink --compress=2 --strip-debug --no-header-files --no-man-pages \
          --add-modules $(cat /tmp/modules.txt) \
          --output jlink-runtime
./jlink-runtime/bin/java -jar social-network-1.0.0.jar
# Rollback last migration
mvn liquibase:rollback -Dliquibase.rollbackCount=1
# Running the Application: Local development
java -jar target/social-network-1.0.0.jar
# Running the Application: Docker (recommended)
docker-compose up
# Application runs on http://localhost:8080
# Run JMeter load tests
jmeter -n -t load-test-plan.jmx -l results.jtl
```

## Architecture Overview

This is a **hexagonal architecture** (clean architecture) Java application with custom HTTP server implementation

## Database Liquibase Schema

@db/changelog/01-create-initial-schema.xml
@db/changelog/db.changelog-master.xml

## API Structure

All endpoints follow REST principles with JWT authentication where required. See @openapi.json for complete API specification.

**Authentication Flow:**
1. `POST /user/register` - Register new user
2. `POST /login` - Get JWT token
3. Use `Authorization: Bearer {token}` header for authenticated endpoints

## Configuration

- Main config: @src/main/resources/config.yml
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
- BCrypt password hashing
- CORS enabled for cross-origin requests
- Command-level authorization with boolean return flags

**Logging:**
- Dual configuration: @src/main/resources/logback.xml (production) and @logback-local.xml (development)
- Production Logs written to `logs/` directory (Docker volume mounted)
