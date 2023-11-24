package de.cmuellerke.kundenverwaltung.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.kundenverwaltung.models.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
}
