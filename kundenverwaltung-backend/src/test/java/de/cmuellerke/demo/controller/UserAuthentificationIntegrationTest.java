package de.cmuellerke.demo.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.google.gson.Gson;

import de.cmuellerke.demo.WebIntegrationTest;
import de.cmuellerke.demo.common.UserLogin;
import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.security.JwtRequest;
import de.cmuellerke.demo.security.JwtResponse;
import de.cmuellerke.demo.security.JwtTokenUtil;

@WebIntegrationTest
@AutoConfigureWebTestClient(timeout = "PT5M")
public class UserAuthentificationIntegrationTest implements WithAssertions {

	private static final String AUTHORIZE = "/authenticate";
	private static final String USERS_CREATE = "/users/create";
	private static final String USERS = "/users/";
    private static final String PASSWORD = "xxx xxx xxx xxx";

    private static final String DEFAULT_USER = "default";
    private static final String DEFAULT_PASSWORD = "default";
    private static final String DEFAULT_TENANT = "0000";
    private static final String DEFAULT_TEST_JWT_SECRET = "javainuse";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    
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

		assertThat(new JwtTokenUtil(DEFAULT_TEST_JWT_SECRET).getUsernameFromToken(jwt.getToken())).isEqualTo("default");
	}

	@Test
	public void shouldAuthorizeDefaultUserTenant0001() throws Exception {
		JwtRequest userToBeAuthorized = JwtRequest.builder()//
				.username("default2")//
				.password(DEFAULT_PASSWORD)//
				.tenantId("0001")//
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

		assertThat(new JwtTokenUtil(DEFAULT_TEST_JWT_SECRET).getUsernameFromToken(jwt.getToken())).isEqualTo("default2");
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
}
