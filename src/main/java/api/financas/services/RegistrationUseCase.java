package api.financas.services;

import api.financas.dtos.response.UserResponse;
import api.financas.entity.AccountEntity;
import api.financas.entity.UserEntity;
import api.financas.exception.DuplicatedEmailError;
import api.financas.dtos.UserCreateDTO;
import api.financas.domain.valueobject.Money;
import api.financas.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RegistrationUseCase {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.defaultNameAccount:main}")
  private String defaultNameAccount;

  @Transactional
  public UserResponse execute(UserCreateDTO userCreateDTO) {

    if (this.userRepository.existsByEmail(userCreateDTO.email().value())) {
      throw new DuplicatedEmailError("O Email fornecido ja esta registrado");
    }

    UserEntity userEntity = new UserEntity();

    userEntity.setName(userCreateDTO.name());
    userEntity.setEmail(userCreateDTO.email().value());
    userEntity.setPassword(passwordEncoder.encode(userCreateDTO.rawPassword()));
    userEntity.setCreatedAt(Instant.now());
    userEntity.setUpdatedAt(Instant.now());

    AccountEntity accountEntity = new AccountEntity();
    accountEntity.setLabel(defaultNameAccount);
    accountEntity.setBalance(new Money(BigDecimal.ZERO).inCents());
    accountEntity.setCreatedAt(Instant.now());
    accountEntity.setUpdatedAt(Instant.now());

    userEntity.addAccount(accountEntity);

    UserEntity userSaved = userRepository.save(userEntity);

    return new UserResponse(userSaved.getId());
  }
}
