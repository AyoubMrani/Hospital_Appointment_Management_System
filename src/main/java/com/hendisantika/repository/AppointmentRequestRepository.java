package com.hendisantika.repository;

import com.hendisantika.entity.AppointmentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for AppointmentRequest entity
 */
@Repository
public interface AppointmentRequestRepository extends MongoRepository<AppointmentRequest, String> {
    List<AppointmentRequest> findByPatientId(String patientId);

    List<AppointmentRequest> findByDoctorId(String doctorId);

    List<AppointmentRequest> findByStatus(String status);

    Optional<AppointmentRequest> findByRequestId(String requestId);
}
