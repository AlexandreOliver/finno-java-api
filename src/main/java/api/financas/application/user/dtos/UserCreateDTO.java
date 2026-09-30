package api.financas.application.user.dtos;

import api.financas.domain.valueobject.Email;

public record UserCreateDTO(
    String name,
    Email email,
    String rawPassword
    ) {
}
