package de.cmuellerke.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.cmuellerke.demo.WebIntegrationTest;
import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.service.UserService;

@WebIntegrationTest
@Disabled
public class UserControllerTestWithDBBackend {

	private static final String PASSWORD = "xxx xxx xxx xxx";
	
	@Autowired
	WebTestClient webTestClient;

	@Autowired
	UserService userService;

	@Test
	public void shouldReturnUsers() throws Exception {
		// GIVEN
		List<UserDTO> users = Arrays.asList(//
				UserDTO.builder().userName("test").password(PASSWORD).build(),//
				UserDTO.builder().userName("test2").password(PASSWORD).build()//
				);
		
		users.forEach(userService::save);
		
		ParameterizedTypeReference<UserDTO> typeRef = new ParameterizedTypeReference<UserDTO>() {
		};

		// WHEN/ THEN
		// Wir brechen hier natürlich die Reaktivität auf, aber das ist mir grad egal
		List<UserDTO> returnResult = this.webTestClient//
				.get()//
				.uri("/users/users")//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.returnResult(typeRef)//
				.getResponseBody()//
				.toStream()//
				.collect(Collectors.toList());

		System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(returnResult));
	}
}
