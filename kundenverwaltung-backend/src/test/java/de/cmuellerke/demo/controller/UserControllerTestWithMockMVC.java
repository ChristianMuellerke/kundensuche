package de.cmuellerke.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import de.cmuellerke.demo.common.UserLogin;
import de.cmuellerke.demo.data.dto.UserDTO;
import de.cmuellerke.demo.service.UserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTestWithMockMVC {
	
	private static final String PASSWORD = "xxx xxx xxx xxx";
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JacksonTester<List<UserDTO>> jsonUser;

	@MockBean
	UserService userServiceMock;

	@Test
	public void shouldReturnUsers() throws Exception {
		String token = new UserLogin().authenticate();
		
		// GIVEN
		List<UserDTO> users = Arrays.asList(UserDTO.builder().userName("test").password(PASSWORD).build());
		when(userServiceMock.getAll()).thenReturn(users);

		// WHEN/ THEN
		this.mockMvc.perform(get("/users/users").header("Authorization", token))//
				.andDo(print())//
				.andExpect(status().isOk())//
				.andExpect(content().json(jsonUser.write(users).getJson()));
	}
}
