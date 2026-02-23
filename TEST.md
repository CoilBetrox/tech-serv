# TEST - Endpoints y Request Bodies de referencia

1) Register User (ADMIN)
POST /api/v1/auth/register
Body:
{
  "email": "admin@test.com",
  "password": "admin123",
  "role": "ADMIN"
}

2) Login
POST /api/v1/auth/login
Body:
{
  "email": "admin@test.com",
  "password": "admin123"
}

Respuesta esperada: { "token": "..." }

3) Create Customer
POST /api/v1/customers
Headers: Authorization: Bearer {TOKEN} (requires role ADMIN)
Body:
{
  "firstName": "Juan",
  "lastName": "Perez",
  "phone": "+59170000000"
}

Nota: El proyecto ya incluye `CustomerController` y `ICustomerService` con los endpoints `POST /api/v1/customers` y `GET /api/v1/customers/{id}`.

4) Create Device (ADMIN)
POST /api/v1/devices
Headers: Authorization: Bearer {TOKEN}
Body (referencia si el `Customer` ya existe):
{
  "type": "Laptop",
  "brand": "Dell",
  "model": "XPS 15",
  "serialNumber": "SN123456789",
  "customer": { "id": 1 }
}

Body (si quisiera crear customer en línea — no garantizado sin cascade):
{
  "type": "Laptop",
  "brand": "Dell",
  "model": "XPS 15",
  "serialNumber": "SN123456789",
  "customer": {
    "firstName": "Juan",
    "lastName": "Perez",
    "phone": "+59170000000"
  }
}

5) Create Technical Service (ADMIN)
POST /api/v1/services
Headers: Authorization: Bearer {TOKEN}
Body (usando `device.id` existente):
{
  "description": "Reemplazo de pantalla",
  "status": "RECIBIDO",
  "entryDate": "2026-02-04",
  "estimatedCost": 150.00,
  "device": { "id": 5 }
}

Nota: El backend actual asigna automáticamente el `admin` desde el JWT y genera `ticketCode`, `createdAt`.

6) Update Service Status (ADMIN)
PATCH /api/v1/services/{id}/status
Headers: Authorization: Bearer {TOKEN}
Body:
{
  "status": "EN_DIAGNOSTICO"
}

7) Search Services (Public)
GET /api/v1/services/search?query=TKT-A7F3E2B1

8) Get Customer Services (Public)
GET /api/v1/services/my-services?customerId=1

9) Get Devices by Customer (ADMIN)
GET /api/v1/devices/customer/{customerId}
Headers: Authorization: Bearer {TOKEN}


Conclusión breve sobre creación de Customer:
- El proyecto incluye la entidad `Customer`, `ICustomerRepository`, `ICustomerService` y `CustomerController`.
- Endpoints disponibles: `POST /api/v1/customers` y `GET /api/v1/customers/{id}`.
- Para garantizar el flujo: "Crear Customer si no existe → asignar Device → crear orden", la lógica recomendada es:
  1. Comprobar si `device.customer.id` está presente y existe.
  2. Si `customer` viene sin `id`, crear/guardar el `Customer` primero, luego crear/guardar `Device` (o reusar `deviceRepository`) y finalmente crear la `TechnicalService` apuntando al `Device` creado.

-- Fin de TEST
