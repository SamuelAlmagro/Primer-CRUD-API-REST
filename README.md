# API REST CRUD - Spring Boot

API REST completa con autenticación y autorización basada en roles, desarrollada con Spring Boot, Spring Security y MySQL.

### Descripción

Esta API proporciona un sistema CRUD (Create, Read, Update, Delete) para la gestión de usuarios y productos. Cada usuario puede crear sus propios productos y gestionarlos, mientras que los administradores tienen control total sobre todos los recursos del sistema.

### Características

-  Autenticación HTTP Basic con Spring Security
-  Dos roles: USER y ADMIN
-  Encriptación de contraseñas con BCrypt
-  Validación automática de datos
-  Manejo global de excepciones con mensajes claros
-  Relación uno a muchos entre usuarios y productos
-  Tests unitarios y de integración completos
-  Arquitectura en capas escalable

### Tecnologías

- **Java 17**
- **Spring Boot 3.5.9**
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **MySQL** - Base de datos
- **Maven** - Gestión de dependencias
- **JUnit 5 & Mockito** - Testing

## Instalación y Configuración

### Requisitos Previos

- **Java 17** o superior instalado
- **MySQL 8.0** o superior instalado y ejecutándose
- **Maven 3.6** o superior (o usa el wrapper incluido `./mvnw`)
- **Git** para clonar el repositorio

### Paso 1: Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/tu-repositorio.git
cd tu-repositorio
```

### Paso 2: Configurar MySQL

1. Inicia sesión en MySQL:
```bash
mysql -u root -p
```

2. Crea la base de datos:
```sql
CREATE DATABASE crud;
EXIT;
```

### Paso 3: Configurar las credenciales

Edita el archivo `src/main/resources/application.properties` con tus credenciales de MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crud
spring.datasource.username=root
spring.datasource.password=tu_contraseña_mysql
```

### Paso 4: Compilar y ejecutar

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

O si prefieres usar el wrapper de Maven:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

La API estará disponible en: **http://localhost:8080**

### Paso 5: Verificar que funciona

La aplicación creará automáticamente las tablas `users` y `products` en la base de datos. Puedes verificarlo:

```sql
USE crud;
SHOW TABLES;
DESCRIBE users;
DESCRIBE products;
```

## Guía de Uso Completa

### Cómo funciona la Autenticación

Esta API usa **HTTP Basic Authentication**, que es simple y funcional para APIs REST:

1. **Sin autenticación**: Solo puedes registrarte como nuevo usuario
2. **Con autenticación**: Debes enviar tu email y contraseña en cada petición

**Ejemplo práctico:**
```bash
# Sin autenticación (usuario:contraseña)
curl http://localhost:8080/api/users

# Con autenticación usando -u (user)
curl -u juan@email.com:mipassword123 http://localhost:8080/api/users/me
```

**Roles disponibles:**
- **USER**: Rol predeterminado. Puede gestionar sus propios productos y perfil
- **ADMIN**: Puede ver todos los usuarios, productos y eliminar cualquier recurso

###  Flujo Completo de Uso

#### 1. Registrar tu primer usuario

No necesitas autenticación para esto:

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@email.com",
    "password": "mipassword123"
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Juan Pérez",
  "email": "juan@email.com",
  "role": "USER"
}
```

**Nota:** La contraseña se encripta automáticamente y nunca se devuelve en las respuestas.

#### 2. Registrar un administrador

Para tener un usuario ADMIN, necesitas crear un usuario normal y luego cambiar su rol directamente en la base de datos:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@email.com';
```

O crea uno desde el inicio con SQL:

```sql
INSERT INTO users (name, email, password, role) 
VALUES ('Admin', 'admin@email.com', '$2a$10$encryptedpassword', 'ADMIN');
```

#### 3. Ver tu perfil de usuario

```bash
curl -u juan@email.com:mipassword123 \
  http://localhost:8080/api/users/me
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Juan Pérez",
  "email": "juan@email.com",
  "role": "USER"
}
```

