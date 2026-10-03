package com.smartclinic.controller;

import com.smartclinic.dto.ApiResponse;
import com.smartclinic.entity.Appointment;
import com.smartclinic.entity.Patient;
import com.smartclinic.repository.PatientRepository;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PatientRepository patientRepository;


    // Get all appointments
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }


    // Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(
            @PathVariable Long id) {

        Optional<Appointment> appointment =
                appointmentService.getAppointmentById(id);

        if (appointment.isPresent()) {
            return ResponseEntity.ok(appointment.get());
        }

        return ResponseEntity.notFound().build();
    }


    // Patient gets all of their appointments using JWT
    @GetMapping("/patient")
    public ResponseEntity<ApiResponse<List<Appointment>>> getPatientAppointments(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader) {

        // Extract patient email from JWT
        String email =
                tokenService.getEmailFromAuthorizationHeader(
                        authorizationHeader
                );

        // Token missing or invalid
        if (email == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Unauthorized. Please login as a patient.",
                                    null
                            )
                    );
        }


        // Find patient using email from token
        Optional<Patient> patientOptional =
                patientRepository.findByEmail(email);


        if (patientOptional.isEmpty()) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Patient not found.",
                                    null
                            )
                    );
        }


        Patient patient =
                patientOptional.get();


        // Retrieve appointments belonging to this patient
        List<Appointment> appointments =
                appointmentService.getAppointmentsForPatient(
                        patient.getId()
                );


        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Patient appointments retrieved successfully",
                        appointments
                )
        );
    }


    // Create appointment
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @RequestBody Appointment appointment) {

        Appointment saved =
                appointmentService.bookAppointment(appointment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(saved);
    }


    // Update appointment
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody Appointment appointment) {

        Optional<Appointment> existing =
                appointmentService.getAppointmentById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        appointment.setId(id);

        Appointment updated =
                appointmentService.saveAppointment(appointment);

        return ResponseEntity.ok(updated);
    }


    // Delete appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id) {

        Optional<Appointment> existing =
                appointmentService.getAppointmentById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        appointmentService.deleteAppointment(id);

        return ResponseEntity.noContent().build();
    }
}