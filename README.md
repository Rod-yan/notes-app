# Notes Application

A full-stack note-taking application that allows users to create, edit, archive, and delete personal notes while tagging them with categories for efficient filtering and organization.

## Features

- **Personal Note Management**: Full CRUD operations for notes with support for archiving/unarchiving.
- **Categorization System**: Dynamic tagging and category-based filtering.
- **Secure Authentication**: User isolation via JWT (JSON Web Tokens) with a dedicated login system.
- **API Documentation**: Interactive Swagger UI and OpenAPI 3.0 support.
- **Health Monitoring**: Built-in health checks and metrics endpoints.

## Tech Stack

- **Backend**: Java 21, Spring Boot 3.2.4, Spring Data JPA, Spring Security, PostgreSQL 15
- **Frontend**: React 18.2, TypeScript 5.2, Vite 5.2, Axios 1.6
- **Infrastructure**: Docker Engine 20.10+, Docker Compose 2.0+, Maven 3.9+

## Requirements

- Docker and Docker Compose installed (recommended)
- For manual run: Java 21, Maven 3.9+, Node.js 18+, npm 9+

## Setup & Execution

### One‑Command Start (Docker)

    ./run.sh

This script builds both backend and frontend Docker images, starts a PostgreSQL container, and launches the entire stack. Once running:

- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- Swagger UI: http://localhost:8080/swagger-ui.html

Press `Ctrl+C` to stop all services.

### Manual Run (without Docker)

**Backend**:

    cd backend
    mvn spring-boot:run

**Frontend** (in a separate terminal):

    cd frontend
    npm install
    npm run dev

## Testing

- **Backend**: `cd backend && mvn test` (JaCoCo enforces ≥90% line coverage)
- **Frontend**: `cd frontend && npm test`

## Credentials

**Default admin user** (created automatically on first start):

- Username: `admin`
- Password: `admin123`

## Cloud Deployment

- **Database**: [Neon.tech](https://neon.tech) (free PostgreSQL)
- **Backend** and **Frontend**: [Render](https://render.com) (Web Services with Docker)

## Live Demo

- Frontend: https://notes-app-frontend-s6ph.onrender.com
- Backend API: https://notes-app-qv04.onrender.com
- Swagger UI: https://notes-app-qv04.onrender.com/swagger-ui.html

## Industry Practices

- **Layered Architecture**: Clear separation of concerns (Controller, Service, Repository).
- **Security & Isolation**: User-level data isolation (IDOR protection) and BCrypt password hashing.
- **Optimized Persistence**: JPA Criteria API for dynamic queries with eager fetching to prevent N+1 issues.
- **Code Quality**: Centralized error handling, JSR-303 bean validation, and ~90% test coverage.
- **Cloud Readiness**: Externalized configuration for seamless deployment.
