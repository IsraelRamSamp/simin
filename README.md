
# SIMIN - Sistema de Inversión en Instrumentos de Renta Fija

**Fecha de entrega:** 19/05/2025  
**Versión:** 1.0  
**Autor:** Israel Ramirez Samperio

---

## 1. Nombre y Descripción del Sistema

**SIMIN** (Sistema de Inversión en Instrumentos de Deuda) es una aplicación web desarrollada con Spring Boot que permite simular inversiones en distintos instrumentos financieros de renta fija como CETES, BONOS, BONDDIA, UDIBONOS y más. El sistema está diseñado para inversionistas y analistas financieros que buscan estimar el rendimiento de sus inversiones con base en tasas reales, plazos personalizados y desgloses detallados. 

Incluye:
- Gestión de usuarios, roles y carteras
- Simulación avanzada por cartera o instrumento
- Reportes automáticos en PDF
- Seguridad con Spring Security y JWT
- Interfaz profesional con Thymeleaf y Bootstrap

---

## 2. Tecnologías Usadas

- **Lenguaje:** Java 17
- **Frameworks:** Spring Boot, Spring MVC, Spring Security, Spring Data JPA
- **Motor de vistas:** Thymeleaf
- **ORM:** Hibernate
- **Base de datos:** MariaDB
- **Diseño:** Bootstrap 5, HTML5, CSS3, íconos
- **Seguridad:** JWT, Cookies, Roles personalizados
- **Utilidades:** Lombok, ModelMapper, Slf4j, OpenPDF, Postman

---

## 3. Requisitos para Ejecución

- JDK 17 instalado
- Maven 3.8+ configurado
- Base de datos **MariaDB** con esquema llamado `simin`
- IDE recomendado: IntelliJ IDEA o Visual Studio Code

---

## 4. Configuración de Base de Datos (`application.properties`)

```properties
spring.application.name=simin
spring.datasource.url=jdbc:mariadb://localhost:3306/simin
spring.datasource.username=root
spring.datasource.password=*****
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.thymeleaf.cache=false
logging.level.org.springframework.security=DEBUG
server.error.whitelabel.enabled=false
```

> Asegúrate de reemplazar `*****` por tu contraseña real de la base de datos MariaDB.

---

## 5. ¿Cómo ejecutar el proyecto?

### Opción 1: Desde la línea de comandos
```bash
mvn clean install
mvn spring-boot:run
```

### Opción 2: Desde IntelliJ o VS Code
- Abrir el proyecto
- Ejecutar la clase `SiminApplication.java`

### Acceso al sistema:
- URL: `http://localhost:8080/home`
- Formulario de login: `/login`

---

## 6. Flujo de Uso del Sistema

1. El usuario accede al sistema y se autentica (login)
2. Según su rol, accede a diferentes funcionalidades:
   - **Administrador**: gestión completa de usuarios, carteras, instrumentos y simulaciones
   - **Analista**: consulta simulaciones propias y compartidas, descarga reportes
   - **Inversionista**: crea simulaciones, administra sus carteras e instrumentos
3. Puede realizar simulaciones desde carteras o instrumentos individuales
4. Visualiza los resultados con desglose y puede generar PDF
5. El administrador puede acceder a un historial completo de reportes generados

---

## 7. JWT y Postman: Instrucciones de Prueba

Se agregó una implementación básica de autenticación vía JWT para exponer endpoints protegidos desde Postman.

### Endpoint para autenticación:
```
POST http://localhost:8080/api/auth/login
```

**Body (JSON):**
```json
{
  "correo": "usuario@example.com",
  "contrasena": "password123"
}
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR..."
}
```

### Endpoint protegido de prueba:
```
GET http://localhost:8080/api/test/hello
```

**Headers requeridos:**
```
Authorization: Bearer {{token}}
```

---

## 8. Consideraciones Técnicas

- Se implementó **Spring Security** con:
  - Autenticación basada en formulario con cookies (`Thymeleaf`)
  - Autenticación con **JWT** para pruebas con Postman
  - Roles: ADMIN, ANALISTA, INVERSIONISTA

- **Lombok** se utilizó para reducir boilerplate (`@Getter`, `@Setter`, `@Builder`, etc.)

- **Slf4j** se integró en todos los servicios y controladores para un seguimiento profesional vía logs.

- Se manejaron excepciones personalizadas y control global con `@ControllerAdvice`.

---

## 9. Estructura de Carpetas

```text
src
└── main
    ├── java
    │   └── mx.dgtic.unam.simin
    │       ├── controller
    │       ├── dto
    │       ├── entity
    │       ├── repository
    │       ├── security
    │       ├── service
    │       ├── simulation
    │       │   └── strategy
    │       └── util
    └── resources
        ├── static
        └── templates
```

---

## 10. Ilustraciones y Ejemplos

| Rol         | Vista Inicial        | Permisos Principales                          |
|-------------|----------------------|-----------------------------------------------|
| Admin       | `/usuarios/list`     | CRUD completo, acceso total                   |
| Analista    | `/simulaciones/analista` | Consulta simulaciones propias y compartidas |
| Inversionista | `/simulaciones/inversionista` | Simula y genera PDF de sus carteras         |

---

## 11. Autor y Créditos

**Desarrollado por:**  
Israel Ramirez Samperio  
DGTIC - UNAM  
Proyecto final para diplomado de Java  
Contacto: [israelsamperio7@simin.com]

**Tecnologías y librerías de terceros utilizadas:**
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MariaDB](https://mariadb.org/)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Lombok](https://projectlombok.org/)
- [Postman](https://www.postman.com/)
- [OpenPDF](https://github.com/LibrePDF/OpenPDF)

---

**¡Gracias por revisar el proyecto SIMIN!**  
Tu retroalimentación es bienvenida.
# simin
