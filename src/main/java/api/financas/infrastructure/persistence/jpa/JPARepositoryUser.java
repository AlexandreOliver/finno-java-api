package api.financas.infrastructure.persistence.jpa;

import api.financas.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface JPARepositoryUser extends JpaRepository<UserEntity, UUID> {

  boolean existsByEmail(String email);
  Optional<UserEntity> findByEmail(String email);

}
