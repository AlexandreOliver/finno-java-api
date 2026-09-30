package api.financas;

import api.financas.application.exception.DuplicatedEmailError;
import api.financas.application.user.dtos.UserCreateDTO;
import api.financas.application.user.UserService;
import api.financas.domain.entities.User;
import api.financas.domain.interfaces.IPasswordHasher;
import api.financas.domain.interfaces.IUserRepository;
import api.financas.domain.valueobject.Email;
import api.financas.domain.valueobject.PasswordHash;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  public IUserRepository userRepository;

  @Mock
  public IPasswordHasher passwordHasher;

  @InjectMocks
  public UserService userService;

  @Test
  public void deveCriarUsuarioComSenhaHasheada() {

    UserCreateDTO input = new UserCreateDTO("Alexandre", Email.of("alex@gmail.com"), "senha123");
    PasswordHash hash = new PasswordHash("hash-da-senha");

    when(passwordHasher.enconde("senha123")).thenReturn(hash);
    when(userRepository.save(any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);

    User resultado = userService.createUser(input);

    assertEquals("Alexandre", resultado.getName());
    assertEquals("alex@gmail.com", resultado.getEmail().value());
    assertEquals(hash, resultado.getPassword());

    verify(passwordHasher).enconde("senha123");
    verify(userRepository).save(any(User.class));
  }

  @Test
  public void DaErroAoCriarUsuarioComEmailDuplicado() {
    UserCreateDTO input = new UserCreateDTO("Alexandre", Email.of("alex@gmail.com"), "senha123");

    when(userRepository.existsByEmail(any(Email.class))).thenReturn(true);

    assertThrows(DuplicatedEmailError.class, () -> userService.createUser(input));

  }

  @Test
  void deveDeletarUsuarioPeloId() {
    UUID id = UUID.randomUUID();

    userService.deleteUser(id);

    verify(userRepository).delete(id);
  }
}