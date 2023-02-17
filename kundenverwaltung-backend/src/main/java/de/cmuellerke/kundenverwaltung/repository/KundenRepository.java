package de.cmuellerke.kundenverwaltung.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.kundenverwaltung.models.KundeEntity;

@Repository
public interface KundenRepository extends JpaRepository<KundeEntity, Long> {
	
	Optional<KundeEntity> findByCustomerIdAndTenantId(String kundenId, String tenantId);

	Boolean existsByCustomerIdAndTenantId(String kundenId, String tenantId);

	List<KundeEntity> findByTenantId(String tenantId);
	
	Page<KundeEntity> findByTenantId(String tenantId, Pageable pageable);

}
