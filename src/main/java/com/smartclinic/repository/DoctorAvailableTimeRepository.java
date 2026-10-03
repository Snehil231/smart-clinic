package com.smartclinic.repository;

import com.smartclinic.entity.Doctor;
import com.smartclinic.entity.DoctorAvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DoctorAvailableTimeRepository
        extends JpaRepository<DoctorAvailableTime, Long> {

    // Get available slots for a doctor on a specific date
    List<DoctorAvailableTime> findByDoctorIdAndAvailableDate(
            Long doctorId,
            LocalDate availableDate
    );

    // Find doctors by speciality and available time
    @Query("""
        SELECT d
        FROM Doctor d
        JOIN d.availableTimes a
        WHERE LOWER(d.speciality) = LOWER(:speciality)
        AND :time >= a.startTime
        AND :time <= a.endTime
    """)
    List<Doctor> findDoctorsBySpecialityAndTime(
            @Param("speciality") String speciality,
            @Param("time") LocalTime time
    );
}