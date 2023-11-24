package de.cmuellerke.poc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.cmuellerke.poc.entity.KundeEntity;

@Repository
public interface KundenRepository extends JpaRepository<KundeEntity, UUID> {

}
