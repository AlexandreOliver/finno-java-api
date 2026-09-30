# Finno

API REST para Sistema de Gestão Financeira Pessoal. Projeto de portfólio construído com Java 21 e Spring Boot.

> Esse Projeto é uma implementação em java do [finno](https://github.com/AlexandreOliver/finno)

> API com autenticação stateless por JWT, pensada como base para uma evolução multi-plataforma, incluindo aplicativo mobile.

---

## Stack

- **Linguagem:** Java 21
- **Framework:** Spring Boot 4.1.0
- **Persistência:** Spring Data JPA + PostgreSQL
- **Build:** Maven
- **Ambiente local:** Docker Compose (PostgreSQL)
- **Autenticação:** Spring Security + JWT (HS256)

---

## Arquitetura

O código atual está organizado em `domain`, `application` e `infrastructure`.

```
api.financas
 ├─ FinancasApplication.java
 ├─ application/        → Casos de Uso, Serviços e Orquestração
 ├─ domain/
 │   ├─ entities/       → modelos de domínio
 │   ├─ valueobject/    → Objetos de valor que encapsulam logicas uteis
 │   └─ interfaces/     → contratos do domínio
 └─ infrastructure/
     ├─ http/
     │   └─ controllers/  → endpoints REST (LoginController)
     └─ persistence/
         ├─ entities/   → entidades JPA
         ├─ jpa/        → acesso ao banco e implementações dos repositorios
         └─ UserMapper.java
```

### Decisões de arquitetura relevantes

- **Migrações**: Banco de dados versionado usando Flyway para garantir integridade
- **Autenticação**: o caso de uso de login depende de uma porta de geração de token; emissão e validação JWT ficam na infraestrutura.

---

## Como rodar localmente

```bash
# 1. Subir o banco de dados
docker compose up -d

# 2. Configurar variáveis de ambiente
#    - DB_USERNAME, DB_PASSWORD
#    - JWT_SECRET: chave aleatória Base64 com pelo menos 256 bits
#    - JWT_EXPIRATION_SECONDS (opcional; padrão 3600)

# 3. Rodar a aplicação
./mvnw spring-boot:run
```

A chave JWT pode ser gerada com `openssl rand -base64 32`. Nunca use uma chave de exemplo em produção. A aplicação sobe em `http://localhost:8080` (porta configurável em `application.properties`).

### Autenticação

- `POST /api/v1/auth/register` - Para criar um usuario forneça `email`, `nome` e `senha`.
- `POST /api/v1/auth/login` -  Login com `email` e `password` e retorna o accessToken.
- Envie `Authorization: Bearer <accessToken>` nas demais rotas; sem token válido elas respondem `401`.

---

## Estrutura de branches e commits

- Uma branch por módulo funcional (`feature/01-arquitetura-inicial`, `feature/02-design-banco`, ...)
- Commits seguindo [Conventional Commits](https://www.conventionalcommits.org/), escopo = módulo:
  ```
  feat(banco): cria entidade Transacao com enum TipoTransacao
  fix(servico): corrige cálculo de saldo ao excluir transação
  ```

---

## Licença

Projeto de portfólio pessoal — uso livre para fins de estudo e referência.
