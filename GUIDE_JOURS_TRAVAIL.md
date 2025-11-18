# Guide d'Utilisation - Jours de Travail des Médecins

## Vue d'ensemble

Les jours de travail des médecins sont stockés comme une **liste de chaînes de caractères** (String) dans MongoDB. Cela permet de sélectionner plusieurs jours de la semaine.

---

## Format de Stockage

### Dans MongoDB:

```json
{
  "_id": ObjectId("..."),
  "doctorId": "D00001",
  "firstName": "Jean",
  "lastName": "Dupont",
  "workingDays": ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"],
  "specialization": "Cardiologie",
  "email": "jean.dupont@hospital.fr",
  "phone": "+33612345678",
  "active": true,
  "createdAt": "1700000000000",
  "updatedAt": "1700000000000"
}
```

---

## Formulaire d'Ajout/Édition de Médecin

### HTML Structure:

```html
<div class="form-group">
  <label>Jours de Travail</label>
  <div class="form-check">
    <input
      type="checkbox"
      class="form-check-input"
      id="monday"
      name="workingDays"
      value="Lundi"
      th:field="*{workingDays}"
    />
    <label class="form-check-label" for="monday">Lundi</label>
  </div>
  <div class="form-check">
    <input
      type="checkbox"
      class="form-check-input"
      id="tuesday"
      name="workingDays"
      value="Mardi"
      th:field="*{workingDays}"
    />
    <label class="form-check-label" for="tuesday">Mardi</label>
  </div>
  <!-- ... autres jours ... -->
</div>
```

### Jours Disponibles:

1. ✓ Lundi
2. ✓ Mardi
3. ✓ Mercredi
4. ✓ Jeudi
5. ✓ Vendredi
6. ✓ Samedi
7. ✓ Dimanche

---

## Flux de Traitement

### 1. Création d'un Médecin

**Étape 1:** L'utilisateur accède à `/doctors/add`

**Étape 2:** Formulaire affiché avec les 7 jours comme checkboxes

**Étape 3:** L'utilisateur coche les jours travaillés (ex: Lun-Ven)

**Étape 4:** Soumission du formulaire à `POST /doctors/save`

**Étape 5:** Spring Boot bind automatiquement les checkboxes cochées dans la liste `workingDays`

**Étape 6:** Le contrôleur génère l'ID du médecin et sauvegarde

```java
doctor.setDoctorId(sequenceService.getNextSequenceId("doctor_seq", "D"));
doctor.setCreatedAt(String.valueOf(System.currentTimeMillis()));
doctor.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
doctorRepository.save(doctor); // MongoDB stocke les jours
```

---

### 2. Affichage dans la Liste

**Page:** `/doctors/list`

**Affichage:** Les jours ne sont pas affichés dans le tableau principal, mais disponibles dans la vue détaillée

```html
<td th:text="${doctor.specialization}">Cardiologie</td>
```

---

### 3. Édition d'un Médecin

**Étape 1:** L'utilisateur clique sur le bouton "Modifier" (pencil icon)

**Étape 2:** Accès à `/doctors/edit/{id}`

**Étape 3:** Formulaire pré-rempli:

- Les checkboxes des jours de travail actuels sont **cochés**
- Les autres sont **décochés**

**Étape 4:** L'utilisateur peut modifier les sélections

**Étape 5:** Soumission à `POST /doctors/update`

**Étape 6:** La liste `workingDays` est mise à jour

---

## Exemples de Cas d'Usage

### Cas 1: Médecin à Temps Plein (Lundi-Vendredi)

```json
"workingDays": ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"]
```

### Cas 2: Médecin à Temps Partiel (Mercredi-Samedi)

```json
"workingDays": ["Mercredi", "Jeudi", "Vendredi", "Samedi"]
```

### Cas 3: Médecin Consultant (Jours Spécifiques)

```json
"workingDays": ["Lundi", "Jeudi"]
```

### Cas 4: Médecin 24/7 (Tous les Jours)

```json
"workingDays": ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"]
```

---

## Manipulation Programmatique

