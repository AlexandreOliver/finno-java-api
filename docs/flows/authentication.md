# Fluxo de autenticacao

Os endpoints implementados pelo `AuthController` estao sob `/api/v1/auth`.

## Cadastro

`POST /api/v1/auth/register`

1. A API valida nome, email e senha.
2. Converte o email para o objeto de valor `Email`, que remove espacos nas extremidades e normaliza para minusculas.
3. `UserService` verifica se o email ja existe e aplica hash na senha antes de salvar o usuario.
4. A resposta contem identificador, nome e email; a senha nao e retornada.

## Login

`POST /api/v1/auth/login`

1. A API valida email e senha e solicita a autenticacao ao `AuthenticationManager`.
2. Se as credenciais forem validas, `JwtTokenProvider` emite um JWT assinado com HS256.
3. O token inclui o email como subject, o identificador do usuario e a expiracao configurada.
4. A resposta retorna o token.

## Acesso autenticado

Para rotas protegidas, o cliente deve enviar o token no cabecalho `Authorization`, usando o esquema `Bearer`. O filtro JWT valida a assinatura e a expiracao e estabelece a identidade da requisicao. A aplicacao usa sessao stateless.


## Referências no codigo

- `infrastructure/http/controllers/AuthController.java`
- `application/user/UserService.java`
- `infrastructure/security/SecurityConfiguration.java`
- `infrastructure/security/JwtTokenProvider.java`
- `infrastructure/security/JWTRequestFilter.java`
