package de.cmuellerke.poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.poc.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

}
