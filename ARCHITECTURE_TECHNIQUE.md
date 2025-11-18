# Architecture Technique - Système GRH

## 🏗️ Vue d'Ensemble de l'Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend (Thymeleaf)                       │
│  HTML5, CSS3, Bootstrap, AdminLTE 3.2.0, JavaScript         │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│              Spring Boot Controller Layer                     │
│  PatientController, DoctorController, AppointmentController │
│  DashboardStatsController                                    │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                Service Layer                                  │
│  SequenceService (Auto-increment IDs)                       │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│                Repository Layer                              │
│  Spring Data MongoDB                                         │
│  PatientRepository, DoctorRepository,                        │
│  AppointmentRepository, SequenceRepository                   │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│              MongoDB (NoSQL Database)                         │
│  Collections: patients, doctors, appointments, sequences    │
│  Database: grh_db                                            │
└─────────────────────────────────────────────────────────────┘
```

---

## 📂 Structure des Fichiers

```
springboot-adminlte3-template-main/
│
├── src/
│   ├── main/
│   │   ├── java/com/hendisantika/
│   │   │   ├── SpringbootAdminlte3TemplateApplication.java
│   │   │   ├── config/
│   │   │   │   ├── FaviconConfiguration.java
│   │   │   │   └── WebSecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AdminPageController.java
│   │   │   │   ├── PatientController.java (✨ MODIFIÉ)
│   │   │   │   ├── DoctorController.java (✨ MODIFIÉ)
│   │   │   │   ├── AppointmentController.java (✨ MODIFIÉ)
│   │   │   │   ├── DashboardStatsController.java
│   │   │   │   └── WebPageController.java
│   │   │   ├── entity/
│   │   │   │   ├── Patient.java (✨ MODIFIÉ)
│   │   │   │   ├── Doctor.java (✨ MODIFIÉ)
│   │   │   │   ├── Appointment.java (✨ MODIFIÉ)
│   │   │   │   ├── Sequence.java (🆕 NOUVEAU)
│   │   │   │   └── User.java
│   │   │   ├── repository/
│   │   │   │   ├── PatientRepository.java (✨ MODIFIÉ)
│   │   │   │   ├── DoctorRepository.java
│   │   │   │   ├── AppointmentRepository.java
│   │   │   │   ├── SequenceRepository.java (🆕 NOUVEAU)
│   │   │   │   └── UserRepository.java
│   │   │   └── service/
│   │   │       └── SequenceService.java (🆕 NOUVEAU)
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   │   └── live-search.js
│   │       │   └── img/
│   │       └── templates/
│   │           ├── dashboard.html (✨ MODIFIÉ)
│   │           ├── home.html
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── patient-list.html (✨ MODIFIÉ - French)
│   │           ├── patient-add.html (✨ MODIFIÉ - French)
│   │           ├── patient-edit.html
│   │           ├── patient-view.html
│   │           ├── doctor-list.html (✨ MODIFIÉ - French)
│   │           ├── doctor-add.html (✨ MODIFIÉ - French)
│   │           ├── doctor-edit.html
│   │           ├── doctor-view.html
│   │           ├── appointment-list.html (✨ MODIFIÉ - French)
│   │           ├── appointment-add.html (✨ MODIFIÉ - French)
│   │           ├── appointment-edit.html
│   │           ├── appointment-view.html
│   │           └── shared/
│   │               ├── layout.html (✨ MODIFIÉ)
│   │               ├── header.html
│   │               ├── menu.html (✨ MODIFIÉ - French)
│   │               ├── footer.html
│   │               ├── script.html
│   │               └── style.html
│   │
│   └── test/
│       └── java/com/hendisantika/
│           └── SpringbootAdminlte3TemplateApplicationTests.java
│
├── pom.xml (✨ MODIFIÉ - MongoDB dependencies)
├── mvnw
├── mvnw.cmd
├── MODIFICATIONS_RESUME.md (🆕 NOUVEAU)
├── GUIDE_JOURS_TRAVAIL.md (🆕 NOUVEAU)
├── GUIDE_DEMARRAGE_RAPIDE.md (🆕 NOUVEAU)
└── README.md
```

---

## 🔄 Flux des Données

### 1. Création d'un Patient

```
Formulaire HTML (patient-add.html)
  ↓
