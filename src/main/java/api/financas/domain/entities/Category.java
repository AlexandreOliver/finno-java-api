package api.financas.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Category {
  private UUID id;
  private String label;
  private String description;
}
