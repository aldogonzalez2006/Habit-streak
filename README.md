# Habit Tracker

A personal habit tracking and productivity application designed to help users build consistent routines, monitor daily activities, and visualize their long-term progress.

The application provides a centralized dashboard for managing habits, recording daily completion, tracking individual streaks, and analyzing personal performance over time.

**Status:** Under Development

## Tech Stack

### Frontend
- React
- Next.js
- TypeScript
- Tailwind CSS

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security

### Database
- PostgreSQL

### Infrastructure & DevOps
- Docker
- Docker Compose
- GitHub Actions
- CI/CD

## Core Features

- Habit creation, editing, and management.
- Daily habit completion tracking.
- Persistent progress history.
- Daily productivity dashboard.
- Individual habit streaks.
- Annual habit activity heatmaps.
- Progress statistics and analytics.

## Architecture

The application follows a client-server architecture.

```text
Frontend (Next.js)
        |
        | HTTP / REST API
        |
Backend (Spring Boot)
        |
        | JPA / Hibernate
        |
Database (PostgreSQL)
```

## Project Structure

```text
habit-tracker/
├── frontend/
├── backend/
├── docs/
├── infrastructure/
├── scripts/
└── .github/
```

## Development

The project is being developed incrementally, following software engineering practices focused on maintainability, separation of concerns, testing, and continuous integration.

## License

MIT License.