package api.financas.application.user;

import api.financas.domain.entities.User;
import api.financas.domain.interfaces.IUserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserQueryService {
  private final IUserRepository userRepository;

  public List<User> listarUsers() {
    return userRepository.findAll();
  }
}
