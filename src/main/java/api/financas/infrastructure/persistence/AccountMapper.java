package api.financas.infrastructure.persistence;

import api.financas.domain.entities.Account;
import api.financas.domain.valueobject.Money;
import api.financas.infrastructure.persistence.entities.AccountEntity;
import api.financas.infrastructure.persistence.entities.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapper {

  private final EntityManager em;

  public AccountEntity toEntity(Account account) {
    AccountEntity entity = new AccountEntity();

    if (account.getId() != null) {
      entity.setId(account.getId());
    }

    if (account.getOwnerId() != null) {
      try {
        entity.setOwner(
            em.getReference(UserEntity.class, account.getOwnerId())
        );
      } catch (EntityNotFoundException e) {
        throw new RuntimeException("A conta informada pertence a um usuário que não existe: " + account.getOwnerId());
      }

    }

    entity.setLabel(account.getLabel());
    entity.setBalance(account.getBalance().inCents());
    entity.setCreatedAt(account.getCreatedAt());
    entity.setUpdatedAt(account.getUpdatedAt());

    return entity;
  }

  public Account toDomain(AccountEntity entity) {
    Account.AccountBuilder acc = Account.builder();

    acc.id(entity.getId());
    acc.label(entity.getLabel());
    acc.ownerId(entity.getOwner().getId());
    acc.balance(Money.fromDb(entity.getBalance()));
    acc.createdAt(entity.getCreatedAt());
    acc.updatedAt(entity.getUpdatedAt());

    return acc.build();
  }

  public void mapDomainToEntity(Account sourceAccount, AccountEntity targetEntity) {

    targetEntity.setLabel(sourceAccount.getLabel());
    targetEntity.setBalance(sourceAccount.getBalance().inCents());
    targetEntity.setCreatedAt(sourceAccount.getCreatedAt());
    targetEntity.setUpdatedAt(sourceAccount.getUpdatedAt());

  }

}
