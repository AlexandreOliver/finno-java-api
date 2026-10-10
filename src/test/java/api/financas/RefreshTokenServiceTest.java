package api.financas;

import api.financas.infrastructure.persistence.entities.RefreshTokenEntity;
import api.financas.infrastructure.persistence.entities.UserEntity;
import api.financas.infrastructure.persistence.jpa.JPARepositoryRefreshToken;
import api.financas.infrastructure.security.JwtTokenProvider;
import api.financas.infrastructure.security.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
  @Mock
  private JPARepositoryRefreshToken refreshTokenRepository;

  @Mock
  private JwtTokenProvider jwtTokenProvider;

  private RefreshTokenService refreshTokenService;

  @BeforeEach
  void setUp() {
    refreshTokenService = new RefreshTokenService(refreshTokenRepository, jwtTokenProvider, 3600L);
  }

  @Test
  void issuePersistsOnlyHashOfRefreshToken() {
    UserEntity user = user();
    when(refreshTokenRepository.save(any(RefreshTokenEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(jwtTokenProvider.generateToken(user)).thenReturn("access-token");

    RefreshTokenService.TokenPair tokens = refreshTokenService.issue(user);

    ArgumentCaptor<RefreshTokenEntity> savedToken = ArgumentCaptor.forClass(RefreshTokenEntity.class);
    verify(refreshTokenRepository).save(savedToken.capture());
    assertEquals("access-token", tokens.accessToken());
    assertNotEquals(tokens.refreshToken(), savedToken.getValue().getTokenHash());
    assertEquals(user, savedToken.getValue().getUser());
    assertTrue(savedToken.getValue().getExpiresAt().isAfter(savedToken.getValue().getCreatedAt()));
  }

  @Test
  void rotateRevokesUsedTokenAndCreatesReplacementInSameFamily() {
    UserEntity user = user();
    RefreshTokenEntity current = activeToken(user);
    when(refreshTokenRepository.findByTokenHashForUpdate(anyString())).thenReturn(Optional.of(current));
    when(refreshTokenRepository.save(any(RefreshTokenEntity.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(jwtTokenProvider.generateToken(user)).thenReturn("new-access-token");

    Optional<RefreshTokenService.TokenPair> result = refreshTokenService.rotate("old-refresh-token");

    assertTrue(result.isPresent());
    assertEquals("new-access-token", result.get().accessToken());
    assertNotEquals("old-refresh-token", result.get().refreshToken());
    assertNotNull(current.getRevokedAt());

    ArgumentCaptor<RefreshTokenEntity> savedTokens = ArgumentCaptor.forClass(RefreshTokenEntity.class);
    verify(refreshTokenRepository, times(2)).save(savedTokens.capture());
    RefreshTokenEntity replacement = savedTokens.getAllValues().get(1);
    assertEquals(current.getFamilyId(), replacement.getFamilyId());
    assertEquals(current.getExpiresAt(), replacement.getExpiresAt());
    assertEquals(replacement.getTokenHash(), current.getReplacedByTokenHash());
  }

  @Test
  void replayingRevokedTokenRevokesItsFamily() {
    RefreshTokenEntity revoked = activeToken(user());
    revoked.setRevokedAt(Instant.now());
    when(refreshTokenRepository.findByTokenHashForUpdate(anyString())).thenReturn(Optional.of(revoked));
    when(refreshTokenRepository.revokeActiveFamily(any(UUID.class), any(Instant.class))).thenReturn(1);

    assertTrue(refreshTokenService.rotate("replayed-token").isEmpty());

    verify(refreshTokenRepository).revokeActiveFamily(eq(revoked.getFamilyId()), any(Instant.class));
    verify(refreshTokenRepository, never()).save(any(RefreshTokenEntity.class));
  }

  private static UserEntity user() {
    UserEntity user = new UserEntity();
    user.setId(UUID.randomUUID());
    user.setEmail("person@example.com");
    return user;
  }

  private static RefreshTokenEntity activeToken(UserEntity user) {
    Instant now = Instant.now();
    RefreshTokenEntity token = new RefreshTokenEntity();
    token.setId(UUID.randomUUID());
    token.setTokenHash("existing-hash");
    token.setUser(user);
    token.setFamilyId(UUID.randomUUID());
    token.setCreatedAt(now.minusSeconds(30));
    token.setExpiresAt(now.plusSeconds(3600));
    return token;
  }
}
