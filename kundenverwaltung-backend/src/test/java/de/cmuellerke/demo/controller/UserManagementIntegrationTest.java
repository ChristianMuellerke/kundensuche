package de.cmuellerke.demo.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import de.cmuellerke.demo.WebIntegrationTest;
import de.cmuellerke.demo.common.UserLogin;
import de.cmuellerke.demo.data.dto.ApplicationUser;
import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.security.JwtTokenUtil;
import de.cmuellerke.demo.tenancy.TenantContext;

@WebIntegrationTest
@AutoConfigureWebTestClient(timeout = "PT5M")
public class UserManagementIntegrationTest implements WithAssertions {
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

    @Autowired
    JwtTokenUtil jwtProvider;
    
    private String jwt;

    @BeforeEach
    public void obtainJwtToken() throws IOException, InterruptedException, URISyntaxException {
    	TenantContext.setTenantId(DEFAULT_TENANT);
    	ApplicationUser applicationUser = new ApplicationUser(DEFAULT_USER, DEFAULT_PASSWORD, DEFAULT_TENANT, new ArrayList<>());
		this.jwt = "Bearer " + jwtProvider.generateToken(applicationUser);
    }

	@Test
	public void shouldCreateUser() throws Exception {
		UserDTO newUser = UserDTO.builder()
				.userName("newUserName")
				.password(PASSWORD)
				.tenant(DEFAULT_TENANT)
				.build();
		
		UserDTO createdUser = createUser(newUser, jwt);
		assertThat(createdUser.getUserName()).isEqualTo(newUser.getUserName());
	}

	private UserDTO createUser(UserDTO user, String jwt) {
		return this.webTestClient//
				.put()//
				.uri(USERS_CREATE)//
				.header(AUTHORIZATION_HEADER, jwt)
				.body(BodyInserters.fromValue(user))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(UserDTO.class)//
				.returnResult()//
				.getResponseBody();
	}

	
//	@Test
//	@Disabled
//	public void shouldNotCreateUserWhichHasNoPassword() throws Exception {
//		this.webTestClient//
//				.put()//
//				.uri(USERS_CREATE)//
//				.body(BodyInserters.fromValue(UserDTO.builder().userName("newUserName").build()))//
//				.accept(MediaType.APPLICATION_JSON)//
//				.exchange()//
//				.expectStatus().isBadRequest();
//	}
//
//	@Test
//	@Disabled
//	public void shouldNotCreateUserWhichHasNoUsername() throws Exception {
//		this.webTestClient//
//				.put()//
//				.uri(USERS_CREATE)//
//				.body(BodyInserters.fromValue(UserDTO.builder().password(PASSWORD).build()))//
//				.accept(MediaType.APPLICATION_JSON)//
//				.exchange()//
//				.expectStatus().isBadRequest();
//	}
//
//	@Test
//	@Disabled
//	public void shouldUpdateUser() throws Exception {
//		UserDTO newUser = UserDTO.builder().userName("UpdateUser1-LN").password(PASSWORD).build();
//		UserDTO createdUser = createUser(newUser, jwt);
//
//		createdUser.setUserName("changedUserName");
//
//		UserDTO updatedUser = updateUser(createdUser, jwt);
//
//		assertThat(updatedUser.getUserName()).isEqualTo("changedUserName");
//	}

//	private UserDTO updateUser(UserDTO user, String jwt) {
//		return this.webTestClient//
//				.post()//
//				.uri(USERS + "/" + user.getId())//
//				.header(AUTHORIZATION_HEADER, jwt)
//				.body(BodyInserters.fromValue(user))//
//				.accept(MediaType.APPLICATION_JSON)//
//				.exchange()//
//				.expectStatus().isOk() //
//				.expectBody(UserDTO.class)//
//				.returnResult()//
//				.getResponseBody();
//	}

}
