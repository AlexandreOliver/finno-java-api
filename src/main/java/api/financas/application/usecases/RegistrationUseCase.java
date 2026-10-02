package api.financas.application.usecases;

import api.financas.application.exception.DuplicatedEmailError;
import api.financas.application.user.dtos.UserCreateDTO;
import api.financas.domain.entities.Account;
import api.financas.domain.entities.User;
import api.financas.domain.interfaces.IPasswordHasher;
import api.financas.domain.interfaces.IUserRepository;
import api.financas.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RegistrationUseCase {
  private final IUserRepository userRepository;
  private final IPasswordHasher passwordHasher;

  @Transactional
  public User execute(UserCreateDTO userCreateDTO) {

    if (this.userRepository.existsByEmail(userCreateDTO.email())) {
      throw new DuplicatedEmailError("O Email fornecido ja esta registrado");
    }

    Account newAccount = Account.builder()
        .label("Principal")
        .balance(new Money(BigDecimal.ZERO))
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();

    User newUser = User.builder()
        .name(userCreateDTO.name())
        .email(userCreateDTO.email())
        .password(passwordHasher.enconde(userCreateDTO.rawPassword()))
        .createdAt(Instant.now())
        .updatedAt(Instant.now())
        .build();

    return userRepository.createUserWithAccount(newUser, newAccount);
  }
}
