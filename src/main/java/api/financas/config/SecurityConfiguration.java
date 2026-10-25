package api.financas.config;

import jakarta.servlet.DispatcherType;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

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
            .authenticationEntryPoint(authenticationEntryPoint())
            .accessDeniedHandler(((request, response, accessDeniedException) -> {
              response.setStatus(HttpStatus.FORBIDDEN.value());
            }))
        )
        .authorizeHttpRequests(authorize -> authorize
            .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/auth/refresh").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/auth/logout").permitAll()
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

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write(new ObjectMapper().writeValueAsString(Map.of("message", "Faça login e tente novamente.")));
    };
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
