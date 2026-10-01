# Entidade `Account`

Representa uma conta associada a um user.

## Atributos

| Atributo    | Tipo no dominio | Persistencia   | Observacoes                              |
|-------------|-----------------|----------------|------------------------------------------|
| `id`        | `UUID`          | `uuid`         | Gerado na persistencia.                  |
| `label`     | `String`        | `varchar(100)` | Obrigatorio.                             |
| `ownerId`   | `UUID`          | `uuid`         | Obrigatorio.                             |
| `balance`   | `Money`         | `Integer`      | Valor monetario, persistido em centavos. |
| `createdAt` | `Instant`       | `timestamp`    | Data de criacao.                         |
| `updatedAt` | `Instant`       | `timestamp`    | Data de atualizacao.                     |

## Regras e comportamento atual

- Os valores monetários serão salvos em centavos, por isso balance é do tipo Integer no banco de dados.

## Persistência e codigo

- Tabela: `account`, criada em `src/main/resources/db/migration/V2__create-accounts-table.sql`.
- Modelo de domínio: `src/main/java/api/financas/domain/entities/Account.java`.
- Mapeamento JPA: `src/main/java/api/financas/infrastructure/persistence/entities/AccountEntity.java`.
