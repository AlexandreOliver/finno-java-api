package api.financas.domain;

import api.financas.domain.valueobject.Money;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class AccountDomain {
  private UUID id;
  private String label;
  private UUID ownerId;
  private Money balance;
  private Instant createdAt;
  private Instant updatedAt;
}
