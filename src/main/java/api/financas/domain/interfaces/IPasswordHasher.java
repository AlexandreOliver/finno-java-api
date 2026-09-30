package api.financas.domain.interfaces;

import api.financas.domain.valueobject.PasswordHash;

public interface IPasswordHasher {
  PasswordHash enconde(String rawPassword);

  boolean matches(String rawPassword, PasswordHash passwordHash);
}