POST /patients/save
  ↓
PatientController.savePatient()
  {
    - Génère patientId via SequenceService
    - Crée le Patient
    - Appelle patientRepository.save()
  }
  ↓
PatientRepository.save(patient)
  ↓
MongoDB: db.patients.insertOne({...})
  ↓
Redirection: /patients/list
  ↓
Affichage de la liste mise à jour
```

### 2. Création d'un Rendez-vous

```
Formulaire HTML (appointment-add.html)
  ↓
POST /appointments/save
  ↓
AppointmentController.saveAppointment()
  {
    - Génère appointmentId via SequenceService
    - Récupère les données du Patient (par ID)
    - Récupère les données du Médecin (par ID)
    - Combine les informations
    - Appelle appointmentRepository.save()
  }
  ↓
AppointmentRepository.save(appointment)
  ↓
MongoDB: db.appointments.insertOne({...})
  ↓
Redirection: /appointments/list
  ↓
Affichage de la liste mise à jour
```

### 3. Recherche en Temps Réel

```
Barre de Recherche (live-search.js)
  ↓
user tape "Jean"
  ↓
GET /patients/api/search?query=Jean
  ↓
PatientController.searchPatients()
  {
    - Récupère tous les patients
    - Filtre par firstName, lastName, email, phone, patientId
    - Retourne les résultats en JSON
  }
  ↓
Response JSON: [patient1, patient2, ...]
  ↓
live-search.js filtre le tableau HTML
  ↓
Affichage en temps réel sans rechargement
```

---

## 🗄️ Schéma de la Base de Données MongoDB

### Collection: patients

```javascript
{
  _id: ObjectId,
  patientId: "P00001" (unique),
  firstName: "Jean",
  lastName: "Dupont",
  dateOfBirth: "1990-01-15",
  gender: "Homme",
  email: "jean@example.com" (unique),
  phone: "+33612345678" (unique),
  address: "123 Rue de la Paix",
  city: "Paris",
  postalCode: "75000",
  country: "France",
  active: true,
  createdAt: "1700000000000",
  updatedAt: "1700000000000"
}
```

### Collection: doctors

```javascript
{
  _id: ObjectId,
  doctorId: "D00001" (unique),
  firstName: "Marie",
  lastName: "Bernard",
  specialization: "Cardiologie",
  email: "marie@hospital.fr" (unique),
  phone: "+33698765432" (unique),
  workingDays: ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"],
  officeLocation: "Étage 3, Bureau 301",
  officePhone: "+33112345678",
  active: true,
  createdAt: "1700000000000",
  updatedAt: "1700000000000"
}
```

### Collection: appointments

```javascript
{
  _id: ObjectId,
  appointmentId: "A00001" (unique),
  patientId: "P00001",
  patientName: "Jean Dupont",
  doctorId: "D00001",
  doctorName: "Marie Bernard",
  doctorSpecialization: "Cardiologie",
  date: "2025-12-01",
  time: "14:30",
  status: "Planifié", // ou "Terminé", "Annulé"
  remarks: "Consultation de suivi",
  createdAt: "1700000000000",
  updatedAt: "1700000000000",
  cancelledAt: null,
  cancelledReason: null
}
```

### Collection: sequences

```javascript
{
  _id: "patient_seq",
  seq: 42
}
{
  _id: "doctor_seq",
  seq: 18
}
{
  _id: "appointment_seq",
  seq: 156
}
```

---

## 🔐 Couche de Sécurité

### Spring Security Configuration

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/public/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .defaultSuccessUrl("/")
        .and()
        .logout()
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login");
    return http.build();
}

@Bean
public InMemoryUserDetailsManager userDetailsManager() {
    // admin/admin, user/user, manager/manager
    return new InMemoryUserDetailsManager(...);
}
```

