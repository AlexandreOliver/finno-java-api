package api.financas.domain;

import api.financas.enums.TransactionType;
import api.financas.domain.valueobject.Money;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
public class TransactionDomain {
  private UUID id;
  private String description;
  private TransactionType type;
  private String category;
  private Money amount;
  private UUID accountId;
  private Instant paidAt;

  public TransactionDomain(UUID id,
                           @NonNull String description,
                           @NonNull TransactionType type,
                           @NonNull String category,
                           @NonNull Money amount,
                           @NonNull UUID accountId,
                           @NonNull Instant paidAt) {

    if (amount.isNegative())
      throw new IllegalArgumentException("O amount nao pode ser negativo.");

    if (Objects.equals(amount.value(), BigDecimal.ZERO))
      throw new IllegalArgumentException("O amount nao pode ser zero.");

    this.id = id;
    this.description = description;
    this.type = type;
    this.category = category;
    this.amount = amount;
    this.accountId = accountId;
    this.paidAt = paidAt;
  }
}
