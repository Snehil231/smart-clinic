# 🏥 Smart Clinic Management System

A Spring Boot backend for running a small clinic: **admins** manage doctors, **doctors** publish their available time slots and write prescriptions, and **patients** search doctors and book appointments. Each role has its own login and dashboard.

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)
![CI](https://github.com/Snehil231/smart-clinic/actions/workflows/compile-backend.yml/badge.svg)

## ✨ Features

- **Role-based login** for Admin, Doctor and Patient, using JWT tokens (`jjwt`)
- **Doctors** – CRUD, search, and availability lookup
- **Doctor available times** – manage the slots a doctor can be booked in
- **Appointments** – book, reschedule, cancel; list a patient's appointments
- **Prescriptions** – doctors create and update prescriptions for appointments
- **Validation** with Jakarta Bean Validation and a common `ApiResponse` wrapper
- **Dashboards** – static HTML pages for each role (`/admin-login.html`, `/doctor-login.html`, `/patient-login.html`)
- **Multi-stage Dockerfile** (Maven build → slim JRE image) and a **GitHub Actions** workflow that compiles the backend on every push

## 🧱 Architecture

```
controller  →  service  →  repository (Spring Data JPA)  →  MySQL
     ↑             ↑
    DTOs      TokenService (JWT)
```

```
src/main/java/com/smartclinic
├── controller   # REST endpoints for each module
├── service      # business logic + JWT token handling
├── repository   # Spring Data JPA repositories
├── entity       # Admin, Doctor, Patient, Appointment, DoctorAvailableTime, Prescription
└── dto          # ApiResponse, LoginRequest, LoginResponse
```

Database design: see [`schema-design.md`](schema-design.md).

## 🔌 API Endpoints

| Module | Method & path |
|---|---|
| Admin | `POST /api/admin/login` · `POST /api/admin/doctors` |
| Doctors | `GET /api/doctors` · `GET /api/doctors/{id}` · `GET /api/doctors/search` · `GET /api/doctors/availability` · `POST /api/doctors/login` · `POST /api/doctors` · `PUT /api/doctors/{id}` · `DELETE /api/doctors/{id}` |
| Patients | `GET /api/patients` · `GET /api/patients/{id}` · `POST /api/patients/login` · `POST /api/patients` · `PUT /api/patients/{id}` · `DELETE /api/patients/{id}` |
| Appointments | `GET /api/appointments` · `GET /api/appointments/{id}` · `GET /api/appointments/patient` · `POST /api/appointments` · `PUT /api/appointments/{id}` · `DELETE /api/appointments/{id}` |
| Available times | `GET /api/available-times` · `GET /api/available-times/{id}` · `POST /api/available-times` · `PUT /api/available-times/{id}` · `DELETE /api/available-times/{id}` |
| Prescriptions | `GET /api/prescriptions` · `GET /api/prescriptions/{id}` · `POST /api/prescriptions` · `PUT /api/prescriptions/{id}` · `DELETE /api/prescriptions/{id}` |

## ▶️ Run locally

**Prerequisites:** Java 17+, MySQL 8, Maven (or use the bundled wrapper)

```bash
git clone https://github.com/Snehil231/smart-clinic.git
cd smart-clinic
# create the database, then set your MySQL username/password in src/main/resources/application.properties
mysql -u root -p -e "CREATE DATABASE smart_clinic;"
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

The app starts on **http://localhost:8084**.

**With Docker**

```bash
docker build -t smart-clinic .
docker run -p 8084:8084 smart-clinic
```

## 🧪 Example request

```bash
curl -X POST http://localhost:8084/api/patients/login \
  -H "Content-Type: application/json" \
  -d '{"email":"patient@example.com","password":"secret"}'
```

## 📋 User stories

Admin, Doctor and Patient user stories are tracked as [GitHub Issues](https://github.com/Snehil231/smart-clinic/issues).

## 👤 Author

**Snehil Harsh** – [@Snehil231](https://github.com/Snehil231)