### Endpoints Protégés:

- ✅ `/patients/**` - Authentification requise
- ✅ `/doctors/**` - Authentification requise
- ✅ `/appointments/**` - Authentification requise
- ✅ `/api/**` - Authentification requise

### Endpoints Publics:

- 🔓 `/login` - Pas d'authentification
- 🔓 `/static/**` - CSS, JS, images
- 🔓 `/webjars/**` - AdminLTE, Bootstrap

---

## 🚀 Technologies Utilisées

### Backend

| Composant           | Version | Rôle                          |
| ------------------- | ------- | ----------------------------- |
| Spring Boot         | 3.5.7   | Framework principal           |
| Spring Data MongoDB | 4.x     | Accès aux données             |
| Spring Security     | 6.x     | Authentification/Autorisation |
| Java                | 21      | Langage                       |
| Maven               | 3.x     | Build tool                    |
| Tomcat              | 10.1.48 | Serveur web                   |

### Frontend

| Composant    | Version | Rôle                |
| ------------ | ------- | ------------------- |
| Thymeleaf    | 3.x     | Template engine     |
| Bootstrap    | 5.x     | Framework CSS       |
| AdminLTE     | 3.2.0   | Dashboard UI        |
| Font Awesome | 6.x     | Icônes              |
| JavaScript   | ES6     | Scripts côté client |

### Database

| Composant | Version | Rôle              |
| --------- | ------- | ----------------- |
| MongoDB   | 5.x+    | NoSQL Database    |
| Mongoose  | -       | ODM (Spring Data) |

---

## 🔌 Endpoints API REST

### Patients

| Méthode | Endpoint                         | Description               |
| ------- | -------------------------------- | ------------------------- |
| GET     | `/patients/list`                 | Liste tous les patients   |
| GET     | `/patients/add`                  | Formulaire d'ajout        |
| POST    | `/patients/save`                 | Créer un patient          |
| GET     | `/patients/edit/{id}`            | Formulaire d'édition      |
| POST    | `/patients/update`               | Mettre à jour un patient  |
| GET     | `/patients/delete/{id}`          | Supprimer un patient      |
| GET     | `/patients/view/{id}`            | Voir détails d'un patient |
| GET     | `/patients/api/search?query=...` | Rechercher des patients   |

### Médecins

| Méthode | Endpoint                        | Description               |
| ------- | ------------------------------- | ------------------------- |
| GET     | `/doctors/list`                 | Liste tous les médecins   |
| GET     | `/doctors/add`                  | Formulaire d'ajout        |
| POST    | `/doctors/save`                 | Créer un médecin          |
| GET     | `/doctors/edit/{id}`            | Formulaire d'édition      |
| POST    | `/doctors/update`               | Mettre à jour un médecin  |
| GET     | `/doctors/delete/{id}`          | Supprimer un médecin      |
| GET     | `/doctors/view/{id}`            | Voir détails d'un médecin |
| GET     | `/doctors/api/search?query=...` | Rechercher des médecins   |

### Rendez-vous

| Méthode | Endpoint                             | Description                   |
| ------- | ------------------------------------ | ----------------------------- |
| GET     | `/appointments/list`                 | Liste tous les rendez-vous    |
| GET     | `/appointments/add`                  | Formulaire d'ajout            |
| POST    | `/appointments/save`                 | Créer un rendez-vous          |
| GET     | `/appointments/edit/{id}`            | Formulaire d'édition          |
| POST    | `/appointments/update`               | Mettre à jour un rendez-vous  |
| GET     | `/appointments/delete/{id}`          | Supprimer un rendez-vous      |
| GET     | `/appointments/view/{id}`            | Voir détails d'un rendez-vous |
| GET     | `/appointments/api/search?query=...` | Rechercher des rendez-vous    |

### Dashboard Stats

| Méthode | Endpoint                        | Description           |
| ------- | ------------------------------- | --------------------- |
| GET     | `/api/stats/counts`             | Tous les compteurs    |
| GET     | `/api/stats/patients/count`     | Nombre de patients    |
| GET     | `/api/stats/doctors/count`      | Nombre de médecins    |
| GET     | `/api/stats/appointments/count` | Nombre de rendez-vous |

