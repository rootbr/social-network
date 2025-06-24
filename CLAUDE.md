# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Social Network Application** is a high-performance social media platform built with **pure Java 21** and **hexagonal architecture**. The system implements a social network with user management, posts, friendships, and messaging capabilities using clean architecture patterns and custom HTTP server implementation.

**Technology Stack:**
- **Backend**: Pure Java 21 (no heavyweight frameworks in core domain)
- **Database**: PostgreSQL with HikariCP connection pooling
- **Schema Management**: Liquibase migrations
- **Authentication**: JWT tokens with BCrypt password hashing
- **JSON Processing**: Jackson streaming API
- **HTTP Server**: Custom implementation using `com.sun.net.httpserver.HttpServer`
- **Configuration**: YAML-based with environment variable overrides
- **Build**: Maven with shade plugin for fat JAR packaging
- **Deployment**: Docker Compose with containerized PostgreSQL

## Architecture Overview

This application follows **hexagonal architecture** (ports and adapters pattern) with pure Java implementation emphasizing classical design patterns over framework magic.

### Core Architecture Principles

1. **Pure Java Core**: No reflection or heavy frameworks in application core - uses classic design patterns
2. **Hexagonal Architecture**: Clear separation between domain, application, and adapter layers
3. **Command Pattern**: All state changes through commands with decorator patterns
4. **Visitor Pattern**: Data extraction without breaking encapsulation
5. **Memory Efficient**: Streams data directly to JSON without intermediate DTOs

### Architecture Layers

```
┌─────────────────────────────────────────┐
│           Adapter Layer (In)            │
│  REST Handlers, Filters, HTTP Server    │
├─────────────────────────────────────────┤
│          Application Layer              │
│    Commands, Principal, Use Cases       │
├─────────────────────────────────────────┤
│            Domain Layer                 │
│       Business Logic, Entities          │
├─────────────────────────────────────────┤
│          Adapter Layer (Out)            │
│    Database Ports, External Services    │
└─────────────────────────────────────────┘
```

## Build and Development Commands

```bash
# Build application
mvn clean package

# Run database migrations
mvn liquibase:update

# Rollback last migration
mvn liquibase:rollback -Dliquibase.rollbackCount=1

# Running the Application: Local development
java -jar target/social-network-1.0.0.jar

# Running the Application: Docker (recommended)
docker-compose up

# Application runs on http://localhost:8080

# Run JMeter load tests
jmeter -n -t load-test-plan.jmx -l results.jtl

# Advanced: Run with jlink optimized runtime
jdeps --multi-release 21 --ignore-missing-deps --print-module-deps ./social-network-1.0.0.jar > /tmp/modules.txt && \
    jlink --compress=2 --strip-debug --no-header-files --no-man-pages \
          --add-modules $(cat /tmp/modules.txt) \
          --output jlink-runtime
./jlink-runtime/bin/java -jar social-network-1.0.0.jar
```

## Core Application Components

### 1. Application Entry Point

**RestService** (`src/main/java/com/rootbr/network/adapter/in/rest/RestService.java:36-224`)
- Main application entry point and dependency injection container
- Configures and wires all components: HTTP server, database, security, JSON processing
- Sets up the complete REST API endpoint routing with filter chains
- Uses virtual threads (`Executors.newVirtualThreadPerTaskExecutor()`) for high concurrency

**Key responsibilities:**
- Application bootstrapping and configuration loading
- Component assembly and dependency injection
- HTTP server configuration and endpoint registration
- Virtual thread executor setup for request handling

### 2. Core Application Logic

**SocialNetworkApplication** (`src/main/java/com/rootbr/network/application/SocialNetworkApplication.java:17-193`)
- Central application facade implementing all business use cases
- Factory for creating commands that encapsulate business operations
- Manages authentication and user session state
- Provides anonymous and authenticated principals

