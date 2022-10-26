package de.cmuellerke.kundenverwaltung;

import java.lang.reflect.Type;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.reactive.function.BodyInserters;

import com.google.gson.GsonBuilder;

import de.cmuellerke.kundenverwaltung.payload.MessageResponse;
import lombok.extern.slf4j.Slf4j;

@WebIntegrationTest
@Slf4j
@AutoConfigureWebTestClient
public class MyWebApplicationIntegrationTest implements WithAssertions {

    @Autowired
    private WebApplicationContext context;
	
	@Autowired
	private WebTestClient webTestClient;

	private static final String TEST_GET_ENDPOINT = "/api/test/hello";
	private static final String TEST_POST_ENDPOINT = "/api/test/reply";

    @BeforeEach
    public void setup() throws Exception {
    	webTestClient = MockMvcWebTestClient.bindToApplicationContext(this.context).build();
		log.info("server configured...");
    }
	
	@Test
	void restEndpointKannAufgerufenWerden_RawString() {
		log.debug("starte test...");
		
		String messageResponse = this.webTestClient//
				.get()//
				.uri(TEST_GET_ENDPOINT)//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(String.class)//
				.returnResult()//
				.getResponseBody();	
		
		log.info("Response: {}", messageResponse);
	}
	
	@Test
	void restEndpointKannAufgerufenWerden_Deserialize() {
		log.debug("starte test...");
		
		MessageResponse messageResponse = this.webTestClient//
				.get()//
				.uri(TEST_GET_ENDPOINT)//
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(MessageResponse.class)//
				.returnResult()//
				.getResponseBody();	

		log.info("Response: {}", toPrettyJson(messageResponse, MessageResponse.class));
	}

	@Test
	void postMessageWorks() {
		MessageResponse message = new MessageResponse();
		message.setMessage("Hello from Post!");
		
		MessageResponse messageResponse = this.webTestClient//
				.post()//
				.uri(TEST_POST_ENDPOINT)//
				.body(BodyInserters.fromValue(message)) //
				.accept(MediaType.APPLICATION_JSON)//
				.exchange()//
				.expectStatus().isOk() //
				.expectBody(MessageResponse.class)//
				.returnResult()//
				.getResponseBody();	
		
		assertThat(messageResponse.getMessage()).isEqualTo("R=" + message.getMessage());
		
		log.info("Response: {}", toPrettyJson(messageResponse, MessageResponse.class));
	}

	private String toPrettyJson(Object src, Type typeOfSrc) {
		return new GsonBuilder().setPrettyPrinting().create().toJson(src, typeOfSrc);		
	}
	
}

