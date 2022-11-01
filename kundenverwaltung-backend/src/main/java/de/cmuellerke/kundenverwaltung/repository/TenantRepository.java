package de.cmuellerke.kundenverwaltung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.kundenverwaltung.models.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

}
