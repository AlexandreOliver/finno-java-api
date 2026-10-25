package api.financas.domain;

import api.financas.domain.valueobject.Email;
import api.financas.domain.valueobject.PasswordHash;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class UserDomain {
  public UUID id;
  public String name;
  public Email email;
  public String password;
  public Instant createdAt;
  public Instant updatedAt;
}
