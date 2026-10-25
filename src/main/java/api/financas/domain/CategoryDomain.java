package api.financas.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CategoryDomain {
  private UUID id;
  private String label;
  private String description;
}