**Key methods:**
- `registerUserCommand()`: User registration with password encoding
- `getUserByIdCommand()`: User profile retrieval
- `searchUsersCommand()`: User search functionality
- `login()`: Authentication and principal creation
- `createPostCommand()`, `updatePostCommand()`, `deletePostCommand()`: Post management (TODO implementations)
- `addFriendCommand()`, `removeFriendCommand()`: Friend relationship management (TODO implementations)
- `sendMessageCommand()`, `getDialogMessagesCommand()`: Messaging system (TODO implementations)

### 3. Command Pattern Implementation

**Command** (`src/main/java/com/rootbr/network/application/command/Command.java:9-23`)
- Abstract base class for all business operations
- Implements authorization through `authorize()` method
- Tracks execution context with principal information and timestamps
- Template for consistent command execution pattern

**TransactionalCommand** (`src/main/java/com/rootbr/network/application/command/TransactionalCommand.java:7-48`)
- Extends Command with database transaction management
- Implements automatic commit/rollback with connection handling
- Provides transaction lifecycle hooks: `beforeTransaction()`, `afterTransaction()`, `rollback()`
- Ensures data integrity for all state-changing operations

**ReadCommand** (`src/main/java/com/rootbr/network/application/command/ReadCommand.java`)
- Specialized command for read-only operations
- No transaction management overhead
- Used for queries and data retrieval operations

### 4. Security and Principal Management

**Principal** (`src/main/java/com/rootbr/network/application/Principal.java:9-71`)
- Central security context representing authenticated user
- Command executor with authorization checking
- Integrates with password verification and token generation
- Uses internal visitor pattern for safe state updates

**Key features:**
- `execute()`: Runs commands with authorization checks
- `generateToken()`: Creates JWT tokens for session management
- `verifyPassword()`: BCrypt password verification
- `visitor()`: Provides visitor for safe principal state updates

## HTTP Server Architecture

### 1. Custom HTTP Server Implementation

**Server** (`src/main/java/com/rootbr/network/adapter/in/rest/server/Server.java:12-41`)
- Custom HTTP server using Java's built-in `HttpServer`
- Configured with virtual thread executor for high concurrency
- Supports CORS filtering in debug mode
- Centralized request routing through `RouterHttpHandler`

**RouterHttpHandler** (`src/main/java/com/rootbr/network/adapter/in/rest/handler/RouterHttpHandler.java:12-40`)
- Main HTTP request router supporting path variables and static routes
- Implements method-based routing (GET, POST, PUT, DELETE)
- Handles 404 responses for unmatched routes
- Delegates to appropriate `RestHandler` implementations

### 2. Advanced Routing System

**Router** (`src/main/java/com/rootbr/network/adapter/in/rest/server/Router.java:14-103`)
- Tree-based routing with support for path variables (`${id}`, `${user_id}`)
- Query parameter parsing and URL decoding
- Static route optimization for performance
- Dynamic route resolution with parameter extraction

**Route Registration Examples:**
```java
// Static route
registerRoute("/login", loginHandler)

// Dynamic route with path variable
registerRoute("/user/get/${id}", getUserHandler)

// Multiple path variables
registerRoute("/dialog/${user_id}/send", dialogSendHandler)
```

### 3. Filter Chain Architecture

**ErrorInterceptor** (`src/main/java/com/rootbr/network/adapter/in/rest/filter/ErrorInterceptor.java:18-47`)
- Centralized exception handling for all HTTP requests
- Converts exceptions to JSON error responses with 500 status
- Ensures request body is properly consumed and connections closed
- Logs all errors for debugging and monitoring

**AuthenticationRestHandler** (`src/main/java/com/rootbr/network/adapter/in/rest/filter/AuthenticationRestHandler.java:15-49`)
- JWT token validation and principal extraction
- Bearer token parsing from Authorization header
- Automatic 401 responses for invalid/missing tokens
- Integrates with `TokenService` for token validation

