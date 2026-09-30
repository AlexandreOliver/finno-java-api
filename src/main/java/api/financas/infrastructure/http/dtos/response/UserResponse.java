package api.financas.infrastructure.http.dtos.response;

import api.financas.domain.entities.User;

import java.util.UUID;

public record UserResponse(UUID id, String name, String email) {
  public static UserResponse from(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail().value());
  }
}
