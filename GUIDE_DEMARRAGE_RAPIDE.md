# Guide de Démarrage Rapide - Système GRH

## 📋 Prérequis

- **Java:** 21 ou supérieur
- **MongoDB:** Installé et en cours d'exécution sur `localhost:27017`
- **Maven:** Inclus (mvnw)

---

## 🚀 Démarrage Rapide

### Étape 1: Vérifier MongoDB

```bash
# Vérifier que MongoDB est démarré
mongosh

# Dans mongosh, créer la base de données (optionnel)
use grh_db
```

### Étape 2: Compiler le Projet

```bash
cd D:\ENS_Documents\Master\S3\database\springboot-adminlte3-template-main
.\mvnw clean package -DskipTests
```

**Résultat Attendu:** `BUILD SUCCESS`

### Étape 3: Lancer l'Application

```bash
.\mvnw spring-boot:run
```

**Ou** exécutez directement le JAR:

```bash
java -jar target/springboot-adminlte3-template-0.0.1-SNAPSHOT.jar
```

### Étape 4: Accéder à l'Application

Ouvrez votre navigateur et allez à:

```
http://localhost:8080
```

---

## 🔐 Connexion

Utilisez les identifiants suivants:

| Utilisateur | Mot de passe |
| ----------- | ------------ |
| admin       | admin        |
| user        | user         |
| manager     | manager      |

---

## 📊 Fonctionnalités Principales

### 1. 👥 Gestion des Patients

- **Ajouter un Patient:** `/patients/add`
- **Voir la Liste:** `/patients/list`
- **Recherche en Temps Réel:** Disponible sur la liste
- **ID Automatique:** P00001, P00002, etc.

#### Champs Visibles:

- Prénom et Nom (séparés)
- Date de Naissance
- Genre (Homme/Femme/Autre)
- Email
- Téléphone
- Adresse
- Statut (Actif/Inactif)

---

### 2. 👨‍⚕️ Gestion des Médecins

- **Ajouter un Médecin:** `/doctors/add`
- **Voir la Liste:** `/doctors/list`
- **Recherche en Temps Réel:** Disponible sur la liste
- **ID Automatique:** D00001, D00002, etc.

#### Champs Visibles:

- Prénom et Nom (séparés)
- Spécialisation
- Email
- Téléphone
- **Jours de Travail:** Checkboxes (Lundi à Dimanche)
- Statut (Actif/Inactif)

---

### 3. 📅 Gestion des Rendez-vous

- **Planifier:** `/appointments/add`
- **Voir la Liste:** `/appointments/list`
- **Recherche en Temps Réel:** Disponible sur la liste
- **ID Automatique:** A00001, A00002, etc.

#### Champs Visibles:

- **Sélection Patient:** Dropdown de tous les patients
- **Sélection Médecin:** Dropdown de tous les médecins
- **Date:** Date picker
- **Heure:** Time picker
- **Statut:** Planifié / Terminé / Annulé
- **Remarques:** Zone de texte libre

---

### 4. 📈 Tableau de Bord

- **URL:** `/`
- **Fonctionnalités:**
  - Affichage du nombre de patients
  - Affichage du nombre de médecins
  - Affichage du nombre de rendez-vous
  - Auto-rafraîchissement toutes les 30 secondes

---

## 🔄 Flux de Travail Typique

### Exemple 1: Ajouter un Patient et Planifier un Rendez-vous

```
1. Aller à Patients → Ajouter Patient
2. Remplir les informations:
   - Prénom: Jean
   - Nom: Dupont
   - Téléphone: +33612345678
3. Cliquer sur "Enregistrer"
   → Patient créé avec ID: P00001

4. Aller à Rendez-vous → Planifier Rendez-vous
5. Sélectionner:
   - Patient: Jean Dupont
   - Médecin: (liste déroulante)
   - Date: (sélectionner une date)
   - Heure: (sélectionner une heure)
6. Cliquer sur "Planifier"
   → Rendez-vous créé avec ID: A00001
```

---

## 🛠️ Commandes Utiles

### Build et Exécution

```bash
# Clean build
.\mvnw clean build

# Build avec tests
.\mvnw clean package

# Build sans tests (plus rapide)
.\mvnw clean package -DskipTests

# Exécuter l'application
.\mvnw spring-boot:run

# Voir les logs
.\mvnw spring-boot:run -Dspring-boot.run.arguments="--debug"
```

### MongoDB

```bash
# Se connecter à MongoDB
mongosh

# Voir les bases de données
show dbs

# Utiliser la base grh_db
use grh_db

# Voir les collections
show collections

# Voir les patients
db.patients.find()

# Voir les médecins
db.doctors.find()

# Voir les rendez-vous
db.appointments.find()

# Voir les séquences
db.sequences.find()

# Compter les patients
db.patients.countDocuments()

# Supprimer tous les patients
db.patients.deleteMany({})
```