**ApplicationJsonRestHandler** (`src/main/java/com/rootbr/network/adapter/in/rest/filter/ApplicationJsonRestHandler.java:14-42`)
- Content-Type and Accept header validation
- Ensures JSON-only communication
- Returns 406 (Not Acceptable) for non-JSON requests
- Sets proper response headers for JSON content

**AnonymousRestHandler** (`src/main/java/com/rootbr/network/adapter/in/rest/filter/AnonymousRestHandler.java`)
- Handles requests that don't require authentication
- Creates anonymous principal context
- Used for login, registration, and public endpoints

## Data Access Layer

### 1. Port-Adapter Pattern

**UserPort** (`src/main/java/com/rootbr/network/application/port/UserPort.java`)
- Interface defining user data operations contract
- Abstraction between application layer and database implementation
- Methods for CRUD operations and search functionality

**UserPortImpl** (`src/main/java/com/rootbr/network/adapter/out/db/UserPortImpl.java:10-66`)
- PostgreSQL implementation of UserPort
- Direct JDBC operations with prepared statements
- Implements user creation, retrieval, and search
- Uses visitor pattern for result processing

**Key SQL Operations:**
```sql
-- User creation
INSERT INTO users (id, first_name, last_name, birth_date, biography, city) VALUES (?, ?, ?, ?, ?, ?)

-- User retrieval
SELECT id, first_name, last_name, birth_date, biography, city FROM users WHERE id = ?

-- User search with prefix matching
SELECT * FROM users WHERE first_name LIKE ? AND last_name LIKE ?
```

**PrincipalPort** (`src/main/java/com/rootbr/network/application/port/PrincipalPort.java`)
**PrincipalPortImpl** (`src/main/java/com/rootbr/network/adapter/out/db/PrincipalPortImpl.java`)
- Authentication data management
- Password storage and retrieval
- Principal lookup by login credentials

### 2. Visitor Pattern for Data Processing

**UsersVisitor** (`src/main/java/com/rootbr/network/application/visitor/UsersVisitor.java:5-10`)
- Interface for processing user data without exposing internal structure
- Enables streaming JSON generation without intermediate objects
- Used by database layer to fill response data

**PostVisitor** (`src/main/java/com/rootbr/network/application/visitor/PostVisitor.java`)
**DialogMessageVisitor** (`src/main/java/com/rootbr/network/application/visitor/DialogMessageVisitor.java`)
**PrincipalVisitor** (`src/main/java/com/rootbr/network/application/visitor/PrincipalVisitor.java:6-10`)
- Specialized visitors for different domain entities
- Support direct JSON streaming from database results
- Maintain encapsulation while allowing data extraction

## Security Implementation

### 1. JWT Authentication

**JwtService** (`src/main/java/com/rootbr/network/adapter/in/rest/server/JwtService.java:15-59`)
- JWT token generation, validation, and parsing
- HMAC-SHA256 signing with configurable secret key
- Token expiration handling and claims extraction
- Principal factory integration for authenticated sessions

**Token Structure:**
- **Subject**: User ID (principal ID)
- **Claims**: Email and other user metadata
- **Expiration**: Configurable token lifetime
- **Signature**: HMAC-SHA256 with secret key

### 2. Password Security

**BCryptPasswordEncoder** (`src/main/java/com/rootbr/network/adapter/in/rest/BCryptPasswordEncoder.java:9-30`)
- BCrypt hashing with configurable cost factor
- Secure password encoding and verification
- Implements `PasswordEncoder` port interface
- Protection against timing attacks

**PasswordEncoder** (`src/main/java/com/rootbr/network/application/port/PasswordEncoder.java`)
- Port interface for password hashing strategies
- Abstraction allowing different hashing implementations
- Used throughout application for password operations

## Configuration Management

### 1. Application Configuration

**ApplicationConfiguration** (`src/main/java/com/rootbr/network/adapter/in/rest/ApplicationConfiguration.java:23-66`)
- YAML configuration loading with environment variable overrides
- Supports multiple configuration sources: classpath, filesystem, system properties
- Automatic environment variable mapping (dot notation to underscore)
- Hierarchical configuration with Apache Commons Configuration2

