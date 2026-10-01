# Decisões de arquitetura

Decisões arquiteturais significativas devem ser registradas como ADR (*Architecture Decision Record*). Um ADR explica o contexto, a decisão escolhida e seus efeitos; ele nao e uma lista de tarefas.

## Registros

- [ADR 0001: Autenticacao stateless com JWT](0001-jwt-stateless-authentication.md)
- [ADR 0002: Valores monetários serão encapsulados em Value Object](0002-money-value-objects.md)

## Modelo para novas decisões

Crie um arquivo numerado, por exemplo `0002-nome-da-decisao.md`, com:

```markdown
# ADR 0002: Titulo curto

- Status: Rejeitada | Adotada | Substituida

## Contexto
Qual problema ou necessidade motivou a decisão?

## Decisão
O que foi escolhido?

## Consequências
Quais benefícios, custos e limitações decorrem da escolha?
```
