package com.hendisantika.controller;

import com.hendisantika.entity.Appointment;
import com.hendisantika.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Calendar Controller for displaying appointments in FullCalendar
 */
@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * Display calendar page with AdminLTE layout
     */
    @GetMapping
    public String displayCalendar(Model model) {
        return "calendar";
    }

    /**
     * API endpoint to get all appointments in FullCalendar format
     */
    @GetMapping("/api/events")
    @ResponseBody
    public ResponseEntity<List<CalendarEvent>> getCalendarEvents() {
        List<Appointment> appointments = appointmentRepository.findAll();

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
