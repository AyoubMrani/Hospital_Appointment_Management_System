package com.hendisantika.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * AppointmentRequest Entity - Represents a patient's request for an appointment
 * Doctors can approve or deny the request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "appointment_requests")
public class AppointmentRequest {
    @Id
    private String id;

    private String requestId; // AR00001, AR00002, etc.

    private String patientId; // Link to patient

    private String patientName; // Patient full name for display

    private String doctorId; // Link to doctor

    private String doctorName; // Doctor full name for display

    private String appointmentDate; // Requested appointment date (YYYY-MM-DD)

    private String appointmentTime; // Requested appointment time (HH:mm)

    private String reason; // Reason for appointment

    private String status; // PENDING, APPROVED, DENIED

    private String denialReason; // Reason for denial (if denied)

    private String createdAt;

    private String updatedAt;
}
