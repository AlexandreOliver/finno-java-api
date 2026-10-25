package api.financas.services;

import api.financas.dtos.UserCreateDTO;
import api.financas.dtos.response.UserResponse;
import api.financas.entity.UserEntity;
import api.financas.exception.DuplicatedEmailError;
import api.financas.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserResponse create(UserCreateDTO userCreateDTO) {

    if (this.userRepository.existsByEmail(userCreateDTO.email().value())) {
      throw new DuplicatedEmailError("O Email fornecido ja esta registrado");
    }

    var userEntity = new UserEntity();

    userEntity.setName(userCreateDTO.name());
    userEntity.setEmail(userCreateDTO.email().value());
    userEntity.setPassword(passwordEncoder.encode(userCreateDTO.rawPassword()));
    userEntity.setCreatedAt(Instant.now());
    userEntity.setUpdatedAt(Instant.now());

    UserEntity userSaved = userRepository.save(userEntity);

    return new UserResponse(userSaved.getId());
  }

  public void deleteById(UUID id) {
    this.userRepository.deleteById(id);
  }

}
