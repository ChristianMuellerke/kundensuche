package de.cmuellerke.demo.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.cmuellerke.demo.data.dto.CustomerDTO;
import de.cmuellerke.demo.data.dto.CustomerDTO;
import de.cmuellerke.demo.data.entity.Customer;
import de.cmuellerke.demo.repository.CustomerRepository;
import de.cmuellerke.demo.repository.UserRepository;

@Service
public class CustomerService {
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	private ModelMapper modelMapper;

	public CustomerDTO create(CustomerDTO customer) {
		return convertToDto(customerRepository.save(convertToEntity(customer)));
	}

	public CustomerDTO update(Long id, CustomerDTO customer) {
		Customer customerFromRepo = customerRepository.getById(id);
		modelMapper.map(customer, customerFromRepo);
		return convertToDto(customerRepository.save(customerFromRepo));
	}

	public void delete(Long customerId) {
		customerRepository.deleteById(customerId);
	}

	public Iterable<CustomerDTO> getAll() {
		return customerRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public Page<CustomerDTO> getAllPaged(Pageable pageable) {
		Page<Customer> page = customerRepository.findAll(pageable);
		return page.map(this::convertToDto);
	}

	public CustomerDTO getById(Long id) throws CustomerNotFoundException {
		Optional<Customer> queryResult = customerRepository.findById(id);
		return queryResult.map(this::convertToDto).orElseThrow(() -> new CustomerNotFoundException());
	}

	private CustomerDTO convertToDto(Customer customer) {
		CustomerDTO customerDto = modelMapper.map(customer, CustomerDTO.class);
		return customerDto;
	}

	private Customer convertToEntity(CustomerDTO customerDto) {
		Customer user = modelMapper.map(customerDto, Customer.class);
		return user;
	}
}
