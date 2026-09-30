package api.financas.infrastructure.persistence;

import api.financas.domain.entities.User;
import api.financas.domain.valueobject.Email;
import api.financas.domain.valueobject.PasswordHash;
import api.financas.infrastructure.persistence.entities.UserEntity;

public class UserMapper {

  public static UserEntity mapper(User user) {
    UserEntity entity = new UserEntity();

    entity.setName(user.name);
    entity.setEmail(user.email.value());
    entity.setPassword(user.password.value());
    entity.setCreatedAt(user.createdAt);
    entity.setUpdatedAt(user.updatedAt);

    return entity;
  }

  public static User mapper(UserEntity entity) {
    System.out.println("Entra no mapper");
    return User.builder()
        .id(entity.getId())
        .email(Email.of(entity.getEmail()))
        .password(new PasswordHash(entity.getPassword()))
        .name(entity.getName())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

}
