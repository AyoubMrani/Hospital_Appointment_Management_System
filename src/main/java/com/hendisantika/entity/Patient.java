package com.hendisantika.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Patient Entity for GRH (Gestion de Rendez-vous Hospitaliers)
 * Contains patient information for hospital appointment management
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "patients")
public class Patient {
    @Id
    private String id;

    @Indexed(unique = true)
    private String patientId; // Unique identifier: P00001, P00002, etc.

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String dateOfBirth; // Format: YYYY-MM-DD

    private String gender; // Male, Female, Other

    @Email
    private String email;

    @Indexed(unique = true)
    private String phone;

    private String address;

    private String city;

    private String postalCode;

    private String country;

    private String password; // Password for user account creation

    private boolean active = true;

    private String createdAt;

    private String updatedAt;
}
