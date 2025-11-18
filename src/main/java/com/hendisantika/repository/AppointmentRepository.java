package com.hendisantika.repository;

import com.hendisantika.entity.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Appointment Repository for MongoDB operations
 */
@Repository
public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    Optional<Appointment> findByAppointmentId(String appointmentId);
    List<Appointment> findByPatientId(String patientId);
    List<Appointment> findByDoctorId(String doctorId);
    List<Appointment> findByDate(String date);
    List<Appointment> findByStatus(String status);
    List<Appointment> findByDoctorIdAndDate(String doctorId, String date);
    List<Appointment> findByPatientIdAndStatus(String patientId, String status);
}
