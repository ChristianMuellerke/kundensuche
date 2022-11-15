package de.cmuellerke.kundenverwaltung;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.reactive.server.MockServerConfigurer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.server.WebFilter;

import com.google.gson.GsonBuilder;

import de.cmuellerke.kundenverwaltung.models.ERole;
import de.cmuellerke.kundenverwaltung.payload.request.LoginRequest;
import de.cmuellerke.kundenverwaltung.payload.request.SignupRequest;
import de.cmuellerke.kundenverwaltung.payload.request.TokenRefreshRequest;
import de.cmuellerke.kundenverwaltung.payload.response.ErrorMessage;
import de.cmuellerke.kundenverwaltung.payload.response.JwtResponse;
import de.cmuellerke.kundenverwaltung.payload.response.MessageResponse;
import de.cmuellerke.kundenverwaltung.payload.response.TokenRefreshResponse;
import de.cmuellerke.kundenverwaltung.payload.user.UserDTO;
import de.cmuellerke.kundenverwaltung.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@WebIntegrationTest
@Slf4j
@AutoConfigureWebTestClient
public class UserManagementIntegrationTests implements WithAssertions {

	private static final String TENANT_1 = "tenant1";
	private static final String TENANT_2 = "tenant2";
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private JwtUtils jwtUtils;
	
	private static final String REGISTER = "/api/auth/signup";
	private static final String LOGIN = "/api/auth/signin";
	private static final String REFRESH_TOKEN = "/api/auth/refreshtoken";
	private static final String USERLIST_ENDPOINT = "/users/all";
	private static final String AUTHORIZATION = "Authorization";

    @BeforeEach
    public void setup() throws Exception {
    	webTestClient = MockMvcWebTestClient
    			.bindToApplicationContext(this.context)
    			.apply(springSecurity()).build();
		log.debug("server configured...");
    }
	
	@Test
	@Order(100)
	void canRegisterNewUser() {
		final String USERNAME = "user100";
		final String PASSWORD = "password100";
		final String EMAIL = "user100@unittest.de";
		final String TENANT = TENANT_1;

		doRegister(TENANT, USERNAME, PASSWORD, EMAIL);
	}

	@Test
	@Order(101)
	void canNotRegisterExistingUser() {
		final String USERNAME = "user101";
		final String PASSWORD = "password101";
		final String EMAIL = "user101@unittest.de";
		final String TENANT = TENANT_1;

		doRegister(TENANT, USERNAME, PASSWORD, EMAIL);
		
		SignupRequest signupRequest = new SignupRequest();

		signupRequest.setTenantId(TENANT);
		signupRequest.setEmail(EMAIL);
		signupRequest.setPassword(PASSWORD);
		Set<String> roles = Set.of(ERole.ROLE_USER.name());
		signupRequest.setRole(roles);
		signupRequest.setUsername(USERNAME);
		
		MessageResponse messageResponse = this.webTestClient//
				.post()//
				.uri(REGISTER)//
				.body(BodyInserters.fromValue(signupRequest ))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().is4xxClientError() //
				.expectBody(MessageResponse.class)//
				.returnResult()//
				.getResponseBody();	

		assertThat(messageResponse.getMessage()).isEqualTo("Error: Username is already taken!");
		
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(messageResponse, MessageResponse.class);
		
		log.debug("Response: {}", json);
	}

	@Test
	@Order(200)
	void canLoginIn() {
		final String USERNAME = "user200";
		final String PASSWORD = "password200";
		final String EMAIL = "user200@unittest.de";
		final String TENANT = TENANT_1;
		
		doRegister(TENANT, USERNAME, PASSWORD, EMAIL);
		doLogin(TENANT, USERNAME, PASSWORD, EMAIL);
	}

	@Test
	@Order(201)
	void cannotSignInOnWrongTenant() {
		final String USERNAME = "user201";
		final String PASSWORD = "password200";
		final String EMAIL = "user201@unittest.de";
		
		doRegister(TENANT_1, USERNAME, PASSWORD, EMAIL);
		doLogin(TENANT_1, USERNAME, PASSWORD, EMAIL);
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPassword(PASSWORD);
		loginRequest.setUsername(USERNAME);
		loginRequest.setTenantId(TENANT_2);

		
		BadCredentialsException response = this.webTestClient//
				.post()//
				.uri(LOGIN)//
				.body(BodyInserters.fromValue(loginRequest))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isUnauthorized() //
				.expectBody(BadCredentialsException.class)//
				.returnResult()//
				.getResponseBody();	
	}

	
	
