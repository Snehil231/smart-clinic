# Smart Clinic Management System - Database Schema Design

## Database
MySQL Database: `smart_clinic`

## Tables

### 1. Admin
| Field | Type | Constraints |
|---|---|---|
| id | BIGINT | Primary Key, Auto Increment |
| name | VARCHAR(100) | NOT NULL |
| email | VARCHAR(150) | NOT NULL, UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| phone | VARCHAR(20) | |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

### 2. Doctor
| Field | Type | Constraints |
|---|---|---|
| id | BIGINT | Primary Key, Auto Increment |
| name | VARCHAR(100) | NOT NULL |
| email | VARCHAR(150) | NOT NULL, UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| phone | VARCHAR(20) | |
| speciality | VARCHAR(100) | NOT NULL |
| qualification | VARCHAR(150) | |
| experience_years | INT | DEFAULT 0 |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

### 3. Patient
| Field | Type | Constraints |
|---|---|---|
| id | BIGINT | Primary Key, Auto Increment |
| name | VARCHAR(100) | NOT NULL |
| email | VARCHAR(150) | NOT NULL, UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| phone | VARCHAR(20) | NOT NULL, UNIQUE |
| date_of_birth | DATE | |
| gender | VARCHAR(20) | |
| address | VARCHAR(255) | |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

### 4. Doctor Available Time
| Field | Type | Constraints |
|---|---|---|
| id | BIGINT | Primary Key, Auto Increment |
| doctor_id | BIGINT | Foreign Key → doctor(id) |
| available_date | DATE | NOT NULL |
| start_time | TIME | NOT NULL |
| end_time | TIME | NOT NULL |

### 5. Appointment
| Field | Type | Constraints |
|---|---|---|
| id | BIGINT | Primary Key, Auto Increment |
| doctor_id | BIGINT | Foreign Key → doctor(id) |
| patient_id | BIGINT | Foreign Key → patient(id) |
| appointment_time | DATETIME | NOT NULL |
| status | VARCHAR(30) | DEFAULT 'BOOKED' |
| reason | VARCHAR(500) | |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

### 6. Prescription
| Field | Type | Constraints |
|---|---|---|
| id | BIGINT | Primary Key, Auto Increment |
| appointment_id | BIGINT | Foreign Key → appointment(id) |
| doctor_id | BIGINT | Foreign Key → doctor(id) |
| patient_id | BIGINT | Foreign Key → patient(id) |
| medicines | VARCHAR(1000) | NOT NULL |
| diagnosis | VARCHAR(500) | |
| instructions | VARCHAR(1000) | |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

## Relationships

- One Doctor can have many available time slots.
- One Doctor can have many appointments.
- One Patient can have many appointments.
- One Appointment can have one Prescription.
- Doctor and Patient are connected through the Appointment table.
- Prescription references the Appointment, Doctor, and Patient.

## Foreign Keys

- `doctor_available_time.doctor_id` → `doctor.id`
- `appointment.doctor_id` → `doctor.id`
- `appointment.patient_id` → `patient.id`
- `prescription.appointment_id` → `appointment.id`
- `prescription.doctor_id` → `doctor.id`
- `prescription.patient_id` → `patient.id`