package com.smartclinic.controller;

import com.smartclinic.dto.ApiResponse;
import com.smartclinic.dto.LoginRequest;
import com.smartclinic.dto.LoginResponse;
import com.smartclinic.entity.Doctor;
import com.smartclinic.entity.DoctorAvailableTime;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private TokenService tokenService;

    // Get all doctors
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // Get doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(
            @PathVariable Long id) {

        Optional<Doctor> doctor =
                doctorService.getDoctorById(id);

        if (doctor.isPresent()) {
            return ResponseEntity.ok(doctor.get());
        }

        return ResponseEntity.notFound().build();
    }

    // Doctor login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        ApiResponse<LoginResponse> response =
                doctorService.login(
                        request.getEmail(),
                        request.getPassword()
                );

        if (!response.isSuccess()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        return ResponseEntity.ok(response);
    }

    // Get doctor availability
    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<List<DoctorAvailableTime>>> getAvailability(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader,
            @RequestParam Long doctorId,
            @RequestParam String date) {

        String email =
                tokenService.getEmailFromAuthorizationHeader(
                        authorizationHeader
                );

        if (email == null) {
            ApiResponse<List<DoctorAvailableTime>> response =
                    new ApiResponse<>(
                            false,
                            "Unauthorized",
                            null
                    );

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }

        LocalDate localDate;

        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {

            ApiResponse<List<DoctorAvailableTime>> response =
                    new ApiResponse<>(
                            false,
                            "Invalid date format. Use YYYY-MM-DD",
                            null
                    );

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }

        List<DoctorAvailableTime> slots =
                doctorService.getAvailableSlots(
                        doctorId,
                        localDate
                );

        ApiResponse<List<DoctorAvailableTime>> response =
                new ApiResponse<>(
                        true,
                        "Availability retrieved successfully",
                        slots
                );

        return ResponseEntity.ok(response);
    }

    // Q26: Get doctors by speciality and available time
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Doctor>>> searchDoctors(
            @RequestParam String speciality,
            @RequestParam String time) {

        LocalTime localTime;

        try {
            localTime = LocalTime.parse(time);
        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponse<>(
                            false,
                            "Invalid time format. Use HH:mm",
                            null
                    ));
        }

        List<Doctor> doctors =
                doctorService.getDoctorsBySpecialityAndTime(
                        speciality,
                        localTime
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Doctors retrieved successfully",
                        doctors
                )
        );
    }

    // Create doctor
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(
            @RequestBody Doctor doctor) {

        Doctor savedDoctor =
                doctorService.saveDoctor(doctor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedDoctor);
    }

    // Update doctor
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {

        Optional<Doctor> existing =
                doctorService.getDoctorById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        doctor.setId(id);

        Doctor updated =
                doctorService.saveDoctor(doctor);

        return ResponseEntity.ok(updated);
    }

    // Delete doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable Long id) {

        Optional<Doctor> existing =
                doctorService.getDoctorById(id);

        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }
}