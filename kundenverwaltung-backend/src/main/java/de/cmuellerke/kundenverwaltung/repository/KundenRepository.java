package de.cmuellerke.kundenverwaltung.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.kundenverwaltung.models.KundeEntity;

@Repository
public interface KundenRepository extends JpaRepository<KundeEntity, UUID> {
	
//	Optional<KundeEntity> findByCustomerIdAndTenantId(String kundenId, String tenantId);
//
//	Boolean existsByCustomerIdAndTenantId(String kundenId, String tenantId);
//
//	List<KundeEntity> findByTenantId(String tenantId);
//	
//	Page<KundeEntity> findByTenantId(String tenantId, Pageable pageable);

}
