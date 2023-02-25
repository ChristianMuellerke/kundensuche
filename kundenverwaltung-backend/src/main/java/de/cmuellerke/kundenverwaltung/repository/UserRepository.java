package de.cmuellerke.kundenverwaltung.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.kundenverwaltung.models.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	
	Optional<UserEntity> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	Page<UserEntity> findByTenantId(String tenantId, Pageable pageable);
}