**Configuration Priority:**
1. System Properties (highest)
2. Environment Variables
3. YAML Configuration File (lowest)

**Configuration Files:**
- **Main Config**: `src/main/resources/config.yml`
- **Local Override**: Environment variables with uppercase and underscores
- **Examples**: `DATASOURCE_JDBCURL`, `SERVER_PORT`, `JWT_SECRET`

### 2. Database Configuration

**HikariCP Connection Pool** (configured in `src/main/resources/config.yml:1-16`)
```yaml
dataSource:
  jdbcUrl: jdbc:postgresql://localhost:5432/social_network
  username: social_network  
  password: social_network
  minimumIdle: 10
  maximumPoolSize: 50
  connectionTimeout: 30000
  idleTimeout: 600000
  maxLifetime: 1800000
  leakDetectionThreshold: 60000
```

## Database Schema Management

### 1. Liquibase Integration

**Schema Files:**
- **Master Changelog**: `db/changelog/db.changelog-master.xml`
- **Initial Schema**: `db/changelog/01-create-initial-schema.xml:1-38`

**Current Schema:**
```sql
-- Users table
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    biography TEXT,
    city VARCHAR(255)
);

-- Principals table (authentication)
CREATE TABLE principals (
    id VARCHAR(36) PRIMARY KEY,
    encoded_password VARCHAR(255) NOT NULL
);
```

**Migration Commands:**
```bash
# Apply migrations
mvn liquibase:update

# Rollback latest migration  
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

## API Structure and Endpoints

All endpoints follow REST principles with JWT authentication where required. See `openapi.json:1-395` for complete API specification.

### 1. Authentication Flow
1. `POST /user/register` - Register new user
2. `POST /login` - Get JWT token  
3. Use `Authorization: Bearer {token}` header for authenticated endpoints

### 2. Endpoint Categories

**Public Endpoints** (no authentication required):
- `POST /login` - User authentication
- `POST /user/register` - User registration
- `GET /user/get/${id}` - Get user profile
- `GET /user/search` - Search users by name
- `GET /post/get/${id}` - Get individual post

**Authenticated Endpoints** (JWT token required):
- `PUT /friend/set/${user_id}` - Add friend
- `PUT /friend/delete/${user_id}` - Remove friend  
- `POST /post/create` - Create new post
- `PUT /post/update` - Update existing post
- `PUT /post/delete/${id}` - Delete post
- `GET /post/feed` - Get personalized feed
- `POST /dialog/${user_id}/send` - Send message
- `GET /dialog/${user_id}/list` - Get conversation history

### 3. Request/Response Handlers

**Example Handler Implementation** (`src/main/java/com/rootbr/network/adapter/in/rest/handler/LoginHandler.java:17-60`)
- JSON streaming request parsing
- Business logic delegation to application layer
- Direct JSON response generation
- Proper HTTP status code handling

## Deployment Architecture

### 1. Docker Configuration

**Dockerfile** - Multi-stage build with optimized Java runtime
**docker-compose.yml** (`docker-compose.yml:1-28`)
```yaml
services:
  app:
    build: .
    network_mode: host
    environment:
      - DATASOURCE_JDBCURL=jdbc:postgresql://postgres:5432/social_network
    depends_on:
      postgres:
        condition: service_healthy
    volumes:
      - app-logs:/app/logs

  postgres:
    image: postgres:17-alpine
    environment:
      - POSTGRES_USER=social_network
      - POSTGRES_PASSWORD=social_network  
      - POSTGRES_DB=social_network
    volumes:
      - postgres-data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 5s
      timeout: 5s
      retries: 5
