# FME Backend

Backend API for Faculty of Mechanical Engineering (FME) Management System built with Spring Boot 3.2, Java 21, and PostgreSQL.

## Tech Stack

- **Java 21**
- **Spring Boot 3.2.1**
- **Spring Security** - JWT Authentication
- **Spring Data JPA** - Database access
- **PostgreSQL** - Database
- **MapStruct** - DTO mapping
- **Lombok** - Boilerplate reduction
- **SpringDoc OpenAPI** - API documentation

## Prerequisites

- Java 21 or higher
- PostgreSQL 14 or higher
- Gradle 8.5 or higher (or use included wrapper)

## Getting Started

### 1. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE fme_db;
```

### 2. Configuration

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fme_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run

Using Gradle wrapper:

```bash
# Build
./gradlew build

# Run
./gradlew bootRun
```

Or run the JAR directly:

```bash
./gradlew build
java -jar build/libs/fme-backend-1.0.0.jar
```

### 4. Access the Application

- API Base URL: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- API Docs: `http://localhost:8080/api/api-docs`

## Default Accounts

| Role  | Email                             | Password    |
|-------|-----------------------------------|-------------|
| Admin | 23146053@student.hcmute.edu.vn    | admin123    |
| User  | 20190001@student.hcmute.edu.vn    | student123  |

## API Endpoints

### Authentication (`/api/auth`)
- `POST /signup` - Register new user
- `POST /signin` - Login
- `POST /refresh` - Refresh token
- `GET /me` - Get current user
- `PUT /profile` - Update profile
- `PUT /change-password` - Change password

### Schedules (`/api/schedules`)
- `GET /` - Get all schedules
- `GET /{id}` - Get schedule by ID
- `GET /date/{date}` - Get schedules by date
- `GET /range` - Get schedules by date range
- `GET /student/{email}` - Get schedules by student
- `POST /` - Create schedule (Admin)
- `PUT /{id}` - Update schedule (Admin)
- `PUT /{id}/confirm` - Confirm schedule (Admin)
- `DELETE /{id}` - Delete schedule (Admin)

### Duty Reports (`/api/reports`)
- `GET /` - Get all reports
- `GET /{id}` - Get report by ID
- `GET /student/{email}` - Get reports by student
- `GET /schedule/{scheduleId}` - Get reports by schedule
- `GET /filter` - Get reports with filters
- `POST /` - Create report
- `PUT /{id}` - Update report
- `PUT /{id}/status` - Update status (Admin)
- `DELETE /{id}` - Delete report

### News (`/api/news`)
- `GET /` - Get all news
- `GET /{id}` - Get news by ID
- `GET /category/{category}` - Get news by category
- `GET /trending` - Get trending news
- `POST /` - Create news (Admin)
- `PUT /{id}` - Update news (Admin)
- `DELETE /{id}` - Delete news (Admin)

### Banners (`/api/banners`)
- `GET /` - Get all banners
- `GET /active` - Get active banners
- `GET /{id}` - Get banner by ID
- `POST /` - Create banner (Admin)
- `PUT /{id}` - Update banner (Admin)
- `DELETE /{id}` - Delete banner (Admin)

### Programs (`/api/programs`)
- `GET /` - Get all programs
- `GET /{id}` - Get program by ID
- `GET /code/{code}` - Get program by code
- `GET /type/{type}` - Get programs by type
- `POST /` - Create program (Admin)
- `PUT /{id}` - Update program (Admin)
- `DELETE /{id}` - Delete program (Admin)

### Admin - Users (`/api/admin/users`)
- `GET /` - Get all users
- `GET /{id}` - Get user by ID
- `PUT /{id}/activate` - Activate user
- `PUT /{id}/deactivate` - Deactivate user

## Project Structure

```
src/main/java/com/hcmute/fme/
├── FmeBackendApplication.java
├── config/
│   ├── DataInitializer.java
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
├── controller/
│   ├── AdminUserController.java
│   ├── AuthController.java
│   ├── BannerController.java
│   ├── DutyReportController.java
│   ├── NewsController.java
│   ├── ProgramController.java
│   └── ScheduleController.java
├── dto/
│   ├── request/
│   │   ├── BannerRequest.java
│   │   ├── ChangePasswordRequest.java
│   │   ├── DutyReportRequest.java
│   │   ├── GoogleSignInRequest.java
│   │   ├── NewsRequest.java
│   │   ├── ProgramRequest.java
│   │   ├── ScheduleRequest.java
│   │   ├── SignInRequest.java
│   │   ├── SignUpRequest.java
│   │   └── UpdateProfileRequest.java
│   └── response/
│       ├── ApiResponse.java
│       ├── AuthResponse.java
│       ├── BannerDTO.java
│       ├── DutyReportDTO.java
│       ├── NewsDTO.java
│       ├── ProgramDTO.java
│       ├── ScheduleDTO.java
│       └── UserDTO.java
├── entity/
│   ├── Banner.java
│   ├── DutyReport.java
│   ├── News.java
│   ├── Program.java
│   ├── Schedule.java
│   └── User.java
├── exception/
│   ├── ApiException.java
│   ├── DuplicateResourceException.java
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
├── mapper/
│   ├── BannerMapper.java
│   ├── DutyReportMapper.java
│   ├── NewsMapper.java
│   ├── ProgramMapper.java
│   ├── ScheduleMapper.java
│   └── UserMapper.java
├── repository/
│   ├── BannerRepository.java
│   ├── DutyReportRepository.java
│   ├── NewsRepository.java
│   ├── ProgramRepository.java
│   ├── ScheduleRepository.java
│   └── UserRepository.java
├── security/
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtTokenProvider.java
└── service/
    ├── AuthService.java
    ├── BannerService.java
    ├── DutyReportService.java
    ├── NewsService.java
    ├── ProgramService.java
    ├── ScheduleService.java
    ├── UserService.java
    └── impl/
        ├── AuthServiceImpl.java
        ├── BannerServiceImpl.java
        ├── DutyReportServiceImpl.java
        ├── NewsServiceImpl.java
        ├── ProgramServiceImpl.java
        ├── ScheduleServiceImpl.java
        └── UserServiceImpl.java
```

## Docker

Build and run with Docker:

```bash
# Build image
docker build -t fme-backend .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/fme_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  fme-backend
```

## License

MIT License
