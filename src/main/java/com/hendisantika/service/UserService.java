package com.hendisantika.service;

import com.hendisantika.entity.User;
import com.hendisantika.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User Service for managing user account creation and operations
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a user account for a doctor
     */
    public User createDoctorUser(String username, String email, String password, String doctorId) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("DOCTOR");
        user.setDoctorId(doctorId);
        user.setActive(true);
        return userRepository.save(user);
    }

    /**
     * Create a user account for a patient
     */
    public User createPatientUser(String username, String email, String password, String patientId) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("PATIENT");
        user.setPatientId(patientId);
        user.setActive(true);
        return userRepository.save(user);
    }

    /**
     * Check if username already exists
     */
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Deactivate user account
     */
    public void deactivateUser(String doctorId, String patientId) {
        if (doctorId != null && !doctorId.isEmpty()) {
            userRepository.findAll().stream()
                    .filter(u -> doctorId.equals(u.getDoctorId()))
                    .forEach(u -> {
                        u.setActive(false);
                        userRepository.save(u);
                    });
        }
        if (patientId != null && !patientId.isEmpty()) {
            userRepository.findAll().stream()
                    .filter(u -> patientId.equals(u.getPatientId()))
                    .forEach(u -> {
                        u.setActive(false);
                        userRepository.save(u);
                    });
        }
    }
}
