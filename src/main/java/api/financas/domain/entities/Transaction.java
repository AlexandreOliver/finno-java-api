package api.financas.domain.entities;

import api.financas.domain.enums.TransactionType;
import api.financas.domain.valueobject.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Transaction {
  private UUID id;
  private String description;
  private TransactionType type;
  private Category category;
  private Money amount;
  private Account account;
  private Instant paidAt;
}