	@Test
	@Order(300)
	void canRefreshNonExpiredToken() {
		final String USERNAME = "user300";
		final String PASSWORD = "password300";
		final String EMAIL = "user300@unittest.de";
		final String TENANT = TENANT_1;

		doRegister(TENANT, USERNAME, PASSWORD, EMAIL);
		
		JwtResponse firstToken = doLogin(TENANT, USERNAME, PASSWORD, EMAIL);

		log.debug("Refresh Token after Login: {}", firstToken.getRefreshToken());

		Date expirationFirstToken = jwtUtils.getExpiration(firstToken.getToken());
		
		TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(firstToken.getRefreshToken());
		
		TokenRefreshResponse tokenRefreshResponse = this.webTestClient//
				.post()//
				.uri(REFRESH_TOKEN)//
				.body(BodyInserters.fromValue(tokenRefreshRequest))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(TokenRefreshResponse.class)//
				.returnResult()//
				.getResponseBody();	

		String json = new GsonBuilder().setPrettyPrinting().create().toJson(tokenRefreshResponse, TokenRefreshResponse.class);

		Date expirationRefreshedToken = jwtUtils.getExpiration(tokenRefreshResponse.getAccessToken());

		assertThat(expirationRefreshedToken.after(expirationFirstToken));
		
		log.info("Response: {}", json);
	}

	@Test
	@Order(310)
	void canNotRefreshNonExistentToken() {
		TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest("123");
		
		ErrorMessage errorMessage = this.webTestClient//
				.post()//
				.uri(REFRESH_TOKEN)//
				.body(BodyInserters.fromValue(tokenRefreshRequest))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isForbidden() //
				.expectBody(ErrorMessage.class)//
				.returnResult()//
				.getResponseBody();	

		assertThat(errorMessage.getDescription()).isEqualTo("uri=" + REFRESH_TOKEN);
		assertThat(errorMessage.getMessage()).isEqualTo("Failed for [123]: Refresh token is not in database!");
		assertThat(errorMessage.getStatusCode()).isEqualTo(403);
		assertThat(errorMessage.getTimestamp()).isNotNull();
	}

	@Test
	@Order(500)
	void canGetListOfAllUsers() {
		final String USERNAME = "user500";
		final String PASSWORD = "password500";
		final String EMAIL = "user500@unittest.de";
		
		doRegister(TENANT_1, USERNAME, PASSWORD, EMAIL);
		doLogin(TENANT_1, USERNAME, PASSWORD, EMAIL);
		
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPassword(PASSWORD);
		loginRequest.setUsername(USERNAME);
		loginRequest.setTenantId(TENANT_1);

		JwtResponse firstToken = doLogin(TENANT_1, USERNAME, PASSWORD, EMAIL);

		log.debug("Refresh Token after Login: {}", firstToken.getRefreshToken());
		
		UserDTO[] userArray = this.webTestClient//
				.get()//
				.uri(USERLIST_ENDPOINT)//
				.header(AUTHORIZATION, "Bearer " + firstToken.getToken()) //
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(UserDTO[].class)//
				.returnResult()//
				.getResponseBody();	
		
		List<UserDTO> users = Arrays.stream(userArray).collect(Collectors.toList()); // not so fine
		
		assertThat(users).isNotEmpty();
	}
	
	private MessageResponse doRegister(String tenantId, String username, String password, String email) {
		SignupRequest signupRequest = new SignupRequest();

		signupRequest.setTenantId(tenantId);
		signupRequest.setUsername(username);
		signupRequest.setPassword(password);
		signupRequest.setEmail(email);
		Set<String> roles = Set.of(ERole.ROLE_USER.name());
		signupRequest.setRole(roles);
		
		MessageResponse messageResponse = this.webTestClient//
				.post()//
				.uri(REGISTER)//
				.body(BodyInserters.fromValue(signupRequest ))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(MessageResponse.class)//
				.returnResult()//
				.getResponseBody();	

		assertThat(messageResponse.getMessage()).isEqualTo("User registered successfully!");
		
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(messageResponse, MessageResponse.class);
		
		log.debug("Response: {}", json);

		return messageResponse;
	}

	private JwtResponse doLogin(String tenantId, String username, String password, String email) {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPassword(password);
		loginRequest.setUsername(username);
		loginRequest.setTenantId(tenantId);
		
		JwtResponse jwtResponse = this.webTestClient//
				.post()//
				.uri(LOGIN)//
				.body(BodyInserters.fromValue(loginRequest))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(JwtResponse.class)//
				.returnResult()//
				.getResponseBody();	

		assertThat(jwtResponse.getEmail()).isEqualTo(email);
		assertThat(jwtResponse.getToken()).isNotNull();
		assertThat(jwtResponse.getUsername()).isEqualTo(username);
		assertThat(jwtResponse.getRoles()).isNotEmpty();
		assertThat(jwtResponse.getId()).isNotNull();
		assertThat(jwtResponse.getRefreshToken()).isNotNull();
		
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(jwtResponse, JwtResponse.class);
		
		log.info("Response: {}", json);

		return jwtResponse;
	}

	
	
}

