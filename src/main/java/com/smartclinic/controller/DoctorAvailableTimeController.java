package com.smartclinic.controller;

import com.smartclinic.entity.DoctorAvailableTime;
import com.smartclinic.service.DoctorAvailableTimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/available-times")
@CrossOrigin(origins = "*")
public class DoctorAvailableTimeController {

    private final DoctorAvailableTimeService doctorAvailableTimeService;

    public DoctorAvailableTimeController(
            DoctorAvailableTimeService doctorAvailableTimeService) {

        this.doctorAvailableTimeService = doctorAvailableTimeService;
    }

    @GetMapping
    public List<DoctorAvailableTime> getAllAvailableTimes() {
        return doctorAvailableTimeService.getAllAvailableTimes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorAvailableTime> getAvailableTimeById(
            @PathVariable Long id) {

        return doctorAvailableTimeService.getAvailableTimeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DoctorAvailableTime createAvailableTime(
            @RequestBody DoctorAvailableTime availableTime) {

        return doctorAvailableTimeService.saveAvailableTime(availableTime);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorAvailableTime> updateAvailableTime(
            @PathVariable Long id,
            @RequestBody DoctorAvailableTime availableTime) {

        return doctorAvailableTimeService.getAvailableTimeById(id)
                .map(existingTime -> {
                    availableTime.setId(id);
                    return ResponseEntity.ok(
                            doctorAvailableTimeService.saveAvailableTime(
                                    availableTime)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailableTime(
            @PathVariable Long id) {

        if (doctorAvailableTimeService
                .getAvailableTimeById(id).isPresent()) {

            doctorAvailableTimeService.deleteAvailableTime(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}