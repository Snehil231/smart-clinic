package com.smartclinic.service;

import com.smartclinic.dto.ApiResponse;
import com.smartclinic.dto.LoginResponse;
import com.smartclinic.entity.Admin;
import com.smartclinic.repository.AdminRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final TokenService tokenService;

    public AdminService(
            AdminRepository adminRepository,
            TokenService tokenService) {
        this.adminRepository = adminRepository;
        this.tokenService = tokenService;
    }

    /**
     * Validates admin login credentials and generates a JWT.
     */
    public ApiResponse<LoginResponse> login(
            String email,
            String password) {

        Optional<Admin> adminOptional =
                adminRepository.findByEmail(email);

        if (adminOptional.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "Invalid admin email or password",
                    null
            );
        }

        Admin admin = adminOptional.get();

        if (!admin.getPassword().equals(password)) {
            return new ApiResponse<>(
                    false,
                    "Invalid admin email or password",
                    null
            );
        }

        String token =
                tokenService.generateToken(admin.getEmail());

        LoginResponse loginResponse =
                new LoginResponse(
                        token,
                        admin.getEmail(),
                        "ADMIN"
                );

        return new ApiResponse<>(
                true,
                "Admin login successful",
                loginResponse
        );
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
}