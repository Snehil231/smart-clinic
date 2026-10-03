package com.smartclinic.service;

import com.smartclinic.entity.DoctorAvailableTime;
import com.smartclinic.repository.DoctorAvailableTimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorAvailableTimeService {

    private final DoctorAvailableTimeRepository doctorAvailableTimeRepository;

    public DoctorAvailableTimeService(DoctorAvailableTimeRepository doctorAvailableTimeRepository) {
        this.doctorAvailableTimeRepository = doctorAvailableTimeRepository;
    }

    public List<DoctorAvailableTime> getAllAvailableTimes() {
        return doctorAvailableTimeRepository.findAll();
    }

    public Optional<DoctorAvailableTime> getAvailableTimeById(Long id) {
        return doctorAvailableTimeRepository.findById(id);
    }

    public DoctorAvailableTime saveAvailableTime(DoctorAvailableTime availableTime) {
        return doctorAvailableTimeRepository.save(availableTime);
    }

    public void deleteAvailableTime(Long id) {
        doctorAvailableTimeRepository.deleteById(id);
    }
}