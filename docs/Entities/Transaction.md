# Entidade `Transaction`

Representa uma movimentação financeira executada.

## Atributos

| Atributo      | Tipo no domínio   | Persistência | Observações                                                             |
|---------------|-------------------|--------------|-------------------------------------------------------------------------|
| `id`          | `UUID`            | uuid         | Identificador da movimentação.                                          |
| `description` | `String`          | varchar(200) | Descrição da movimentação.                                              |
| `type`        | `TypeTransaction` | varchar      | `SAIDA` para despesa ou `ENTRADA` para receita.                         |
| `amount`      | `Money`           | Integer      | Valor encapsulado pelo value object `Money`.                            |
| `account`     | `Account`         | account_id   | Conta à qual a movimentação está associada.                             |
| `paidAt`      | `Instant`         | timestamp    | Data da execução no formato `YYYY-MM-DDTHH:MM:SS`; não pode ser futura. |
| `category`    | `Category`        | category_id  | Categoria da transação.                                                 |

## Regras e comportamento definido

- Receitas e despesas são representadas pela mesma entidade e diferenciadas por `type`.
- Os tipos definidos são `SAIDA` e `ENTRADA`.
- O valor não é negativo e é representado no domínio por `Money`, persistido em centavos.
- A movimentação pertence a uma [Account](./Account.md).
- `paidAt` representa uma transação executada e não pode estar no futuro.
- A tabela `transactions` deve conter apenas transações executadas; agendamento não está incluído nessa decisão.

## Persistência e código

- Tabela `transactions`: `src/main/resources/db/migration/V4__create_transactions_table.sql`
- Modelo de domínio: `src/main/java/api/financas/domain/entities/Transaction.java`.
- Tipos de movimentação: `src/main/java/api/financas/domain/enums/TransactionType.java`.
- Mapeamento JPA: `src/main/java/api/financas/infrastructure/persistence/entities/TransactionEntity.java`
