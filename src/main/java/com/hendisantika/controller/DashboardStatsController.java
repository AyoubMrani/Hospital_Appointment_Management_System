package com.hendisantika.controller;

import com.hendisantika.repository.AppointmentRepository;
import com.hendisantika.repository.DoctorRepository;
import com.hendisantika.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Dashboard Statistics REST Controller
 * Provides endpoints to fetch counts of patients, doctors, and appointments
 */
@RestController
@RequestMapping("/api/stats")
public class DashboardStatsController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * Get total counts of patients, doctors, and appointments
     */
    @GetMapping("/counts")
    public ResponseEntity<Map<String, Object>> getCounts() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("patientCount", patientRepository.count());
        stats.put("doctorCount", doctorRepository.count());
        stats.put("appointmentCount", appointmentRepository.count());
        stats.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(stats);
    }

    /**
     * Get only patient count
     */
    @GetMapping("/patients/count")
    public ResponseEntity<Map<String, Object>> getPatientsCount() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", patientRepository.count());
        return ResponseEntity.ok(stats);
    }

    /**
     * Get only doctor count
     */
    @GetMapping("/doctors/count")
    public ResponseEntity<Map<String, Object>> getDoctorsCount() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", doctorRepository.count());
        return ResponseEntity.ok(stats);
    }

    /**
     * Get only appointment count
     */
    @GetMapping("/appointments/count")
    public ResponseEntity<Map<String, Object>> getAppointmentsCount() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", appointmentRepository.count());
        return ResponseEntity.ok(stats);
    }
}
