package api.financas.domain.entities;

import api.financas.domain.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Account {
  private UUID id;
  private String label;
  private UUID ownerId;
  private Money balance;
  private Instant createdAt;
  private Instant updatedAt;
}
