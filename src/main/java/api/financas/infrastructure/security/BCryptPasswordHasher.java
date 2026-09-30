package api.financas.infrastructure.security;

import api.financas.domain.interfaces.IPasswordHasher;
import api.financas.domain.valueobject.PasswordHash;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BCryptPasswordHasher implements IPasswordHasher {
  private final PasswordEncoder bCryptPasswordEncoder;

  @Override
  public PasswordHash enconde(String rawPassword) {

    String hash = bCryptPasswordEncoder.encode(rawPassword);

    return new PasswordHash(hash);
  }

  @Override
  public boolean matches(String rawPassword, PasswordHash passwordHash) {
    return bCryptPasswordEncoder.matches(rawPassword, passwordHash.value());
  }
}
