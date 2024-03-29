package de.cmuellerke.kundenverwaltung.security.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.cmuellerke.kundenverwaltung.models.RefreshToken;
import de.cmuellerke.kundenverwaltung.repository.RefreshTokenRepository;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {
	@Value("${jwt.secret.expiration}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(String userId) {
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setUser(userRepository.findById(UUID.fromString(userId)).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedAt(LocalDateTime.now());

		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
		}

		return token;
	}

	@Transactional
	public int deleteByUserId(String userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(UUID.fromString(userId)).get());
	}
}
