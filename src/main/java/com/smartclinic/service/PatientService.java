package com.smartclinic.service;

import com.smartclinic.dto.ApiResponse;
import com.smartclinic.dto.LoginResponse;
import com.smartclinic.entity.Patient;
import com.smartclinic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final TokenService tokenService;

    public PatientService(
            PatientRepository patientRepository,
            TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.tokenService = tokenService;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    /**
     * Validates patient login credentials and generates a JWT.
     */
    public ApiResponse<LoginResponse> login(
            String email,
            String password) {

        Optional<Patient> patientOptional =
                patientRepository.findByEmail(email);

        if (patientOptional.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "Invalid patient email or password",
                    null
            );
        }

        Patient patient = patientOptional.get();

        if (!patient.getPassword().equals(password)) {
            return new ApiResponse<>(
                    false,
                    "Invalid patient email or password",
                    null
            );
        }

        String token =
                tokenService.generateToken(patient.getEmail());

        LoginResponse loginResponse =
                new LoginResponse(
                        token,
                        patient.getEmail(),
                        "PATIENT"
                );

        return new ApiResponse<>(
                true,
                "Patient login successful",
                loginResponse
        );
    }
}