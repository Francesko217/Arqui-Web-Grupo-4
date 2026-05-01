# TuTrade

Plataforma de intercambio de artículos entre usuarios (trueques). API REST desarrollada con Spring Boot y PostgreSQL.

---

## Stack

| Tecnología | Versión |
|---|---|
| Java | 17 |
| Spring Boot | 3.2.5 |
| Spring Security | 6.x |
| PostgreSQL | - |
| JPA / Hibernate | (incluido en Spring Boot) |
| JWT (jjwt) | 0.11.5 |
| ModelMapper | 3.2.0 |
| SpringDoc / Swagger | 2.5.0 |

---

## Requisitos previos

- Java 17+
- Maven
- PostgreSQL corriendo en `localhost`

---

## Configuración

Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost/bdTuTrade
spring.datasource.username=postgres
spring.datasource.password=<tu_password>

jwt.secret=tutrade-clave-secreta-muy-segura-2024-upc
jwt.expiration=86400000
```

Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE "bdTuTrade";
```

Hibernate crea las tablas automáticamente al arrancar (`ddl-auto=update`).

---

## Ejecutar el proyecto

```bash
mvn spring-boot:run
```

Al arrancar, `DataInitializer` siembra automáticamente (solo si la BD está vacía):
- Roles: `ADMIN` y `CLIENT`
- Usuario admin: `biney-debug@tutrade.com` / `biney`

---

## Documentación interactiva (Swagger)

```
http://localhost:8080/swagger-ui/index.html
```

---

## Autenticación

El sistema usa JWT stateless. Flujo:

1. `POST /auth/login` con `{ "emailUser": "...", "password": "..." }`
2. La respuesta incluye un `token`
3. En cada request protegida, enviar el header:
   ```
   Authorization: Bearer <token>
   ```
El token expira en **24 horas**.

---

## Endpoints

### Autenticación

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/auth/login` | Público | Login, devuelve JWT |

### Usuarios

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/users` | Público | Registro (siempre asigna rol CLIENT) |
| GET | `/users` | ADMIN | Listar todos |
| GET | `/users/{id}` | Autenticado | Buscar por ID |
| DELETE | `/users/{id}` | ADMIN | Eliminar usuario |

### Categorías

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/categories` | Público | Listar categorías |
| GET | `/categories/{id}` | Público | Buscar por ID |
| POST | `/categories` | ADMIN | Crear categoría |
| PUT | `/categories/{id}` | ADMIN | Actualizar |
| DELETE | `/categories/{id}` | ADMIN | Eliminar |

### Ítems

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/items` | Autenticado | Listar todos |
| GET | `/items/{id}` | Autenticado | Buscar por ID |
| GET | `/items/user/{userId}` | Autenticado | Ítems de un usuario |
| GET | `/items/category/{categoryId}` | Autenticado | Ítems por categoría |
| GET | `/items/status/{status}` | Autenticado | Ítems por estado |
| POST | `/items` | Autenticado | Crear ítem (owner = usuario del token) |
| PUT | `/items/{id}` | Dueño o ADMIN | Actualizar |
| DELETE | `/items/{id}` | Dueño o ADMIN | Eliminar |

Estados del ítem: `1` = Disponible, `2` = Pausado, `3` = Intercambiado

### Trades (Trueques)

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/trades` | Autenticado | Proponer un trueque |
| GET | `/trades` | ADMIN | Listar todos |
| GET | `/trades/my` | Autenticado | Mis trades (como proposer o receiver) |
| GET | `/trades/{id}` | Partes o ADMIN | Ver detalle |
| PUT | `/trades/{id}/accept` | Receiver | Aceptar |
| PUT | `/trades/{id}/reject` | Receiver | Rechazar |
| PUT | `/trades/{id}/cancel` | Proposer, Receiver o ADMIN | Cancelar |

Estados del trade: `PENDING` → `ACCEPTED` / `REJECTED` / `CANCELLED`

### Roles

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET/POST/PUT/DELETE | `/roles` | ADMIN | CRUD de roles |

---

## Flujo de un trade

```
1. Usuario A propone:
   POST /trades
   {
     "receiverId": 2,
     "proposerItemIds": [1, 2],
     "receiverItemIds": [3, 4]
   }

   Validaciones:
   - A y B son usuarios distintos
   - Items 1, 2 pertenecen a A y tienen status=1
   - Items 3, 4 pertenecen a B y tienen status=1
   - Ningún item está en otro trade PENDING

2. Trade creado con status PENDING

3. Usuario B decide:
   PUT /trades/{id}/accept  → items pasan a status=3, trade = ACCEPTED
   PUT /trades/{id}/reject  → trade = REJECTED
   PUT /trades/{id}/cancel  → trade = CANCELLED (también puede A o ADMIN)
```

---

## Modelo de datos

```
Role
 └── User
       ├── Item
       ├── Trade (como proposer)
       ├── Trade (como receiver)
       └── Profile

Trade
 └── Trade_Item (side 1 = proposer, side 2 = receiver)
       └── Item

Category (jerarquía con parent_idCategory)
 └── Item
```

---

## Estructura del proyecto

```
src/main/java/pe/edu/upc/tutrade/
├── Config/
│   ├── DataInitializer.java       # Seed de roles y admin al arrancar
│   └── ModelMapperConfig.java
├── Controllers/
│   ├── AuthController.java
│   ├── CategoryController.java
│   ├── ItemController.java
│   ├── RoleController.java
│   ├── TradeController.java
│   └── UserController.java
├── DTOs/
│   ├── ItemRequestDTO.java
│   ├── ItemResponseDTO.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── TradeRequestDTO.java
│   ├── TradeResponseDTO.java
│   └── UserResponseDTO.java
├── Entities/
│   ├── Category.java
│   ├── Chat.java
│   ├── Item.java
│   ├── Meeting_point.java
│   ├── Message.java
│   ├── Profile.java
│   ├── Rating.java
│   ├── Role.java
│   ├── Trade.java
│   ├── Trade_item.java
│   └── User.java
├── Repositories/
│   ├── ICategoryRepository.java
│   ├── IItemRepository.java
│   ├── IRoleRepository.java
│   ├── ITradeItemRepository.java
│   ├── ITradeRepository.java
│   └── IUserRepository.java
├── Security/
│   ├── JwtFilter.java
│   ├── JwtUtil.java
│   ├── SecurityConfig.java
│   └── UserDetailsServiceImpl.java
├── ServicesInterfaces/
│   ├── ICategoryService.java
│   ├── IItemService.java
│   ├── IRoleService.java
│   ├── ITradeService.java
│   └── IUserService.java
├── ServicesImplements/
│   ├── CategoryServiceImplement.java
│   ├── ItemServiceImplement.java
│   ├── RoleServiceImplement.java
│   ├── TradeServiceImplement.java
│   └── UserServiceImplement.java
└── TuTradeApplication.java
```

---

## Pendiente

| Feature | Descripción |
|---|---|
| Ratings | Valorar al otro usuario al completar un trade |
| Meeting points | Coordinar lugar y hora del intercambio |
| Chat | Mensajería dentro de un trade |
| Perfiles | Información adicional del usuario |

Las entidades para estas features ya existen. Falta implementar servicio y controlador.
