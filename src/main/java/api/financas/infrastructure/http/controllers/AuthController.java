package api.financas.infrastructure.http.controllers;

import api.financas.application.usecases.RegistrationUseCase;
import api.financas.application.user.dtos.UserCreateDTO;
import api.financas.domain.valueobject.Email;
import api.financas.infrastructure.http.dtos.request.LoginRequest;
import api.financas.infrastructure.http.dtos.request.RegisterRequest;
import api.financas.infrastructure.http.dtos.response.RegisterResponse;
import api.financas.infrastructure.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final RegistrationUseCase registrationUseCase;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider JwtTokenService;

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
  public Map<String, String> login(@Valid @RequestBody LoginRequest request) {

    UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());

    Authentication authentication = authenticationManager.authenticate(userAndPass);

    String token = JwtTokenService.generateToken(authentication);

    return Map.of("token", token);
  }
}
