package api.financas.infrastructure.persistence.entities;

import api.financas.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 200)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id",  nullable = false)
  private AccountEntity account;

  @Column(name = "amount", nullable = false)
  private Integer amount;

  @Column(name = "paid_at", nullable = false)
  private Instant paidAt;
}
