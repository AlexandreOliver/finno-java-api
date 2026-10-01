# ADR 0001: Autenticação stateless com JWT

- Status: Adotada (implementação atual)

## Contexto

A API precisa autenticar usuários sem depender de uma sessão mantida no servidor. O projeto também pretende servir diferentes clientes, incluindo um aplicativo mobile.

## Decisão

Usar Spring Security com autenticação stateless e tokens JWT assinados com HS256. O token e emitido após login bem-sucedido e enviado pelo cliente nas requisições protegidas.

## Consequências

- A API não precisa manter sessão entre requisições.
- O segredo de assinatura e uma configuração operacional e deve ser protegido; não se deve usar um valor de desenvolvimento em produção.
- O token deixa de ser aceito quando expira. Uma estratégia de renovação ou revogação ainda precisa ser definida.
- Uma solução para resolver as idas e vindas ao banco de dados para consultar as permissões em cada requisição deverá ser pensada

## Referencias

- `src/main/java/api/financas/infrastructure/security/SecurityConfiguration.java`
- `src/main/java/api/financas/infrastructure/security/JwtTokenProvider.java`
- `src/main/java/api/financas/infrastructure/security/JWTRequestFilter.java`
