# Roadmap

Este roadmap e uma proposta inicial, não um compromisso de prazo. Prioridades e escopo devem ser revistos conforme o projeto evoluir.

## Concluído

- [x] Estrutura inicial da API e persistência PostgreSQL com migrações Flyway.
- [x] Implementar autenticação baseada em Tokens JWT
- [x] Definir como valores monetários e moeda serão representados.
- [x] Criar entidade Account
- [x] Definir os conceitos e regras de negócio de receita e despesa.

## Em andamento

- [ ] Criar fluxo de criação de conta

## Proposto

- [ ] Definir a política de exclusão de dados
- [ ] Criar estratégias de Reembolso.
- [ ] Definir o formato e a politica de expiracao/renovacao dos tokens.
- [ ] Definir as primeiras permissões do sistema e criar a tabela permissions no banco de dados
- [ ] Definir os conceitos e regras de negócio de uma transferência.
- [ ] Criar estratégias de Transação Recorrente 
- [ ] Criar Agendamento de Transações.

### 3. Organizar e consultar dados financeiros

- [ ] Avaliar categorias para movimentações.
- [ ] Definir consultas de saldo e períodos, incluindo suas regras de cálculo.
- [ ] Adicionar testes para regras de negócio e isolamento dos dados por usuário.

### 4. Preparar a API para uso

- [ ] Publicar uma especificação de API (por exemplo, OpenAPI).
- [ ] Documentar configuração e execução dos ambientes.
- [ ] Definir estrategia de deploy, observabilidade e gestão de segredos.
