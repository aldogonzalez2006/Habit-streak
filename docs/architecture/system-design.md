
# System Design - Habit Tracker

## 1. Introduction

### 1.1 Purpose

Habit Tracker is a personal productivity web application designed to help users establish consistent routines, record daily activities, and monitor their progress over time.

The system provides tools for habit management, daily completion tracking, historical records, individual streak calculations, and productivity analytics.

### 1.2 Project Objectives

- Provide a centralized habit management system.
- Allow users to record daily habit completion.
- Preserve historical records permanently.
- Calculate individual habit streaks.
- Visualize long-term progress.
- Provide a maintainable and extensible architecture.

### 1.3 Scope

The initial release focuses on a functional single-user habit tracking system.

Multi-user authentication and advanced functionality will be introduced in subsequent development phases.

---

## 2. System Architecture

### 2.1 Architectural Style

The application follows a client-server architecture organized as a monorepo.

The system is divided into three primary components:

1. Frontend: User interface and interaction.
2. Backend: Business logic and REST API.
3. Database: Persistent data storage.

### 2.2 Technology Stack

| Component | Technology |
|---|---|
| Frontend | Next.js |
| Programming Language | TypeScript |
| Styling | Tailwind CSS |
| Backend | Spring Boot |
| Programming Language | Java 21 |
| API | REST |
| Data Format | JSON |
| ORM | Hibernate |
| Persistence | Spring Data JPA |
| Database | PostgreSQL |
| Containerization | Docker |
| Container Orchestration | Docker Compose |
| CI/CD | GitHub Actions |

### 2.3 Communication

The frontend communicates with the backend through HTTP requests.

The backend exposes REST endpoints and returns JSON responses.

Spring Boot processes requests, applies business rules, and interacts with PostgreSQL through Spring Data JPA.

---

## 3. Backend Architecture

The backend follows a modular architecture organized by business functionality.

Each module separates responsibilities into:

- Controller: Handles HTTP requests and responses.
- Service: Implements business logic.
- Repository: Handles database operations.
- Entity: Represents persistent domain data.
- DTO: Defines data exchanged through the API.
- Mapper: Converts between entities and DTOs.

Initial functional modules:

- Habit
- Progress
- Analytics

Shared components:

- Configuration
- Exception handling
- Common responses

---

## 4. Data Architecture

### 4.1 Core Entities

#### Habit

Represents a habit registered by the user.

Initial attributes:

- id
- name
- description
- color
- frequency
- target
- active
- createdAt
- updatedAt

#### HabitProgress

Represents the completion status of a habit on a specific date.

Initial attributes:

- id
- habitId
- progressDate
- completed
- notes
- createdAt
- updatedAt

### 4.2 Entity Relationships

One Habit can have multiple HabitProgress records.

Relationship:

Habit (1) -------- (N) HabitProgress

Each progress record belongs to exactly one habit.

A unique constraint must prevent multiple progress records for the same habit and date.

Conceptually:

UNIQUE (habit_id, progress_date)

### 4.3 Data Persistence

PostgreSQL is the primary database.

All habit definitions and daily progress records must survive:

- Browser refreshes.
- Browser closure.
- Application restarts.
- Backend restarts.
- Container recreation.

Docker volumes will be used to preserve database data across container recreation.

### 4.4 Historical Data Integrity

Historical progress is an important part of the application.

Deleting or archiving a habit must not accidentally erase its historical records.

Habit modifications must not automatically rewrite previous completion records.

The initial implementation will favor archiving over permanent deletion when historical information exists.

---

## 5. Functional Requirements

### 5.1 Habit Management

The system must allow users to:

- Create habits.
- View registered habits.
- Edit habit information.
- Archive inactive habits.
- Retrieve active habits.

### 5.2 Daily Progress

The system must allow users to:

- Mark a habit as completed.
- Update its completion status.
- Retrieve progress for a specific date.
- View historical completion records.

### 5.3 Dashboard

The dashboard must display:

- Current date.
- Active habits.
- Daily completion status.
- Completed habits.
- Pending habits.
- Overall daily completion percentage.

### 5.4 Individual Streaks

Each habit must have an independent streak calculation.

The system will eventually calculate:

- Current streak.
- Longest streak.
- Total completed days.
- Individual completion percentage.

Streak calculations must respect the habit's configured schedule.

### 5.5 Annual Activity Heatmap

The application will provide a calendar visualization for each habit.

Each calendar cell represents a date.

The visualization must distinguish:

- Completed days.
- Incomplete scheduled days.
- Days without a scheduled occurrence.
- Future dates.

The annual view will also display individual habit completion percentages.

---

## 6. API Design

The backend exposes REST endpoints.

Initial endpoint proposal:

### Habits

GET /api/v1/habits

GET /api/v1/habits/{id}

POST /api/v1/habits

PUT /api/v1/habits/{id}

DELETE /api/v1/habits/{id}

### Daily Progress

GET /api/v1/progress?date=YYYY-MM-DD

GET /api/v1/progress/habit/{habitId}

PUT /api/v1/progress/habit/{habitId}?date=YYYY-MM-DD

The endpoint definitions may evolve during implementation.

---

## 7. Infrastructure and Deployment

### 7.1 Docker

Docker will containerize the application components.

Initial services:

- Frontend.
- Backend.
- PostgreSQL.

### 7.2 Docker Compose

Docker Compose will coordinate local development services.

It will provide:

- Service configuration.
- Internal networking.
- Environment variables.
- Database persistence through volumes.
- Service dependencies and health checks.

### 7.3 CI/CD

GitHub Actions will automate:

- Backend compilation.
- Frontend validation.
- Automated tests.
- Docker image builds.

Deployment automation will be introduced after the application reaches a stable state.

---

## 8. Security Considerations

Security will be incorporated incrementally.

Initial requirements:

- Never commit credentials or secrets.
- Use environment variables for sensitive configuration.
- Validate incoming API requests.
- Handle exceptions without exposing internal details.
- Configure database access securely.

Future requirements:

- User authentication.
- Authorization.
- Password hashing.
- User-specific data isolation.
- Protected API endpoints.
- HTTPS.

Public deployment with personal data will require appropriate authentication and access controls.

---

## 9. Non-Functional Requirements

### Reliability

The system must preserve registered habits and historical progress.

### Maintainability

The codebase must maintain clear separation of responsibilities.

### Scalability

The architecture must support future modules without requiring a complete redesign.

### Usability

The interface must be intuitive and responsive.

### Testability

Business logic must be testable independently of the user interface and database infrastructure where practical.

### Portability

Docker will provide reproducible development environments.

---

## 10. Development Strategy

Development will follow an incremental approach.

1. Project initialization.
2. Database design.
3. Backend implementation.
4. Frontend implementation.
5. System integration.
6. Historical tracking and analytics.
7. Security implementation.
8. CI/CD and deployment.

All changes will be integrated through GitHub Pull Requests.

The main branch must remain stable.

---

## 11. Future Considerations

Potential future capabilities:

- Multiple user accounts.
- Custom habit schedules.
- Notifications and reminders.
- Daily journal.
- CSV and PDF exports.
- Mobile application.
- Advanced analytics.
- AI-powered productivity insights.

These features are not part of the initial MVP scope.
