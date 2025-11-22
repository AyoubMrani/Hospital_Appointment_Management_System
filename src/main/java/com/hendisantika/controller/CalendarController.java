package com.hendisantika.controller;

import com.hendisantika.entity.Appointment;
import com.hendisantika.entity.User;
import com.hendisantika.repository.AppointmentRepository;
import com.hendisantika.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Calendar Controller for displaying appointments in FullCalendar
 */
@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private AppointmentRepository appointmentRepository;

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
     * Display calendar page with AdminLTE layout
     */
    @GetMapping
    public String displayCalendar(Model model) {
        return "calendar";
    }

    /**
     * API endpoint to get current user information (ID, role, etc)
     */
    @GetMapping("/api/current-user")
    @ResponseBody
    public ResponseEntity<?> getCurrentUserInfo() {
        User user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.ok(null);
        }
        
        return ResponseEntity.ok(new java.util.HashMap<String, Object>() {{
            put("id", user.getId());
            put("username", user.getUsername());
            put("role", user.getRole());
            put("doctorId", user.getDoctorId());
            put("patientId", user.getPatientId());
        }});
    }

    /**
     * API endpoint to get all appointments in FullCalendar format (filtered by
     * role)
     */
    @GetMapping("/api/events")
    @ResponseBody
    public ResponseEntity<List<CalendarEvent>> getCalendarEvents() {
        List<Appointment> appointments = appointmentRepository.findAll();

        // Filter based on user role
        String role = getCurrentUserRole();
        User currentUser = getCurrentUser();

        if ("DOCTOR".equals(role) && currentUser != null && currentUser.getDoctorId() != null) {
            // Doctors see only their appointments
            appointments = appointments.stream()
                    .filter(a -> a.getDoctorId().equals(currentUser.getDoctorId()))
                    .collect(Collectors.toList());
        } else if ("PATIENT".equals(role) && currentUser != null && currentUser.getPatientId() != null) {
            // Patients see only their appointments
            appointments = appointments.stream()
                    .filter(a -> a.getPatientId().equals(currentUser.getPatientId()))
                    .collect(Collectors.toList());
        }
        // ADMIN sees all appointments

        List<CalendarEvent> events = appointments.stream()
                .map(appointment -> new CalendarEvent(
                        appointment.getId(),
                        appointment.getAppointmentId() + " - " + appointment.getPatientName(),
                        appointment.getDate() + "T" + appointment.getTime(),
                        appointment.getDoctorName(),
                        appointment.getStatus(),
                        getColorByStatus(appointment.getStatus())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(events);
    }

    /**
     * Get color based on appointment status
     */
    private String getColorByStatus(String status) {
        if (status == null)
            return "#3498db";

        return switch (status) {
            case "Planifié" -> "#3498db"; // Blue
            case "Confirmé" -> "#2ecc71"; // Green
            case "Terminé" -> "#27ae60"; // Green
            case "Annulé" -> "#e74c3c"; // Red
            case "En cours" -> "#f39c12"; // Orange
            default -> "#3498db";
        };
    }

    /**
     * Inner class for FullCalendar event format
     */
    public static class CalendarEvent {
        public String id;
        public String title;
        public String start;
        public String extendedProps;
        public String backgroundColor;

        public CalendarEvent(String id, String title, String start, String doctor, String status, String color) {
            this.id = id;
            this.title = title;
            this.start = start;
            this.extendedProps = doctor;
            this.backgroundColor = color;
        }

        // Getters
        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getStart() {
            return start;
        }

        public String getExtendedProps() {
            return extendedProps;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }
    }
}