```

### 2. Build Configuration

**Maven Configuration** (`pom.xml:1-154`)
- Java 21 target with modern language features
- Shade plugin for fat JAR packaging
- Liquibase plugin for database migrations
- Main class: `com.rootbr.network.adapter.in.rest.RestService`

**Dependencies:**
- **Database**: PostgreSQL driver, HikariCP, Liquibase
- **JSON**: Jackson streaming API
- **Security**: JJWT, BCrypt
- **Configuration**: Apache Commons Configuration2
- **Logging**: SLF4J with Logback

## Logging Configuration

### 1. Production Logging

**logback.xml** (`src/main/resources/logback.xml:1-30`)
- Console and file appenders
- Daily log rotation with compression
- 30-day retention policy with 100MB file size limits
- Structured logging with timestamps and thread information

### 2. Development Logging

**logback-local.xml** (`logback-local.xml:1-9`)
- Console-only output for local development
- Colored output for better readability
- DEBUG level for application packages

**Log Configuration:**
```xml
<!-- Production logs to files and console -->
<appender name="DailyFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
  <file>logs/social-network.log</file>
  <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
    <fileNamePattern>logs/social-network.%d{yyyy-MM-dd}.%i.log.gz</fileNamePattern>
    <maxHistory>30</maxHistory>
  </rollingPolicy>
</appender>
```

## Directory Structure

```
/
├── src/main/java/com/rootbr/network/
│   ├── adapter/
│   │   ├── in/rest/                    # HTTP adapters
│   │   │   ├── RestService.java        # Main application entry point
│   │   │   ├── ApplicationConfiguration.java  # Config management
│   │   │   ├── BCryptPasswordEncoder.java     # Password security
│   │   │   ├── TokenService.java       # JWT token interface
│   │   │   ├── filter/                 # Request filters and interceptors
│   │   │   │   ├── ErrorInterceptor.java      # Global error handling
│   │   │   │   ├── AuthenticationRestHandler.java  # JWT validation
│   │   │   │   ├── ApplicationJsonRestHandler.java # Content-Type handling
│   │   │   │   ├── AnonymousRestHandler.java       # Public endpoints
│   │   │   │   └── CorsFilter.java     # CORS support
│   │   │   ├── handler/                # HTTP request handlers
│   │   │   │   ├── RouterHttpHandler.java  # Main request router
│   │   │   │   ├── LoginHandler.java       # Authentication endpoint
│   │   │   │   ├── UserRegisterHandler.java # User registration
│   │   │   │   ├── GetUserHandler.java     # User profile retrieval
│   │   │   │   ├── UserSearchHandler.java  # User search
│   │   │   │   ├── PostCreateHandler.java  # Post creation
│   │   │   │   ├── PostUpdateHandler.java  # Post updates
│   │   │   │   ├── PostDeleteHandler.java  # Post deletion
│   │   │   │   ├── PostGetHandler.java     # Post retrieval
│   │   │   │   ├── PostFeedHandler.java    # User feed
│   │   │   │   ├── FriendSetHandler.java   # Add friend
│   │   │   │   ├── FriendDeleteHandler.java # Remove friend
│   │   │   │   ├── DialogSendHandler.java   # Send message
│   │   │   │   └── DialogListHandler.java   # Get messages
│   │   │   └── server/                 # HTTP server infrastructure
│   │   │       ├── Server.java         # HTTP server wrapper
│   │   │       ├── Router.java         # Advanced routing engine
│   │   │       ├── RestHandler.java    # Handler interface
│   │   │       ├── HttpMethod.java     # HTTP method enumeration
│   │   │       └── JwtService.java     # JWT implementation
│   │   └── out/db/                     # Database adapters
│   │       ├── UserPortImpl.java       # User data access
│   │       └── PrincipalPortImpl.java  # Authentication data
│   ├── application/                    # Application layer
│   │   ├── SocialNetworkApplication.java # Main application facade
│   │   ├── Principal.java              # Security context
│   │   ├── command/                    # Command pattern implementation
│   │   │   ├── Command.java            # Base command class
│   │   │   ├── TransactionalCommand.java # Transaction management
│   │   │   ├── ReadCommand.java        # Read-only operations
│   │   │   └── AutoCommitCommand.java  # Auto-commit operations
│   │   ├── port/                       # Port interfaces
│   │   │   ├── UserPort.java           # User data port
│   │   │   ├── PrincipalPort.java      # Authentication port
│   │   │   └── PasswordEncoder.java    # Password encoding port
│   │   └── visitor/                    # Visitor pattern implementations
│   │       ├── UsersVisitor.java       # User data visitor
│   │       ├── PostVisitor.java        # Post data visitor
│   │       ├── DialogMessageVisitor.java # Message visitor
│   │       └── PrincipalVisitor.java   # Principal visitor
│   └── resources/
│       ├── config.yml                  # Main configuration
│       └── logback.xml                 # Production logging config
├── db/changelog/                       # Database migrations
│   ├── db.changelog-master.xml         # Master changelog
│   └── 01-create-initial-schema.xml    # Initial schema
├── logs/                               # Application logs (Docker volume)
├── docker-compose.yml                  # Container orchestration
├── Dockerfile                          # Container build definition
├── pom.xml                            # Maven build configuration
├── openapi.json                       # API specification
├── load-test-plan.jmx                 # JMeter load tests
├── logback-local.xml                  # Development logging
└── CLAUDE.md                          # This documentation
```

## Performance and Scalability Features

### 1. Virtual Threads
- Uses Java 21 virtual threads for request handling
- High concurrency with minimal resource overhead
- Configured in `RestService.java:40`

### 2. Connection Pooling
- HikariCP with optimized pool settings
- Connection leak detection and monitoring
- Configurable pool size and timeout settings

### 3. Streaming JSON Processing
- Jackson streaming API for minimal memory usage
- Direct database-to-HTTP response streaming via visitor pattern
- No intermediate object allocation for large datasets

### 4. Custom HTTP Server
- Lightweight alternative to servlet containers
- Path-based routing with variable extraction
- Minimal overhead for simple REST operations

## Development Workflow

### 1. Local Development Setup
```bash
# Start PostgreSQL locally or via Docker
docker-compose up postgres