#### 4. Crear productos

Ahora que estás autenticado, puedes crear productos:

```bash
curl -X POST http://localhost:8080/api/products \
  -u juan@email.com:mipassword123 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Dell XPS 15",
    "price": 1299.99
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Laptop Dell XPS 15",
  "price": 1299.99,
  "userId": 1,
  "userName": "Juan Pérez"
}
```

Crea más productos:

```bash
# Producto 2
curl -X POST http://localhost:8080/api/products \
  -u juan@email.com:mipassword123 \
  -H "Content-Type: application/json" \
  -d '{"name": "Mouse Logitech MX Master", "price": 99.99}'

# Producto 3
curl -X POST http://localhost:8080/api/products \
  -u juan@email.com:mipassword123 \
  -H "Content-Type: application/json" \
  -d '{"name": "Teclado Mecánico", "price": 149.99}'
```

#### 5. Ver tus productos

```bash
curl -u juan@email.com:mipassword123 \
  http://localhost:8080/api/products/my-products
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Laptop Dell XPS 15",
    "price": 1299.99,
    "userId": 1,
    "userName": "Juan Pérez"
  },
  {
    "id": 2,
    "name": "Mouse Logitech MX Master",
    "price": 99.99,
    "userId": 1,
    "userName": "Juan Pérez"
  },
  {
    "id": 3,
    "name": "Teclado Mecánico",
    "price": 149.99,
    "userId": 1,
    "userName": "Juan Pérez"
  }
]
```

#### 6. Ver un producto específico

```bash
curl -u juan@email.com:mipassword123 \
  http://localhost:8080/api/products/1
```

#### 7. Actualizar un producto

Solo puedes actualizar tus propios productos (o cualquiera si eres ADMIN):

```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -u juan@email.com:mipassword123 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Dell XPS 15 - Reacondicionada",
    "price": 999.99
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Laptop Dell XPS 15 - Reacondicionada",
  "price": 999.99,
  "userId": 1,
  "userName": "Juan Pérez"
}
```

#### 8. Actualizar tu perfil de usuario

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -u juan@email.com:mipassword123 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez García",
    "email": "juan.perez@email.com",
    "password": "nuevapassword456"
  }'
```

**Nota:** Si cambias el email o contraseña, usa las nuevas credenciales en las siguientes peticiones.

#### 9. Eliminar un producto

Solo puedes eliminar tus propios productos (o cualquiera si eres ADMIN):

```bash
curl -X DELETE http://localhost:8080/api/products/3 \
  -u juan@email.com:mipassword123
```

Si es exitoso, recibirás un código **204 No Content** (sin cuerpo de respuesta).

#### 10. Funcionalidades de ADMIN

Si tienes un usuario con rol ADMIN:

**Ver todos los usuarios:**
```bash
curl -u admin@email.com:adminpass \
  http://localhost:8080/api/users
```

**Ver todos los productos del sistema:**
```bash
curl -u admin@email.com:adminpass \
  http://localhost:8080/api/products
```

**Eliminar cualquier usuario:**
```bash
curl -X DELETE http://localhost:8080/api/users/5 \
  -u admin@email.com:adminpass
```

**Eliminar cualquier producto:**
```
curl -X DELETE http://localhost:8080/api/products/10 \
  -u admin@email.com:adminpass
