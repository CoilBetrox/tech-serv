## TechServ - Sistema de Gestión de Reparaciones Técnicas

#### 🚀 Demo en Vivo
URL: https://tech-serv.rdtsolutions.site/

#### 📋 Descripción
Plataforma full-stack para la gestión eficiente de reparaciones técnicas. Diseñada para talleres que necesitan digitalizar su flujo de trabajo y ofrecer seguimiento en tiempo real a sus clientes.

### ✨ Características Principales
#### 👨‍🔧 Para Administradores
Autenticación segura mediante JWT

Dashboard con listado completo de órdenes

Gestión CRUD de tickets de reparación

Búsqueda y filtrado por estado, cliente o dispositivo

Actualización del progreso de cada reparación

#### 👤 Para Clientes
Consulta pública sin necesidad de registro

Tracker visual con stepper de progreso

Información detallada del dispositivo y última actividad

Acceso 24/7 desde cualquier dispositivo

#### 🎨 UX/UI
Modo oscuro/claro automático

Diseño 100% responsive

Iconografía intuitiva con Material Symbols

Feedback visual en todas las acciones

#### 🔧 Credenciales de Prueba
##### Administrador:

Email: admin1@mail.com (solicitar por interno)
 
Password: admin1

##### Ticket de prueba:

Número: TIC-98721

#### 🛠️ Stack Tecnológico

##### Frontend

Framework: Vue 3 (Composition API)

Estado: Pinia

Enrutamiento: Vue Router

Estilos: TailwindCSS + Inter Font

Iconos: Material Symbols

HTTP Client: Fetch API

##### Backend

Framework: Spring Boot

Seguridad: Spring Security + JWT

Base de Datos: PostgreSQL

API: RESTful (/api/v1)

#### 🚀 Instalación Local
Prerrequisitos
Node.js 18+

Java 17+

PostgreSQL

Maven

##### Frontend
bash
cd tech-serv-front
npm install
cp .env.example .env  # Configurar variables
npm run dev
##### Backend
bash
cd tech-serv-back
./mvnw spring-boot:run

#### 🚀 Despliegue
El proyecto está configurado para CI/CD con GitHub Actions:

Frontend: Build automático y deploy a servidor

Backend: Compilación y despliegue con Maven

#### 📄 Licencia
Distribuido bajo la licencia MIT. Ver LICENSE.txt para más información.

#### 📧 Contacto
Demo: https://tech-serv.rdtsolutions.site/
