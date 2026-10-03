package com.smartclinic.controller;

import com.smartclinic.dto.ApiResponse;
import com.smartclinic.dto.LoginRequest;
import com.smartclinic.dto.LoginResponse;
import com.smartclinic.entity.Admin;
import com.smartclinic.entity.Doctor;
import com.smartclinic.service.AdminService;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final DoctorService doctorService;
    private final TokenService tokenService;

    public AdminController(
            AdminService adminService,
            DoctorService doctorService,
            TokenService tokenService) {
        this.adminService = adminService;
        this.doctorService = doctorService;
        this.tokenService = tokenService;
    }

    /**
     * Admin login.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        ApiResponse<LoginResponse> response =
                adminService.login(
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

    /**
     * Admin adds a new doctor.
     * Requires a valid admin JWT token.
     */
    @PostMapping("/doctors")
    public ResponseEntity<ApiResponse<Doctor>> addDoctor(
            @Valid @RequestBody Doctor doctor,
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorizationHeader) {

        String adminEmail =
                tokenService.getEmailFromAuthorizationHeader(
                        authorizationHeader
                );

        if (adminEmail == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(
                            false,
                            "Invalid or missing admin token",
                            null
                    ));
        }

        Doctor savedDoctor = doctorService.saveDoctor(doctor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Doctor added successfully",
                        savedDoctor
                ));
    }
}