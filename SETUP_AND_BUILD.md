# GRH - Gestion de Rendez-vous Hospitaliers (Hospital Appointment Management System)

## Project Overview

This is a Spring Boot application built with AdminLTE 3 template for managing hospital appointments. The system allows administrators to manage patients, doctors, and appointments in a hospital setting.

**Technologies Used:**

- **Backend:** Java 21, Spring Boot 3.5.7
- **Database:** MongoDB with `grh_db` database
- **Frontend:** Thymeleaf, AdminLTE 3, Bootstrap
- **Security:** Spring Security
- **Build Tool:** Maven

---

## System Requirements

### Prerequisites

- **Java 21** or higher
- **MongoDB** (v4.0 or higher) running locally on `localhost:27017`
- **Maven** 3.6+ (included with project as `mvnw`)
- **Git** (optional)

### Installation Steps

#### 1. Install MongoDB

**Windows:**

- Download MongoDB Community Edition from [mongodb.com](https://www.mongodb.com/try/download/community)
- Run the installer and follow the installation wizard
- MongoDB will be installed as a Windows service and start automatically
- Verify installation by opening PowerShell and running:
  ```powershell
  mongosh
  ```
  This should connect to your local MongoDB instance

**Alternative: Using Docker**

```powershell
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

#### 2. Create the Database

Once MongoDB is running, create the `grh_db` database:

```powershell
mongosh
# Inside mongosh shell:
use grh_db
db.createCollection("patients")
db.createCollection("doctors")
db.createCollection("appointments")
db.createCollection("users")
```

---

## Project Structure

```
src/main/
├── java/com/hendisantika/
│   ├── SpringbootAdminlte3TemplateApplication.java  # Main application class
│   ├── config/
│   │   ├── FaviconConfiguration.java
│   │   └── WebSecurityConfig.java
│   ├── controller/
│   │   ├── AdminPageController.java
│   │   ├── WebPageController.java
│   │   ├── PatientController.java           # Patient management
│   │   ├── DoctorController.java            # Doctor management
│   │   └── AppointmentController.java       # Appointment management
│   ├── entity/
│   │   ├── User.java                        # MongoDB user entity
│   │   ├── Patient.java                     # Patient entity
│   │   ├── Doctor.java                      # Doctor entity
│   │   └── Appointment.java                 # Appointment entity
│   └── repository/
│       ├── UserRepository.java              # User data access
│       ├── PatientRepository.java           # Patient data access
│       ├── DoctorRepository.java            # Doctor data access
│       └── AppointmentRepository.java       # Appointment data access
└── resources/
    ├── application.properties                # MongoDB configuration
    ├── static/                               # CSS, JS, images
    └── templates/                            # Thymeleaf HTML templates
        ├── shared/                           # Layout components
        │   ├── header.html
        │   ├── menu.html
        │   ├── footer.html
        │   ├── style.html
        │   ├── script.html
        │   └── layout.html
        ├── patient-list.html
        ├── patient-add.html
        ├── doctor-list.html
        ├── doctor-add.html
        ├── appointment-list.html
        ├── appointment-add.html
        ├── login.html
        ├── register.html
        ├── dashboard.html
        └── home.html
```

---

## Key Changes from Original Template

### 1. Database Migration: JPA → MongoDB

**Original (JPA/MySQL):**

```java
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue
    private Long id;
}
```

**New (MongoDB):**

```java
@Document(collection = "users")
public class User {
    @Id
    private String id;
}
```

### 2. Dependencies Updated

**Removed:**

- `spring-boot-starter-data-jpa`
- `mysql-connector-j`
- `h2` database

**Added:**

- `spring-boot-starter-data-mongodb`

### 3. Configuration Changes

**application.properties:**

```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/grh_db
spring.data.mongodb.auto-index-creation=true
```

### 4. Repository Pattern

All repositories now extend `MongoRepository` instead of `JpaRepository`:

```java
public interface UserRepository extends MongoRepository<User, String> {
}
```

---

## New Entities Created

### Patient Entity

- **Collection:** `patients`
- **Fields:** id, name, dateOfBirth, gender, identifiant (unique), email, phone, address, city, postal code, country, active status, timestamps

### Doctor Entity

- **Collection:** `doctors`
- **Fields:** id, name, doctorId (unique), specialization, email, phone, working days, office location, office phone, active status, timestamps

### Appointment Entity

- **Collection:** `appointments`
- **Fields:** id, appointmentId (unique), patientId, doctorId, date, time, status (Scheduled/Completed/Cancelled), remarks, timestamps, cancellation details

---

## Building and Running the Application

### Step 1: Clean Build

Navigate to the project directory and run:

```powershell
.\mvnw clean
```

This removes any previous build artifacts.

### Step 2: Compile and Package

```powershell
.\mvnw package
```

This compiles the code and creates a JAR file in the `target/` directory.

### Step 3: Run the Application

**Option A: Using Spring Boot Maven Plugin**

```powershell
.\mvnw spring-boot:run
```

**Option B: Using the Generated JAR**

```powershell
java -jar target/springboot-adminlte3-template-0.0.1-SNAPSHOT.jar
```

### Step 4: Access the Application

Once the application starts successfully, you'll see:

```
Started SpringbootAdminlte3TemplateApplication in X.XXX seconds
```

Open your browser and navigate to:

- **Application URL:** `http://localhost:8080`
- **Login Page:** `http://localhost:8080/login`

