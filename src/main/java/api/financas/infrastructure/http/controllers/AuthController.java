package api.financas.infrastructure.http.controllers;

import api.financas.application.usecases.RegistrationUseCase;
import api.financas.application.user.dtos.UserCreateDTO;
import api.financas.domain.valueobject.Email;
import api.financas.infrastructure.http.dtos.request.LoginRequest;
import api.financas.infrastructure.http.dtos.request.RegisterRequest;
import api.financas.infrastructure.http.dtos.response.AuthTokenResponse;
import api.financas.infrastructure.http.dtos.response.RegisterResponse;
import api.financas.infrastructure.persistence.entities.UserEntity;
import api.financas.infrastructure.security.JwtTokenProvider;
import api.financas.infrastructure.security.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final RegistrationUseCase registrationUseCase;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public RegisterResponse signup(@Valid @RequestBody RegisterRequest request) {

    var userData = new UserCreateDTO(
        request.name(),
        Email.of(request.email()),
        request.password()
    );

    var user = registrationUseCase.execute(userData);

    return new RegisterResponse(user.getId());
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody LoginRequest request) {

    UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());

    Authentication authentication = authenticationManager.authenticate(userAndPass);

    if (!(authentication.getPrincipal() instanceof UserEntity user)) {
      throw new IllegalStateException("Authenticated principal is not a persisted user");
    }

    var tokens = refreshTokenService.issue(user);

    var cookie = ResponseCookie.from("refresh_token", tokens.refreshToken())
        .path("/api/v1/auth/refresh")
        .httpOnly(true)
        .secure(true)
        .maxAge(refreshTokenService.getRefreshExpirationSeconds())
        .sameSite("strict")
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(tokenResponse(tokens));
  }

  @GetMapping("/refresh")
  public ResponseEntity<?> refresh(@CookieValue(name = "refresh_token") String refreshToken) {

    var tokenAndRefresh = refreshTokenService.rotate(refreshToken);

    if (tokenAndRefresh.isEmpty()) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("message", "Refresh token invalido ou expirado"));
    }

    var cookie = ResponseCookie.from("refresh_token", tokenAndRefresh.get().refreshToken())
        .path("/api/v1/auth/refresh")
        .httpOnly(true)
        .secure(true)
        .maxAge(refreshTokenService.getRefreshExpirationSeconds())
        .sameSite("strict")
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(tokenResponse(tokenAndRefresh.get()));
  }

  @GetMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token",  required = false) String refreshToken) {

    refreshTokenService.revoke(refreshToken);

    var cookie = ResponseCookie.from("refresh_token", "")
        .path("/api/v1/auth/refresh")
        .httpOnly(true)
        .secure(true)
        .maxAge(0)
        .sameSite("strict")
        .build();

    return ResponseEntity.noContent()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .build();
  }

  private AuthTokenResponse tokenResponse(RefreshTokenService.TokenPair tokens) {
    return new AuthTokenResponse(
        tokens.accessToken(),
        tokens.refreshToken(),
        "Bearer",
        jwtTokenProvider.getExpirationSeconds()
    );
  }
}
