package de.cmuellerke.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.cmuellerke.demo.data.entity.UserDAO;

public interface UserRepository extends JpaRepository<UserDAO, Long> {
	
	Optional<UserDAO> findByUserName(String userName);
}
