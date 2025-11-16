# 🎓 Sistema de Gestión Académica con Integración Moodle

Sistema backend completo para la gestión de instituciones educativas con sincronización automática a Moodle. Construido con Java, Spring Boot, arquitectura hexagonal y programación concurrente.

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Módulos y Endpoints](#-módulos-y-endpoints)
  - [Grupos](#1-grupos)
  - [Alumnos](#2-alumnos)
  - [Docentes](#3-docentes)
  - [Asignaturas](#4-asignaturas)
  - [Programas de Estudio](#5-programas-de-estudio)
  - [Inscripciones (Alumno-Grupo)](#6-inscripciones-alumno-grupo)
  - [Integración Moodle](#7-integración-moodle)
- [Base de Datos](#-base-de-datos)
- [Integración con Moodle](#-integración-con-moodle)
- [Ejemplos de Uso](#-ejemplos-de-uso)

---

## ✨ Características

### Funcionalidades Principales
- ✅ **CRUD Completo** para 6 entidades principales
- ✅ **58 Endpoints RESTful** documentados
- ✅ **Arquitectura Hexagonal** (Clean Architecture)
- ✅ **Programación Concurrente** con CompletableFuture
- ✅ **Pool de Conexiones** optimizado con HikariCP
- ✅ **Integración con Moodle** mediante Web Services
- ✅ **Validaciones Robustas** en todas las operaciones
- ✅ **Manejo Centralizado de Errores**
- ✅ **CORS Configurado** para frontends

### Características Técnicas
- 🚀 **Operaciones Asíncronas** para mejor rendimiento
- 🔒 **Validación de Datos** en múltiples capas
- 📊 **Estadísticas y Reportes** en tiempo real
- 🔄 **Sincronización Bidireccional** con Moodle
- 🎯 **Endpoints Especializados** por caso de uso
- 💾 **Transacciones Seguras** con MySQL

---

## 🛠 Tecnologías

| Categoría | Tecnología | Versión |
|-----------|-----------|---------|
| **Backend** | Java | 17+ |
| **Framework** | Spring Boot | 3.x |
| **Build Tool** | Gradle | 8.x |
| **Base de Datos** | MySQL | 8.0+ |
| **Connection Pool** | HikariCP | Latest |
| **HTTP Client** | Java HTTP Client | Native |
| **JSON Processing** | Jackson | Latest |
| **Environment** | Dotenv | Latest |

---

## 🏗 Arquitectura

### Arquitectura Hexagonal (Ports & Adapters)

```
src/main/java/com/gestion/
│
├── core/                           # Configuración central
│   └── ConnMySQL.java             # Pool de conexiones
│
├── [Módulo]/                      # Ejemplo: Alumno, Docente, etc.
│   ├── domain/                    # Capa de dominio
│   │   ├── entities/              # Entidades de negocio
│   │   ├── dto/                   # Data Transfer Objects
│   │   └── [Modulo]_Repository.java  # Puerto (Interface)
│   │
│   ├── application/               # Casos de uso
│   │   ├── Create[Modulo]_UseCase.java
│   │   ├── Get[Modulo]_UseCase.java
│   │   ├── Update[Modulo]_UseCase.java
│   │   └── Delete[Modulo]_UseCase.java
│   │
│   └── infrastructure/            # Adaptadores
│       ├── adapters/              # Implementaciones de repositorios
│       │   └── MySQL[Modulo]Repository.java
│       ├── controllers/           # Controladores REST
│       │   └── [Modulo]Controller.java
│       └── [Modulo]Dependencies.java  # Configuración de Beans
│
└── main.java                      # Punto de entrada
```

### Principios Aplicados

- **Separación de Responsabilidades**: Cada capa tiene un propósito específico
- **Inversión de Dependencias**: Las capas internas no conocen las externas
- **Inyección de Dependencias**: Uso de Spring para gestionar beans
- **Single Responsibility**: Cada clase tiene una única responsabilidad
- **Open/Closed**: Abierto para extensión, cerrado para modificación

---

## 📦 Instalación

### Requisitos Previos

- **Java JDK 17+**
- **MySQL 8.0+**
- **Gradle 8.x** (o usar el wrapper incluido)
- **Moodle 4.x** (opcional, para integración)

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone <tu-repositorio>
cd gestion-academica
```

2. **Crear la base de datos**
```bash
mysql -u root -p < script_db.sql
```

3. **Configurar variables de entorno**
```bash
cp .env.example .env
nano .env  # Editar con tus credenciales
```

4. **Compilar el proyecto**
```bash
./gradlew build
```

5. **Ejecutar la aplicación**
```bash
./gradlew bootRun
```

El servidor estará disponible en: `http://localhost:8080`

---

## ⚙️ Configuración

### Archivo `.env`

Crea un archivo `.env` en la raíz del proyecto:

```properties
# ============================================
# CONFIGURACIÓN DE BASE DE DATOS
# ============================================
DB_HOST=localhost
DB_PORT=3306
DB_NAME=gestion_academica
DB_USER=tu_usuario
DB_PASSWORD=tu_password
DB_SSL=false

# ============================================
# CONFIGURACIÓN DE MOODLE (Opcional)
# ============================================
MOODLE_URL=http://localhost/moodle
MOODLE_TOKEN=tu_token_de_moodle
```

### Configuración de HikariCP

El pool de conexiones está optimizado con estos parámetros:

```java
maximumPoolSize = 20       // Máximo de conexiones
minimumIdle = 10           // Conexiones mínimas en espera
connectionTimeout = 10000  // Timeout de conexión (ms)
idleTimeout = 600000       // Timeout de inactividad (ms)
maxLifetime = 1800000      // Tiempo de vida máximo (ms)
```

---

## 📚 Módulos y Endpoints

## 1. Grupos

Gestiona los grupos de clases con docentes y asignaturas.

### **Modelo de Datos**

```json
{
  "id": 1,
  "nombre": "1A",
  "asignaturaId": 5,
  "docenteId": 3,
  "cuatrimestre": 1,
  "capacidadMaxima": 25
}
```

### **Endpoints**

#### 📝 Crear Grupo
```http
POST /api/grupos
Content-Type: application/json

{
  "nombre": "1A",
  "asignaturaId": 1,
  "docenteId": 1,
  "cuatrimestre": 1,
  "capacidadMaxima": 25
}
```

**Respuesta:**
```json
{
  "message": "Grupo creado exitosamente",
  "grupo": {
    "id": 1,
    "nombre": "1A",
    "asignaturaId": 1,
    "docenteId": 1,
    "cuatrimestre": 1,
    "capacidadMaxima": 25
  }
}
```

#### 📋 Obtener Todos los Grupos
```http
GET /api/grupos
```

**Respuesta:**
```json
{
  "grupos": [
    {
      "id": 1,
      "nombre": "1A",
      "asignaturaId": 1,
      "docenteId": 1,
      "cuatrimestre": 1,
      "capacidadMaxima": 25
    }
  ],
  "total": 35
}
```

#### 🔍 Obtener Grupo por ID
```http
GET /api/grupos/{id}
```

**Ejemplo:** `GET /api/grupos/1`

#### 🔍 Filtrar Grupos por Cuatrimestre
```http
GET /api/grupos/cuatrimestre/{cuatrimestre}
```

**Ejemplo:** `GET /api/grupos/cuatrimestre/1`

**Respuesta:**
```json
{
  "cuatrimestre": 1,
  "grupos": [...],
  "total": 7
}
```

#### 🔍 Filtrar Grupos por Docente
```http
GET /api/grupos/docente/{docenteId}
```

**Ejemplo:** `GET /api/grupos/docente/1`

#### 🔍 Filtrar Grupos por Asignatura
```http
GET /api/grupos/asignatura/{asignaturaId}
```

**Ejemplo:** `GET /api/grupos/asignatura/5`

#### ✏️ Actualizar Grupo
```http
PUT /api/grupos/{id}
Content-Type: application/json

{
  "nombre": "1B",
  "asignaturaId": 1,
  "docenteId": 2,
  "cuatrimestre": 1,
  "capacidadMaxima": 30
}
```

#### 🗑️ Eliminar Grupo
```http
DELETE /api/grupos/{id}
```

---

## 2. Alumnos

Gestiona la información de los estudiantes.

### **Modelo de Datos**

```json
{
  "id": 1,
  "nombre": "Juan",
  "apellido": "Pérez García",
  "nombreCompleto": "Juan Pérez García",
  "matricula": "A000001",
  "cuatrimestre": 1,
  "email": "alumno1@estudiante.ids.upchiapas.edu.mx",
  "programaId": 1,
  "fechaIngreso": "2024-01-15"
}
```

### **Endpoints**

#### 📝 Crear Alumno
```http
POST /api/alumnos
Content-Type: application/json

{
  "nombre": "María",
  "apellido": "González López",
  "matricula": "A000876",
  "cuatrimestre": 3,
  "email": "maria.gonzalez@estudiante.ids.upchiapas.edu.mx",
  "programaId": 1
}
```

**Validaciones:**
- ✅ Nombre y apellido obligatorios
- ✅ Matrícula única y obligatoria
- ✅ Email válido y único
- ✅ Cuatrimestre entre 1 y 10
- ✅ Programa debe existir

**Respuesta:**
```json
{
  "message": "Alumno creado exitosamente",
  "alumno": {
    "id": 876,
    "nombre": "María",
    "apellido": "González López",
    "nombreCompleto": "María González López",
    "matricula": "A000876",
    "cuatrimestre": 3,
    "email": "maria.gonzalez@estudiante.ids.upchiapas.edu.mx",
    "programaId": 1,
    "fechaIngreso": "2024-11-16"
  }
}
```

#### 📋 Obtener Todos los Alumnos
```http
GET /api/alumnos
```

#### 🔍 Obtener Alumno por ID
```http
GET /api/alumnos/{id}
```

#### 🔍 Obtener Alumno por Matrícula
```http
GET /api/alumnos/matricula/{matricula}
```

**Ejemplo:** `GET /api/alumnos/matricula/A000001`

#### 🔍 Filtrar Alumnos por Cuatrimestre
```http
GET /api/alumnos/cuatrimestre/{cuatrimestre}
```

**Ejemplo:** `GET /api/alumnos/cuatrimestre/3`

**Respuesta:**
```json
{
  "cuatrimestre": 3,
  "alumnos": [...],
  "total": 175
}
```

#### 🔎 Buscar Alumnos
```http
GET /api/alumnos/search?q={término}
```

**Ejemplo:** `GET /api/alumnos/search?q=maria`

Busca en:
- Nombre completo
- Matrícula

**Respuesta:**
```json
{
  "searchTerm": "maria",
  "alumnos": [...],
  "total": 12
}
```

#### ✏️ Actualizar Alumno
```http
PUT /api/alumnos/{id}
Content-Type: application/json

{
  "nombre": "María",
  "apellido": "González López",
  "matricula": "A000876",
  "cuatrimestre": 4,
  "email": "maria.gonzalez@estudiante.ids.upchiapas.edu.mx",
  "programaId": 1
}
```

#### 🗑️ Eliminar Alumno
```http
DELETE /api/alumnos/{id}
```

---

## 3. Docentes

Gestiona la información del personal docente.

### **Modelo de Datos**

```json
{
  "id": 1,
  "nombre": "Carlos",
  "apellido": "García López",
  "nombreCompleto": "Carlos García López",
  "email": "carlos.garcia@ids.upchiapas.edu.mx",
  "telefono": "961-123-4501",
  "fechaContratacion": "2024-01-10"
}
```

### **Endpoints**

#### 📝 Crear Docente
```http
POST /api/docentes
Content-Type: application/json

{
  "nombre": "Pedro",
  "apellido": "Ramírez García",
  "email": "pedro.ramirez@ids.upchiapas.edu.mx",
  "telefono": "961-234-5678"
}
```

**Validaciones:**
- ✅ Nombre y apellido obligatorios
- ✅ Email válido y único
- ✅ Teléfono con mínimo 10 dígitos (opcional)

#### 📋 Obtener Todos los Docentes
```http
GET /api/docentes
```

#### 🔍 Obtener Docente por ID
```http
GET /api/docentes/{id}
```

#### 🔎 Buscar Docentes
```http
GET /api/docentes/search?q={término}
```

**Ejemplo:** `GET /api/docentes/search?q=garcia`

Busca en:
- Nombre completo
- Email

#### ✏️ Actualizar Docente
```http
PUT /api/docentes/{id}
Content-Type: application/json

{
  "nombre": "Pedro",
  "apellido": "Ramírez García",
  "email": "pedro.ramirez@ids.upchiapas.edu.mx",
  "telefono": "961-234-5678"
}
```

#### 🗑️ Eliminar Docente
```http
DELETE /api/docentes/{id}
```

### **Gestión de Asignaturas del Docente**

#### ➕ Asignar Asignatura a Docente
```http
POST /api/docentes/{docenteId}/asignaturas/{asignaturaId}
```

**Ejemplo:** `POST /api/docentes/1/asignaturas/5`

**Respuesta:**
```json
{
  "message": "Asignatura asignada exitosamente",
  "docenteId": 1,
  "asignaturaId": 5
}
```

#### ➖ Remover Asignatura de Docente
```http
DELETE /api/docentes/{docenteId}/asignaturas/{asignaturaId}
```

#### 📋 Ver Asignaturas del Docente
```http
GET /api/docentes/{docenteId}/asignaturas
```

**Respuesta:**
```json
{
  "docenteId": 1,
  "asignaturaIds": [1, 5, 10],
  "total": 3
}
```

---

## 4. Asignaturas

Gestiona las materias del plan de estudios.

### **Modelo de Datos**

```json
{
  "id": 1,
  "nombre": "Fundamentos de Programación",
  "cuatrimestre": 1,
  "programaId": 1,
  "creditos": 6
}
```

### **Endpoints**

#### 📝 Crear Asignatura
```http
POST /api/asignaturas
Content-Type: application/json

{
  "nombre": "Inteligencia Artificial Avanzada",
  "cuatrimestre": 8,
  "programaId": 1,
  "creditos": 8
}
```

**Validaciones:**
- ✅ Nombre obligatorio y único por programa
- ✅ Cuatrimestre entre 1 y 10
- ✅ Créditos entre 1 y 12
- ✅ Programa debe existir

**Respuesta:**
```json
{
  "message": "Asignatura creada exitosamente",
  "asignatura": {
    "id": 71,
    "nombre": "Inteligencia Artificial Avanzada",
    "cuatrimestre": 8,
    "programaId": 1,
    "creditos": 8
  }
}
```

#### 📋 Obtener Todas las Asignaturas
```http
GET /api/asignaturas
```

**Respuesta:**
```json
{
  "asignaturas": [...],
  "total": 70
}
```

#### 🔍 Obtener Asignatura por ID
```http
GET /api/asignaturas/{id}
```

#### 🔍 Filtrar Asignaturas por Cuatrimestre
```http
GET /api/asignaturas/cuatrimestre/{cuatrimestre}
```

**Ejemplo:** `GET /api/asignaturas/cuatrimestre/5`

**Respuesta:**
```json
{
  "cuatrimestre": 5,
  "asignaturas": [
    {
      "id": 29,
      "nombre": "Base de Datos",
      "cuatrimestre": 5,
      "programaId": 1,
      "creditos": 6
    },
    ...
  ],
  "total": 7
}
```

#### 🔍 Filtrar Asignaturas por Programa
```http
GET /api/asignaturas/programa/{programaId}
```

**Ejemplo:** `GET /api/asignaturas/programa/1`

#### 🔎 Buscar Asignaturas
```http
GET /api/asignaturas/search?q={término}
```

**Ejemplo:** `GET /api/asignaturas/search?q=base`

**Respuesta:**
```json
{
  "searchTerm": "base",
  "asignaturas": [
    {
      "id": 29,
      "nombre": "Base de Datos",
      "cuatrimestre": 5,
      "programaId": 1,
      "creditos": 6
    },
    {
      "id": 36,
      "nombre": "Administración de Base de Datos",
      "cuatrimestre": 6,
      "programaId": 1,
      "creditos": 6
    }
  ],
  "total": 2
}
```

#### ✏️ Actualizar Asignatura
```http
PUT /api/asignaturas/{id}
Content-Type: application/json

{
  "nombre": "Fundamentos de Programación I",
  "cuatrimestre": 1,
  "programaId": 1,
  "creditos": 7
}
```

#### 🗑️ Eliminar Asignatura
```http
DELETE /api/asignaturas/{id}
```

---

## 5. Programas de Estudio

Gestiona los programas académicos de la institución.

### **Modelo de Datos**

```json
{
  "id": 1,
  "nombre": "Ingeniería en Sistemas Computacionales",
  "numCuatrimestres": 10,
  "fechaCreacion": "2024-01-01T00:00:00"
}
```

### **Endpoints**

#### 📝 Crear Programa de Estudio
```http
POST /api/programas
Content-Type: application/json

{
  "nombre": "Ingeniería en Desarrollo de Software",
  "numCuatrimestres": 10
}
```

**Validaciones:**
- ✅ Nombre obligatorio y único
- ✅ Número de cuatrimestres entre 1 y 15

**Respuesta:**
```json
{
  "message": "Programa de estudio creado exitosamente",
  "programa": {
    "id": 2,
    "nombre": "Ingeniería en Desarrollo de Software",
    "numCuatrimestres": 10,
    "fechaCreacion": "2024-11-16T10:30:00"
  }
}
```

#### 📋 Obtener Todos los Programas
```http
GET /api/programas
```

#### 🔍 Obtener Programa por ID
```http
GET /api/programas/{id}
```

#### 📊 Obtener Estadísticas del Programa
```http
GET /api/programas/{id}/stats
```

**Respuesta:**
```json
{
  "stats": {
    "programaId": 1,
    "nombre": "Ingeniería en Sistemas Computacionales",
    "numCuatrimestres": 10,
    "totalAlumnos": 875,
    "totalAsignaturas": 70,
    "fechaCreacion": "2024-01-01T00:00:00"
  }
}
```

#### 🔎 Buscar Programas
```http
GET /api/programas/search?q={término}
```

**Ejemplo:** `GET /api/programas/search?q=ingenieria`

#### ✏️ Actualizar Programa
```http
PUT /api/programas/{id}
Content-Type: application/json

{
  "nombre": "Ingeniería en Sistemas Computacionales",
  "numCuatrimestres": 11
}
```

#### 🗑️ Eliminar Programa
```http
DELETE /api/programas/{id}
```

**Nota:** No se puede eliminar un programa si tiene alumnos inscritos.

---

## 6. Inscripciones (Alumno-Grupo)

Gestiona la relación entre alumnos y grupos (matriculación).

### **Modelo de Datos**

```json
{
  "alumnoId": 1,
  "grupoId": 1,
  "fechaInscripcion": "2024-09-01T08:00:00"
}
```

### **Endpoints**

#### ➕ Inscribir Alumno a Grupo
```http
POST /api/inscripciones/alumnos/{alumnoId}/grupos/{grupoId}
```

**Ejemplo:** `POST /api/inscripciones/alumnos/1/grupos/1`

**Validaciones:**
- ✅ Alumno debe existir
- ✅ Grupo debe existir
- ✅ No puede estar ya inscrito
- ✅ El grupo debe tener capacidad disponible

**Respuesta:**
```json
{
  "message": "Alumno inscrito exitosamente",
  "inscripcion": {
    "alumnoId": 1,
    "grupoId": 1,
    "fechaInscripcion": "2024-11-16T10:45:00"
  }
}
```

#### ➖ Desinscribir Alumno de Grupo
```http
DELETE /api/inscripciones/alumnos/{alumnoId}/grupos/{grupoId}
```

**Respuesta:**
```json
{
  "message": "Alumno desinscrito exitosamente",
  "alumnoId": 1,
  "grupoId": 1
}
```

#### 📋 Obtener Todas las Inscripciones
```http
GET /api/inscripciones
```

**Respuesta:**
```json
{
  "inscripciones": [
    {
      "alumnoId": 1,
      "grupoId": 1,
      "fechaInscripcion": "2024-09-01T08:00:00"
    },
    ...
  ],
  "total": 875
}
```

#### 🔍 Obtener Grupos de un Alumno
```http
GET /api/inscripciones/alumnos/{alumnoId}/grupos
```

**Ejemplo:** `GET /api/inscripciones/alumnos/1/grupos`

**Respuesta:**
```json
{
  "alumnoId": 1,
  "grupoIds": [1, 2, 3, 4, 5, 6, 7],
  "total": 7
}
```

#### 🔍 Obtener Alumnos de un Grupo
```http
GET /api/inscripciones/grupos/{grupoId}/alumnos
```

**Ejemplo:** `GET /api/inscripciones/grupos/1/alumnos`

**Respuesta:**
```json
{
  "grupoId": 1,
  "alumnoIds": [1, 2, 3, ..., 25],
  "total": 25
}
```

#### 📊 Estadísticas de un Grupo
```http
GET /api/inscripciones/grupos/{grupoId}/stats
```

**Respuesta:**
```json
{
  "stats": {
    "grupoId": 1,
    "totalAlumnos": 25,
    "tieneCapacidad": false
  }
}
```

#### 📊 Estadísticas de un Alumno
```http
GET /api/inscripciones/alumnos/{alumnoId}/stats
```

**Respuesta:**
```json
{
  "stats": {
    "alumnoId": 1,
    "totalGrupos": 7
  }
}
```

---

## 7. Integración Moodle

Sincroniza automáticamente datos con Moodle mediante Web Services.

### **Configuración Previa en Moodle**

Antes de usar estos endpoints, debes configurar Moodle:

1. **Habilitar Web Services**
   - Administración del sitio > Avanzadas > Habilitar servicios web = SÍ

2. **Habilitar Protocolo REST**
   - Administración del sitio > Plugins > Servicios web > Gestionar protocolos
   - Activar "REST protocol"

3. **Crear Servicio Externo**
   - Administración del sitio > Plugins > Servicios web > Servicios externos
   - Crear servicio: "Sistema de Gestión Académica"

4. **Agregar Funciones al Servicio**
   - `core_webservice_get_site_info`
   - `core_user_create_users`
   - `core_user_get_users_by_field`
   - `core_course_create_courses`
   - `core_course_get_courses_by_field`
   - `enrol_manual_enrol_users`

5. **Generar Token**
   - Administración del sitio > Plugins > Servicios web > Administrar tokens
   - Crear token para un usuario con permisos de Manager

6. **Configurar en `.env`**
   ```properties
   MOODLE_URL=http://localhost/moodle
   MOODLE_TOKEN=tu_token_generado
   ```

### **Endpoints**

#### ⚙️ Ver Configuración de Moodle
```http
GET /api/moodle/config
```

**Respuesta:**
```json
{
  "configured": true,
  "moodleUrl": "http://localhost/moodle",
  "webServiceUrl": "http://localhost/moodle/webservice/rest/server.php"
}
```

#### 🔌 Test de Conexión con Moodle
```http
GET /api/moodle/test
```

**Respuesta (Exitosa):**
```json
{
  "connected": true,
  "sitename": "Mi Sitio Moodle",
  "siteurl": "http://localhost/moodle",
  "moodleversion": "4.3"
}
```

**Respuesta (Error):**
```json
{
  "connected": false,
  "message": "No se pudo conectar con Moodle"
}
```

### **Sincronización de Usuarios**

#### 👤 Sincronizar Alumno
```http
POST /api/moodle/sync/alumno/{alumnoId}
```

**Ejemplo:** `POST /api/moodle/sync/alumno/1`

**Respuesta (Usuario Nuevo):**
```json
{
  "action": "created",
  "moodleUserId": 123,
  "username": "a000001",
  "temporalPassword": "A0000012024!",
  "message": "Usuario creado exitosamente en Moodle"
}
```

**Respuesta (Usuario Existente):**
```json
{
  "action": "existing",
  "moodleUserId": 123,
  "username": "a000001",
  "message": "El usuario ya existe en Moodle"
}
```

**Credenciales Generadas:**
- **Username:** Matrícula en minúsculas (ej: `a000001`)
- **Password Temporal:** `{matricula}2024!` (ej: `A0000012024!`)
- Los usuarios deben cambiar su contraseña en el primer acceso

#### 👨‍🏫 Sincronizar Docente
```http
POST /api/moodle/sync/docente/{docenteId}
```

**Ejemplo:** `POST /api/moodle/sync/docente/1`

**Respuesta (Usuario Nuevo):**
```json
{
  "action": "created",
  "moodleUserId": 10,
  "username": "carlos.garcia",
  "temporalPassword": "carlos.garcia2024!",
  "message": "Docente creado exitosamente en Moodle"
}
```

**Credenciales Generadas:**
- **Username:** Primera parte del email (antes del @)
- **Password Temporal:** `{username}2024!`

#### 👥 Sincronizar Todos los Alumnos
```http
POST /api/moodle/sync/alumnos/all
```

Sincroniza todos los alumnos de la base de datos a Moodle (proceso en lote).

**Respuesta:**
```json
{
  "total": 875,
  "results": [
    {
      "alumnoId": 1,
      "action": "created",
      "moodleUserId": 123,
      "username": "a000001"
    },
    {
      "alumnoId": 2,
      "action": "existing",
      "moodleUserId": 124,
      "username": "a000002"
    },
    ...
  ]
}
```

### **Sincronización de Cursos**

#### 📚 Sincronizar Asignatura como Curso
```http
POST /api/moodle/sync/asignatura/{asignaturaId}
```

**Ejemplo:** `POST /api/moodle/sync/asignatura/5`

**Respuesta (Curso Nuevo):**
```json
{
  "action": "created",
  "moodleCourseId": 45,
  "shortname": "C1_fundamento",
  "message": "Curso creado exitosamente en Moodle"
}
```

**Respuesta (Curso Existente):**
```json
{
  "action": "existing",
  "moodleCourseId": 45,
  "shortname": "C1_fundamento",
  "message": "El curso ya existe en Moodle"
}
```

**Formato de Shortname:**
- Patrón: `C{cuatrimestre}_{nombre_limpio}`
- Ejemplo: `C1_fundamento` para "Fundamentos de Programación"
- Máximo 10 caracteres para el nombre

### **Matriculación**

#### 🎓 Matricular Alumno en Curso
```http
POST /api/moodle/enrol/alumno/{alumnoId}/course/{courseId}
```

**Ejemplo:** `POST /api/moodle/enrol/alumno/1/course/45`

**Respuesta:**
```json
{
  "message": "Alumno matriculado exitosamente",
  "moodleUserId": 123,
  "moodleCourseId": 45,
  "role": "student"
}
```

**Requisitos:**
- ✅ El alumno debe estar sincronizado primero
- ✅ El curso debe existir en Moodle
- ✅ Rol asignado: **Student** (ID: 5)

#### 👨‍🏫 Asignar Docente a Curso
```http
POST /api/moodle/enrol/docente/{docenteId}/course/{courseId}
```

**Ejemplo:** `POST /api/moodle/enrol/docente/1/course/45`

**Respuesta:**
```json
{
  "message": "Docente asignado exitosamente",
  "moodleUserId": 10,
  "moodleCourseId": 45,
  "role": "teacher"
}
```

**Requisitos:**
- ✅ El docente debe estar sincronizado primero
- ✅ El curso debe existir en Moodle
- ✅ Rol asignado: **Teacher** (ID: 3)

### **Función Personalizada**

#### ⚡ Ejecutar Función de Moodle
```http
POST /api/moodle/execute
Content-Type: application/json

{
  "function": "nombre_funcion_moodle",
  "params": {
    "param1": "valor1",
    "param2": "valor2"
  }
}
```

**Ejemplo - Obtener Usuarios:**
```http
POST /api/moodle/execute
Content-Type: application/json

{
  "function": "core_user_get_users",
  "params": {
    "criteria[0][key]": "email",
    "criteria[0][value]": "alumno@example.com"
  }
}
```

**Respuesta:**
```json
{
  "function": "core_user_get_users",
  "result": {
    "users": [...],
    "warnings": []
  }
}
```

Este endpoint permite ejecutar cualquier función disponible en Moodle Web Services.

---

## 💾 Base de Datos

### **Esquema de Datos**

```
┌─────────────────────┐
│ programa_estudio    │
├─────────────────────┤
│ id (PK)            │
│ nombre             │
│ num_cuatrimestres  │
│ fecha_creacion     │
└─────────────────────┘
         │
         ├───────────────────────┐
         │                       │
         ▼                       ▼
┌─────────────────────┐  ┌─────────────────────┐
│ asignaturas         │  │ alumnos             │
├─────────────────────┤  ├─────────────────────┤
│ id (PK)            │  │ id (PK)            │
│ nombre             │  │ nombre             │
│ cuatrimestre       │  │ apellido           │
│ programa_id (FK)   │  │ matricula (UNIQUE) │
│ creditos           │  │ cuatrimestre       │
└─────────────────────┘  │ email (UNIQUE)     │
         │               │ programa_id (FK)   │
         │               │ fecha_ingreso      │
         │               └─────────────────────┘
         │                       │
         │                       │
         ▼                       │
┌─────────────────────┐          │
│ docentes            │          │
├─────────────────────┤          │
│ id (PK)            │          │
│ nombre             │          │
│ apellido           │          │
│ email (UNIQUE)     │          │
│ telefono           │          │
│ fecha_contratacion │          │
└─────────────────────┘          │
         │                       │
         │                       │
         ▼                       │
┌─────────────────────┐          │
│ docente_asignatura  │          │
├─────────────────────┤          │
│ docente_id (FK)    │          │
│ asignatura_id (FK) │          │
└─────────────────────┘          │
         │                       │
         │                       │
         ▼                       ▼
┌─────────────────────┐  ┌─────────────────────┐
│ grupos              │  │ alumno_grupo        │
├─────────────────────┤  ├─────────────────────┤
│ id (PK)            │  │ alumno_id (FK)     │
│ nombre             │  │ grupo_id (FK)      │
│ asignatura_id (FK) │  │ fecha_inscripcion  │
│ docente_id (FK)    │  └─────────────────────┘
│ cuatrimestre       │
│ capacidad_maxima   │
└─────────────────────┘
```

### **Relaciones**

| Tabla | Relación | Tabla Relacionada |
|-------|----------|-------------------|
| `asignaturas` | N:1 | `programa_estudio` |
| `alumnos` | N:1 | `programa_estudio` |
| `docente_asignatura` | N:M | `docentes` ↔ `asignaturas` |
| `grupos` | N:1 | `asignaturas` |
| `grupos` | N:1 | `docentes` |
| `alumno_grupo` | N:M | `alumnos` ↔ `grupos` |

### **Índices Principales**

- `idx_matricula` en `alumnos(matricula)`
- `idx_cuatrimestre` en `alumnos(cuatrimestre)`
- `idx_cuatrimestre` en `asignaturas(cuatrimestre)`
- `idx_programa` en `asignaturas(programa_id)`
- `idx_cuatrimestre` en `grupos(cuatrimestre)`

### **Datos de Ejemplo**

La base de datos incluye datos de ejemplo:
- **1 Programa** de estudio
- **70 Asignaturas** (7 por cuatrimestre × 10 cuatrimestres)
- **35 Docentes**
- **35 Grupos** (7 grupos por cuatrimestre impar)
- **875 Alumnos** (175 por cuatrimestre impar)
- **875 Inscripciones** (25 alumnos por grupo)

---

## 🔄 Integración con Moodle

### **Flujos de Trabajo Completos**

#### **Flujo 1: Configurar un Grupo Completo en Moodle**

```bash
# Paso 1: Sincronizar la asignatura como curso
POST /api/moodle/sync/asignatura/5
# Respuesta: moodleCourseId = 45

# Paso 2: Sincronizar el docente
POST /api/moodle/sync/docente/3
# Respuesta: moodleUserId = 10

# Paso 3: Asignar docente al curso
POST /api/moodle/enrol/docente/3/course/45

# Paso 4: Obtener alumnos del grupo
GET /api/inscripciones/grupos/1/alumnos
# Respuesta: alumnoIds = [1, 2, 3, ..., 25]

# Paso 5: Sincronizar cada alumno
POST /api/moodle/sync/alumno/1
POST /api/moodle/sync/alumno/2
...

# Paso 6: Matricular alumnos en el curso
POST /api/moodle/enrol/alumno/1/course/45
POST /api/moodle/enrol/alumno/2/course/45
...
```

#### **Flujo 2: Sincronización Masiva**

```bash
# Sincronizar todos los alumnos de una vez
POST /api/moodle/sync/alumnos/all
```

#### **Flujo 3: Verificación de Estado**

```bash
# 1. Verificar configuración
GET /api/moodle/config

# 2. Test de conexión
GET /api/moodle/test

# 3. Ejecutar función personalizada
POST /api/moodle/execute
{
  "function": "core_webservice_get_site_info",
  "params": {}
}
```

### **Roles de Moodle**

Los roles predefinidos son:

| Rol | ID | Descripción |
|-----|----|-----------| 
| Student | 5 | Alumno con acceso a cursos |
| Teacher | 3 | Docente con permisos de edición |

**Nota:** Los IDs pueden variar según tu instalación de Moodle. Verifica con:

```sql
SELECT id, shortname, name FROM mdl_role;
```

Si necesitas cambiarlos, edita en `MoodleSyncService.java`:

```java
private static final int STUDENT_ROLE_ID = 5;  // Tu ID
private static final int TEACHER_ROLE_ID = 3;  // Tu ID
```

### **Gestión de Contraseñas**

Las contraseñas se generan automáticamente:

**Para Alumnos:**
- Patrón: `{MATRICULA}2024!`
- Ejemplo: `A0000012024!`

**Para Docentes:**
- Patrón: `{username}2024!`
- Ejemplo: `carlos.garcia2024!`

**⚠️ Importante:**
- Son contraseñas **temporales**
- Los usuarios deben cambiarlas en su primer acceso
- Se recomienda configurar política de cambio obligatorio en Moodle

### **Manejo de Errores Comunes**

| Error | Causa | Solución |
|-------|-------|----------|
| `accessexception` | Token inválido | Verifica `MOODLE_TOKEN` en `.env` |
| `webservicenotavailable` | Web services deshabilitado | Habilita en Moodle > Avanzadas |
| `invalidparameter` | Función no autorizada | Agrega función al servicio |
| `Usuario ya existe` | Email duplicado | Es informativo, no es error |
| `No se pudo conectar` | URL incorrecta o Moodle caído | Verifica `MOODLE_URL` |

---

## 📖 Ejemplos de Uso

### **Ejemplo 1: Crear un Alumno y Sincronizarlo con Moodle**

```bash
# 1. Crear alumno en el sistema local
curl -X POST http://localhost:8080/api/alumnos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Ana",
    "apellido": "Martínez Silva",
    "matricula": "A000900",
    "cuatrimestre": 5,
    "email": "ana.martinez@estudiante.ids.upchiapas.edu.mx",
    "programaId": 1
  }'

# Respuesta: id = 900

# 2. Sincronizar con Moodle
curl -X POST http://localhost:8080/api/moodle/sync/alumno/900

# Respuesta:
{
  "action": "created",
  "moodleUserId": 501,
  "username": "a000900",
  "temporalPassword": "A0009002024!",
  "message": "Usuario creado exitosamente en Moodle"
}
```

### **Ejemplo 2: Configurar un Curso Completo**

```bash
# 1. Crear asignatura
curl -X POST http://localhost:8080/api/asignaturas \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Desarrollo Web Avanzado",
    "cuatrimestre": 6,
    "programaId": 1,
    "creditos": 8
  }'

# Respuesta: id = 71

# 2. Crear grupo
curl -X POST http://localhost:8080/api/grupos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "6A",
    "asignaturaId": 71,
    "docenteId": 5,
    "cuatrimestre": 6,
    "capacidadMaxima": 30
  }'

# Respuesta: id = 36

# 3. Sincronizar asignatura como curso en Moodle
curl -X POST http://localhost:8080/api/moodle/sync/asignatura/71

# Respuesta: moodleCourseId = 52

# 4. Sincronizar docente
curl -X POST http://localhost:8080/api/moodle/sync/docente/5

# 5. Asignar docente al curso
curl -X POST http://localhost:8080/api/moodle/enrol/docente/5/course/52
```

### **Ejemplo 3: Inscribir Alumnos a un Grupo**

```bash
# 1. Inscribir alumno al grupo local
curl -X POST http://localhost:8080/api/inscripciones/alumnos/900/grupos/36

# 2. Sincronizar alumno con Moodle (si no está)
curl -X POST http://localhost:8080/api/moodle/sync/alumno/900

# 3. Matricular en el curso de Moodle
curl -X POST http://localhost:8080/api/moodle/enrol/alumno/900/course/52
```

### **Ejemplo 4: Búsqueda y Filtrado**

```bash
# Buscar alumnos por nombre
curl "http://localhost:8080/api/alumnos/search?q=maria"

# Obtener alumnos del cuatrimestre 5
curl http://localhost:8080/api/alumnos/cuatrimestre/5

# Obtener asignaturas de un cuatrimestre
curl http://localhost:8080/api/asignaturas/cuatrimestre/5

# Obtener grupos de un docente
curl http://localhost:8080/api/grupos/docente/5

# Obtener estadísticas de un grupo
curl http://localhost:8080/api/inscripciones/grupos/1/stats
```

### **Ejemplo 5: Gestión de Asignaturas de Docente**

```bash
# Ver asignaturas actuales del docente
curl http://localhost:8080/api/docentes/5/asignaturas

# Asignar nueva asignatura
curl -X POST http://localhost:8080/api/docentes/5/asignaturas/15

# Remover asignatura
curl -X DELETE http://localhost:8080/api/docentes/5/asignaturas/10
```

### **Ejemplo 6: Usando JavaScript/Fetch**

```javascript
// Función para crear alumno
async function crearAlumno(alumno) {
  const response = await fetch('http://localhost:8080/api/alumnos', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(alumno)
  });
  
  const data = await response.json();
  return data;
}

// Función para sincronizar con Moodle
async function syncConMoodle(alumnoId) {
  const response = await fetch(
    `http://localhost:8080/api/moodle/sync/alumno/${alumnoId}`,
    { method: 'POST' }
  );
  
  const data = await response.json();
  return data;
}

// Uso
const nuevoAlumno = {
  nombre: "Carlos",
  apellido: "Pérez López",
  matricula: "A001000",
  cuatrimestre: 3,
  email: "carlos.perez@estudiante.ids.upchiapas.edu.mx",
  programaId: 1
};

crearAlumno(nuevoAlumno)
  .then(result => {
    console.log('Alumno creado:', result);
    return syncConMoodle(result.alumno.id);
  })
  .then(syncResult => {
    console.log('Sincronizado con Moodle:', syncResult);
  })
  .catch(error => {
    console.error('Error:', error);
  });
```

---

## 🔐 Seguridad

### **Recomendaciones de Seguridad**

1. **Variables de Entorno**
   - ✅ Nunca subas `.env` al repositorio
   - ✅ Usa `.gitignore` para excluirlo
   - ✅ Crea `.env.example` sin valores sensibles

2. **Base de Datos**
   - ✅ Usa usuarios con permisos limitados
   - ✅ No uses el usuario `root` en producción
   - ✅ Habilita SSL en producción (`DB_SSL=true`)

3. **Moodle**
   - ✅ Tokens específicos por servicio
   - ✅ Revoca tokens no utilizados
   - ✅ Monitorea logs de Web Services
   - ✅ Usa HTTPS en producción

4. **API**
   - ⚠️ Implementa autenticación (JWT recomendado)
   - ⚠️ Agrega rate limiting
   - ⚠️ Valida inputs en todas las capas
   - ⚠️ Usa HTTPS en producción

### **Archivo .gitignore**

```gitignore
# Environment
.env

# Build
build/
target/
*.class

# IDE
.idea/
.vscode/
*.iml

# OS
.DS_Store
Thumbs.db

# Logs
*.log
logs/
```

---

## 📊 Estadísticas del Proyecto

| Métrica | Valor |
|---------|-------|
| **Módulos** | 7 (6 CRUD + 1 Moodle) |
| **Endpoints** | 58 |
| **Tablas de BD** | 7 |
| **Líneas de Código** | ~8,000 |
| **Clases Java** | 50+ |
| **Casos de Uso** | 40+ |
| **Validaciones** | 100+ |

---

## 🚀 Despliegue

### **Desarrollo**

```bash
# Compilar
./gradlew build

# Ejecutar
./gradlew bootRun

# Ejecutar con perfil específico
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### **Producción**

```bash
# Generar JAR
./gradlew bootJar

# El JAR se encuentra en:
build/libs/gestion-academica-1.0.0.jar

# Ejecutar JAR
java -jar build/libs/gestion-academica-1.0.0.jar
```

### **Variables de Entorno en Producción**

```bash
export DB_HOST=tu_servidor_mysql
export DB_USER=tu_usuario
export DB_PASSWORD=tu_password_seguro
export DB_SSL=true
export MOODLE_URL=https://tu-moodle.com
export MOODLE_TOKEN=tu_token_produccion
```

---

## 🤝 Contribución

### **Agregar un Nuevo Módulo**

1. Crear estructura de directorios siguiendo arquitectura hexagonal
2. Implementar entidad en `domain/entities`
3. Crear interfaz de repositorio en `domain`
4. Implementar casos de uso en `application`
5. Crear adaptador de BD en `infrastructure/adapters`
6. Implementar controlador en `infrastructure/controllers`
7. Configurar dependencias en `infrastructure/[Modulo]Dependencies.java`

---

## 📝 Notas Adicionales

### **Consideraciones Importantes**

1. **Concurrencia**
   - Todas las operaciones son asíncronas
   - Se usa CompletableFuture para operaciones no bloqueantes
   - El pool de conexiones maneja múltiples peticiones simultáneas

2. **Transacciones**
   - Las operaciones de BD son transaccionales
   - Los rollbacks se manejan automáticamente en caso de error

3. **Validaciones**
   - Validaciones en capa de aplicación (casos de uso)
   - Validaciones en capa de base de datos (constraints)
   - Mensajes de error descriptivos

4. **Caché**
   - Sin caché implementado actualmente
   - Se puede agregar Redis para mejorar rendimiento

5. **Logs**
   - Los errores se registran en consola
   - Se recomienda configurar un sistema de logs robusto

### **Limitaciones Conocidas**

- ⚠️ Sin autenticación/autorización implementada
- ⚠️ Sin paginación en endpoints que retornan listas grandes
- ⚠️ Sin rate limiting
- ⚠️ Sin cache de respuestas
- ⚠️ IDs de roles de Moodle pueden variar según instalación

### **Próximas Mejoras Sugeridas**

1. ✨ Implementar autenticación con JWT
2. ✨ Agregar paginación a todos los endpoints GET
3. ✨ Implementar sistema de logs con SLF4J
4. ✨ Agregar documentación con Swagger/OpenAPI
5. ✨ Implementar tests unitarios y de integración
6. ✨ Agregar caché con Redis
7. ✨ Dockerizar la aplicación
8. ✨ Implementar CI/CD

---

## Proyecto desarrollado utilizando:
- Spring Boot
- MySQL
- Moodle Web Services
- Arquitectura Hexagonal
- Programación Concurrente

**Desarrollado por Fabricio Pérez y Ameth Toledo (EST-Software)**