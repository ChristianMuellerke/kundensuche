package de.cmuellerke.kundenverwaltung.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.cmuellerke.kundenverwaltung.entity.KundeEntity;

public interface KundenRepository extends JpaRepository<KundeEntity, UUID> {

//	@Query("select p from customers p where email = :email")
//	KundeEntity findJpqlByEmail(String email);
//
//	@Query(value = "select * from customers p where email = :email", nativeQuery = true)
//	KundeEntity findSqlByEmail(String email);
}