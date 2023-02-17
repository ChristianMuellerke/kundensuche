package de.cmuellerke.kundenverwaltung.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import de.cmuellerke.kundenverwaltung.models.RefreshToken;
import de.cmuellerke.kundenverwaltung.models.UserEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);

	@Modifying
	int deleteByUser(UserEntity user);
}
