package api.financas.infrastructure.persistence.jpa;

import api.financas.domain.entities.User;
import api.financas.domain.interfaces.IUserRepository;
import api.financas.domain.valueobject.Email;
import api.financas.infrastructure.persistence.UserMapper;
import api.financas.infrastructure.persistence.entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UserRepository implements IUserRepository {

  private final JPARepositoryUser jpaRepositoryUser;

  @Override
  public User save(User user) {

    UserEntity userEntity = jpaRepositoryUser.save(UserMapper.mapper(user));

    return UserMapper.mapper(userEntity);
  }

  @Override
  public void delete(UUID id) {

    jpaRepositoryUser.deleteById(id);
  }

  @Override
  public List<User> findAll() {

    List<UserEntity> users = jpaRepositoryUser.findAll();

    return users.stream().map(UserMapper::mapper).toList();
  }

  @Override
  public boolean existsByEmail(Email email) {
    return this.jpaRepositoryUser.existsByEmail(email.value());
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    return jpaRepositoryUser.findByEmail(email.value()).map(UserMapper::mapper);
  }
}
