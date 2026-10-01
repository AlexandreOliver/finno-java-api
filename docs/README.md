# Documentação do Finno

Este diretório registra o comportamento atual do sistema e as decisões que orientam sua evolução. A documentação deve acompanhar o codigo: descreva o que existe como **atual** e o que ainda e ideia como **proposta**.

## Índice

- [Roadmap](Roadmap.md): etapas sugeridas, sem datas ou compromissos implícitos.
- [Fluxo de autenticacao](flows/authentication.md): registro, login e uso do token.
- [Entidade User](Entities/User.md): atributos, regras e persistência.
- [Decisoes de arquitetura](decisions/README.md): ADRs (registros de decisões).

## Como manter

- Atualize o fluxo e a entidade quando o comportamento ou o modelo mudar.
- Registre decisões importantes em um ADR quando forem tomadas; não use ADR para ideias ainda em discussão.
- No roadmap, mova itens entre **Proposto**, **Em andamento** e **Concluído**. Evite estimativas de data sem planejamento explicito.
- Prefira documentar o motivo e as consequências de uma decisão, em vez de repetir detalhes óbvios do codigo.
