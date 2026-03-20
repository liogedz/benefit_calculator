# kood/JobFair 2026 Technical Challenge

## Helmes Technical Challenge for koodJõhvi students

## Parental Benefit Calculator

## 👥 Team

**Team Name:** *Init to Win it*

Aleksei Gedz (Full-stack / Project owner)

Meribel Kuum

Patrick Kekki

## 🚀 Features

- Java / Spring Boot / Maven
- H2 (In-Memory Database)
- Layered Architecture (Controller → Service → Repository)
- Angular (Signals-based state management)
- Tailwind CSS (automatic dark/light mode)
- Session-based workflow (no authentication required)

## ✅ Validation Rules

- Minimum salary: 100 €
  Childbirth date:
- Cannot be in the future
- Cannot be older than 3 years
- Maximum salary considered for calculation is capped at **4000 €**
- UI notifies that capped value is used

## 🔄 Workflow

First Visit

- User opens the application
- createSession() is triggered
- sessionId is stored in localStorage

Subsequent Visits

- Application checks for sessionId in localStorage

If found:

- Fetch session via API
- Restore form state automatically

## 🔗 Backend Endpoints

| Method | Endpoint                              | Description                |
|--------|---------------------------------------|----------------------------|
| POST   | `/api/session`                        | Create new session         |
| GET    | `/api/session/{sessionId}`            | Retrieve session data      |
| POST   | `/api/session/{sessionId}`            | Update session data        |
| GET    | `/api/session/{sessionId}/calculator` | Calculate parental benefit |

## ⚠️ Global Exception Handling

The backend uses a centralized global exception handler to ensure consistent API responses.

Covers:

- Validation errors (@Valid, custom rules)
- Entity not found (invalid sessionId)
- Illegal arguments / bad requests
- Generic server errors

Benefits:

- Clean controller code
- Consistent error response format
- Easier debugging and frontend integration

## 🧪 Testing

Backend (Spring Boot)

Controller Tests

- Endpoint validation
- Request/response correctness

Repository Tests

- Database interactions (H2 in-memory)

Frontend (Angular)

Vitest Setup

- Component tests
- Service tests

Run tests:

```bash
cd frontend
ng test
```

```bash
cd backend
mvn test
```

or

```bash
mvn clean test
```

specific test

```bash
mvn -Dtest=<TestName> test
```

## 🗄️ Database

- H2 in-memory database

To reset DB on application restart:

```yaml
ddl-auto: create-drop
```

- see [application.yaml](./backend/src/main/resources/application.yaml)

## ⏱️ Scheduled Cleanup (Cron Job)

- Sessions are automatically deleted if inactive for 7 days
- Applies only when persistence is enabled `(ddl-auto: update)`

## ▶️ Running the Application

Using Maven

```bash
cd backend
mvn spring-boot:run
```

```bash
cd frontend
npm install
ng serve
```

## Using IDE

Run the main Spring Boot application class directly (IntelliJ / Eclipse / VS Code)

## 🌐 Access

Backend API:
http://localhost:8080

Frontend UI:
http://localhost:4200

## 🧰 API Testing

Postman collection:

- pleas see [docs](./backend/docs/BenefitCalculation.postman_collection.json) folder

IntelliJ HTTP client:

- please see [requests](requests.http) in the root of the project
