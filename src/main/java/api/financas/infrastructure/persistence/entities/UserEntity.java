package api.financas.infrastructure.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Size(max = 100)
  @NotNull
  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Size(max = 120)
  @NotNull
  @Column(name = "email", nullable = false, length = 120)
  private String email;

  @Size(max = 72)
  @NotNull
  @Column(name = "password", nullable = false, length = 72)
  private String password;

  @NotNull
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @NotNull
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @OneToMany(mappedBy = "ownerId",  cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
  public Set<AccountEntity> accounts;

  @Override
  public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public @NonNull String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {return this.password;}

}