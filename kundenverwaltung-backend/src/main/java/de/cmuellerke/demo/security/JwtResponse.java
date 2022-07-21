package de.cmuellerke.demo.security;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.cmuellerke.demo.data.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
public class JwtResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8866069118117135310L;

	private final String token;

	@JsonCreator
	public JwtResponse(@JsonProperty("token") String jwttoken) {
		this.token = jwttoken;
	}
}
