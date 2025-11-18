package com.hendisantika.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Appointment Entity for GRH (Gestion de Rendez-vous Hospitaliers)
 * Contains appointment information for hospital appointment management
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;

    @Indexed(unique = true)
    private String appointmentId; // Unique identifier for appointment

    @NotBlank(message = "Patient ID is required")
    private String patientId;

    private String patientName;

    @NotBlank(message = "Doctor ID is required")
    private String doctorId;

    private String doctorName;

    private String doctorSpecialization;

    @NotBlank(message = "Appointment date is required")
    private String date; // Format: YYYY-MM-DD

    @NotBlank(message = "Appointment time is required")
    private String time; // Format: HH:MM

    private String status; // Planifié (Scheduled), Terminé (Completed), Annulé (Cancelled)

    private String remarks; // Additional remarks about the appointment

    private String createdAt;

    private String updatedAt;

    private String cancelledAt;

    private String cancelledReason;
}
