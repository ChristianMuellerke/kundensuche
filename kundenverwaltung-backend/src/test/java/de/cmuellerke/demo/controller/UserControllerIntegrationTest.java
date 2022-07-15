package de.cmuellerke.demo.controller;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import de.cmuellerke.demo.WebIntegrationTest;
import de.cmuellerke.demo.data.dto.UserDTO;

@WebIntegrationTest
public class UserControllerIntegrationTest implements WithAssertions {

	private static final String USERS_CREATE = "/users/create";
	private static final String USERS = "/users/";
    private static final String PASSWORD = "xxx xxx xxx xxx";
	@Autowired
	WebTestClient webTestClient;

	@Test
	public void shouldCreateUser() throws Exception {
		UserDTO newUser = UserDTO.builder().userName("newUserName").password(PASSWORD).build();
		UserDTO createdUser = createUser(newUser);
		assertThat(createdUser.getUserName()).isEqualTo(newUser.getUserName());
	}

	@Test
	public void shouldNotCreateUserWhichHasNoPassword() throws Exception {
		this.webTestClient//
				.put()//
				.uri(USERS_CREATE)//
				.body(BodyInserters.fromValue(UserDTO.builder().userName("newUserName").build()))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isBadRequest();
	}

	@Test
	public void shouldNotCreateUserWhichHasNoUsername() throws Exception {
		this.webTestClient//
				.put()//
				.uri(USERS_CREATE)//
				.body(BodyInserters.fromValue(UserDTO.builder().password(PASSWORD).build()))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isBadRequest();
	}

	@Test
	public void shouldUpdateUser() throws Exception {
		UserDTO newUser = UserDTO.builder().userName("UpdateUser1-LN").password(PASSWORD).build();
		UserDTO createdUser = createUser(newUser);

		createdUser.setUserName("changedUserName");

		UserDTO updatedUser = updateUser(createdUser);

		assertThat(updatedUser.getUserName()).isEqualTo("changedUserName");
	}

	private UserDTO createUser(UserDTO user) {
		return this.webTestClient//
				.put()//
				.uri(USERS_CREATE)//
				.body(BodyInserters.fromValue(user))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(UserDTO.class)//
				.returnResult()//
				.getResponseBody();
	}

	private UserDTO updateUser(UserDTO user) {
		return this.webTestClient//
				.post()//
				.uri(USERS + "/" + user.getId())//
				.body(BodyInserters.fromValue(user))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(UserDTO.class)//
				.returnResult()//
				.getResponseBody();
	}
}