---

## 📱 Navigation Sidebar

### Menu Principal (Français):

```
🏠 Tableau de Bord

👥 Patients
  ├─ Liste des Patients
  └─ Ajouter Patient

👨‍⚕️ Médecins
  ├─ Liste des Médecins
  └─ Ajouter Médecin

📅 Rendez-vous
  ├─ Liste des Rendez-vous
  └─ Planifier Rendez-vous

--- COMPTE ---
🚪 Déconnexion
```

---

## 🔍 Fonctionnalités Avancées

### Recherche en Temps Réel

Chaque liste (Patients, Médecins, Rendez-vous) propose une **barre de recherche** qui:

- Filtre les résultats **à la volée** sans recharger la page
- Supporte les recherches partielles
- Recherche sur plusieurs champs

**Exemple:**

```
Patient: "Jean Dupont" → Rechercher "Jean" → Résultats filtrés
Patient: "jean.dupont@email.com" → Rechercher "email" → Résultats filtrés
```

### Auto-Génération d'IDs

Les IDs sont générés **automatiquement** lors de la création:

- **Patient:** P00001, P00002, P00003...
- **Médecin:** D00001, D00002, D00003...
- **Rendez-vous:** A00001, A00002, A00003...

**Important:** L'utilisateur n'a **pas besoin** d'entrer l'ID, il est généré automatiquement!

---

## ⚙️ Configuration

### Fichier `application.properties`

**Localisation:** `src/main/resources/application.properties`

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017
spring.data.mongodb.database=grh_db

# Application Name
spring.application.name=springboot-adminlte3-template
```

### Modifier le Port (optionnel)

```properties
server.port=9090  # Changer de 8080 à 9090
```

---

## 🐛 Dépannage

### Problem: Connection Refused MongoDB

**Solution:**

```bash
# Vérifier que MongoDB est démarré
mongosh

# Si pas démarré, démarrer le service MongoDB
# Sur Windows: Services → MongoDB Server
# Sur macOS/Linux: brew services start mongodb-community
```

### Problem: Port 8080 Already in Use

**Solution:**

```bash
# Trouver le processus utilisant le port 8080
netstat -ano | findstr :8080

# Tuer le processus (remplacer PID)
taskkill /PID <PID> /F

# Ou changer le port dans application.properties
server.port=9090
```

### Problem: BUILD FAILURE

**Solution:**

```bash
# Nettoyer les caches Maven
.\mvnw clean

# Réessayer le build
.\mvnw clean package -DskipTests
```

### Problem: Base de Données Vide

**Solution:**

```bash
# L'application crée automatiquement les collections
# Aucun setup de base de données n'est nécessaire
# Ajouter des données via l'interface
```

---

## 📊 Base de Données

### Collections Créées Automatiquement:

| Collection   | Clé Primaire | Index Unique            |
| ------------ | ------------ | ----------------------- |
| patients     | \_id         | patientId, email, phone |
| doctors      | \_id         | doctorId, email, phone  |
| appointments | \_id         | appointmentId           |
| sequences    | \_id         | -                       |

### Exemple de Document Patient:

```json
{
  "_id": ObjectId("..."),
  "patientId": "P00001",
  "firstName": "Jean",
  "lastName": "Dupont",
  "dateOfBirth": "1990-01-15",
  "gender": "Homme",
  "email": "jean.dupont@email.com",
  "phone": "+33612345678",
  "address": "123 Rue de la Paix, Paris",
  "active": true,
  "createdAt": "1700000000000",
  "updatedAt": "1700000000000"
}
```

---

## 🎯 Points Importants

✅ **Tout est en Français** - L'interface complète est en français  
✅ **IDs Auto-Incrémentés** - Pas besoin de saisir l'ID manuellement  
✅ **Recherche Temps Réel** - Filtrez les listes à la volée  
✅ **MongoDB** - Stockage flexible et scalable  
✅ **Responsive** - Fonctionne sur desktop et mobile  
✅ **Authentification** - Sécurisé avec Spring Security

---

## 📞 Support

**Environnement de Développement:**

- IDE: VS Code / IntelliJ IDEA / Eclipse
- Version Java: 21
- Build Tool: Maven 3.x
- Framework: Spring Boot 3.5.7
- Serveur: Tomcat 10.1.48

---

## 📝 Fichiers Documentaires

Pour plus de détails, consultez:

- `MODIFICATIONS_RESUME.md` - Résumé complet des changements
- `GUIDE_JOURS_TRAVAIL.md` - Guide spécifique pour les jours de travail

---

**Statut:** ✅ Prêt pour Production  
**Dernière Mise à Jour:** 16 Novembre 2025
