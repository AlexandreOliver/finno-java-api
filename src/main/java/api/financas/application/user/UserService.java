package api.financas.application.user;

import api.financas.application.user.dtos.UserCreateDTO;
import api.financas.application.exception.DuplicatedEmailError;
import api.financas.domain.entities.User;
import api.financas.domain.interfaces.IPasswordHasher;
import api.financas.domain.interfaces.IUserRepository;
import api.financas.domain.valueobject.PasswordHash;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
  private final IUserRepository userRepository;
  private final IPasswordHasher passwordHasher;

  public User createUser(UserCreateDTO userCreateDTO) {

    if (this.userRepository.existsByEmail(userCreateDTO.email())) {
      throw new DuplicatedEmailError("O Email fornecido ja esta registrado");
    }

    User.UserBuilder builder = User.builder();

    PasswordHash passwordHashed = passwordHasher.enconde(userCreateDTO.rawPassword());

    builder.name(userCreateDTO.name());
    builder.email(userCreateDTO.email());
    builder.password(passwordHashed);
    builder.createdAt(Instant.now());
    builder.updatedAt(Instant.now());

    User userSaved = userRepository.save(builder.build());

    return userSaved;
  }

  public void deleteUser(UUID id) {
    this.userRepository.delete(id);
  }

}
