package de.cmuellerke.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.cmuellerke.demo.data.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
}
