# Lista API

A RESTful API for task management built with Java, Spring Boot, and PostgreSQL. The project includes JWT authentication, user-specific task management, request validation, and automated tests.

Developed as a backend portfolio project to practice REST API development, authentication, database integration, and software testing.

## Features

- User registration and login
- JWT-based authentication
- Secure password hashing
- Create, retrieve, update, and delete tasks
- User-specific task access
- Request validation
- Centralized exception handling
- Interactive API documentation with Swagger UI
- Automated tests with JUnit and Spring Boot testing tools

## Technologies

- **Java 21**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Spring Security**
- **PostgreSQL**
- **Maven**
- **JWT**
- **Swagger / OpenAPI**
- **JUnit**

## Architecture

The project follows a layered architecture:

- **Controller:** handles HTTP requests and responses.
- **Service:** contains application and business logic.
- **Repository:** handles database access.
- **Entity:** represents database entities.
- **DTO:** defines the data exchanged through the API.
- **Config:** contains security and application configuration.
- **Exception:** centralizes error handling.

## Requirements

Before running the application, make sure you have:

- Java 21
- PostgreSQL
- Git

Maven Wrapper is included, so a separate Maven installation is not required.

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/LeonardoGarcia-dev/lista-api.git
cd lista-api
```

### 2. Create the database

Create a PostgreSQL database named:

```sql
CREATE DATABASE list_api_db;
```

### 3. Configure environment variables

Configure the following variables for your local environment:

| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL JDBC connection URL |
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `JWT_SECRET` | Secret key used to sign JWTs |

Example for a local development environment:

```text
DB_URL=jdbc:postgresql://localhost:5432/list_api_db
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
JWT_SECRET=your_long_random_secret
```

Set these variables in your operating system or IDE run configuration before starting the application. Do not commit real passwords, tokens, or secret keys to the repository.

### 4. Run the application

On Windows, using PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

The application is configured to run locally on port `8080`, unless configured otherwise.

### 5. Open Swagger UI

Once the application is running, access:

http://localhost:8080/swagger-ui/index.html

Use the interactive documentation to explore the endpoints and test requests.

## API Endpoints

### Authentication

| Method | Endpoint | Description | Authentication |
|---|---|---|---|
| `POST` | `/register` | Register a user | Not required |
| `POST` | `/login` | Authenticate a user and obtain a JWT | Not required |

### Tasks

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/tasks` | Create a task |
| `GET` | `/tasks` | Retrieve the authenticated user's tasks |
| `GET` | `/tasks/{id}` | Retrieve a task by ID |
| `PUT` | `/tasks/{id}` | Update a task |
| `DELETE` | `/tasks/{id}` | Delete a task |

Task endpoints require a valid JWT.

### Authentication flow

1. Register a user using `POST /register`.
2. Log in using `POST /login`.
3. Copy the JWT returned by the login endpoint.
4. Authorize requests through Swagger UI using the **Authorize** button.
5. Use the task endpoints with the authenticated user's token.

## Example: Create a Task

**Request**

```http
POST /tasks
Content-Type: application/json
Authorization: Bearer YOUR_JWT_TOKEN
```

```json
{
  "title": "Study Spring Boot",
  "description": "Build a REST API with Java",
  "completed": false
}
```

The API returns the created task with its assigned ID.

## Security

- JWT authentication for protected endpoints
- Password hashing
- Stateless authentication
- User-specific task access
- Validation of incoming request data
- Centralized handling of application errors

Users can access only their own tasks through the task endpoints.

## Testing

Run the automated test suite with Maven Wrapper.

**Windows (PowerShell):**

```powershell
.\mvnw.cmd clean test
```

**Linux or macOS:**

```bash
./mvnw clean test
```

The project currently has 16 automated tests passing in the local development environment.

## Current Status

- [x] REST API implementation
- [x] CRUD operations for tasks
- [x] JWT authentication
- [x] PostgreSQL integration
- [x] Swagger / OpenAPI documentation
- [x] Automated tests
- [ ] Production deployment

## Future Improvements

- Production deployment
- Database migration management
- Continuous integration with GitHub Actions
- Additional integration tests
- Frontend integration

## Author

**Leonardo Garcia**

GitHub: [LeonardoGarcia-dev](https://github.com/LeonardoGarcia-dev)

Project repository: [lista-api](https://github.com/LeonardoGarcia-dev/lista-api)
