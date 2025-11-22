package com.hendisantika;

import com.hendisantika.entity.User;
import com.hendisantika.entity.Doctor;
import com.hendisantika.entity.Patient;
import com.hendisantika.repository.UserRepository;
import com.hendisantika.repository.DoctorRepository;
import com.hendisantika.repository.PatientRepository;
import com.hendisantika.repository.SequenceRepository;
import com.hendisantika.service.SequenceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import java.util.Arrays;

@SpringBootApplication
public class SpringbootAdminlte3TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAdminlte3TemplateApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository, DoctorRepository doctorRepository,
            PatientRepository patientRepository, SequenceRepository sequenceRepository,
            MongoOperations mongoOperations, SequenceService sequenceService) {
        return (args) -> {
            try {
                // Drop all collections to ensure clean state
                // mongoOperations.dropCollection("users");
                // mongoOperations.dropCollection("doctors");
                // mongoOperations.dropCollection("patients");
                // mongoOperations.dropCollection("appointments");
                // mongoOperations.dropCollection("appointment_requests");
                // mongoOperations.dropCollection("sequences");
                // System.out.println("✅ Dropped all existing collections");
            } catch (Exception e) {
                System.out.println("ℹ️  Collections don't exist yet or already cleaned");
            }

            long currentTime = System.currentTimeMillis();
            String timeStr = String.valueOf(currentTime);

            // DEMO DATA INITIALIZATION DISABLED - Data will persist between restarts
            /*
             * // Create 3 Doctors
             * Doctor doctor1 = new Doctor();
             * doctor1.setDoctorId(sequenceService.getNextSequenceId("doctor_seq", "D"));
             * doctor1.setFirstName("Ahmed");
             * doctor1.setLastName("Benali");
             * doctor1.setSpecialization("Cardiologue");
             * doctor1.setEmail("ahmed.benali@hospital.com");
             * doctor1.setPhone("+212 601234567");
             * doctor1.setWorkingDays(Arrays.asList("Lundi", "Mardi", "Mercredi", "Jeudi"));
             * doctor1.setOfficeLocation("Bureau 101");
             * doctor1.setOfficePhone("+212 537123456");
             * doctor1.setActive(true);
             * doctor1.setCreatedAt(timeStr);
             * doctor1.setUpdatedAt(timeStr);
             * Doctor savedDoc1 = doctorRepository.save(doctor1);
             * 
             * Doctor doctor2 = new Doctor();
             * doctor2.setDoctorId(sequenceService.getNextSequenceId("doctor_seq", "D"));
             * doctor2.setFirstName("Maryam");
             * doctor2.setLastName("Huffman");
             * doctor2.setSpecialization("Dermatologue");
             * doctor2.setEmail("maryam.huffman@hospital.com");
             * doctor2.setPhone("+212 602234567");
             * doctor2.setWorkingDays(Arrays.asList("Mardi", "Mercredi", "Jeudi",
             * "Vendredi"));
             * doctor2.setOfficeLocation("Bureau 202");
             * doctor2.setOfficePhone("+212 537234567");
             * doctor2.setActive(true);
             * doctor2.setCreatedAt(timeStr);
             * doctor2.setUpdatedAt(timeStr);
             * Doctor savedDoc2 = doctorRepository.save(doctor2);
             * 
             * Doctor doctor3 = new Doctor();
             * doctor3.setDoctorId(sequenceService.getNextSequenceId("doctor_seq", "D"));
             * doctor3.setFirstName("Hassan");
             * doctor3.setLastName("Mohammad");
             * doctor3.setSpecialization("Médecin Généraliste");
             * doctor3.setEmail("hassan.mohammad@hospital.com");
             * doctor3.setPhone("+212 603234567");
             * doctor3.setWorkingDays(Arrays.asList("Lundi", "Mercredi", "Vendredi",
             * "Samedi"));
             * doctor3.setOfficeLocation("Bureau 303");
             * doctor3.setOfficePhone("+212 537345678");
             * doctor3.setActive(true);
             * doctor3.setCreatedAt(timeStr);
             * doctor3.setUpdatedAt(timeStr);
             * Doctor savedDoc3 = doctorRepository.save(doctor3);
             * 
             * // Create 9 Patients (3 for each doctor)
             * // Patients for Doctor 1
             * Patient patient1 = new Patient();
             * patient1.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
             * patient1.setFirstName("Fatima");
             * patient1.setLastName("Aziz");
             * patient1.setDateOfBirth("1985-03-15");
             * patient1.setGender("Femme");
             * patient1.setEmail("fatima.aziz@example.com");
             * patient1.setPhone("+212 611111111");
             * patient1.setAddress("123 Rue de la Paix");
             * patient1.setCity("Casablanca");
             * patient1.setPostalCode("20000");
             * patient1.setCountry("Maroc");
             * patient1.setActive(true);
             * patient1.setCreatedAt(timeStr);
             * patient1.setUpdatedAt(timeStr);
             * Patient savedPat1 = patientRepository.save(patient1);
             * 
             * Patient patient2 = new Patient();
             * patient2.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
             * patient2.setFirstName("Ibrahim");
             * patient2.setLastName("Hassan");
             * patient2.setDateOfBirth("1978-07-22");
             * patient2.setGender("Homme");
             * patient2.setEmail("ibrahim.hassan@example.com");
             * patient2.setPhone("+212 611111112");
             * patient2.setAddress("456 Avenue Mohamed V");
             * patient2.setCity("Rabat");
             * patient2.setPostalCode("10000");
             * patient2.setCountry("Maroc");
             * patient2.setActive(true);
             * patient2.setCreatedAt(timeStr);
             * patient2.setUpdatedAt(timeStr);
             * Patient savedPat2 = patientRepository.save(patient2);
             * 
             * Patient patient3 = new Patient();
             * patient3.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
             * patient3.setFirstName("Leila");
             * patient3.setLastName("Bennani");
             * patient3.setDateOfBirth("1992-11-08");
             * patient3.setGender("Femme");
             * patient3.setEmail("leila.bennani@example.com");
             * patient3.setPhone("+212 611111113");
             * patient3.setAddress("789 Boulevard Hassan II");
             * patient3.setCity("Marrakech");
             * patient3.setPostalCode("40000");
             * patient3.setCountry("Maroc");
             * patient3.setActive(true);
             * patient3.setCreatedAt(timeStr);
             * patient3.setUpdatedAt(timeStr);
             * Patient savedPat3 = patientRepository.save(patient3);
             * 
             * // Patients for Doctor 2
             * Patient patient4 = new Patient();
             * patient4.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
             * patient4.setFirstName("Youssef");
             * patient4.setLastName("Sadir");
             * patient4.setDateOfBirth("1988-05-12");
             * patient4.setGender("Homme");
             * patient4.setEmail("youssef.sadir@example.com");
             * patient4.setPhone("+212 611111114");
             * patient4.setAddress("321 Rue de l'Independence");
             * patient4.setCity("Fez");
             * patient4.setPostalCode("30000");
             * patient4.setCountry("Maroc");
             * patient4.setActive(true);
             * patient4.setCreatedAt(timeStr);
             * patient4.setUpdatedAt(timeStr);
             * Patient savedPat4 = patientRepository.save(patient4);
             * 
             * Patient patient5 = new Patient();
             * patient5.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
             * patient5.setFirstName("Noor");
             * patient5.setLastName("Al-Rashid");
             * patient5.setDateOfBirth("1995-02-28");
             * patient5.setGender("Femme");
             * patient5.setEmail("noor.rashid@example.com");
             * patient5.setPhone("+212 611111115");
             * patient5.setAddress("654 Avenue Sidi Mohammad");
             * patient5.setCity("Tangier");
             * patient5.setPostalCode("90000");
             * patient5.setCountry("Maroc");
             * patient5.setActive(true);
             * patient5.setCreatedAt(timeStr);
             * patient5.setUpdatedAt(timeStr);
             * Patient savedPat5 = patientRepository.save(patient5);
             * 
             * Patient patient6 = new Patient();
             * patient6.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
             * patient6.setFirstName("Karim");
             * patient6.setLastName("Driss");
             * patient6.setDateOfBirth("1980-09-17");
             * patient6.setGender("Homme");
             * patient6.setEmail("karim.driss@example.com");
             * patient6.setPhone("+212 611111116");
             * patient6.setAddress("987 Rue Prince Héritier");
             * patient6.setCity("Agadir");
             * patient6.setPostalCode("80000");
             * patient6.setCountry("Maroc");
             * patient6.setActive(true);
             * patient6.setCreatedAt(timeStr);
             * patient6.setUpdatedAt(timeStr);
             * Patient savedPat6 = patientRepository.save(patient6);
             * 
             * // Patients for Doctor 3
             * Patient patient7 = new Patient();
             * patient7.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
             * patient7.setFirstName("Amira");
             * patient7.setLastName("Khalil");
             * patient7.setDateOfBirth("1990-06-03");
             * patient7.setGender("Femme");
             * patient7.setEmail("amira.khalil@example.com");
             * patient7.setPhone("+212 611111117");
             * patient7.setAddress("147 Boulevard Zaid");
             * patient7.setCity("Sale");
             * patient7.setPostalCode("11000");
             * patient7.setCountry("Maroc");
             * patient7.setActive(true);
             * patient7.setCreatedAt(timeStr);
             * patient7.setUpdatedAt(timeStr);
             * Patient savedPat7 = patientRepository.save(patient7);
             * 
             * Patient patient8 = new Patient();
             * patient8.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
             * patient8.setFirstName("Mohamed");
             * patient8.setLastName("Imani");
             * patient8.setDateOfBirth("1975-12-25");
             * patient8.setGender("Homme");
             * patient8.setEmail("mohamed.imani@example.com");
             * patient8.setPhone("+212 611111118");
             * patient8.setAddress("258 Rue du Port");
             * patient8.setCity("Tétouan");
             * patient8.setPostalCode("93000");
             * patient8.setCountry("Maroc");
             * patient8.setActive(true);
             * patient8.setCreatedAt(timeStr);
             * patient8.setUpdatedAt(timeStr);
             * Patient savedPat8 = patientRepository.save(patient8);
             * 
             * Patient patient9 = new Patient();
             * patient9.setPatientId(sequenceService.getNextSequenceId("patient_seq", "P"));
             * patient9.setFirstName("Zahra");
             * patient9.setLastName("Taoufiq");
             * patient9.setDateOfBirth("1987-08-19");
             * patient9.setGender("Femme");
             * patient9.setEmail("zahra.taoufiq@example.com");
             * patient9.setPhone("+212 611111119");
             * patient9.setAddress("369 Rue Majorelle");
             * patient9.setCity("Meknes");
             * patient9.setPostalCode("50000");
             * patient9.setCountry("Maroc");
             * patient9.setActive(true);
             * patient9.setCreatedAt(timeStr);
             * patient9.setUpdatedAt(timeStr);
             * Patient savedPat9 = patientRepository.save(patient9);
             * 
             * // Create Users (Admins and Doctors)
             * User adminUser = new User();
             * adminUser.setUsername("admin");
             * adminUser.setEmail("admin@hospital.com");
             * adminUser.setPassword("admin123");
             * adminUser.setRole("ADMIN");
             * adminUser.setDoctorId(null);
             * adminUser.setPatientId(null);
             * adminUser.setActive(true);
             * userRepository.save(adminUser);
             * 
             * User doctor1User = new User();
             * doctor1User.setUsername("dr_ahmed");
             * doctor1User.setEmail("ahmed.benali@hospital.com");
             * doctor1User.setPassword("doctor123");
             * doctor1User.setRole("DOCTOR");
             * doctor1User.setDoctorId(savedDoc1.getId());
             * doctor1User.setPatientId(null);
             * doctor1User.setActive(true);
             * userRepository.save(doctor1User);
             * 
             * User doctor2User = new User();
             * doctor2User.setUsername("dr_maryam");
             * doctor2User.setEmail("maryam.huffman@hospital.com");
             * doctor2User.setPassword("doctor123");
             * doctor2User.setRole("DOCTOR");
             * doctor2User.setDoctorId(savedDoc2.getId());
             * doctor2User.setPatientId(null);
             * doctor2User.setActive(true);
             * userRepository.save(doctor2User);
             * 
             * User doctor3User = new User();
             * doctor3User.setUsername("dr_hassan");
             * doctor3User.setEmail("hassan.mohammad@hospital.com");
             * doctor3User.setPassword("doctor123");
             * doctor3User.setRole("DOCTOR");
             * doctor3User.setDoctorId(savedDoc3.getId());
             * doctor3User.setPatientId(null);
             * doctor3User.setActive(true);
             * userRepository.save(doctor3User);
             * 
             * // Create Patient Users
             * User patient1User = new User();
             * patient1User.setUsername("patient_fatima");
             * patient1User.setEmail("fatima.aziz@example.com");
             * patient1User.setPassword("patient123");
             * patient1User.setRole("PATIENT");
             * patient1User.setDoctorId(null);
             * patient1User.setPatientId(savedPat1.getId());
             * patient1User.setActive(true);
             * userRepository.save(patient1User);
             * 
             * User patient2User = new User();
             * patient2User.setUsername("patient_ibrahim");
             * patient2User.setEmail("ibrahim.hassan@example.com");
             * patient2User.setPassword("patient123");
             * patient2User.setRole("PATIENT");
             * patient2User.setDoctorId(null);
             * patient2User.setPatientId(savedPat2.getId());
             * patient2User.setActive(true);
             * userRepository.save(patient2User);
             * 
             * User patient3User = new User();
             * patient3User.setUsername("patient_leila");
             * patient3User.setEmail("leila.bennani@example.com");
             * patient3User.setPassword("patient123");
             * patient3User.setRole("PATIENT");
             * patient3User.setDoctorId(null);
             * patient3User.setPatientId(savedPat3.getId());
             * patient3User.setActive(true);
             * userRepository.save(patient3User);
             * 
             * User patient4User = new User();
             * patient4User.setUsername("patient_youssef");
             * patient4User.setEmail("youssef.sadir@example.com");
             * patient4User.setPassword("patient123");
             * patient4User.setRole("PATIENT");
             * patient4User.setDoctorId(null);
             * patient4User.setPatientId(savedPat4.getId());
             * patient4User.setActive(true);
             * userRepository.save(patient4User);
             * 
             * User patient5User = new User();
             * patient5User.setUsername("patient_noor");
             * patient5User.setEmail("noor.rashid@example.com");
             * patient5User.setPassword("patient123");
             * patient5User.setRole("PATIENT");
             * patient5User.setDoctorId(null);
             * patient5User.setPatientId(savedPat5.getId());
             * patient5User.setActive(true);
             * userRepository.save(patient5User);
             * 
             * User patient6User = new User();
             * patient6User.setUsername("patient_karim");
             * patient6User.setEmail("karim.driss@example.com");
             * patient6User.setPassword("patient123");
             * patient6User.setRole("PATIENT");
             * patient6User.setDoctorId(null);
             * patient6User.setPatientId(savedPat6.getId());
             * patient6User.setActive(true);
             * userRepository.save(patient6User);
             * 
             * User patient7User = new User();
             * patient7User.setUsername("patient_amira");
             * patient7User.setEmail("amira.khalil@example.com");
             * patient7User.setPassword("patient123");
             * patient7User.setRole("PATIENT");
             * patient7User.setDoctorId(null);
             * patient7User.setPatientId(savedPat7.getId());
             * patient7User.setActive(true);
             * userRepository.save(patient7User);
             * 
             * User patient8User = new User();
             * patient8User.setUsername("patient_mohamed");
             * patient8User.setEmail("mohamed.imani@example.com");
             * patient8User.setPassword("patient123");
             * patient8User.setRole("PATIENT");
             * patient8User.setDoctorId(null);
             * patient8User.setPatientId(savedPat8.getId());
             * patient8User.setActive(true);
             * userRepository.save(patient8User);
             * 
             * User patient9User = new User();
             * patient9User.setUsername("patient_zahra");
             * patient9User.setEmail("zahra.taoufiq@example.com");
             * patient9User.setPassword("patient123");
             * patient9User.setRole("PATIENT");
             * patient9User.setDoctorId(null);
             * patient9User.setPatientId(savedPat9.getId());
             * patient9User.setActive(true);
             * userRepository.save(patient9User);
             * 
             * System.out.println("✅ Sample data generated successfully!");
             * System.out.println("   3 Doctors created");
             * System.out.println("   9 Patients created (3 per doctor)");
             * System.out.println("   13 Users created (1 admin, 3 doctors, 9 patients)");
             * System.out.println("   No appointments created");
             * System.out.println("   No appointment_requests created");
             */

            System.out.println("ℹ️  Demo data initialization is disabled. Using existing database data.");
        };
    }
}
