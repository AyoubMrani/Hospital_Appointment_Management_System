package com.hendisantika.controller;

import com.hendisantika.entity.User;
import com.hendisantika.repository.AppointmentRepository;
import com.hendisantika.repository.AppointmentRequestRepository;
import com.hendisantika.repository.DoctorRepository;
import com.hendisantika.repository.PatientRepository;
import com.hendisantika.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Dashboard Statistics REST Controller
 * Provides endpoints to fetch counts of patients, doctors, and appointments
 * with role-based filtering for ADMIN, DOCTOR, and PATIENT roles
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

    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get current logged-in user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);
            return user.orElse(null);
        }
        return null;
    }

    /**
     * Get current user's role
     */
    private String getCurrentUserRole() {
        User user = getCurrentUser();
        return user != null ? user.getRole() : "PATIENT";
    }

    /**
     * Get total counts of patients, doctors, and appointments
     * with role-based filtering
     */
    @GetMapping("/counts")
    public ResponseEntity<Map<String, Object>> getCounts() {
        Map<String, Object> stats = new HashMap<>();
        User currentUser = getCurrentUser();
        String role = getCurrentUserRole();

        // Add general counts (for all roles)
        stats.put("patientCount", patientRepository.count());
        stats.put("doctorCount", doctorRepository.count());
        stats.put("appointmentCount", appointmentRepository.count());
        stats.put("timestamp", System.currentTimeMillis());

        // Add role-specific statistics
        if ("ADMIN".equals(role)) {
            // ADMIN sees all statistics
            stats.put("adminView", true);
        } else if ("DOCTOR".equals(role) && currentUser != null) {
            // DOCTOR sees their own appointments and pending requests
            String doctorId = currentUser.getDoctorId();

            // Count appointments for this doctor
            long myAppointments = appointmentRepository.findAll().stream()
                    .filter(apt -> doctorId != null && doctorId.equals(apt.getDoctorId()))
                    .count();
            stats.put("myAppointmentCount", myAppointments);

            // Count pending appointment requests
            long pendingRequests = appointmentRequestRepository.findAll().stream()
                    .filter(req -> doctorId != null && doctorId.equals(req.getDoctorId())
                            && "PENDING".equals(req.getStatus()))
                    .count();
            stats.put("pendingRequestCount", pendingRequests);

            // Count total unique patients in the system (all patients)
            long totalPatients = patientRepository.count();
            stats.put("totalPatientCount", totalPatients);
            stats.put("doctorView", true);
        } else if ("PATIENT".equals(role) && currentUser != null) {
            // PATIENT sees their own appointments and requests
            String patientId = currentUser.getPatientId();

            // Count upcoming appointments for this patient
            long upcomingAppointments = appointmentRepository.findAll().stream()
                    .filter(apt -> patientId != null && patientId.equals(apt.getPatientId()))
                    .filter(apt -> {
                        try {
                            if (apt.getDate() != null && !apt.getDate().isEmpty()) {
                                LocalDate appointmentDate = LocalDate.parse(apt.getDate());
                                return !appointmentDate.isBefore(LocalDate.now());
                            }
                            return false;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .count();
            stats.put("upcomingAppointmentCount", upcomingAppointments);

            // Count patient's appointment requests
            long myRequests = appointmentRequestRepository.findAll().stream()
                    .filter(req -> patientId != null && patientId.equals(req.getPatientId()))
                    .count();
            stats.put("myRequestCount", myRequests);
            stats.put("patientView", true);
        }

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
