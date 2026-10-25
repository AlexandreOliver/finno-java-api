# ADR 0005: Arquitetura em camadas simplificada

- Status: Adotada

## Contexto

A organização anterior separava domínio, aplicação e infraestrutura com abstrações inspiradas em Clean Architecture. Para o escopo e o ritmo atual do projeto, essa separação trouxe indireções e decisões arquiteturais que dificultavam implementar funcionalidades sem benefício proporcional.

## Decisão

Organizar o fluxo principal da aplicação em camadas simples:

`Controller → Service → Repository → Entity`

- Controllers tratam HTTP e validam/recebem DTOs de requisição.
- Services coordenam os casos de uso e as regras de negócio.
- Repositories fazem o acesso à persistência.
- Entities representam os dados persistidos com JPA.
- DTOs definem os contratos de entrada e saída da API; entities não devem ser expostas diretamente nas respostas HTTP.
- Value objects e modelos de domínio separados podem ser usados quando encapsularem regras que realmente se beneficiem dessa separação. Não são obrigatórios para cada entidade ou operação.

A estrutura pode evoluir quando problemas concretos justificarem mais abstrações.

## Consequências

- O fluxo entre HTTP, regra de aplicação e persistência fica direto, com menos interfaces e mapeamentos obrigatórios.
- Services podem ficar mais acoplados ao Spring e às entities JPA; manter DTOs nas fronteiras HTTP reduz o acoplamento do contrato público com o banco.
- Regras de negócio precisam permanecer nos services ou em objetos de domínio apropriados, evitando controllers com lógica ou repositories responsáveis por regras de negócio.
- Novas camadas, interfaces ou patterns devem ser introduzidos quando resolverem uma necessidade observável, como regras complexas, duplicação ou dificuldade de teste.

## Referências

- Estrutura atual em `src/main/java/api/financas/`
- [README: arquitetura](../../README.md#arquitetura)
