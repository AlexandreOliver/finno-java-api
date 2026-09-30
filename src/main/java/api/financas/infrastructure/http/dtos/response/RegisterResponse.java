package api.financas.infrastructure.http.dtos.response;

import java.util.UUID;

public record RegisterResponse(UUID id, String name, String email) {
}
