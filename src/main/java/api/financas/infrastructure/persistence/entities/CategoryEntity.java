package api.financas.infrastructure.persistence.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class CategoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Size(min = 2, max = 50)
  @Column(nullable = false)
  private String label;

  @Size(min = 2, max = 200)
  @Column(nullable = false)
  private String description;
}
