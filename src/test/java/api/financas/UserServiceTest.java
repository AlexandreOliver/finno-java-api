package api.financas;

import api.financas.dtos.response.UserResponse;
import api.financas.entity.UserEntity;
import api.financas.exception.DuplicatedEmailError;
import api.financas.dtos.UserCreateDTO;
import api.financas.persistence.UserRepository;
import api.financas.services.UserService;
import api.financas.domain.valueobject.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  public UserRepository userRepository;

  @Mock
  public PasswordEncoder passwordHasher;

  @InjectMocks
  public UserService userService;

  @Captor
  public ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

  @Test
  public void deveCriarUsuarioComSenhaHasheada() {

    UserCreateDTO input = new UserCreateDTO("Alexandre", Email.of("alex@gmail.com"), "senha123");
    String hash = "hash-da-senha";

    when(passwordHasher.encode(input.rawPassword())).thenReturn(hash);
    when(userRepository.save(any(UserEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(userRepository.existsByEmail(any(String.class))).thenReturn(false);

    UserResponse resultado = userService.create(input);

    verify(userRepository).save(userEntityArgumentCaptor.capture());
    verify(userRepository).save(any(UserEntity.class));

    UserEntity userEntityInDb = userEntityArgumentCaptor.getValue();

    assertEquals(resultado.id(), userEntityInDb.getId());
    assertEquals(input.name(), userEntityInDb.getName());
    assertEquals(input.name(), userEntityInDb.getEmail());
    assertEquals(hash, userEntityInDb.getPassword());

  }

  @Test
  public void DaErroAoCriarUsuarioComEmailDuplicado() {
    UserCreateDTO input = new UserCreateDTO("Alexandre", Email.of("alex@gmail.com"), "senha123");

    when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

    assertThrows(DuplicatedEmailError.class, () -> userService.create(input));
  }

  @Test
  void deveDeletarUsuarioPeloId() {
    UUID id = UUID.randomUUID();

    userService.deleteById(id);

    verify(userRepository).deleteById(id);
  }
}