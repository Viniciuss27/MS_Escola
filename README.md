# Sistema de Gestão Escolar — Arquitetura de Microsserviços

Sistema de gestão escolar desenvolvido sob uma arquitetura de microsserviços com Spring Cloud, com autenticação centralizada via OAuth2 + JWT e isolamento de banco de dados por serviço.

## 🏗️ Arquitetura do Sistema

O sistema segue o padrão **Database-per-Service**: cada microsserviço de negócio possui sua própria instância PostgreSQL isolada, eliminando acoplamento a nível de dados.

A comunicação externa passa obrigatoriamente por um **API Gateway** único, que valida a autenticação (JWT) e roteia as requisições. O **Eureka** cuida da descoberta de instâncias, e o **Config Server** centraliza as configurações específicas de cada serviço, buscadas de um repositório Git remoto.

```
                    ┌─────────────────┐
                    │  Config Server   │
                    │      :8888      │
                    └────────▲─────────┘
                             │
                    ┌────────┴─────────┐
                    │   Eureka Server   │
                    │      :8761       │
                    └────────▲──────────┘
                             │ registro/descoberta
        ┌────────────────────┼────────────────────┐
        │                    │                     │
Cliente ──▶│   API Gateway    │──▶│    Auth    │  │   Professor   │
        │      :8765       │   ├────────────┤  ├───────────────┤
        │                  │   │   Aluno    │  ├───────────────┤
        └──────────────────┘   ├────────────┤  │     Turma     │
                                │   Nota     │  ├───────────────┤
                                └────────────┘
```

## 🧩 Microsserviços

| Serviço | Responsabilidade | Porta (host) |
|---|---|---|
| Eureka | Service Discovery — registro e localização dos microsserviços | 8761 |
| Config Server | Configuração centralizada, buscada de repositório Git remoto | 8888 |
| API Gateway | Ponto único de entrada; roteamento via `lb://` e validação de JWT | 8765 |
| Auth | Autenticação de usuários e emissão de tokens JWT | interno |
| Professor | CRUD de professores | interno |
| Aluno | CRUD de alunos | interno |
| Turma | Gestão de turmas | interno |
| Nota | Lançamento e consulta de notas | interno |

Os microsserviços de negócio não expõem porta ao host — todo tráfego externo passa obrigatoriamente pelo API Gateway, na porta `8765`.

## 🔐 Segurança e Autenticação

A autenticação segue o modelo **OAuth2 Client Credentials + JWT**:

1. O cliente autentica-se no serviço **Auth**, informando as credenciais da aplicação (`client-id`/`client-secret`, via header) e as credenciais do usuário (`email`/`password`).
2. O Auth valida as credenciais e emite um token JWT assinado com HMAC-SHA (chave compartilhada via Config Server), contendo `email`, `userId` e `roles` como claims.
3. O **API Gateway**, atuando como *OAuth2 Resource Server* (reativo/WebFlux), valida a assinatura do token localmente em cada requisição — sem precisar chamar o Auth a cada acesso — e aplica controle de acesso por rota, com base nas roles do usuário.

### Provisionamento de acesso

Diferente de um cadastro fixo via `data.sql`, o acesso de professores e alunos é criado dinamicamente: ao cadastrar um Professor ou Aluno em seu respectivo serviço, uma chamada **Feign** é disparada para o endpoint interno `POST /auth/usuarios` no Auth, criando o usuário e associando a role correta (`ROLE_PROFESSOR` / `ROLE_ALUNO`).

Essa chamada é protegida por **Circuit Breaker com Fallback Factory** (Resilience4j + Feign): se o Auth estiver indisponível, o cadastro do Professor/Aluno segue normalmente (estratégia de "melhor esforço"), e a falha é registrada em log para criação manual do acesso posteriormente — evitando que uma instabilidade no Auth derrube o cadastro de outros domínios.

As senhas são armazenadas com hash **BCrypt**, nunca em texto puro.

## 🛠️ Stack Tecnológica

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.4.5 / Spring Cloud 2024.0.1
- **Service Discovery:** Netflix Eureka
- **Configuração centralizada:** Spring Cloud Config Server (backend Git)
- **Gateway:** Spring Cloud Gateway (reativo/WebFlux)
- **Segurança:** Spring Security + OAuth2 Resource Server + JWT (JJWT)
- **Persistência:** Spring Data JPA + PostgreSQL
- **Resiliência:** Resilience4j (Circuit Breaker) + Feign Fallback Factory
- **Comunicação entre serviços:** OpenFeign
- **Containerização:** Docker + Docker Compose

## 🚀 Como executar

### Pré-requisitos

- Docker e Docker Compose instalados
- JDK 21 (para gerar os artefatos localmente)
- Acesso ao repositório Git de configuração

### 1. Gerar os artefatos de cada serviço

Em cada pasta de microsserviço (`Eureka`, `Config_Server`, `Auth`, `Professor`, `Aluno`, `Turma`, `Nota`, `Gateway`):

```bash
./mvnw clean package -DskipTests
```

### 2. Configurar variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

```
GIT_USERNAME=seu_usuario_github
GIT_PASSWORD=seu_token_github
```

### 3. Subir toda a stack

```bash
docker-compose up --build
```

### 4. Validar

Acesse o painel do Eureka e confirme que todos os serviços estão `UP`:

```
http://localhost:8761
```

## 📡 Testando a API

### 1. Criar o primeiro usuário administrador

```
POST http://localhost:8765/auth/usuarios
Headers: client-id: name123, client-secret: secret123
Body:
{
  "name": "Admin Geral",
  "email": "admin@escola.com",
  "password": "123456",
  "roleName": "ROLE_ADMIN"
}
```

### 2. Autenticação (gerar token)

```
POST http://localhost:8765/auth/token
Headers: client-id: name123, client-secret: secret123
Body:
{
  "email": "admin@escola.com",
  "password": "123456"
}
```

### 3. Cadastrar um professor (exige ROLE_ADMIN)

```
POST http://localhost:8765/professores
Headers: Authorization: Bearer {token}
Body:
{
  "nome": "Professor Teste",
  "email": "professor@escola.com",
  "cpf": "33333333333",
  "disciplina": "Matemática",
  "senha": "123456"
}
```

### 4. Consumindo rotas protegidas

```
GET http://localhost:8765/professores
Authorization: Bearer {token}
```

## 📂 Estrutura do repositório

```
ms_escola/
├── docker-compose.yml
├── .env
├── Eureka/
├── Config_Server/
├── Auth/
├── Professor/
├── Aluno/
├── Turma/
├── Nota/
└── Gateway/
```

## 👤 Autor

Desenvolvido por Vinícius como projeto de estudo em arquitetura de microsserviços com Spring Cloud, com foco em autenticação distribuída, resiliência entre serviços e boas práticas de separação DTO/Entity.
