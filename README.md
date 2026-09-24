# SeismicMonitor 🌍

An earthquake early-warning system built with **Spring Boot**. The application receives readings from seismic sensors, calculates the earthquake's epicenter from three readings, and creates a warning that administrators can review and citizens can report on.

Built as an individual programming exam project at the Computer Science AP Degree (Datamatiker), Erhvervsakademi København (EK), 3rd semester.

<!-- Add a screenshot of the admin or user view here:
![Screenshot](docs/screenshot.png)
-->

## Purpose

The exam tested whether I could build a complete, secured backend on my own under time pressure. A provided Docker container simulates a network of seismic sensors, and my job was to build the system that receives the data and turns it into useful warnings:

- design a **REST API** that other systems (the sensors) can post data to
- model the domain with **JPA entities and relationships** (sensors, readings, warnings, reports)
- implement a real **algorithm** (trilateration) in the service layer and **unit test** it
- secure the API with **role-based access** (Spring Security)
- **integrate an external API** (OpenStreetMap) for reverse geocoding

## Features

- **Sensor data ingestion**: A sensor simulator (Docker) continuously posts readings to the REST API
- **Epicenter calculation**: The epicenter is located by *trilateration* from three sensors' positions and estimated distances
- **Reverse geocoding**: Coordinates are looked up in OpenStreetMap (Nominatim) to give each warning a place name
- **Warning status workflow**: `UNDER_REVIEW` → `ACTIVE` / `FALSE_ALARM` / `NOT_ACTIVE`, managed by an administrator
- **Citizen reports**: Users can report the intensity they felt, limited to one report per warning per user
- **Role-based access**: Spring Security with separate `USER` and `ADMIN` endpoints
- **Unit tests**: Services and the epicenter calculation are covered by tests

## Tech stack

| Area | Technology |
|---|---|
| Backend | Java 25, Spring Boot 4, Spring Web MVC, Spring Security |
| Data | Spring Data JPA, H2 (in-memory) |
| Integration | WebClient → OpenStreetMap Nominatim |
| Frontend | HTML, CSS, vanilla JavaScript (`fetch`) |
| Infrastructure | Docker Compose (sensor simulator), Maven |
| Testing | JUnit 5, Mockito |

## Architecture

```
Sensor simulator (Docker)
        │  POST /api/sensor-data
        ▼
SeismicController → SeismicService ──► ReadingRepository / SensorRepository
                          │
                          ▼ (3 readings)
               EarthquakeWarningService
                 ├─ EpicenterCalculator  (trilateration)
                 └─ GeoLocator           (Nominatim reverse geocoding)
                          │
                          ▼
EarthquakeWarningController ◄── Frontend (admin / user)
```

The code is layered as `controller` → `service` → `repository`, with DTOs separating the API contract from the JPA entities. The calculation and geolocation live behind interfaces (`EpicenterCalculator`, `GeoLocator`) so they can be swapped out and mocked in tests.

## Getting started

**Requirements:** Java 25 and Docker Desktop

```bash
git clone https://github.com/SimAbild/ProgExam.git
cd ProgExam
./mvnw spring-boot:run
```

Spring Boot's Docker Compose integration starts the sensor simulator from `compose.yml` automatically. Then open <http://localhost:8080>.

**Demo users:**

| Role | Username | Password |
|---|---|---|
| User | `user` | `user123` |
| Admin | `admin` | `admin123` |

The H2 console is available at <http://localhost:8080/h2-console> (JDBC URL: `jdbc:h2:mem:ProgExam`).

## API (selection)

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/api/sensor-data` | Public (sensors) |
| `GET` | `/api/warnings/active` | USER, ADMIN |
| `POST` | `/api/warnings/{id}/reports` | USER, ADMIN |
| `GET` | `/api/warnings` | ADMIN |
| `POST` | `/api/warnings/{id}/status` | ADMIN |
| `GET` | `/api/warnings/{id}/readings` | ADMIN |
| `GET` | `/api/sensors` | ADMIN |

## Running the tests

```bash
./mvnw test
```

## Improvements after submission

After the exam I refactored parts of the code. Each change is documented with before/after code and the reasoning behind it in [IMPROVEMENTS.md](IMPROVEMENTS.md) (in Danish).
