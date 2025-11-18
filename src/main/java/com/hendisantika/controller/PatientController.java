package com.hendisantika.controller;

import com.hendisantika.entity.Patient;
import com.hendisantika.repository.PatientRepository;
import com.hendisantika.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Patient Controller for handling patient management operations
 */
@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private SequenceService sequenceService;

    /**
     * Display all patients
     */
    @GetMapping("/list")
    public String listPatients(Model model) {
        List<Patient> patients = patientRepository.findAll();
        model.addAttribute("patients", patients);
        return "patient-list";
    }

    /**
     * Show form to add new patient
     */
    @GetMapping("/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-add";
    }

    /**
     * Save new patient
     */
    @PostMapping("/save")
    public String savePatient(@ModelAttribute Patient patient) {
        patient.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
        patient.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        patient.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
        patientRepository.save(patient);
        return "redirect:/patients/list";
    }

    /**
     * Edit existing patient
     */
    @GetMapping("/edit/{id}")
    public String showEditPatientForm(@PathVariable String id, Model model) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            return "patient-edit";
        }
        return "redirect:/patients/list";
    }

    /**
     * Update patient
     */
    @PostMapping("/update")
    public String updatePatient(@ModelAttribute Patient patient) {
        // Preserve existing data
        Optional<Patient> existingPatient = patientRepository.findById(patient.getId());
        if (existingPatient.isPresent()) {
            Patient existing = existingPatient.get();
            patient.setPatientId(existing.getPatientId());
            patient.setCreatedAt(existing.getCreatedAt());
        }
        patient.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
        patientRepository.save(patient);
        return "redirect:/patients/list";
    }

    /**
     * Delete patient
     */
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable String id) {
        patientRepository.deleteById(id);
        return "redirect:/patients/list";
    }

    /**
     * View patient details
     */
    @GetMapping("/view/{id}")
    public String viewPatient(@PathVariable String id, Model model) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            return "patient-view";
        }
        return "redirect:/patients/list";
    }

    /**
     * API endpoint for live search
     */
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String query) {
        List<Patient> allPatients = patientRepository.findAll();
        String lowerQuery = query.toLowerCase();
        
        List<Patient> results = allPatients.stream()
                .filter(p -> p.getFirstName().toLowerCase().contains(lowerQuery) ||
                        p.getLastName().toLowerCase().contains(lowerQuery) ||
                        p.getEmail().toLowerCase().contains(lowerQuery) ||
                        p.getPhone().toLowerCase().contains(lowerQuery) ||
                        p.getPatientId().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(results);
    }
}
