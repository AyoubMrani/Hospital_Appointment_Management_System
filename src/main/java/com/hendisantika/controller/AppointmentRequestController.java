package com.hendisantika.controller;

import com.hendisantika.entity.Appointment;
import com.hendisantika.entity.AppointmentRequest;
import com.hendisantika.entity.Doctor;
import com.hendisantika.entity.Patient;
import com.hendisantika.entity.User;
import com.hendisantika.repository.AppointmentRepository;
import com.hendisantika.repository.AppointmentRequestRepository;
import com.hendisantika.repository.DoctorRepository;
import com.hendisantika.repository.PatientRepository;
import com.hendisantika.repository.UserRepository;
import com.hendisantika.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Appointment Request Controller - Handles appointment request workflows
 */
@Controller
@RequestMapping("/appointment-requests")
public class AppointmentRequestController {

    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SequenceService sequenceService;

    /**
     * Show form for patient to request appointment
     */
    @GetMapping("/request")
    public String showRequestForm(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (!userOpt.isPresent() || userOpt.get().getPatientId() == null) {
            return "redirect:/login";
        }

        User user = userOpt.get();
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        model.addAttribute("appointmentRequest", new AppointmentRequest());
        model.addAttribute("patientId", user.getPatientId());
        return "appointment-request";
    }

    /**
     * Save appointment request from patient
     */
    @PostMapping("/save")
    public String saveAppointmentRequest(@ModelAttribute AppointmentRequest appointmentRequest,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (!userOpt.isPresent() || userOpt.get().getPatientId() == null) {
            return "redirect:/login";
        }

        User user = userOpt.get();
        String patientId = user.getPatientId();
        Optional<Patient> patient = patientRepository.findById(patientId);

        if (patient.isPresent()) {
            Patient p = patient.get();
            appointmentRequest.setRequestId(sequenceService.getNextSequenceId("appointment_request_seq", "AR"));
            appointmentRequest.setPatientId(patientId);
            appointmentRequest.setPatientName(p.getFirstName() + " " + p.getLastName());

            // Get doctor info
            Optional<Doctor> doctor = doctorRepository.findById(appointmentRequest.getDoctorId());
            if (doctor.isPresent()) {
                Doctor d = doctor.get();
                appointmentRequest.setDoctorName(d.getFirstName() + " " + d.getLastName());
            }

            appointmentRequest.setStatus("PENDING");
            appointmentRequest.setCreatedAt(String.valueOf(System.currentTimeMillis()));
            appointmentRequest.setUpdatedAt(String.valueOf(System.currentTimeMillis()));

            appointmentRequestRepository.save(appointmentRequest);
        }

        return "redirect:/appointment-requests/my-requests";
    }

    /**
     * View appointment requests for patient (my requests)
     */
    @GetMapping("/my-requests")
    public String myRequests(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (!userOpt.isPresent() || userOpt.get().getPatientId() == null) {
            return "redirect:/login";
        }

        User user = userOpt.get();
        String patientId = user.getPatientId();
        List<AppointmentRequest> requests = appointmentRequestRepository.findByPatientId(patientId);
        model.addAttribute("requests", requests);
        return "my-requests";
    }

    /**
     * View appointment requests for doctor (requests from patients)
     */
    @GetMapping("/pending-requests")
    public String pendingRequests(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findByUsername(authentication.getName());
        if (!userOpt.isPresent() || userOpt.get().getDoctorId() == null) {
            return "redirect:/login";
        }

        User user = userOpt.get();
        String doctorId = user.getDoctorId();
        List<AppointmentRequest> requests = appointmentRequestRepository.findByDoctorId(doctorId);
        model.addAttribute("requests", requests);
        model.addAttribute("doctorId", doctorId);
        return "pending-requests";
    }

    /**
     * Doctor approves appointment request
     */
    @PostMapping("/approve/{id}")
    public String approveRequest(@PathVariable String id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<AppointmentRequest> request = appointmentRequestRepository.findById(id);

        if (request.isPresent()) {
            AppointmentRequest ar = request.get();
            ar.setStatus("APPROVED");
            ar.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
            appointmentRequestRepository.save(ar);

            // Create appointment from the approved request
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(sequenceService.getNextSequenceId("appointment_seq", "APT"));
            appointment.setPatientId(ar.getPatientId());
            appointment.setPatientName(ar.getPatientName());
            appointment.setDoctorId(ar.getDoctorId());
            appointment.setDoctorName(ar.getDoctorName());
            appointment.setDate(ar.getAppointmentDate());
            appointment.setTime(ar.getAppointmentTime());
            appointment.setStatus("Planifié"); // Scheduled
            appointment.setRemarks("Created from appointment request: " + ar.getRequestId());
            appointment.setCreatedAt(String.valueOf(System.currentTimeMillis()));
            appointment.setUpdatedAt(String.valueOf(System.currentTimeMillis()));

            appointmentRepository.save(appointment);
        }

        return "redirect:/appointment-requests/pending-requests";
    }

    /**
     * Doctor denies appointment request
     */
    @PostMapping("/deny/{id}")
    public String denyRequest(@PathVariable String id,
            @RequestParam String denialReason,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Optional<AppointmentRequest> request = appointmentRequestRepository.findById(id);

        if (request.isPresent()) {
            AppointmentRequest ar = request.get();
            ar.setStatus("DENIED");
            ar.setDenialReason(denialReason);
            ar.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
            appointmentRequestRepository.save(ar);
        }

        return "redirect:/appointment-requests/pending-requests";
    }

    /**
     * View request details
     */
    @GetMapping("/view/{id}")
    public String viewRequest(@PathVariable String id, Model model) {
        Optional<AppointmentRequest> request = appointmentRequestRepository.findById(id);

        if (request.isPresent()) {
            model.addAttribute("appointmentRequest", request.get());
            return "appointment-request-detail";
        }

        return "redirect:/";
    }
}
