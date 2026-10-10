package api.financas.infrastructure.http.dtos.response;

public record AuthTokenResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn
) {
}