### Vérifier si un Médecin Travaille un Jour Spécifique

```java
Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
List<String> workingDays = doctor.getWorkingDays();

if (workingDays.contains("Lundi")) {
    System.out.println("Le médecin travaille le lundi");
} else {
    System.out.println("Le médecin ne travaille pas le lundi");
}
```

### Ajouter un Jour de Travail

```java
doctor.getWorkingDays().add("Samedi");
doctorRepository.save(doctor);
```

### Retirer un Jour de Travail

```java
doctor.getWorkingDays().remove("Dimanche");
doctorRepository.save(doctor);
```

### Obtenir les Médecins qui Travaillent un Jour Spécifique

```java
List<Doctor> doctors = doctorRepository.findAll()
    .stream()
    .filter(d -> d.getWorkingDays() != null && d.getWorkingDays().contains("Lundi"))
    .collect(Collectors.toList());
```

---

## Points Techniques

### Dans `Doctor.java` Entity:

```java
@Document(collection = "doctors")
public class Doctor {
    @Id
    private String id;

    @Indexed(unique = true)
    private String doctorId;

    private List<String> workingDays; // ← Stocke les jours

    // ... autres champs ...
}
```

### Dans `DoctorRepository.java`:

```java
@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {
    Optional<Doctor> findByDoctorId(String doctorId);
    // ... autres méthodes ...
}
```

### Dans `DoctorController.java` - Affichage des Jours:

```java
@GetMapping("/view/{id}")
public String viewDoctor(@PathVariable String id, Model model) {
    Optional<Doctor> doctor = doctorRepository.findById(id);
    if (doctor.isPresent()) {
        // Les jours de travail sont automatiquement inclus dans le modèle
        model.addAttribute("doctor", doctor.get());
        return "doctor-view"; // Affiche workingDays
    }
    return "redirect:/doctors/list";
}
```

---

## Affichage des Jours de Travail (Template Thymeleaf)

### doctor-view.html (Exemple):

```html
<div class="form-group">
  <label>Jours de Travail</label>
  <ul>
    <li th:each="day : ${doctor.workingDays}" th:text="${day}">Lundi</li>
  </ul>
</div>
```

**Rendu:**

```
Jours de Travail:
- Lundi
- Mardi
- Mercredi
- Jeudi
- Vendredi
```

---

## Validation et Contraintes

**Remarques:**

- ✓ Aucune validation spéciale ne force le médecin à travailler au moins un jour
- ✓ Un médecin peut avoir une liste vide de jours de travail
- ✓ Les doublons dans la liste sont possibles mais non recommandés
- ✓ Les noms des jours sont sensibles à la casse (Lundi ≠ lundi)

---

## Intégration avec les Rendez-vous

### À l'Avenir (Optionnel):

Les jours de travail peuvent être utilisés pour:

1. **Validation:** Empêcher la création de rendez-vous les jours non travaillés
2. **Suggestion:** Proposer uniquement les jours travaillés lors de la sélection
3. **Rapport:** Analyser les disponibilités par jour

### Exemple de Validation:

```java
Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId()).orElse(null);
LocalDate appointmentDate = LocalDate.parse(appointmentRequest.getDate());
String dayOfWeek = appointmentDate.getDayOfWeek().getDisplayName(
    TextStyle.FULL,
    Locale.FRENCH
);

if (!doctor.getWorkingDays().contains(dayOfWeek)) {
    throw new AppointmentException("Le médecin ne travaille pas ce jour");
}
```

---

## Résumé

| Aspect                 | Détail               |
| ---------------------- | -------------------- |
| **Type de Données**    | `List<String>`       |
| **Collection MongoDB** | `doctors`            |
| **Champ MongoDB**      | `workingDays`        |
| **Contrôle Interface** | Checkboxes multiples |
| **Jours Disponibles**  | 7 (Lun-Dim)          |
| **Stockage**           | Array dans MongoDB   |
| **Défaut**             | Liste vide []        |
| **Éditable**           | Oui                  |
| **Obligatoire**        | Non                  |

---

**Dernière Mise à Jour:** 16 Novembre 2025
