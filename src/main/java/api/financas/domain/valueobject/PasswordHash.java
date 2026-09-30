package api.financas.domain.valueobject;

public record PasswordHash(String value) {
  public PasswordHash {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Forneça a hash da senha");
    }
  }

  public static PasswordHash of(String value) {
    return new PasswordHash(value);
  }
}

