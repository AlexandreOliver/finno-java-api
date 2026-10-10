# ADR 0004: Rotação de refresh tokens

- Status: Adotada

## Contexto

O access token JWT tem validade limitada. Para permitir que o usuário continue autenticado sem informar suas credenciais repetidamente, é necessário renovar os tokens e também permitir revogação, por exemplo, no logout ou diante de um token comprometido.

Um refresh token mantido apenas em memória não sobrevive a reinicializações da aplicação e não funciona de forma confiável com múltiplas instâncias.

## Decisão

- Usar access tokens JWT de curta duração, configuráveis por `JWT_EXPIRATION_SECONDS` (padrão: 15 minutos).
- Usar refresh tokens opacos, gerados criptograficamente e trafegados no corpo JSON dos endpoints de autenticação.
- Persistir somente o hash SHA-256 do refresh token, associado ao usuário, à expiração, ao estado de revogação e à família da sessão.
- Definir validade absoluta de 30 dias para cada família, configurável por `JWT_REFRESH_EXPIRATION_SECONDS`. A rotação não estende esse prazo.
- Expor `POST /api/v1/auth/refresh`. Cada renovação válida revoga o token apresentado e emite novos refresh e access tokens atomicamente.
- Se um refresh token revogado for reapresentado, revogar os tokens ativos da mesma família.
- Expor `POST /api/v1/auth/logout` para revogar a família associada ao refresh token apresentado.
- Clientes web que optarem por cookies devem usar `HttpOnly`, `Secure` e `SameSite` e implementar proteção contra CSRF. A API atual usa JSON; clientes mobile devem manter o token em armazenamento seguro do sistema operacional.

## Consequências

- A renovação e a revogação dependem de armazenamento persistente, substituindo qualquer armazenamento apenas em memória.
- A rotação limita o uso continuado de um refresh token roubado; detectar sua reutilização permite revogar a sessão associada.
- A renovação exige acesso ao armazenamento persistente e bloqueio do registro para impedir que o mesmo token seja usado simultaneamente.
- Registros expirados podem ser removidos por uma política de limpeza.
- O access token continua válido até expirar, mesmo após a revogação do refresh token, a menos que seja adotada uma estratégia adicional de revogação de access tokens.

## Referências

- [ADR 0001: Autenticação stateless com JWT](0001-jwt-stateless-authentication.md)
- `src/main/java/api/financas/infrastructure/security/RefreshTokenService.java`
- `src/main/java/api/financas/infrastructure/persistence/entities/RefreshTokenEntity.java`
- `src/main/resources/db/migration/V5__create_refresh_tokens_table.sql`
- `src/main/java/api/financas/infrastructure/security/JwtTokenProvider.java`