```

### Referencia de Endpoints

## Endpoints de Usuarios
```
| Método   | Endpoint          | Descripción                 | Auth  | Rol 
-------------------------------------------------------------------------
| `POST`   | `/api/users`      | Registrar nuevo usuario     | ❌ No | - 
| `GET`    | `/api/users`      | Listar todos los usuarios   | ✅ Sí | ADMIN 
| `GET`    | `/api/users/me`   | Ver tu propio perfil        | ✅ Sí | USER 
| `GET`    | `/api/users/{id}` | Ver usuario por ID          | ✅ Sí | USER (solo propio) / ADMIN 
| `PUT`    | `/api/users/{id}` | Actualizar usuario          | ✅ Sí | USER (solo propio) / ADMIN 
| `DELETE` | `/api/users/{id}` | Eliminar usuario            | ✅ Sí | ADMIN 
```
## Endpoints de Productos
```
| Método   | Endpoint                    | Descripción                | Auth |  Rol 
---------------------------------------------------------------------------------------
| `POST`   | `/api/products`             | Crear producto             | ✅ Sí|    USER 
| `GET`    | `/api/products`             | Listar todos los productos | ✅ Sí|    ADMIN
| `GET`    | `/api/products/my-products` | Ver tus productos          | ✅ Sí|    USER
| `GET`    | `/api/products/{id}`        | Ver producto por ID        | ✅ Sí|    USER
| `PUT`    | `/api/products/{id}`        | Actualizar producto        | ✅ Sí|    USER (solo propio) / ADMIN
| `DELETE` | `/api/products/{id}`        | Eliminar producto          | ✅ Sí|    USER (solo propio) / ADMIN
```
###  Reglas de Seguridad
### Control de Acceso

**Usuario normal (USER):**
-  Puede ver su propio perfil
-  Puede actualizar su propio perfil
-  Puede crear productos
-  Puede ver sus propios productos
-  Puede actualizar sus propios productos
-  Puede eliminar sus propios productos
-  No puede ver otros usuarios
-  No puede ver todos los productos del sistema
-  No puede modificar productos de otros usuarios

**Administrador (ADMIN):**
-  Puede hacer todo lo que hace un USER
-  Puede ver todos los usuarios del sistema
-  Puede ver todos los productos del sistema
-  Puede modificar cualquier producto
-  Puede eliminar cualquier producto
-  Puede eliminar cualquier usuario

### Validaciones Automáticas

El sistema valida automáticamente:

**Para usuarios:**
- Nombre: obligatorio, entre 2 y 100 caracteres
- Email: obligatorio, formato válido, único en el sistema
- Contraseña: obligatoria (se encripta automáticamente)

**Para productos:**
- Nombre: obligatorio, entre 2 y 200 caracteres
- Precio: obligatorio, no puede ser negativo

**Ejemplo de error de validación:**
```json
{
  "timestamp": "2025-12-31T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "name": "El nombre debe tener entre 2 y 100 caracteres",
    "email": "El email debe tener un formato válido",
    "price": "El precio no puede ser negativo"
  }
}
```

### Tests Implementados
### Tests de Controladores (Integración)

**UserControllerTest:**
- Crear usuario sin autenticación (registro público)
- Listar usuarios como ADMIN
- Impedir listar usuarios como USER (403 Forbidden)
- Impedir acceso sin autenticación (401 Unauthorized)
- Ver usuario por ID con autenticación
- Actualizar usuario autenticado
- Eliminar usuario como ADMIN
- Impedir eliminar usuario como USER

**ProductControllerTest:**
- Crear producto con autenticación
- Impedir crear producto sin autenticación
- Listar productos como ADMIN
- Impedir listar productos como USER
- Ver producto por ID
- Actualizar producto con autenticación
- Eliminar producto con autenticación
- Impedir eliminar sin autenticación

### Tests de Servicios (Unitarios)

**UserServiceManagerTest:**
- Obtener lista de usuarios
- Buscar usuario por ID existente
- Lanzar excepción cuando usuario no existe
- Guardar usuario con encriptación de contraseña
- Actualizar campos de usuario
- Actualizar contraseña con encriptación
- Eliminar usuario existente
- Lanzar excepción al eliminar usuario inexistente

**ProductServiceManagerTest:**
- Obtener lista de productos
- Buscar producto por ID
- Control de acceso: USER solo ve sus productos
- Control de acceso: ADMIN ve todos los productos
- Guardar producto con usuario asociado
- Guardar producto sin usuario
- Validar que usuario existe al crear producto
- Actualizar campos de producto
- Control de acceso al actualizar
- Eliminar producto con validación de acceso
- ADMIN puede eliminar cualquier producto

### Cobertura de Tests

Los tests cubren:
- Casos de éxito (happy path)
- Casos de error (validaciones, no encontrado)
- Seguridad (autenticación y autorización)
- Lógica de negocio (encriptación, validaciones)

### Códigos de Error

 `200 OK` Éxito  Operación exitosa 
 `204 No Content` Éxito sin contenido  Eliminación exitosa 
 `400 Bad Request` Datos inválidos  Validaciones fallidas 
 `401 Unauthorized` Sin autenticación  Credenciales no proporcionadas o inválidas 
 `403 Forbidden` Sin permisos  Intentas acceder a recursos de otros usuarios 
 `404 Not Found` Recurso no existe  ID de usuario o producto no encontrado 
 `500 Internal Server Error` Error del servidor | Error inesperado (raro) 

### Estructura del Proyecto

```
src/
├── main/
│   ├── java/net/miPrimerCRUD/app/CRUD/
│   │   ├── config/
│   │   │   └── SecurityConfig.java          # Configuración de seguridad y roles
│   │   ├── controllers/
│   │   │   ├── UserController.java          # Endpoints de usuarios
│   │   │   └── ProductController.java       # Endpoints de productos
│   │   ├── DTO/
│   │   │   ├── UserDTO.java                 # DTO para exponer usuarios
│   │   │   └── ProductDTO.java              # DTO para exponer productos
│   │   ├── entities/
│   │   │   ├── User.java                    # Entidad Usuario (tabla users)
│   │   │   └── Product.java                 # Entidad Producto (tabla products)
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java  # Manejo global de errores
│   │   ├── mapper/
│   │   │   ├── UserMapper.java              # Conversión User <-> UserDTO
│   │   │   └── ProductMapper.java           # Conversión Product <-> ProductDTO
│   │   ├── repositories/
│   │   │   ├── UserRepository.java          # Acceso a datos de usuarios
│   │   │   └── ProductRepository.java       # Acceso a datos de productos
│   │   ├── services/
│   │   │   ├── UserService.java             # Interfaz de servicio de usuarios
│   │   │   ├── UserServiceManager.java      # Lógica de negocio de usuarios
│   │   │   ├── ProductService.java          # Interfaz de servicio de productos
│   │   │   ├── ProductServiceManager.java   # Lógica de negocio de productos
│   │   │   └── CustomUserDetailsService.java # Autenticación personalizada
│   │   └── CrudApplication.java             # Clase principal
│   └── resources/
│       └── application.properties           # Configuración de la aplicación
└── test/
    └── java/net/miPrimerCRUD/app/CRUD/
        ├── controllers/
        │   ├── UserControllerTest.java      # Tests de integración de usuarios
        │   └── ProductControllerTest.java   # Tests de integración de productos
        └── services/
            ├── UserServiceManagerTest.java  # Tests unitarios de usuarios
            └── ProductServiceManagerTest.java # Tests unitarios de productos
```

### Solución de Problemas

### Error: "Access denied for user 'root'@'localhost'"
- Verifica que las credenciales en `application.properties` sean correctas
- Asegúrate de que MySQL esté ejecutándose

### Error: "Table 'crud.users' doesn't exist"
- Verifica que `spring.jpa.hibernate.ddl-auto=update` esté en `application.properties`
- Reinicia la aplicación para que cree las tablas automáticamente

### Error: 401 Unauthorized en todas las peticiones
- Verifica que estés enviando las credenciales correctas con `-u`
- Formato: `-u email:contraseña`

### Error: 403 Forbidden
- Estás intentando acceder a un recurso que no te pertenece
- Verifica que tu rol sea el adecuado (ADMIN para ver todos los recursos)

---
