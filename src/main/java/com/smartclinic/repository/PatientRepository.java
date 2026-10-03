package com.smartclinic.repository;

import com.smartclinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find patient by email
    Optional<Patient> findByEmail(String email);

    // Find patient using either email or phone
    Optional<Patient> findByEmailOrPhone(String email, String phone);
}