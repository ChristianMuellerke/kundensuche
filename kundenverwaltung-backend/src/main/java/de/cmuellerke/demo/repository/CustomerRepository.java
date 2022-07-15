package de.cmuellerke.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.cmuellerke.demo.data.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
