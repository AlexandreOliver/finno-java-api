package api.financas.application.auth;

import lombok.Builder;

@Builder
public record JWTUserData(String userId, String email, String issuer) {
}
