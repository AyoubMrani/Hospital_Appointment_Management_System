package com.hendisantika.controller;

import com.hendisantika.entity.Patient;
import com.hendisantika.repository.PatientRepository;
import com.hendisantika.service.SequenceService;
import com.hendisantika.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Public Registration Controller for handling user sign-ups
 */
@Controller
@RequestMapping("/public")
public class RegistrationController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private UserService userService;

    /**
     * Save new patient from public registration form
     */
    @PostMapping("/register")
    public String registerPatient(@ModelAttribute Patient patient,
                                  @RequestParam(required = false) String generatedUsername,
                                  Model model) {
        // Determine the username to use
        String username = (generatedUsername != null && !generatedUsername.isEmpty())
                ? generatedUsername
                : "patient_" + patient.getFirstName().toLowerCase();

        // Validate username starts with "patient_"
        if (!username.startsWith("patient_")) {
            username = "patient_" + username;
        }

        // Check if username already exists
        if (userService.usernameExists(username)) {
            model.addAttribute("patient", patient);
            model.addAttribute("error", "Le nom d'utilisateur '" + username
                    + "' existe déjà. Veuillez choisir un autre nom d'utilisateur.");
            model.addAttribute("showUsernameEdit", true);
            return "register";
        }

        // Set default values
        patient.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
        patient.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        patient.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
        patient.setActive(true);
        
        // Save patient
        patientRepository.save(patient);

        // Create user account for patient
        userService.createPatientUser(username, patient.getEmail(), patient.getPassword(), patient.getPatientId());

        // Redirect to login with success message
        return "redirect:/login?registered=true";
    }
}