---

## 📊 Pattern MVC

```
Model (Entités)
├── Patient
├── Doctor
├── Appointment
├── Sequence
└── User

View (Templates Thymeleaf)
├── patient-list.html
├── patient-add.html
├── doctor-list.html
├── doctor-add.html
├── appointment-list.html
└── appointment-add.html

Controller (Spring @Controller)
├── PatientController
├── DoctorController
├── AppointmentController
└── DashboardStatsController

Repository (Spring Data)
├── PatientRepository
├── DoctorRepository
├── AppointmentRepository
├── SequenceRepository
└── UserRepository

Service
└── SequenceService
```

---

## 🔄 Cycle de Vie d'une Requête

```
1. USER REQUEST
   └─> HTTP GET /patients/list

2. SPRING DISPATCHER SERVLET
   └─> Route vers PatientController

3. CONTROLLER HANDLER
   └─> @GetMapping("/list")
       public String listPatients(Model model)

4. REPOSITORY CALL
   └─> patientRepository.findAll()

5. MONGODB QUERY
   └─> db.patients.find({})

6. DATA MAPPING
   └─> MongoDB Document → Patient Object

7. MODEL POPULATION
   └─> model.addAttribute("patients", patients)

8. VIEW RENDERING
   └─> Thymeleaf renders patient-list.html

9. HTML GENERATION
   └─> Génère le HTML final

10. RESPONSE
    └─> HTTP 200 OK + HTML Content
```

---

## ⚡ Performance et Optimisations

### Indexes MongoDB

```javascript
// Indexes créés automatiquement:
db.patients.createIndex({ patientId: 1 }, { unique: true });
db.patients.createIndex({ email: 1 }, { unique: true });
db.patients.createIndex({ phone: 1 }, { unique: true });

db.doctors.createIndex({ doctorId: 1 }, { unique: true });
db.doctors.createIndex({ email: 1 }, { unique: true });
db.doctors.createIndex({ phone: 1 }, { unique: true });

db.appointments.createIndex({ appointmentId: 1 }, { unique: true });
```

### Caching JavaScript

```javascript
// live-search.js stocke en cache:
- Endpoint d'API
- Sélecteur du formulaire de recherche
- Sélecteur des lignes du tableau
- Type de page actuelle
```

---

## 📝 Logging et Monitoring

### Logs Application

```
[INFO] [PatientController] Patient created: P00001
[INFO] [DoctorController] Doctor created: D00001
[INFO] [AppointmentController] Appointment created: A00001
[ERROR] [SequenceService] Failed to generate sequence ID
[WARN] [WebSecurityConfig] Deprecated authorizeRequests() method used
```

### Accès aux Logs

```bash
# Fichier de log (si configuré)
tail -f logs/application.log

# Console lors de l'exécution
.\mvnw spring-boot:run
```

---

## 🔧 Configuration Personnalisable

### `application.properties`

```properties
# Port serveur
server.port=8080

# Base de données MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017
spring.data.mongodb.database=grh_db

# Logging
logging.level.root=INFO
logging.level.com.hendisantika=DEBUG

# Thymeleaf
spring.thymeleaf.cache=false  # Développement
spring.thymeleaf.cache=true   # Production
```

---

## 🎯 Considérations de Production

✅ **Authentification:** Implémenter OAuth2 ou JWT  
✅ **Encryptage:** Hashage des mots de passe bcrypt  
✅ **Audit:** Logger toutes les modifications  
✅ **Backups:** Sauvegardes régulières MongoDB  
✅ **Monitoring:** APM (Application Performance Monitoring)  
✅ **SSL/TLS:** HTTPS en production  
✅ **Scalabilité:** Replica Sets MongoDB  
✅ **Load Balancing:** Nginx/Apache en proxy

---

**Dernière Mise à Jour:** 16 Novembre 2025  
**Statut:** ✅ Documentation Complète
