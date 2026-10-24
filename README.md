# Booking App

A personal beauty salon booking application with a Spring Boot backend, PostgreSQL database, and React/Vite frontend.

## Prerequisites

Install these before running the project:

- Java 21
- Node.js and npm
- PostgreSQL

## Project Structure

```text
booking-app/
  backend/    Spring Boot API
  frontend/   React + TypeScript + Vite app
```

## Database Setup

The backend is configured to connect to PostgreSQL with these defaults:

```text
Database: bookingdb
Username: postgres
Password: postgres
Port:     5432
```

Create the database before starting the backend:

```bash
createdb -U postgres bookingdb
```

If your PostgreSQL username or password is different, update:

```text
backend/src/main/resources/application.properties
```

Flyway runs automatically when the backend starts and creates the tables plus initial service data.

## Run the Backend

From the project root:

```bash
cd backend
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

## Run the Frontend

Open a second terminal and run:

```bash
cd frontend
npm install
npm run dev
```

The frontend runs at:

```text
http://localhost:5173
```

The Vite dev server proxies API calls from `/api` to `http://localhost:8080`, so keep the backend running while using the frontend.

## Useful Commands

Backend tests:

```bash
cd backend
mvnw.cmd test
```

Frontend build:

```bash
cd frontend
npm run build
```

Frontend lint:

```bash
cd frontend
npm run lint
```

## Troubleshooting

- If the backend cannot connect to the database, confirm that PostgreSQL is running and that `bookingdb` exists.

- If the frontend shows API errors, confirm that the backend is running on `http://localhost:8080`.

- If port `5173` is already in use, Vite may choose another port. Use the URL printed in the frontend terminal.
