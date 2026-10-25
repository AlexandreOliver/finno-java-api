package api.financas.dtos;

import lombok.Builder;

@Builder
public record JWTUserData(String userId, String email, String issuer) {
}
