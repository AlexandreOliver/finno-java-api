package api.financas.infrastructure.persistence.jpa;

import api.financas.infrastructure.persistence.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JPARepositoryAccount  extends JpaRepository<AccountEntity, UUID> { }
