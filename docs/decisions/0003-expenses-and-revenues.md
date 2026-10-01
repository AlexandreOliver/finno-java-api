# ADR 0003: Receitas e Despesas

- Status: Adotada

## Contexto

É necessário modelar e caracterizar as movimentações financeiras no sistema, levando em consideração possíveis estornos e auditoria financeira.

## Decisão

   - Despesas e Receitas serão uma só entidade chamada `transactions` e serão diferenciadas por tipo: SAIDA e ENTRADA.
   - Cada `transaction` será associada a uma [Account](../Entities/Account.md).
   - Cada `transaction` terá um campo `paidAt` que será uma data no formato `YYY-MM-DDTHH:MM:SS`, não podendo ser uma data futura.
   - Não terá valores negativos, sendo mapeados para `Money`.
   - Serão Categorizadas.
   - A tabela `transactions` no banco será apenas para transações executadas.

## Consequências

 - Cálculos de balanço deverão levar em consideração o tipo de cada transaction.
 - Deve se discutir estratégias de Reembolso, Transação Recorrente e agendamento de transações.

## Referencias

- `src/main/java/api/financas/domain/entities/Transaction.java`
