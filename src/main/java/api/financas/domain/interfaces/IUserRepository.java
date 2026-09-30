package api.financas.domain.interfaces;

import api.financas.domain.entities.User;
import api.financas.domain.valueobject.Email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {
  User save(User user);
  void delete(UUID id);
  List<User> findAll();
  boolean existsByEmail(Email email);
  Optional<User> findByEmail(Email email);
}
