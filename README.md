# kood/JobFair 2026 Technical Challenge

## `Helmes` Technical Challenge for `Kood/Jõhvi` students

## Parental Benefit Calculator

## 🚀 Features

- Java 25.0.2 / Spring Boot / Maven
- H2 (In-Memory Database)
- Layered Architecture (Controller → Service → Repository)
- Angular 21.1.0 (Signals-based state management)
- Tailwind CSS (automatic dark/light mode)
- Session-based workflow (no authentication required)

## ✅ Validation Rules

Childbirth date:

- Cannot be in the future
- Cannot be older than 3 years

Parental salary:

- Minimum considered not lower than **100 €**
- Maximum salary for calculation is capped at **4000 €**
- UI notifies that capped value is used

## 🔄 Workflow

First Visit:

- user opens the application
- `CreateSession`is triggered
- sessionId is stored in localStorage and DB
- user enters dob and salary
- user clicks `Calculate` triggers both:
- values saved in DB under sessionId
- payment breakdown is calculated and displayed
- presence of results changes button to `Clear`

Subsequent Visits:

- application checks for sessionId in localStorage

If found:

- Fetch session via API
- dob and salary pre-populated in user entry
- user may alter dob and salary
- next steps as the `First Visit` last 4

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

Controller Tests:

- Endpoint validation
- Request/response correctness

Repository Tests:

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
- Job is scheduled for 03:00
- Also checked every startup
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
