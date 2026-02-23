# Testing Tech-Serv Endpoints

## Request Body Examples

### 1. Register User
**Endpoint:** `POST /api/v1/auth/register`  
**Access:** PUBLIC

**Body (ADMIN):**
```json
{
  "email": "admin@test.com",
  "password": "admin123",
  "role": "ADMIN"
}
```

**Expected Response (200):**
```json
{
  "id": 1,
  "email": "admin@test.com",
  "password": "$2a$10$...",
  "role": "ADMIN"
}
```

---

### 2. Login
**Endpoint:** `POST /api/v1/auth/login`  
**Access:** PUBLIC

**Body:**
```json
{
  "email": "admin@test.com",
  "password": "admin123"
}
```

**Expected Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ0ZWNoLXNlcnYtYmFjayIsInN1YiI6IjEiLCJlbWFpbCI6ImFkbWluQHRlc3QuY29tIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNjM5NzU0MzIwLCJleHAiOjE2Mzk4NDA3MjB9..."
}
```

---

### 3. Create Device
**Endpoint:** `POST /api/v1/devices`  
**Access:** ADMIN ONLY  
**Headers:** `Authorization: Bearer {TOKEN}`

**Body:**
```json
{
  "type": "Laptop",
  "brand": "Dell",
  "model": "XPS 15",
  "serialNumber": "SN123456789",
  "customer": {
    "id": 1
  }
}
```

**Expected Response (200):**
```json
{
  "id": 5,
  "type": "Laptop",
  "brand": "Dell",
  "model": "XPS 15",
  "serialNumber": "SN123456789",
  "customer": {
    "id": 1
  }
}
```

---

### 4. Create Technical Service
**Endpoint:** `POST /api/v1/services`  
**Access:** ADMIN ONLY  
**Headers:** `Authorization: Bearer {TOKEN}`

**Body (Using Device ID):**
```json
{
  "description": "Laptop screen replacement",
  "status": "RECIBIDO",
  "entryDate": "2026-02-04",
  "estimatedCost": 150.00,
  "device": {
    "id": 5
  }
}
```

**Status Values Allowed:**
- `RECIBIDO` - Service received
- `EN_DIAGNOSTICO` - Under diagnosis
- `EN_REPARACION` - Under repair
- `LISTO_PARA_ENTREGA` - Ready for delivery
- `ENTREGADO` - Delivered

**Expected Response (200):**
```json
{
  "id": 1,
  "ticketCode": "TKT-A7F3E2B1",
  "description": "Laptop screen replacement",
  "status": "RECIBIDO",
  "entryDate": "2026-02-04",
  "estimatedCost": 150.00,
  "device": {
    "id": 5,
    "type": "Laptop",
    "brand": "Dell",
    "model": "XPS 15",
    "serialNumber": "SN123456789",
    "customer": {
      "id": 1
    }
  }
}
```

**Note:** `ticketCode` is auto-generated in format `TKT-XXXXXXXX`

---

### 5. Update Service Status
**Endpoint:** `PATCH /api/v1/services/{id}/status`  
**Access:** ADMIN ONLY  
**Headers:** `Authorization: Bearer {TOKEN}`

**Body:**
```json
{
  "status": "EN_DIAGNOSTICO"
}
```

**Valid Status Transitions:**
```
RECIBIDO → EN_DIAGNOSTICO → EN_REPARACION → LISTO_PARA_ENTREGA → ENTREGADO
```

**Expected Response (200):**
```json
{
  "id": 1,
  "ticketCode": "TKT-A7F3E2B1",
  "description": "Laptop screen replacement",
  "status": "EN_DIAGNOSTICO",
  "entryDate": "2026-02-04",
  "estimatedCost": 150.00,
  "device": {
    "id": 5,
    "type": "Laptop",
    "brand": "Dell",
    "model": "XPS 15",
    "serialNumber": "SN123456789",
    "customer": {
      "id": 1
    }
  }
}
```

---

### 6. Search Services (Public)
**Endpoint:** `GET /api/v1/services/search?query=TKT-A7F3E2B1`  
**Access:** PUBLIC (No authentication required)

**Query Parameters:**
- `query` (string): Search term for ticket code or description

**Example Requests:**
```
GET /api/v1/services/search?query=TKT-A7F3E2B1
GET /api/v1/services/search?query=pantalla
GET /api/v1/services/search?query=laptop
```

**Expected Response (200):**
```json
[
  {
    "id": 1,
    "ticketCode": "TKT-A7F3E2B1",
    "description": "Laptop screen replacement",
    "status": "EN_DIAGNOSTICO",
    "entryDate": "2026-02-04",
    "estimatedCost": 150.00,
    "device": {
      "id": 5,
      "type": "Laptop",
      "brand": "Dell",
      "model": "XPS 15",
      "serialNumber": "SN123456789",
      "customer": {
        "id": 1
      }
    }
  }
]
```

---

### 7. Get Customer Services
**Endpoint:** `GET /api/v1/services/my-services?customerId=1`  
**Access:** PUBLIC (No authentication required)

**Query Parameters:**
- `customerId` (Long): Customer ID

**Expected Response (200):**
```json
[
  {
    "id": 1,
    "ticketCode": "TKT-A7F3E2B1",
    "description": "Laptop screen replacement",
    "status": "EN_DIAGNOSTICO",
    "entryDate": "2026-02-04",
    "estimatedCost": 150.00,
    "device": {
      "id": 5,
      "type": "Laptop",
      "brand": "Dell",
      "model": "XPS 15",
      "serialNumber": "SN123456789",
      "customer": {
        "id": 1
      }
    }
  },
  {
    "id": 2,
    "ticketCode": "TKT-B9E5F3C2",
    "description": "Desktop hard drive replacement",
    "status": "LISTO_PARA_ENTREGA",
    "entryDate": "2026-02-02",
    "estimatedCost": 200.00,
    "device": {
      "id": 6,
      "type": "Desktop",
      "brand": "HP",
      "model": "Pavilion",
      "serialNumber": "SN987654321",
      "customer": {
        "id": 1
      }
    }
  }
]
```

---

### 8. Get Device by ID
**Endpoint:** `GET /api/v1/devices/{id}`  
**Access:** ADMIN ONLY  
**Headers:** `Authorization: Bearer {TOKEN}`

**Example:** `GET /api/v1/devices/5`

**Expected Response (200):**
```json
{
  "id": 5,
  "type": "Laptop",
  "brand": "Dell",
  "model": "XPS 15",
  "serialNumber": "SN123456789",
  "customer": {
    "id": 1
  }
}
```

---

### 9. Update Device
**Endpoint:** `PUT /api/v1/devices/{id}`  
**Access:** ADMIN ONLY  
**Headers:** `Authorization: Bearer {TOKEN}`

**Body:**
```json
{
  "type": "Laptop",
  "brand": "Dell",
  "model": "XPS 17",
  "serialNumber": "SN123456790"
}
```

**Expected Response (200):**
```json
{
  "id": 5,
  "type": "Laptop",
  "brand": "Dell",
  "model": "XPS 17",
  "serialNumber": "SN123456790",
  "customer": {
    "id": 1
  }
}
```

---

### 10. Delete Device
**Endpoint:** `DELETE /api/v1/devices/{id}`  
**Access:** ADMIN ONLY  
**Headers:** `Authorization: Bearer {TOKEN}`

**Example:** `DELETE /api/v1/devices/5`

**Expected Response (204):** No Content

---

## Testing Checklist

### Authentication Tests
- [ ] Register new ADMIN user
- [ ] Register new CUSTOMER user  
- [ ] Login with valid credentials
- [ ] Login with invalid credentials (should fail)
- [ ] Access protected endpoint without token (should fail with 401)
- [ ] Access protected endpoint with invalid token (should fail with 401)

### Device Management Tests
- [ ] Create device as ADMIN with valid data
- [ ] Create device as ADMIN with customer reference
- [ ] Get device by ID as ADMIN
- [ ] Get all devices as ADMIN
- [ ] Update device as ADMIN
- [ ] Delete device as ADMIN
- [ ] Get devices by customer ID as ADMIN

### Service Management Tests
- [ ] Create service as ADMIN with device reference
- [ ] Auto-generated ticket code is created
- [ ] Default status is "RECIBIDO"
- [ ] Default entry date is set to today
- [ ] Update service status (RECIBIDO → EN_DIAGNOSTICO)
- [ ] Update service status (EN_DIAGNOSTICO → EN_REPARACION)
- [ ] Update service status (EN_REPARACION → LISTO_PARA_ENTREGA)
- [ ] Update service status (LISTO_PARA_ENTREGA → ENTREGADO)

### Public Search Tests
- [ ] Search services by ticket code (no auth required)
- [ ] Search services by description (no auth required)
- [ ] Get customer services by customer ID (no auth required)
- [ ] Search returns all matching results

### Authorization Tests
- [ ] ADMIN can create services
- [ ] ADMIN can update service status
- [ ] ADMIN can manage devices
- [ ] Non-authenticated users can search services
- [ ] Non-authenticated users can view customer services
- [ ] CUSTOMER role cannot create services (if implemented)

---

## Error Scenarios

### 401 Unauthorized
**Request:** Protected endpoint without Bearer token
```
GET /api/v1/devices
```

**Response (401):**
```json
{
  "error": "Unauthorized"
}
```

### 403 Forbidden
**Request:** User without ADMIN role trying to create device
```
POST /api/v1/devices
Authorization: Bearer {CUSTOMER_TOKEN}
```

**Response (403):**
```json
{
  "error": "Access Denied"
}
```

### 404 Not Found
**Request:** Non-existent resource
```
GET /api/v1/services/9999
Authorization: Bearer {TOKEN}
```

**Response (404):**
```json
{
  "error": "Service not found"
}
```

---

## Quick Test Script

```bash
# 1. Login as admin
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@mail.com","password":"adminpass123"}'

# Save token as TOKEN

# 2. Create device
curl -X POST http://localhost:8080/api/v1/devices \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"type":"Laptop","brand":"Dell","model":"XPS 15","serialNumber":"SN123","customer":{"id":1}}'

# 3. Create service
curl -X POST http://localhost:8080/api/v1/services \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"description":"Pantalla rota","device":{"id":5},"estimatedCost":150}'

# 4. Search service (public)
curl http://localhost:8080/api/v1/services/search?query=TKT

# 5. Update service status
curl -X PATCH http://localhost:8080/api/v1/services/1/status \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status":"EN_DIAGNOSTICO"}'
```
