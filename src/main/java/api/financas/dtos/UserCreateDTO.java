package api.financas.dtos;

public record UserCreateDTO(
    String name,
    String email,
    String rawPassword
    ) {
}
