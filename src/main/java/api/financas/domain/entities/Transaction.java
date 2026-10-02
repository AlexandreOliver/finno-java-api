package api.financas.domain.entities;

import api.financas.domain.enums.TransactionType;
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
public class Transaction {
  private UUID id;
  private String description;
  private TransactionType type;
  private Category category;
  private Money amount;
  private Account account;
  private Instant paidAt;

  public Transaction(UUID id,
                     @NonNull String description,
                     @NonNull TransactionType type,
                     @NonNull Category category,
                     @NonNull Money amount,
                     @NonNull Account account,
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
    this.account = account;
    this.paidAt = paidAt;
  }
}