### Step 5: Login Credentials

The application uses in-memory authentication. Use the following credentials:

| Role    | Username | Password |
| ------- | -------- | -------- |
| Admin   | admin    | admin    |
| User    | user     | user     |
| Manager | manager  | manager  |

---

## Available Controllers and Routes

### Patient Management

| Route                   | Method | Description                |
| ----------------------- | ------ | -------------------------- |
| `/patients/list`        | GET    | View all patients          |
| `/patients/add`         | GET    | Show add patient form      |
| `/patients/save`        | POST   | Save new patient           |
| `/patients/edit/{id}`   | GET    | Show edit form for patient |
| `/patients/update`      | POST   | Update patient             |
| `/patients/delete/{id}` | GET    | Delete patient             |
| `/patients/view/{id}`   | GET    | View patient details       |

### Doctor Management

| Route                            | Method | Description                      |
| -------------------------------- | ------ | -------------------------------- |
| `/doctors/list`                  | GET    | View all doctors                 |
| `/doctors/specialization/{spec}` | GET    | Filter doctors by specialization |
| `/doctors/add`                   | GET    | Show add doctor form             |
| `/doctors/save`                  | POST   | Save new doctor                  |
| `/doctors/edit/{id}`             | GET    | Show edit form for doctor        |
| `/doctors/update`                | POST   | Update doctor                    |
| `/doctors/delete/{id}`           | GET    | Delete doctor                    |
| `/doctors/view/{id}`             | GET    | View doctor details              |

### Appointment Management

| Route                               | Method | Description                    |
| ----------------------------------- | ------ | ------------------------------ |
| `/appointments/list`                | GET    | View all appointments          |
| `/appointments/by-date/{date}`      | GET    | Get appointments by date       |
| `/appointments/doctor/{doctorId}`   | GET    | Get appointments by doctor     |
| `/appointments/patient/{patientId}` | GET    | Get appointments by patient    |
| `/appointments/add`                 | GET    | Show add appointment form      |
| `/appointments/save`                | POST   | Save new appointment           |
| `/appointments/edit/{id}`           | GET    | Show edit form for appointment |
| `/appointments/update`              | POST   | Update appointment             |
| `/appointments/cancel/{id}`         | POST   | Cancel appointment             |
| `/appointments/complete/{id}`       | GET    | Mark appointment as completed  |
| `/appointments/delete/{id}`         | GET    | Delete appointment             |
| `/appointments/view/{id}`           | GET    | View appointment details       |

---

## Common Build Commands

### Full Build and Run

```powershell
.\mvnw clean package spring-boot:run
```

### Build Without Tests

```powershell
.\mvnw clean package -DskipTests
```

### View Dependency Tree

```powershell
.\mvnw dependency:tree
```

### Clean Specific Target

```powershell
.\mvnw clean
rm -r target
```

---

## Troubleshooting

### Issue: MongoDB Connection Refused

**Solution:**

1. Verify MongoDB is running:
   ```powershell
   mongosh
   ```
2. If not running, start the MongoDB service:
   ```powershell
   net start MongoDB
   ```
3. Or check the `application.properties` MongoDB URI is correct

### Issue: Port 8080 Already in Use

**Solution:**
Change the port in `application.properties`:

```properties
server.port=8081
```

### Issue: Build Fails with Compilation Errors

**Solution:**

```powershell
.\mvnw clean compile
```

Check error messages for missing dependencies or imports.

### Issue: Database Initialization Failed

**Solution:**
Ensure MongoDB collections exist:

```powershell
mongosh
use grh_db
db.patients.insertOne({test: true}) # Creates collection if not exists
db.patients.deleteOne({test: true})
```

---

## Security Notes

- Default in-memory users are for development only
- For production, implement proper user authentication and database-backed users
- Change security configuration in `WebSecurityConfig.java`
- Enable CSRF protection for production deployments
- Use HTTPS in production

---

## Development Features

- **Auto-reload:** Spring Boot automatically recompiles on file changes
- **Embedded Server:** Runs on Tomcat (port 8080 by default)
- **MongoDB Integration:** Full ODM support with Spring Data MongoDB
- **Validation:** Bean Validation (Jakarta Validation) integrated
- **Security:** Spring Security with form-based authentication
- **UI Framework:** AdminLTE 3 responsive dashboard template

---

## Next Steps

1. Create HTML templates for patient, doctor, and appointment management
2. Implement REST API endpoints for API clients
3. Add comprehensive error handling and logging
4. Implement advanced search and filtering features
5. Add email/SMS notifications for appointments
6. Create reports (PDF, Excel) for appointments and patient history
7. Implement appointment scheduling with availability calendar
8. Add audit logging for all operations
9. Implement backup and restore functionality for MongoDB
10. Add unit and integration tests

---

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
- [AdminLTE 3 Documentation](https://adminlte.io/themes/v3/)
- [Thymeleaf Template Engine](https://www.thymeleaf.org/)
- [MongoDB Documentation](https://docs.mongodb.com/)

---

## License

This project is based on the AdminLTE Spring Boot template.

---

## Support

For issues or questions:

1. Check the troubleshooting section above
2. Review log output in the console
3. Verify MongoDB is running and accessible
4. Ensure all dependencies are correctly resolved by Maven

---

**Last Updated:** November 2025
**Project Version:** 0.0.1-SNAPSHOT
