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
2. Se as credenciais forem validas, `JwtTokenProvider` emite um JWT assinado com HS256.
3. O token inclui o email como subject, o identificador do usuário e a expiração configurada.
4. A resposta retorna o token.

## Acesso autenticado

Para rotas protegidas, o cliente deve enviar o token no cabeçalho `Authorization`, usando o esquema `Bearer`. O filtro JWT valida a assinatura e a expiração e estabelece a identidade da requisição. A aplicação usa sessão stateless.


## Referências no codigo

- `infrastructure/http/controllers/AuthController.java`
- `application/usecases/RegistrationUseCase.java`
- `infrastructure/security/SecurityConfiguration.java`
- `infrastructure/security/JwtTokenProvider.java`
- `infrastructure/security/JWTRequestFilter.java`
