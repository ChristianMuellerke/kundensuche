package de.cmuellerke.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.service.UserService;

@RestController
@RequestMapping("users")
@Validated
public class UserController {

	@Autowired
	UserService userService;

	@PutMapping(path = "/create")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO user) {
		UserDTO savedUser = userService.save(user);
		return ResponseEntity.ok(savedUser);
	}

	@PostMapping(path = "/{userId}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO user) {
		UserDTO savedUser = userService.update(userId, user);
		return ResponseEntity.ok(savedUser);
	}

	@GetMapping(path = "/users")
	public ResponseEntity<Iterable<UserDTO>> getAllUsers() {
		Iterable<UserDTO> users = userService.getAll();
		return ResponseEntity.ok(users);
	}

	
	@GetMapping(path = "/")
	public ResponseEntity<Page<UserDTO>> getAllUsersPaged(@RequestParam("page") int page,
			@RequestParam("size") int size) {
		return ResponseEntity.ok(userService.getAllPaged(PageRequest.of(page, size)));
	}

	@PostMapping(path = "/")
	public ResponseEntity<Page<UserDTO>> getAllUsersPaged2(@RequestBody Pageable pageable) {
		return ResponseEntity.ok(userService.getAllPaged(pageable));
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
