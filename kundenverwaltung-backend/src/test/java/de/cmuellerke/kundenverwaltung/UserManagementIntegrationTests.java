package de.cmuellerke.kundenverwaltung;

import java.util.Set;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.google.gson.GsonBuilder;

import de.cmuellerke.kundenverwaltung.models.ERole;
import de.cmuellerke.kundenverwaltung.payload.request.SignupRequest;
import de.cmuellerke.kundenverwaltung.payload.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;

@WebIntegrationTest
@Slf4j
public class UserManagementIntegrationTests implements WithAssertions {

	@Autowired
	WebTestClient webTestClient;

	private static final String SIGNUP = "/api/auth/signup";

	@Test
	public void canRegisterNewUser() {
		SignupRequest signupRequest = new SignupRequest();
		
		signupRequest.setEmail("cm@cmuellerke.de");
		signupRequest.setPassword("test_test");
		Set<String> roles = Set.of(ERole.ROLE_USER.name());
		signupRequest.setRole(roles);
		signupRequest.setUsername("unittest");
		
		MessageResponse messageResponse = this.webTestClient//
				.post()//
				.uri(SIGNUP)//
				.body(BodyInserters.fromValue(signupRequest ))//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(MessageResponse.class)//
				.returnResult()//
				.getResponseBody();	
		
		String json = new GsonBuilder().setPrettyPrinting().create().toJson(messageResponse, MessageResponse.class);
		
		log.debug("Response: {}", json);
	}

}
