# Guide: Testing REST APIs with Postman

## 📋 Prerequisites

1. **Postman installed** - Download from https://www.postman.com/downloads/
2. **Application running** - http://localhost:8080
3. **Test data initialized** - Visit http://localhost:8080/admin/init-users first
4. **Users created** - Check via http://localhost:8080/admin/check-users

---

## 🔐 Authentication

### For Postman API Testing:

The `/api/*` endpoints are **PUBLIC** and don't require authentication!

You can test them directly without logging in.

### For Web UI:

If you want to use the web UI, login with:

- **Username:** `admin`
- **Password:** `admin`

Or initialize users first: `http://localhost:8080/admin/init-users`

## 🔌 API Endpoints & Postman Requests

### 1️⃣ Dashboard Statistics API

#### Get All Counts

**Method:** `GET`
**URL:** `http://localhost:8080/api/stats/counts`

**Headers:**

```
Content-Type: application/json
```

**Steps in Postman:**

1. Create new request → Select **GET**
2. Paste URL: `http://localhost:8080/api/stats/counts`
3. Click **Send**

**Expected Response (200 OK):**

```json
{
  "patientCount": 5,
  "doctorCount": 3,
  "appointmentCount": 2,
  "timestamp": 1731784235123
}
```

---

#### Get Patient Count Only

**Method:** `GET`
**URL:** `http://localhost:8080/api/stats/patients/count`

**Steps:**

1. Select **GET**
2. Paste: `http://localhost:8080/api/stats/patients/count`
3. Click **Send**

**Expected Response:**

```json
{
  "count": 5
}
```

---

#### Get Doctor Count Only

**Method:** `GET`
**URL:** `http://localhost:8080/api/stats/doctors/count`

---

#### Get Appointment Count Only

**Method:** `GET`
**URL:** `http://localhost:8080/api/stats/appointments/count`

---

### 2️⃣ Patient Search API

#### Search Patients by Query

**Method:** `GET`
**URL:** `http://localhost:8080/patients/api/search?query=jean`

**Headers:**

```
Content-Type: application/json
```

**Parameters:**

- `query` (required): Search term (firstName, lastName, email, phone, or patientId)

**Examples:**

```
http://localhost:8080/patients/api/search?query=jean      (search by firstName)
http://localhost:8080/patients/api/search?query=dupont    (search by lastName)
http://localhost:8080/patients/api/search?query=jean@     (search by email)
http://localhost:8080/patients/api/search?query=06        (search by phone)
http://localhost:8080/patients/api/search?query=P00001    (search by patientId)
```

**Steps in Postman:**

1. Select **GET**
2. Paste: `http://localhost:8080/patients/api/search?query=jean`
3. Click **Send**

**Expected Response:**

```json
[
  {
    "id": "507f1f77bcf86cd799439011",
    "patientId": "P00001",
    "firstName": "Jean",
    "lastName": "Dupont",
    "dateOfBirth": "1990-01-15",
    "gender": "Homme",
    "email": "jean@example.com",
    "phone": "+33612345678",
    "address": "123 Rue de la Paix",
    "active": true,
    "createdAt": "1731784000000",
    "updatedAt": "1731784000000"
  }
]
```

---

### 3️⃣ Doctor Search API

#### Search Doctors by Query

**Method:** `GET`
**URL:** `http://localhost:8080/doctors/api/search?query=marie`

**Parameters:**

- `query`: Search by firstName, lastName, specialization, email, phone, or doctorId

**Examples:**

```
http://localhost:8080/doctors/api/search?query=marie          (search by firstName)
http://localhost:8080/doctors/api/search?query=cardiologie    (search by specialization)
http://localhost:8080/doctors/api/search?query=D00001         (search by doctorId)
```

**Steps in Postman:**

1. Select **GET**
2. Paste: `http://localhost:8080/doctors/api/search?query=marie`
3. Click **Send**

**Expected Response:**

```json
[
  {
    "id": "507f1f77bcf86cd799439012",
    "doctorId": "D00001",
    "firstName": "Marie",
    "lastName": "Bernard",
    "specialization": "Cardiologie",
    "email": "marie@hospital.fr",
    "phone": "+33698765432",
    "workingDays": ["Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"],
    "active": true,
    "createdAt": "1731784000000",
    "updatedAt": "1731784000000"
  }
]
```

---

### 4️⃣ Appointment Search API

#### Search Appointments by Query

**Method:** `GET`
**URL:** `http://localhost:8080/appointments/api/search?query=jean`

