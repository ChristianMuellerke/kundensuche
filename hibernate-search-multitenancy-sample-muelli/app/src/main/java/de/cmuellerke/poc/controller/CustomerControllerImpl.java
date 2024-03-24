package de.cmuellerke.poc.controller;

import de.cmuellerke.kundensuche.api.dto.CustomerDTO;
import de.cmuellerke.poc.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class CustomerControllerImpl implements de.cmuellerke.kundensuche.api.CustomerController {

    private final CustomerService customerService;

    @Override
    public CustomerDTO getCustomer(@PathVariable String id) {
        log.info("getting customer with id {}", id);
        Optional<CustomerDTO> foundCustomer = customerService.find(id);
        return foundCustomer.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "customer not found"));
    }

    /*
        PUT: only for known entities -> the id must been known by client
        can put a known resource as new resource
        can replace entirely a known resource (if resource exists, it will be replaced)
     */
    @Override
    public CustomerDTO replaceCustomer(@PathVariable String id, CustomerDTO customer) {
        log.info("creating customer with id {}", id);
        Optional<CustomerDTO> foundCustomer = customerService.find(id);

        foundCustomer.ifPresent(existingCustomer -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "customer already exists");
        });

        return customerService.save(customer);
    }

    /*
        POST: create a new resource
     */
    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
        log.info("creating new customer");
        return customerService.save(customer);
    }

    @Override
    public List<CustomerDTO> createCustomers(List<CustomerDTO> customers) {
        log.info("creating new customer");
        return customerService.bulkImport(customers);
    }

    @Override
    public CustomerDTO updateCustomer(@PathVariable String id, CustomerDTO customer) {
        log.info("updating customer with id {}", id);
        return customerService.save(customer);
    }

}
