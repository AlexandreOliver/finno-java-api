package api.financas.infrastructure.persistence.jpa;

import api.financas.domain.entities.Account;
import api.financas.domain.entities.User;
import api.financas.domain.interfaces.IUserRepository;
import api.financas.domain.valueobject.Email;
import api.financas.infrastructure.persistence.AccountMapper;
import api.financas.infrastructure.persistence.UserMapper;
import api.financas.infrastructure.persistence.entities.AccountEntity;
import api.financas.infrastructure.persistence.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserRepository implements IUserRepository {
  private final UserMapper userMapper;
  private final AccountMapper accountMapper;
  private final JPARepositoryUser jpaRepositoryUser;

  @Override
  public User save(User user) {
    UserEntity entity;

    if (user.getId() == null) {
      entity = new UserEntity();
    } else {
      entity = jpaRepositoryUser
          .findById(user.getId())
          .orElseGet(UserEntity::new);
    }

    userMapper.mapDomainToEntity(user, entity);

    UserEntity userEntity = jpaRepositoryUser.save(entity);

    return userMapper.toDomain(userEntity);
  }

  @Override
  public User createUserWithAccount(User user, Account account) {
    UserEntity userEntity = new UserEntity();
    AccountEntity accountEntity = new AccountEntity();

    accountMapper.mapDomainToEntity(account, accountEntity);
    userMapper.mapDomainToEntity(user, userEntity);

    userEntity.addAccount(accountEntity);

    UserEntity saved = this.jpaRepositoryUser.save(userEntity);

    return userMapper.toDomain(saved);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return jpaRepositoryUser.findById(id).map(userMapper::toDomain);
  }

  @Override
  public void delete(UUID id) {

    jpaRepositoryUser.deleteById(id);
  }

  @Override
  public List<User> findAll() {

    List<UserEntity> users = jpaRepositoryUser.findAll();

    return users.stream().map(userMapper::toDomain).toList();
  }

  @Override
  public boolean existsByEmail(Email email) {
    return this.jpaRepositoryUser.existsByEmail(email.value());
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    return jpaRepositoryUser.findByEmail(email.value()).map(userMapper::toDomain);
  }
}
