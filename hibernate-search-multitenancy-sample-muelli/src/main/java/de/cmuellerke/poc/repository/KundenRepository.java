package de.cmuellerke.poc.repository;

import de.cmuellerke.poc.entity.KundeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KundenRepository extends JpaRepository<KundeEntity, UUID> {
    public Optional<KundeEntity> findByCustomerIdAndTenantId(UUID id, String tenantId);
}
