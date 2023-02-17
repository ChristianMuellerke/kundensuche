package de.cmuellerke.kundenverwaltung.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.kundenverwaltung.models.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByUsernameAndTenantId(String username, String tenantId);

	Boolean existsByUsernameAndTenantId(String username, String tenantId);

	Boolean existsByEmail(String email);
	
	List<UserEntity> findByTenantId(String tenantId);
	
	Page<UserEntity> findByTenantId(String tenantId, Pageable pageable);
}
