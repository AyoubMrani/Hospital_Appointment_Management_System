package com.hendisantika.controller;

import com.hendisantika.entity.Appointment;
import com.hendisantika.entity.Patient;
import com.hendisantika.entity.Doctor;
import com.hendisantika.repository.AppointmentRepository;
import com.hendisantika.repository.PatientRepository;
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
 * Appointment Controller for handling appointment management operations
 */
@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SequenceService sequenceService;

    /**
     * Display all appointments
     */
    @GetMapping("/list")
    public String listerTousLesRendezVous(Model model) {
        List<Appointment> appointments = appointmentRepository.findAll();
        model.addAttribute("appointments", appointments);
        return "appointment-list";
    }

    /**
     * Get appointments by date
     */
    @GetMapping("/by-date/{date}")
    public String afficherRendezVousParDate(@PathVariable String date, Model model) {
        List<Appointment> appointments = appointmentRepository.findByDate(date);
        model.addAttribute("appointments", appointments);
        model.addAttribute("date", date);
        return "appointment-list-by-date";
    }

    /**
     * Get appointments by doctor
     */
    @GetMapping("/doctor/{doctorId}")
    public String afficherRendezVousParMedecin(@PathVariable String doctorId, Model model) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        Optional<Doctor> doctor = doctorRepository.findById(doctorId);
        model.addAttribute("appointments", appointments);
        model.addAttribute("doctor", doctor.orElse(null));
        return "appointment-list-by-doctor";
    }

    /**
     * Get appointments by patient
     */
    @GetMapping("/patient/{patientId}")
    public String afficherRendezVousParPatient(@PathVariable String patientId, Model model) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        Optional<Patient> patient = patientRepository.findById(patientId);
        model.addAttribute("appointments", appointments);
        model.addAttribute("patient", patient.orElse(null));
        return "appointment-list-by-patient";
    }

    /**
     * Show form to add new appointment
     */
    @GetMapping("/add")
    public String afficherFormulaireReserverRendezVous(Model model) {
        List<Patient> patients = patientRepository.findAll();
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", patients);
        model.addAttribute("doctors", doctors);
        return "appointment-add";
    }

    /**
     * Save new appointment
     */
    @PostMapping("/save")
    public String reserverRendezVous(@ModelAttribute Appointment appointment) {
        appointment.setAppointmentId(sequenceService.getNextSequenceId("appointment_seq", "A"));
        appointment.setCreatedAt(String.valueOf(System.currentTimeMillis()));
        appointment.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
        appointment.setStatus("Planifié");

        // Fetch and set patient and doctor names
        Optional<Patient> patient = patientRepository.findById(appointment.getPatientId());
        if (patient.isPresent()) {
            Patient p = patient.get();
            appointment.setPatientName(p.getFirstName() + " " + p.getLastName());
        }

        Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
        if (doctor.isPresent()) {
            Doctor d = doctor.get();
            appointment.setDoctorName(d.getFirstName() + " " + d.getLastName());
            appointment.setDoctorSpecialization(d.getSpecialization());
        }

        appointmentRepository.save(appointment);
        return "redirect:/calendar#calendar";
    }

    /**
     * Edit existing appointment
     */
    @GetMapping("/edit/{id}")
    public String afficherFormulaireModifierRendezVous(@PathVariable String id, Model model) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            List<Patient> patients = patientRepository.findAll();
            List<Doctor> doctors = doctorRepository.findAll();
            model.addAttribute("appointment", appointment.get());
            model.addAttribute("patients", patients);
            model.addAttribute("doctors", doctors);
            return "appointment-edit";
        }
        return "redirect:/appointments/list";
    }

    /**
     * Update appointment
     */
    @PostMapping("/update")
    public String modifierRendezVous(@ModelAttribute Appointment appointment) {
        // Preserve existing data
        Optional<Appointment> existingAppointment = appointmentRepository.findById(appointment.getId());
        if (existingAppointment.isPresent()) {
            Appointment existing = existingAppointment.get();
            appointment.setAppointmentId(existing.getAppointmentId());
            appointment.setCreatedAt(existing.getCreatedAt());
            appointment.setPatientId(existing.getPatientId());
            appointment.setDoctorId(existing.getDoctorId());
            appointment.setPatientName(existing.getPatientName());
            appointment.setDoctorName(existing.getDoctorName());
            appointment.setDoctorSpecialization(existing.getDoctorSpecialization());
        }
        appointment.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
        appointmentRepository.save(appointment);
        return "redirect:/appointments/list";
    }

    /**
     * Cancel appointment
     */
    @PostMapping("/cancel/{id}")
    public String annulerRendezVous(@PathVariable String id, @RequestParam String reason) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            Appointment app = appointment.get();
            app.setStatus("Annulé");
            app.setCancelledAt(String.valueOf(System.currentTimeMillis()));
            app.setCancelledReason(reason);
            app.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
            appointmentRepository.save(app);
        }
        return "redirect:/appointments/list";
    }

    /**
     * Delete appointment
     */
    @GetMapping("/delete/{id}")
    public String supprimerRendezVous(@PathVariable String id) {
        appointmentRepository.deleteById(id);
        return "redirect:/calendar#calendar";
    }

    /**
     * View appointment details
     */
    @GetMapping("/view/{id}")
    public String afficherRendezVousParMedecinEtDate(@PathVariable String id, Model model) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            model.addAttribute("appointment", appointment.get());
            return "appointment-view";
        }
        return "redirect:/appointments/list";
    }

    /**
     * Mark appointment as completed
     */
    @GetMapping("/complete/{id}")
    public String trouverCreneauxDisponibles(@PathVariable String id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            Appointment app = appointment.get();
            app.setStatus("Terminé");
            app.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
            appointmentRepository.save(app);
        }
        return "redirect:/appointments/list";
    }

    /**
     * API endpoint for live search
     */
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<Appointment>> rechercherRendezVous(@RequestParam String query) {
        List<Appointment> allAppointments = appointmentRepository.findAll();
        String lowerQuery = query.toLowerCase();

        List<Appointment> results = allAppointments.stream()
                .filter(a -> a.getPatientName().toLowerCase().contains(lowerQuery) ||
                        a.getDoctorName().toLowerCase().contains(lowerQuery) ||
                        a.getAppointmentId().toLowerCase().contains(lowerQuery) ||
                        a.getDate().toLowerCase().contains(lowerQuery) ||
                        a.getStatus().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }

    /**
     * API endpoint to check doctor availability
     */
    @GetMapping("/api/check-availability")
    @ResponseBody
    public ResponseEntity<AvailabilityResponse> checkDoctorAvailability(
            @RequestParam String doctorId,
            @RequestParam String date,
            @RequestParam String time) {

        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);

        // Check if doctor exists
        if (!doctorOpt.isPresent()) {
            return ResponseEntity.ok(new AvailabilityResponse(false, "Médecin non trouvé"));
        }

        Doctor doctor = doctorOpt.get();

        // Check if doctor is active
        if (!doctor.isActive()) {
            return ResponseEntity.ok(new AvailabilityResponse(false, "Le médecin est actuellement inactif"));
        }

        // Check if doctor works on this day using MongoDB workingDays
        java.time.LocalDate appointmentDate = java.time.LocalDate.parse(date);
        java.time.DayOfWeek dayOfWeek = appointmentDate.getDayOfWeek();
        String dayName = dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH);

        // Check if working days are configured for the doctor
        if (doctor.getWorkingDays() == null || doctor.getWorkingDays().isEmpty()) {
            return ResponseEntity.ok(new AvailabilityResponse(false,
                    "Aucun jour de travail configuré pour ce médecin"));
        }

        // Check if the selected day is in the doctor's working days
        boolean isWorkingDay = doctor.getWorkingDays().stream()
                .anyMatch(day -> day.equalsIgnoreCase(dayName));

        if (!isWorkingDay) {
            return ResponseEntity.ok(new AvailabilityResponse(false,
                    "Le médecin ne travaille pas le " + dayName.toLowerCase(),
                    doctor.getWorkingDays()));
        }

        // Check if doctor already has an appointment at this time
        List<Appointment> existingAppointments = appointmentRepository.findByDoctorId(doctorId);

        // Parse the requested time
        java.time.LocalTime requestedTime = java.time.LocalTime.parse(time);

        for (Appointment existingApt : existingAppointments) {
            // Only check non-cancelled appointments
            if (existingApt.getStatus().equals("Annulé")) {
                continue;
            }

            // Check if it's the same date
            if (existingApt.getDate().equals(date)) {
                java.time.LocalTime existingTime = java.time.LocalTime.parse(existingApt.getTime());

                // Check if there's a conflict at the exact same time
                if (existingTime.equals(requestedTime)) {
                    return ResponseEntity
                            .ok(new AvailabilityResponse(false, "Le médecin a déjà un rendez-vous à cette heure"));
                }

                // Check for 10-minute gap
                long minutesDifference = Math
                        .abs(java.time.temporal.ChronoUnit.MINUTES.between(existingTime, requestedTime));
                if (minutesDifference < 10) {
                    return ResponseEntity.ok(new AvailabilityResponse(false,
                            "Il doit y avoir un minimum de 10 minutes entre chaque rendez-vous du médecin. " +
                                    "Rendez-vous existant: " + existingApt.getTime()));
                }
            }
        }

        // Doctor is available
        return ResponseEntity.ok(new AvailabilityResponse(true, "Le médecin est disponible"));
    }

    /**
     * Inner class for availability response
     */
    public static class AvailabilityResponse {
        public boolean available;
        public String message;
        public List<String> workingDays;

        public AvailabilityResponse(boolean available, String message) {
            this.available = available;
            this.message = message;
            this.workingDays = null;
        }

        public AvailabilityResponse(boolean available, String message, List<String> workingDays) {
            this.available = available;
            this.message = message;
            this.workingDays = workingDays;
        }

        public boolean isAvailable() {
            return available;
        }

        public String getMessage() {
            return message;
        }

        public List<String> getWorkingDays() {
            return workingDays;
        }
    }
}