# Run database migrations
mvn liquibase:update

# Start application in development mode
java -Dlogback.configurationFile=logback-local.xml -jar target/social-network-1.0.0.jar
```

### 2. Database Operations
```bash
# Create new migration
# Add new changeset to db/changelog/01-create-initial-schema.xml

# Apply migrations
mvn liquibase:update

# Rollback if needed
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

### 3. Load Testing
```bash
# Run JMeter load tests
jmeter -n -t load-test-plan.jmx -l results.jtl

# Import test data for performance testing
python3 homework/hw1-load-testing/import_test_data.py
```

## Future Enhancements and TODOs

### 1. Missing Implementations
The following command implementations are marked as TODO in `SocialNetworkApplication.java`:

- **Posts System** (lines 92-131):
  - `createPostCommand()`: Post creation logic
  - `updatePostCommand()`: Post editing functionality  
  - `deletePostCommand()`: Post deletion
  - `getPostByIdCommand()`: Individual post retrieval
  - `getUserFeedCommand()`: Personalized user feed generation

- **Friends System** (lines 134-150):
  - `addFriendCommand()`: Friend relationship creation
  - `removeFriendCommand()`: Friend relationship removal

- **Messaging System** (lines 153-169):
  - `sendMessageCommand()`: Message sending between users
  - `getDialogMessagesCommand()`: Conversation history retrieval

### 2. Database Schema Extensions
Additional tables needed for complete functionality:
- `posts` table for user posts
- `friendships` table for friend relationships  
- `messages` table for direct messaging
- Indexes for performance optimization

### 3. Recommended Improvements
- Add comprehensive input validation
- Implement proper error handling with custom exceptions
- Add request/response logging for better observability
- Consider adding caching layer for frequently accessed data
- Implement pagination for large result sets
- Add rate limiting for API endpoints

This documentation provides a comprehensive guide to the social network application architecture, components, and implementation patterns. The codebase demonstrates clean architecture principles with pure Java implementation and efficient resource usage.