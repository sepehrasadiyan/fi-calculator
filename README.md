# FI Calculator

A Spring Boot 3 application for forecasting **Financial Independence (FI)** using deterministic and Monte Carlo simulations.  
Users can register, log in, and run FI projections with configurable assumptions.

---

## ✨ Features
- **User management**: Register & log in with secure JWT authentication (stored in `HttpOnly` cookies).
- **FI Calculator**: Deterministic projection plus optional Monte Carlo simulation.
- **Extensible engine design**: Easily plug in new calculation engines (Command + Strategy pattern).
- **API-first**: All APIs are fully documented with OpenAPI/Swagger.
- **Dockerized**: Easy startup with `docker compose`.

---

## ⚙️ Tech Stack
- **Java 21**, **Spring Boot 3.5**
- **Spring Security 6** (JWT + role-based auth)
- **Hibernate/JPA** with **PostgreSQL 16**
- **Swagger/OpenAPI 3** (via `springdoc-openapi`)
- **Docker Compose** (runs app + DB)
---



## 🚀 Getting Started

### 1. Clone the repo
```bash
git clone https://github.com/sepehrasadiyan/fi-calculator
cd fi-calculator
bash ./here.sh
```



## Step by Step
```mermaid
flowchart LR
  subgraph Client
    A["Swagger UI / API Client"]
  end

  subgraph App["Spring Boot 3 - FI Calculator"]
    B["AuthController<br/>/register, /token, /logout"]
    C["SecurityFilterChain<br/>+ JwtAuthenticationFilter"]
    D["RequestContextFilter<br/>(FiContextHolder)"]
    E["CalculatorController<br/>POST /api/v1/fi/calculate"]
    F["FiCalculatorOrchestrator<br/>(Strategy Selector)"]
    G["FiEngineRegistry"]
    H["MonteCarloFiEngine"]
    J["MonteCarloEngine<br/>(simulation core)"]
    K["Caffeine Cache<br/>usersById / usersByEmail"]
  end

  subgraph DB["PostgreSQL"]
    L["users"]
    M["roles"]
    N["user_roles"]

  end

  A --> B
  B --> A
  A --> C
  C --> D
  D --> E
  E --> F
  F --> G
  G --> H
  I --> J
  I --> O
  C --> K
  K --> L
  L --> N
  N --> M


```

## Diagram of Process
```mermaid
sequenceDiagram
  autonumber
  participant UI as Swagger UI / Client
  participant Sec as SecurityFilterChain + JWT Filter
  participant Ctx as RequestContextFilter (FiContextHolder)
  participant Ctrl as CalculatorController
  participant Orc as FiCalculatorOrchestrator
  participant Reg as FiEngineRegistry
  participant Eng as MonteCarloFiEngine
  participant Svc as FiCalculatorService
  participant Sim as MonteCarloEngine
  participant DB as PostgreSQL

  UI->>Sec: POST /api/v1/fi/calculate (cookie: JWT)
  Sec->>Sec: Validate JWT → set Authentication
  Sec->>Ctx: Continue filter chain
  Ctx->>DB: Load User by email (cache hit/miss)
  DB-->>Ctx: UserEntity
  Ctx->>Ctrl: Controller invoked (FiContextHolder set)
  Ctrl->>Orc: execute(FiCalcCommand)
  Orc->>Reg: get(engine=MONTE_CARLO)
  Reg-->>Orc: MonteCarloFiEngine
  Orc->>Eng: calculate(cmd)
  Eng->>Svc: calculate(user, FiRequest)
  Svc->>Sim: simulateTimeToTarget(...)
  Sim-->>Svc: SimulationResult (percentiles, prob)
  Svc-->>Eng: FiResponse (deterministic + monteCarlo)
  Eng-->>Orc: FiResponse
  Orc-->>Ctrl: FiResponse
  Ctrl-->>UI: 200 OK { ApiResponse<FiResponse> }

```

## Database diagram
```mermaid
erDiagram
  USERS ||--o{ USER_ROLES : has
  ROLES ||--o{ USER_ROLES : contains

  USERS {
    UUID id PK
    varchar email UK
    varchar password_hash
    timestamp created_at
    timestamp updated_at
  }

  ROLES {
    UUID id PK
    varchar code UK "e.g., USER, ADMIN"
    varchar name     "human friendly"
    timestamp created_at
    timestamp updated_at
  }

  USER_ROLES {
    UUID user_id FK "-> USERS.id"
    UUID role_id FK "-> ROLES.id"
    timestamp created_at
  }



```


