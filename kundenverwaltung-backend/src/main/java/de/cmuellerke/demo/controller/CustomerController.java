package de.cmuellerke.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.demo.data.dto.CustomerDTO;
import de.cmuellerke.demo.data.dto.CustomerDTO;
import de.cmuellerke.demo.service.CustomerService;
import de.cmuellerke.demo.service.UserService;

@RestController
@RequestMapping("customers")
@Validated
public class CustomerController {
	@Autowired
	CustomerService customerService;

	@PutMapping(path = "/create")
	public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customer) {
		CustomerDTO savedCustomer = customerService.create(customer);
		return ResponseEntity.ok(savedCustomer);
	}

	@PostMapping(path = "/{customerId}")
	public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody CustomerDTO user) {
		CustomerDTO savedCustomer = customerService.update(customerId, user);
		return ResponseEntity.ok(savedCustomer);
	}

	@GetMapping(path = "/customers")
	public ResponseEntity<Iterable<CustomerDTO>> getAllCustomers() {
		Iterable<CustomerDTO> users = customerService.getAll();
		return ResponseEntity.ok(users);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public Map<String, String> handleBindExceptions(BindException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

}
