package com.hendisantika.controller;

import com.hendisantika.entity.Doctor;
import com.hendisantika.repository.DoctorRepository;
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
 * Doctor Controller for handling doctor management operations
 */
@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SequenceService sequenceService;

    /**
     * Display all doctors
     */
    @GetMapping("/list")
    public String listerTousLesMedecins(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "doctor-list";
    }

    /**
     * Filter doctors by specialization
     */
    @GetMapping("/specialization/{specialization}")
    public String rechercherMedecinParSpecialite(@PathVariable String specialization, Model model) {
        List<Doctor> doctors = doctorRepository.findBySpecialization(specialization);
        model.addAttribute("doctors", doctors);
        model.addAttribute("specialization", specialization);
        return "doctor-list-filtered";
    }

    /**
     * Show form to add new doctor
     */
    @GetMapping("/add")
    public String afficherFormulaireAjouterMedecin(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor-add";
    }

    /**
     * Save new doctor
     */
    @PostMapping("/save")
    public String ajouterMedecin(@ModelAttribute Doctor doctor) {
        doctor.setDoctorId(sequenceService.getNextSequenceId("doctor_seq", "D"));
        doctor.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        doctor.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
        doctorRepository.save(doctor);
        return "redirect:/doctors/list";
    }

    /**
     * Edit existing doctor
     */
    @GetMapping("/edit/{id}")
    public String afficherFormulaireModifierMedecin(@PathVariable String id, Model model) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            model.addAttribute("doctor", doctor.get());
            return "doctor-edit";
        }
        return "redirect:/doctors/list";
    }

    /**
     * Update doctor
     */
    @PostMapping("/update")
    public String modifierMedecin(@ModelAttribute Doctor doctor) {
        // Preserve existing data
        Optional<Doctor> existingDoctor = doctorRepository.findById(doctor.getId());
        if (existingDoctor.isPresent()) {
            Doctor existing = existingDoctor.get();
            doctor.setDoctorId(existing.getDoctorId());
            doctor.setCreatedAt(existing.getCreatedAt());
        }
        doctor.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
        doctorRepository.save(doctor);
        return "redirect:/doctors/list";
    }

    /**
     * Delete doctor
     */
    @GetMapping("/delete/{id}")
    public String supprimerMedecin(@PathVariable String id) {
        doctorRepository.deleteById(id);
        return "redirect:/doctors/list";
    }

    /**
     * View doctor details
     */
    @GetMapping("/view/{id}")
    public String rechercherMedecinParId(@PathVariable String id, Model model) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (doctor.isPresent()) {
            model.addAttribute("doctor", doctor.get());
            return "doctor-view";
        }
        return "redirect:/doctors/list";
    }

    /**
     * API endpoint for live search
     */
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<Doctor>> rechercherMedecins(@RequestParam String query) {
        List<Doctor> allDoctors = doctorRepository.findAll();
        String lowerQuery = query.toLowerCase();
        
        List<Doctor> results = allDoctors.stream()
                .filter(d -> d.getFirstName().toLowerCase().contains(lowerQuery) ||
                        d.getLastName().toLowerCase().contains(lowerQuery) ||
                        d.getDoctorId().toLowerCase().contains(lowerQuery) ||
                        d.getSpecialization().toLowerCase().contains(lowerQuery) ||
                        d.getEmail().toLowerCase().contains(lowerQuery) ||
                        d.getPhone().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(results);
    }
}
