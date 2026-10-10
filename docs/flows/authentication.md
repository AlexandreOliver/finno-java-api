# Fluxo de autenticação

Os endpoints implementados pelo `AuthController` estão sob `/api/v1/auth`.

## Cadastro

`POST /api/v1/auth/register`

1. A API valida nome, email e senha.
2. Converte o email para o objeto de valor `Email`, que remove espaços nas extremidades e normaliza para minusculas.
3. `RegistrationUseCase` verifica se o email já existe e aplica hash na senha antes de salvar o usuário e cria uma conta.
4. A resposta contem identificador; a senha não é retornada.

## Login

`POST /api/v1/auth/login`

1. A API valida email e senha e solicita a autenticação ao `AuthenticationManager`.
2. Se as credenciais forem válidas, `JwtTokenProvider` emite um JWT assinado com HS256 e `RefreshTokenService` cria um refresh token opaco.
3. A resposta retorna `accessToken`, `refreshToken`, `tokenType` e `expiresIn`.
4. A validade do access token é configurável por `JWT_EXPIRATION_SECONDS` (padrão: 15 minutos); a do refresh token é configurável por `JWT_REFRESH_EXPIRATION_SECONDS` (padrão: 30 dias).

## Renovação

`POST /api/v1/auth/refresh`

1. O cliente envia o refresh token atual no corpo JSON como `refreshToken`.
2. A API compara o hash SHA-256 com os registros persistidos.
3. Se válido, revoga o token apresentado e emite um novo refresh token e access token. A expiração absoluta da sessão não é estendida.
4. Um refresh token revogado ou expirado não pode ser renovado. A reutilização de um token revogado revoga a família da sessão.

## Logout

`POST /api/v1/auth/logout`

O cliente envia o refresh token da sessão no corpo JSON. A API revoga todos os refresh tokens ativos dessa família. Access tokens já emitidos continuam válidos até expirarem.

## Acesso autenticado

Para rotas protegidas, o cliente deve enviar o token no cabeçalho `Authorization`, usando o esquema `Bearer`. O filtro JWT valida a assinatura e a expiração e estabelece a identidade da requisição. A aplicação usa sessão stateless.


## Referências no codigo

- `infrastructure/http/controllers/AuthController.java`
- `application/usecases/RegistrationUseCase.java`
- `infrastructure/security/SecurityConfiguration.java`
- `infrastructure/security/JwtTokenProvider.java`
- `infrastructure/security/JWTRequestFilter.java`
- `infrastructure/security/RefreshTokenService.java`
