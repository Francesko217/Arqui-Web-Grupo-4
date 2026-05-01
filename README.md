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

jwt.secret=TuTrade#Grupo4@UPC-2024$xK9mP2qLrB7nZ
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

### Puntos de encuentro (Meeting Points)

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/meeting-points` | Participante del trade | Proponer lugar y hora |
| GET | `/meeting-points` | ADMIN | Listar todos |
| GET | `/meeting-points/trade/{tradeId}` | Participante o ADMIN | Ver el punto de encuentro de un trade |
| PUT | `/meeting-points/{id}` | Participante del trade | Actualizar lugar u hora |
| DELETE | `/meeting-points/{id}` | Participante o ADMIN | Eliminar |

Reglas:
- Solo se puede crear o modificar si el trade está en `PENDING`
- Solo el proposer, el receiver o un ADMIN pueden eliminar
- No se puede eliminar si el trade está en `ACCEPTED` (sirve como evidencia histórica)
- Un trade tiene como máximo un punto de encuentro
- El objeto `meetingPoint` aparece embebido en el response de `/trades` cuando existe

### Valoraciones (Ratings)

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/ratings` | Autenticado | Valorar al otro participante de un trade ACCEPTED |
| GET | `/ratings` | ADMIN | Listar todas las valoraciones |
| GET | `/ratings/user/{userId}` | Público | Valoraciones recibidas por un usuario |
| GET | `/ratings/trade/{tradeId}` | Partes o ADMIN | Valoraciones de un trade |

Reglas:
- Solo se puede valorar si el trade está en estado `ACCEPTED`
- Cada parte valora al otro (proposer → receiver, receiver → proposer)
- No se puede valorar dos veces en el mismo trade
- `score`: entero entre 1 y 5

### Chat y mensajes

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/chats/trade/{tradeId}` | Participante o ADMIN | Obtener el chat de un trade |
| GET | `/chats` | ADMIN | Listar todos los chats |
| GET | `/chats/{chatId}/messages` | Participante o ADMIN | Listar mensajes del chat (orden cronológico) |
| POST | `/messages` | Participante del trade | Enviar un mensaje |
| PUT | `/messages/{messageId}/read` | Receptor del mensaje | Marcar mensaje como leído |

Reglas:
- El chat se crea automáticamente al crear el trade
- Solo proposer y receiver pueden ver y escribir en el chat (ADMIN puede leer cualquier conversación)
- El sender se deduce del JWT — nunca se envía en el body
- `statusMessage`: `SENT` → `READ`
- Solo el receptor puede marcar un mensaje como leído (no el propio emisor)
- El `chatId` aparece embebido en el response de `/trades` para evitar una petición extra

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
       ├── Rating (como rater — quien valora)
       └── Profile

Trade
 ├── Trade_Item (side 1 = proposer, side 2 = receiver)
 │     └── Item
 ├── Rating (valoraciones generadas tras ACCEPTED)
 ├── MeetingPoint (punto de encuentro pactado, uno por trade)
 └── Chat (creado automáticamente con el trade)
       └── Message (mensajes del chat)

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
│   ├── ChatController.java
│   ├── ItemController.java
│   ├── MeetingPointController.java
│   ├── RatingController.java
│   ├── RoleController.java
│   ├── TradeController.java
│   └── UserController.java
├── DTOs/
│   ├── ChatResponseDTO.java
│   ├── ItemRequestDTO.java
│   ├── ItemResponseDTO.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── MeetingPointRequestDTO.java
│   ├── MeetingPointResponseDTO.java
│   ├── MessageRequestDTO.java
│   ├── MessageResponseDTO.java
│   ├── RatingRequestDTO.java
│   ├── RatingResponseDTO.java
│   ├── TradeRequestDTO.java
│   ├── TradeResponseDTO.java
│   └── UserResponseDTO.java
├── Entities/
│   ├── Category.java
│   ├── Chat.java
│   ├── Item.java
│   ├── MeetingPoint.java
│   ├── Message.java
│   ├── Profile.java
│   ├── Rating.java
│   ├── Role.java
│   ├── Trade.java
│   ├── Trade_item.java
│   └── User.java
├── Repositories/
│   ├── ICategoryRepository.java
│   ├── IChatRepository.java
│   ├── IItemRepository.java
│   ├── IMeetingPointRepository.java
│   ├── IMessageRepository.java
│   ├── IRatingRepository.java
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
│   ├── IChatService.java
│   ├── IItemService.java
│   ├── IMeetingPointService.java
│   ├── IRatingService.java
│   ├── IRoleService.java
│   ├── ITradeService.java
│   └── IUserService.java
├── ServicesImplements/
│   ├── CategoryServiceImplement.java
│   ├── ChatServiceImplement.java
│   ├── ItemServiceImplement.java
│   ├── MeetingPointServiceImplement.java
│   ├── RatingServiceImplement.java
│   ├── RoleServiceImplement.java
│   ├── TradeServiceImplement.java
│   └── UserServiceImplement.java
└── TuTradeApplication.java
```

---

## Pendiente

| Feature | Descripción |
|---|---|
| Perfiles | Información adicional del usuario |

La entidad `Profile` ya existe. Falta implementar servicio y controlador.
