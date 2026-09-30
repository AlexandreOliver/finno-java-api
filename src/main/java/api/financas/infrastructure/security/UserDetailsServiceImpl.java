package api.financas.infrastructure.security;

import api.financas.infrastructure.persistence.jpa.JPARepositoryUser;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final JPARepositoryUser jpaRepositoryUser;

  @Override
  public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    return jpaRepositoryUser.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
