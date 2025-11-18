package com.hendisantika.repository;

import com.hendisantika.entity.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Patient Repository for MongoDB operations
 */
@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
    Optional<Patient> findByPatientId(String patientId);
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByPhone(String phone);
}
