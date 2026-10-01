# Entidade `Category`

Representa uma categoria usada para classificar movimentações financeiras. A entidade é associada a transações, que possuem uma categoria obrigatória.

## Atributos

| Atributo      | Tipo no domínio | Persistência   | Observações                           |
|---------------|-----------------|----------------|---------------------------------------|
| `id`          | `UUID`          | `uuid`         | Identificador gerado na persistência. |
| `label`       | `String`        | `varchar(50)`  | Obrigatório.                          |
| `description` | `String`        | `varchar(200)` | Obrigatória.                          |

## Regras e comportamento atual

- `label` e `description` são obrigatórios no banco.
- A entidade JPA define limites de tamanho para ambos os campos.
- A categoria é referenciada por `Transaction`; cada transação persistida exige uma categoria (`category_id` não pode ser nulo).
- O modelo de domínio não contém regras ou validações próprias para os campos.

## Persistência e código

- Tabela: `categories`, criada em `src/main/resources/db/migration/V3__create_categories_table.sql`.
- Modelo de domínio: `src/main/java/api/financas/domain/entities/Category.java`.
- Entidade JPA: `src/main/java/api/financas/infrastructure/persistence/entities/CategoryEntity.java`.
- Relacionamento com transação: `src/main/java/api/financas/infrastructure/persistence/entities/TransactionEntity.java`.
