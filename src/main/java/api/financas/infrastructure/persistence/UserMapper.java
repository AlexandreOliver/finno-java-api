package api.financas.infrastructure.persistence;

import api.financas.domain.entities.User;
import api.financas.domain.valueobject.Email;
import api.financas.domain.valueobject.PasswordHash;
import api.financas.infrastructure.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  public User toDomain(UserEntity entity) {
    User.UserBuilder user = User.builder();

    user.id(entity.getId());
    user.name(entity.getName());
    user.email(Email.of(entity.getEmail()));
    user.password(PasswordHash.of(entity.getPassword()));
    user.createdAt(entity.getCreatedAt());
    user.updatedAt(entity.getUpdatedAt());


    return user.build();
  }

  public void mapDomainToEntity(User sourceUser, UserEntity targetEntity) {

    targetEntity.setName(sourceUser.name);
    targetEntity.setEmail(sourceUser.email.value());
    targetEntity.setPassword(sourceUser.password.value());
    targetEntity.setCreatedAt(sourceUser.createdAt);
    targetEntity.setUpdatedAt(sourceUser.updatedAt);

  }
}

