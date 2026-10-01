# ADR XXXX: Transferências entre contas

- Status: pendente

## Contexto



## Ideias

- Transferências
    - Entidade separada chamada `transfers`, contendo campos como `account_origin` e `account_target`.
    - Cada `transfers` terá um campo `executedAt` que será uma data no formato `YYY-MM-DDTHH:MM:SS`.
    - Não será categorizada e não terá valores negativos.
    - A tabela `transfers` no banco será apenas para transferências executadas.

## Consequências



## Referencias

- 
