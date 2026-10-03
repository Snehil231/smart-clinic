package com.smartclinic.repository;

import com.smartclinic.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Get all appointments for a particular doctor within a time range
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            Long doctorId,
            LocalDateTime start,
            LocalDateTime end
    );

    // Get all appointments for a particular patient
    List<Appointment> findByPatientId(Long patientId);

    // Get a doctor's appointments (with patient loaded) by doctor email
    @Query("SELECT a FROM Appointment a JOIN FETCH a.patient " +
           "WHERE a.doctor.email = :email ORDER BY a.appointmentTime")
    List<Appointment> findByDoctorEmailWithPatient(@Param("email") String email);
}
