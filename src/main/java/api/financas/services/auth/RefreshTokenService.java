package api.financas.services.auth;

import api.financas.entity.RefreshTokenEntity;
import api.financas.entity.UserEntity;
import api.financas.persistence.RefreshTokenRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();
  private static final int TOKEN_BYTES = 32;

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtTokenProvider jwtTokenProvider;
  @Getter private final long refreshExpirationSeconds;

  public RefreshTokenService(
      RefreshTokenRepository refreshTokenRepository,
      JwtTokenProvider jwtTokenProvider,
      @Value("${security.jwt.refresh-expiration-seconds:2592000}") long refreshExpirationSeconds
  ) {
    if (refreshExpirationSeconds <= 0) {
      throw new IllegalArgumentException("Refresh token expiration must be positive");
    }

    this.refreshTokenRepository = refreshTokenRepository;
    this.jwtTokenProvider = jwtTokenProvider;
    this.refreshExpirationSeconds = refreshExpirationSeconds;
  }

  @Transactional
  public TokenPair issue(UserEntity user) {

    RefreshTokenEntity token = new RefreshTokenEntity();

    Instant now = Instant.now();
    Instant expiresAt = now.plusSeconds(refreshExpirationSeconds);
    String rawRefreshToken = generateRawToken();
    UUID familyId = UUID.randomUUID();

    token.setId(UUID.randomUUID());
    token.setTokenHash(hashToken(rawRefreshToken));
    token.setUser(user);
    token.setFamilyId(familyId);
    token.setCreatedAt(now);
    token.setExpiresAt(expiresAt);

    refreshTokenRepository.save(token);

    String accessToken = jwtTokenProvider.generateToken(user);

    return new TokenPair(accessToken, rawRefreshToken);
  }

  @Transactional
  public Optional<TokenPair> rotate(String rawRefreshToken) {

    String tokenReceivedHashed = hashToken(rawRefreshToken);

    Optional<RefreshTokenEntity> found = refreshTokenRepository.findByTokenHashForUpdate(tokenReceivedHashed);

    if (found.isEmpty()) {
      return Optional.empty();
    }

    RefreshTokenEntity current = found.get();

    Instant now = Instant.now();
    if (current.getRevokedAt() != null) {
      refreshTokenRepository.revokeActiveFamily(current.getFamilyId(), now);
      return Optional.empty();
    }

    if (!current.getExpiresAt().isAfter(now)) {
      current.setRevokedAt(now);
      refreshTokenRepository.save(current);
      return Optional.empty();
    }

    String nextRawRefreshToken = generateRawToken();
    String nextTokenHash = hashToken(nextRawRefreshToken);
    current.setRevokedAt(now);
    current.setReplacedByTokenHash(nextTokenHash);
    refreshTokenRepository.save(current);

    RefreshTokenEntity next = new RefreshTokenEntity();
    next.setId(UUID.randomUUID());
    next.setTokenHash(nextTokenHash);
    next.setUser(current.getUser());
    next.setFamilyId(current.getFamilyId());
    next.setCreatedAt(now);
    next.setExpiresAt(current.getExpiresAt());
    refreshTokenRepository.save(next);

    return Optional.of(new TokenPair(
        jwtTokenProvider.generateToken(current.getUser()),
        nextRawRefreshToken
    ));
  }

  @Transactional
  public void revoke(String rawRefreshToken) {
    refreshTokenRepository.findByTokenHashForUpdate(hashToken(rawRefreshToken))
        .ifPresent(token -> refreshTokenRepository.revokeActiveFamily(token.getFamilyId(), Instant.now()));
  }

  private static String generateRawToken() {
    byte[] token = new byte[TOKEN_BYTES];
    SECURE_RANDOM.nextBytes(token);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
  }

  private static String hashToken(String token) {
    try {
      byte[] digest = MessageDigest.getInstance("SHA-256")
          .digest(token.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(digest);
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 is not available", exception);
    }
  }

  public record TokenPair(String accessToken, String refreshToken) {
  }
}
