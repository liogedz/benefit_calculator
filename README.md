# kood/JobFair 2026 Technical Challenge

## Helmes Technical Challenge for koodJõhvi students

## Parental Benefit Calculator

## Features

- ☕ Java / Spring Boot / Maven
- 💾 H2 (In-Memory)
- 🏗️ Layered Architecture
- ⚡ Angular Signals
- 💨 Tailwind CSS

## Validation

- Minimum salary 100 €
- No `Future born child`
- No older than 3 years old child

## Workflow

- User opens page
- createSession()
- store sessionId in localStorage

Later visits:

- User reloads page
- sessionId found in localStorage
- GET session data
- restore form

## Backend endpoints

- POST /api/session
- GET /api/session/{sessionId}
- POST /api/session/{sessionId}
- GET /api/session/{sessionId}/calculator

## H2 database is set to clear data on restart

- for keeping data set `ddl-auto:` to `update` [application.yaml](./backend/src/main/resources/application.yaml)

## Cron Job

- DB entries got cleared if not accessed more than 7 days (if set to update - see previous)

## Running the Application

### Using Maven

```bash
cd backend
mvn spring-boot:run
```

```bash
cd frontend
npm install
ng serve
```

### Using IDE

Run the main application class directly from your IDE (IntelliJ IDEA, Eclipse, VS Code)

The API will be available at: `http://localhost:8080`

UI Angular at: [localhost](http://localhost:4200)

## Endpoints test

- with postman - collection added in the [docs](./backend/docs/BenefitCalculation.postman_collection.json) folder
- with IntelliJ - [requests](requests.http) in the root of the project