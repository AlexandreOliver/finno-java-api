package api.financas.infrastructure.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class AccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Size(max = 100)
  @Column(name = "label", nullable = false)
  private String label;

  @ManyToOne()
  @JoinColumn(name = "owner_id",  nullable = false)
  private UserEntity ownerId;

  @Column(name = "balance", nullable = false)
  private Integer balance;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
  private Set<TransactionEntity> transactions;

}