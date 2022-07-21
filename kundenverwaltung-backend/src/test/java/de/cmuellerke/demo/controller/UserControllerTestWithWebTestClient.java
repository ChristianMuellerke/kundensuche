package de.cmuellerke.demo.controller;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import de.cmuellerke.demo.WebIntegrationTest;
import de.cmuellerke.demo.common.UserLogin;
import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.service.UserService;

@WebIntegrationTest
@Disabled
public class UserControllerTestWithWebTestClient {

	private static final String PASSWORD = "xxx xxx xxx xxx";
	
	@MockBean
	private UserService userServiceMock;

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private UserLogin userLogin;

	private static String userToken;
	
	@BeforeAll
	public static void init() throws IOException, InterruptedException, URISyntaxException {
		userToken = new UserLogin().authenticate();
	}
	
	@Test
	public void shouldReturnUsers() throws Exception {
		// GIVEN
		List<UserDTO> users = Arrays.asList(UserDTO.builder().userName("test").password(PASSWORD).build());
		when(userServiceMock.getAll()).thenReturn(users);

		// WHEN/ THEN
		this.webTestClient//
				.get()//
				.uri("/users/users")//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBodyList(UserDTO.class)//
				.isEqualTo(users);
	}

}
