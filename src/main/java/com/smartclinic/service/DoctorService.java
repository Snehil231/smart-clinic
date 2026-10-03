package com.smartclinic.service;

import com.smartclinic.dto.ApiResponse;
import com.smartclinic.dto.LoginResponse;
import com.smartclinic.entity.Doctor;
import com.smartclinic.entity.DoctorAvailableTime;
import com.smartclinic.repository.DoctorAvailableTimeRepository;
import com.smartclinic.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorAvailableTimeRepository availableTimeRepository;

    @Autowired
    private TokenService tokenService;

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Get doctor by ID
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    // Save doctor
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Delete doctor
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    // Get available slots for a doctor on a specific date
    public List<DoctorAvailableTime> getAvailableSlots(
            Long doctorId,
            LocalDate date) {

        return availableTimeRepository
                .findByDoctorIdAndAvailableDate(doctorId, date);
    }

    // Get doctors by speciality and available time
    public List<Doctor> getDoctorsBySpecialityAndTime(
            String speciality,
            LocalTime time) {

        return availableTimeRepository
                .findDoctorsBySpecialityAndTime(
                        speciality,
                        time
                );
    }

    // Doctor login
    public ApiResponse<LoginResponse> login(
            String email,
            String password) {

        Optional<Doctor> doctorOptional =
                doctorRepository.findByEmail(email);

        if (doctorOptional.isEmpty()) {
            return new ApiResponse<>(
                    false,
                    "Invalid doctor email or password",
                    null
            );
        }

        Doctor doctor = doctorOptional.get();

        if (!doctor.getPassword().equals(password)) {
            return new ApiResponse<>(
                    false,
                    "Invalid doctor email or password",
                    null
            );
        }

        String token = tokenService.generateToken(doctor.getEmail());

        LoginResponse loginResponse =
                new LoginResponse(
                        token,
                        doctor.getEmail(),
                        "DOCTOR"
                );

        return new ApiResponse<>(
                true,
                "Doctor login successful",
                loginResponse
        );
    }
}