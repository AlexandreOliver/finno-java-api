package api.financas.infrastructure.security;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  private final JWTRequestFilter jwtRequestFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .accessDeniedHandler(((request, response, accessDeniedException) -> {
              response.setStatus(HttpStatus.FORBIDDEN.value());
            }))
        )
        .authorizeHttpRequests(authorize -> authorize
            .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
            .anyRequest().authenticated())
        .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//  @Bean
//  public SecretKey jwtSecretKey(@Value("${security.jwt.secret}") String secret) {
//    byte[] decodedSecret = Base64.getDecoder().decode(secret);
//    if (decodedSecret.length < 32) {
//      throw new IllegalArgumentException("security.jwt.secret deve conter pelo menos 256 bits em Base64");
//    }
//    return new SecretKeySpec(decodedSecret, "HmacSHA256");
//  }

//  @Bean
//  public JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
//    return new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(jwtSecretKey));
//  }
//
//  @Bean
//  public JwtDecoder jwtDecoder(SecretKey jwtSecretKey) {
//    return NimbusJwtDecoder.withSecretKey(jwtSecretKey)
//        .macAlgorithm(MacAlgorithm.HS256)
//        .build();
//  }

//  @Bean
//  public Clock clock() {
//    return Clock.systemUTC();
//  }
}
