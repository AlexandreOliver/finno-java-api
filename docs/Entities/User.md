# Entidade `User`

Representa uma pessoa cadastrada na API. No estado atual, a entidade e usada para cadastro e autenticacao.

## Atributos

| Atributo | Tipo no dominio | Persistencia | Observacoes |
|---|---|---|---|
| `id` | `UUID` | `uuid` | Gerado na persistencia. |
| `name` | `String` | `varchar(100)` | Obrigatorio. |
| `email` | `Email` | `varchar(120)` | Obrigatorio e unico; normalizado para minusculas pelo objeto de valor. |
| `password` | `PasswordHash` | `varchar(72)` | Armazena o hash BCrypt, nunca a senha em texto puro. |
| `createdAt` | `Instant` | `timestamp` | Data de criacao. |
| `updatedAt` | `Instant` | `timestamp` | Data de atualizacao. |

## Regras e comportamento atual

- O cadastro rejeita um email que ja esteja registrado.
- A senha e transformada em hash antes de ser persistida.
- A resposta de cadastro expoe apenas id, nome e email.
- Os campos de nome e email tem limites de tamanho definidos na requisicao e no banco.

## Persistencia e codigo

- Tabela: `users`, criada em `src/main/resources/db/migration/V1__create-users-table.sql`.
- Modelo de dominio: `src/main/java/api/financas/domain/entities/User.java`.
- Mapeamento JPA: `src/main/java/api/financas/infrastructure/persistence/entities/UserEntity.java`.
- Conversao entre dominio e persistencia: `src/main/java/api/financas/infrastructure/persistence/UserMapper.java`.
