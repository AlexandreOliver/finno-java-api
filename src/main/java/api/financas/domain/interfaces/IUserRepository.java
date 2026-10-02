package api.financas.domain.interfaces;

import api.financas.domain.entities.Account;
import api.financas.domain.entities.User;
import api.financas.domain.valueobject.Email;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {
  User save(User user);
  User createUserWithAccount(User user, Account account);
  Optional<User> findById(UUID id);
  void delete(UUID id);
  List<User> findAll();
  boolean existsByEmail(Email email);
  Optional<User> findByEmail(Email email);
}
