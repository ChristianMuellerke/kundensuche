package de.cmuellerke.demo.common;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.time.Duration;

import com.google.gson.Gson;

import de.cmuellerke.demo.security.JwtRequest;
import de.cmuellerke.demo.security.JwtResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Realizes a login to the application to get a autorization.
 * @author cm
 *
 */
@Slf4j
public class UserLogin {

	public static void main(String[] args) {
		try {
			UserLogin userLogin = new UserLogin();
			String token = authenticate();
			System.out.println("Token: " + token);
		} catch (Exception e) {
			log.error("Error!", e);
		}
	}

	public static String getToken() throws IOException, InterruptedException, URISyntaxException {
		return "Bearer " + authenticate();
	}
	
	public static String authenticate() throws IOException, InterruptedException, URISyntaxException {
		return authenticate("localhost", 8080);
	}
	
	public static String authenticate(String hostname, int port) throws IOException, InterruptedException, URISyntaxException {
		Gson gson = new Gson();
		String requestBody = gson.toJson(new JwtRequest(Testdata.DEFAULT_USER, Testdata.DEFAULT_PASSWORD, Testdata.DEFAULT_TENANT));
		
		HttpRequest request2 = HttpRequest.newBuilder()
				  .uri(new URI("http://" + hostname + ":" + port + "/authenticate"))
				  .headers("Content-Type", "application/json;charset=UTF-8") //
//				  .headers("X-TENANT-ID", Testdata.DEFAULT_TENANT) //
				  .POST(HttpRequest.BodyPublishers.ofString(requestBody)) //
				  .timeout(Duration.ofSeconds(5)) //
				  .build();
		
		HttpResponse<String> response = HttpClient.newBuilder()//
			.connectTimeout(Duration.ofSeconds(5))//
			.build()//
			.send(request2, BodyHandlers.ofString(Charset.forName("UTF-8")))
			;
		
		if (response.statusCode() != 200) {
			log.error("Unexpected HttpCode: {}\n{}", response.statusCode(), response.body());
			throw new IOException("Unexpected HTTP Response Code " + response.statusCode() + "\n" + response.body());
		}
		
		String responseBody = response.body();

		log.debug("Response Data: \n{}", responseBody);
		
		JwtResponse jwtResponse = gson.fromJson(responseBody, JwtResponse.class);

		log.debug("Received Token: {}", jwtResponse.getToken());
		
		return jwtResponse.getToken();
	}	


}
