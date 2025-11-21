package com.hendisantika.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.util.List;

/**
 * Doctor Entity for GRH (Gestion de Rendez-vous Hospitaliers)
 * Contains doctor information for hospital appointment management
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;

    @Indexed(unique = true)
    private String doctorId; // Unique identifier: D00001, D00002, etc.

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Specialization is required")
    private String specialization; // e.g., Cardiology, Dermatology, etc.

    @Email
    private String email;

    @Indexed(unique = true)
    private String phone;

    private List<String> workingDays; // e.g., [Monday, Tuesday, Wednesday]

    private String officeLocation;

    private String officePhone;

    private String password; // Password for user account creation

    private boolean active = true;

    private String createdAt;

    private String updatedAt;
}
