package de.cmuellerke.kundenverwaltung.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.kundenverwaltung.models.KundeEntity;

public interface KundenRepository extends JpaRepository<KundeEntity, UUID> {
	
}
