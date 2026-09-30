package api.financas.infrastructure.security;

import api.financas.application.auth.JWTUserData;
import api.financas.infrastructure.persistence.entities.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Component
public class JwtTokenProvider {
  private final long expirationSeconds;
  private final Algorithm algorithm;

  public JwtTokenProvider(
      @Value("${security.jwt.secret}")
      String secret,
      @Value("${security.jwt.expiration-seconds:3600}")
      long expirationSeconds
      ) {

    this.expirationSeconds = expirationSeconds;
    this.algorithm = Algorithm.HMAC256(secret);
  }


  public String generateToken(Authentication authentication) {

    UserEntity user = (UserEntity) Objects.requireNonNull(authentication.getPrincipal());

    return JWT.create()
        .withIssuer("finno-api")
        .withClaim("userId", user.getId().toString())
        .withSubject(user.getEmail())
        .withExpiresAt(Instant.now().plusSeconds(expirationSeconds))
        .withIssuedAt(Instant.now())
        .sign(algorithm);

  }

  public Optional<JWTUserData> getClaims(String token) {

    if (!validateToken(token)) return Optional.empty();

    try {

      DecodedJWT jwtDecodded = JWT.decode(token);

      JWTUserData JWTData = JWTUserData.builder()
          .userId(jwtDecodded.getClaim("userId").asString())
          .email(jwtDecodded.getSubject())
          .issuer(jwtDecodded.getIssuer())
          .build();

      return Optional.of(JWTData);

    } catch (JWTDecodeException exception) {
        return Optional.empty();
    }

  }

  public boolean validateToken(String token) {
    try {
      JWT.require(algorithm).build().verify(token);

      return true;
    } catch (JWTVerificationException exception) {
      return false;
    }
  }

  public String getUsername(String token) {

    if (!validateToken(token)) return null;

    return JWT.decode(token).getSubject();
  }
}