**Parameters:**

- `query`: Search by patientName, doctorName, appointmentId, date, or status

**Examples:**

```
http://localhost:8080/appointments/api/search?query=jean              (search by patient name)
http://localhost:8080/appointments/api/search?query=marie             (search by doctor name)
http://localhost:8080/appointments/api/search?query=A00001            (search by appointmentId)
http://localhost:8080/appointments/api/search?query=2025-12-01        (search by date)
http://localhost:8080/appointments/api/search?query=planifié          (search by status)
```

**Steps in Postman:**

1. Select **GET**
2. Paste: `http://localhost:8080/appointments/api/search?query=jean`
3. Click **Send**

**Expected Response:**

```json
[
  {
    "id": "507f1f77bcf86cd799439013",
    "appointmentId": "A00001",
    "patientId": "507f1f77bcf86cd799439011",
    "patientName": "Jean Dupont",
    "doctorId": "507f1f77bcf86cd799439012",
    "doctorName": "Marie Bernard",
    "doctorSpecialization": "Cardiologie",
    "date": "2025-12-01",
    "time": "14:30",
    "status": "Planifié",
    "remarks": "Consultation de suivi",
    "createdAt": "1731784000000",
    "updatedAt": "1731784000000"
  }
]
```

---

## 🎯 Quick Test Examples

### Example 1: Get all statistics

```
Method: GET
URL: http://localhost:8080/api/stats/counts
Headers: Content-Type: application/json
Body: (none)
```

### Example 2: Search for patient "jean"

```
Method: GET
URL: http://localhost:8080/patients/api/search?query=jean
Headers: Content-Type: application/json
Body: (none)
```

### Example 3: Search for doctor "cardio" (specialization)

```
Method: GET
URL: http://localhost:8080/doctors/api/search?query=cardio
Headers: Content-Type: application/json
Body: (none)
```

### Example 4: Search for "planifié" appointments (status)

```
Method: GET
URL: http://localhost:8080/appointments/api/search?query=planifié
Headers: Content-Type: application/json
Body: (none)
```

---

## 🔐 Important Notes

1. **No Authentication Required** - These `/api/` endpoints are accessible without login (they're part of the application)
2. **Query Parameters** - All search endpoints use `?query=` parameter
3. **Case Insensitive** - Searches work regardless of uppercase/lowercase
4. **Partial Matching** - Searches find partial matches (e.g., "jean" matches "Jean Dupont")
5. **JSON Response** - All endpoints return JSON format
6. **HTTP 200 OK** - Successful requests return status 200
7. **Empty Results** - If no matches found, returns empty array `[]`

---

## 📝 Postman Collection Format

You can import this as a Postman collection. Save as `grh-api.json`:

```json
{
  "info": {
    "name": "GRH Hospital API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Stats - Get All Counts",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/api/stats/counts",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "stats", "counts"]
        }
      }
    },
    {
      "name": "Search - Patients",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/patients/api/search?query=jean",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["patients", "api", "search"],
          "query": [{ "key": "query", "value": "jean" }]
        }
      }
    },
    {
      "name": "Search - Doctors",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/doctors/api/search?query=marie",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["doctors", "api", "search"],
          "query": [{ "key": "query", "value": "marie" }]
        }
      }
    },
    {
      "name": "Search - Appointments",
      "request": {
        "method": "GET",
        "url": {
          "raw": "http://localhost:8080/appointments/api/search?query=planifié",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["appointments", "api", "search"],
          "query": [{ "key": "query", "value": "planifié" }]
        }
      }
    }
  ]
}
```

---

## ✅ Testing Workflow

1. **Start application**: `.\mvnw spring-boot:run`
2. **Initialize users**: Visit `http://localhost:8080/admin/init-users`
3. **Create test data**: Login and add patients/doctors/appointments via web UI
4. **Open Postman**
5. **Test API endpoints** using examples above
6. **Verify JSON responses**

---

## 🚀 Common Issues & Solutions

| Issue              | Solution                                                    |
| ------------------ | ----------------------------------------------------------- |
| Connection refused | Ensure app is running on port 8080                          |
| 404 Not Found      | Check URL spelling and endpoint path                        |
| Empty array `[]`   | Search term doesn't match any data - create test data first |
| 500 Error          | Check app logs for error details                            |
| Query not working  | Try simpler search terms, check spelling                    |

---

**Need to expand REST APIs?** I can add full CRUD operations (POST, PUT, DELETE) returning JSON if needed!
