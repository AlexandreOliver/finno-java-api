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

## Documentação

 - [Docs](docs/README.md)

---

## Arquitetura

O fluxo principal segue uma arquitetura em camadas simples:

```text
HTTP → Controller → Service → Repository → Entity (JPA) → PostgreSQL
          ↕
         DTOs
```

- **Controllers** recebem requisições HTTP, validam DTOs e formam as respostas.
- **Services** coordenam os casos de uso e as regras de negócio.
- **Repositories** consultam e persistem dados.
- **Entities** representam o modelo de persistência JPA.
- **DTOs** definem os contratos da API; entities não devem ser retornadas diretamente.

Value objects e modelos de domínio separados são usados quando tornam uma regra mais clara ou segura, sem serem exigidos para toda funcionalidade. A estrutura atual dos pacotes está em `src/main/java/api/financas/`: `controllers`, `services`, `persistence`, `entity`, `dtos`, além de `domain` e `enums`.

Essa abordagem substitui a separação mais estrita anterior. A decisão e seus trade-offs estão registrados na [ADR 0005](docs/decisions/0005-simplified-layered-architecture.md). Outras decisões relevantes incluem:

- **Migrações**: banco de dados versionado com Flyway.
- **Autenticação**: access tokens JWT e refresh tokens rotacionados, conforme as [ADRs de autenticação](docs/decisions/README.md).

---

## Como rodar localmente

```bash
# 1. Subir o banco de dados
docker compose up -d

# 2. Configurar variáveis de ambiente
#    - DB_USERNAME, DB_PASSWORD
#    - JWT_SECRET: chave aleatória Base64 com pelo menos 256 bits
#    - JWT_EXPIRATION_SECONDS (opcional; padrão 900)
#    - JWT_REFRESH_EXPIRATION_SECONDS (opcional; padrão 2592000)

# 3. Rodar a aplicação
./mvnw spring-boot:run
```

A chave JWT pode ser gerada com `openssl rand -base64 32`. Nunca use uma chave de exemplo em produção. A aplicação sobe em `http://localhost:8080` (porta configurável em `application.properties`).

### Autenticação

- `POST /api/v1/auth/register` - Para criar um usuario forneça `email`, `nome` e `senha`.
- `POST /api/v1/auth/login` - Login com `email` e `password`; retorna access token e refresh token.
- `POST /api/v1/auth/refresh` - Rotaciona o refresh token e retorna um novo par de tokens.
- `POST /api/v1/auth/logout` - Revoga a família de refresh tokens da sessão.
- Para renovar tokens e consultar os corpos das requisições, veja o [fluxo de autenticação](docs/flows/authentication.md).
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
