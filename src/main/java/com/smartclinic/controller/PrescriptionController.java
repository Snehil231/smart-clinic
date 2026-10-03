package com.smartclinic.controller;

import com.smartclinic.dto.ApiResponse;
import com.smartclinic.entity.Prescription;
import com.smartclinic.repository.DoctorRepository;
import com.smartclinic.service.PrescriptionService;
import com.smartclinic.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final TokenService tokenService;
    private final DoctorRepository doctorRepository;

    public PrescriptionController(
            PrescriptionService prescriptionService,
            TokenService tokenService,
            DoctorRepository doctorRepository) {

        this.prescriptionService = prescriptionService;
        this.tokenService = tokenService;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping
    public ResponseEntity<List<Prescription>> getAllPrescriptions() {
        return ResponseEntity.ok(
                prescriptionService.getAllPrescriptions()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getPrescriptionById(
            @PathVariable Long id) {

        return prescriptionService.getPrescriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a prescription.
     * Requires a valid doctor JWT token.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Prescription>> createPrescription(
            @Valid @RequestBody Prescription prescription,
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorizationHeader) {

        String doctorEmail = tokenService
                .getEmailFromAuthorizationHeader(
                        authorizationHeader
                );

        if (doctorEmail == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(
                            false,
                            "Invalid or missing authentication token",
                            null
                    ));
        }

        return doctorRepository.findByEmail(doctorEmail)
                .map(doctor -> {

                    prescription.setDoctor(doctor);

                    Prescription savedPrescription =
                            prescriptionService
                                    .savePrescription(prescription);

                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(new ApiResponse<>(
                                    true,
                                    "Prescription created successfully",
                                    savedPrescription
                            ));
                })
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(new ApiResponse<>(
                                        false,
                                        "Doctor account not found",
                                        null
                                ))
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prescription> updatePrescription(
            @PathVariable Long id,
            @Valid @RequestBody Prescription prescription) {

        return prescriptionService
                .getPrescriptionById(id)
                .map(existingPrescription -> {

                    prescription.setId(id);

                    return ResponseEntity.ok(
                            prescriptionService
                                    .savePrescription(prescription)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(
            @PathVariable Long id) {

        if (prescriptionService
                .getPrescriptionById(id)
                .isPresent()) {

            prescriptionService.deletePrescription(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}