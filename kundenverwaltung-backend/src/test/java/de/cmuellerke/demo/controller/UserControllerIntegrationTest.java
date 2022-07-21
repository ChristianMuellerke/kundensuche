package de.cmuellerke.demo.controller;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.google.gson.Gson;

import de.cmuellerke.demo.WebIntegrationTest;
import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.security.JwtRequest;
import de.cmuellerke.demo.security.JwtResponse;
import de.cmuellerke.demo.security.JwtTokenUtil;

@WebIntegrationTest
@AutoConfigureWebTestClient(timeout = "PT5M")
public class UserControllerIntegrationTest implements WithAssertions {

	private static final String AUTHORIZE = "/authenticate";
	private static final String USERS_CREATE = "/users/create";
	private static final String USERS = "/users/";
    private static final String PASSWORD = "xxx xxx xxx xxx";

    private static final String DEFAULT_USER = "default";
    private static final String DEFAULT_PASSWORD = "default";
    private static final String DEFAULT_TENANT = "0000";
    
    @Autowired
	WebTestClient webTestClient;

	@Test
	public void shouldAuthorizeDefaultUserTenant0000() throws Exception {
		JwtRequest userToBeAuthorized = JwtRequest.builder()//
				.username(DEFAULT_USER)//
				.password(DEFAULT_PASSWORD)//
				.tenantId(DEFAULT_TENANT)//
				.build();

		JwtResponse jwt = this.webTestClient//
			.post()//
			.uri(AUTHORIZE)//
			.body(BodyInserters.fromValue(userToBeAuthorized))//
			.accept(MediaType.APPLICATION_JSON)//
			.exchange()//
			.expectStatus().isOk() //
			.expectBody(JwtResponse.class)//
			.returnResult()//
			.getResponseBody();
		
		System.out.println("JWT: " + new Gson().toJson(jwt));
		System.out.println("Token: " + jwt.getToken());

		assertThat(new JwtTokenUtil("javainuse").getUsernameFromToken(jwt.getToken())).isEqualTo(DEFAULT_USER);
	}

	@Test
	public void shouldNotAuthorizeDefaultUserTenant9999() throws Exception {
		JwtRequest userToBeAuthorized = JwtRequest.builder()//
				.username(DEFAULT_USER)//
				.password(DEFAULT_PASSWORD)//
				.tenantId("9999")//
				.build();

		this.webTestClient//
			.post()//
			.uri(AUTHORIZE)//
			.body(BodyInserters.fromValue(userToBeAuthorized))//
			.accept(MediaType.APPLICATION_JSON)//
			.exchange()//
			.expectStatus().is4xxClientError();
	}

    
	@Test
	@Disabled
	public void shouldCreateUser() throws Exception {
		UserDTO newUser = UserDTO.builder().userName("newUserName").password(PASSWORD).build();
		UserDTO createdUser = createUser(newUser);
		assertThat(createdUser.getUserName()).isEqualTo(newUser.getUserName());
	}

	@Test
	@Disabled
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
	@Disabled
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
	@Disabled
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
