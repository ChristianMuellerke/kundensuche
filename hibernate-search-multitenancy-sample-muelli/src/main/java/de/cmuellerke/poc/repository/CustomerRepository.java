package de.cmuellerke.poc.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.poc.entity.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
    public Optional<CustomerEntity> findByCustomerIdAndTenantId(UUID id, String tenantId);
}
