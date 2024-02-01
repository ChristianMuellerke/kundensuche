package de.cmuellerke.poc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.cmuellerke.poc.entity.CustomerEntity;
import de.cmuellerke.poc.payload.CustomerDTO;
import de.cmuellerke.poc.repository.CustomerRepository;
import de.cmuellerke.poc.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public List<CustomerDTO> bulkImport(List<CustomerDTO> newCustomers) {

        log.debug("start bulk import of customers - Size=" + newCustomers.size());

        List<CustomerEntity> customersToBeCreated = newCustomers.stream().map(customerDTO -> {

            CustomerEntity newCustomer = CustomerEntity.builder()
                    .familyname(customerDTO.getFamilyname())
                    .forename(customerDTO.getForename())
                    .fullname(customerDTO.getForename() + " " + customerDTO.getFamilyname())
                    .build();

            return newCustomer;
        }).collect(Collectors.toList());

        List<CustomerEntity> newCreatedCustomers = customerRepository.saveAll(customersToBeCreated);

        log.debug("end of bulk import - Size=" + newCreatedCustomers.size());

        return newCreatedCustomers.stream().map(this::toCustomerDTO).collect(Collectors.toList());
    }

    @Transactional
    public CustomerDTO save(CustomerDTO customerDTO) {

        Optional<CustomerEntity> foundCustomer = Optional.empty();

        if (customerDTO.getId() != null) {
            foundCustomer = customerRepository.findByCustomerIdAndTenantId(UUID.fromString(customerDTO.getId()), TenantContext.getTenantId());
        }

        CustomerEntity customerToSave = foundCustomer.map(customer -> {
            customer.setFamilyname(null);
            customer.setForename(customerDTO.getForename());
            customer.setFullname(customerDTO.getForename() + " " + customerDTO.getFamilyname());
            return customer;
        }).orElseGet(() -> {
            CustomerEntity newCustomer = CustomerEntity.builder()
                    .familyname(customerDTO.getFamilyname())
                    .forename(customerDTO.getForename())
                    .fullname(customerDTO.getForename() + " " + customerDTO.getFamilyname())
                    .build();

            return newCustomer;
        });

        CustomerEntity savedCustomer = customerRepository.save(customerToSave);

        log.debug("[{}] customer {} on tenant {} saved", TenantContext.getTenantId(), savedCustomer.getCustomerId(), savedCustomer.getTenantId());

        return toCustomerDTO(savedCustomer);
    }

    @Transactional
    public List<CustomerDTO> save(List<CustomerDTO> customers) {

    	List<CustomerDTO> savedCustomers = new ArrayList<CustomerDTO>();
    	
    	customers.forEach(customer -> {
    		CustomerDTO savedCustomer = this.save(customer);
    		
    		savedCustomers.add(savedCustomer);
    	});
    	
    	return savedCustomers;
    }

    
    public Optional<CustomerDTO> find(String customerId) {
        Optional<CustomerEntity> foundCustomer = customerRepository.findByCustomerIdAndTenantId(UUID.fromString(customerId), TenantContext.getTenantId());
        return foundCustomer.map(this::toCustomerDTO);
    }

    public List<CustomerDTO> findAll() {
        List<CustomerEntity> foundCustomers = customerRepository.findAll();
        return foundCustomers.stream().map(this::toCustomerDTO).collect(Collectors.toList());
    }

    public CustomerDTO toCustomerDTO(CustomerEntity customerEntity) {
        log.debug("{} Mapping CustomerEntity to DTO, TenantId from Entity is {}", TenantContext.getTenantId(), customerEntity.getTenantId());

        return CustomerDTO.builder()
                .forename(customerEntity.getForename()) 
                .familyname(customerEntity.getFamilyname())
                .fullname(customerEntity.getFullname())
                .id(customerEntity.getCustomerId().toString()) 
                .tenantId(customerEntity.getTenantId()) 
                .build();
    }
}
