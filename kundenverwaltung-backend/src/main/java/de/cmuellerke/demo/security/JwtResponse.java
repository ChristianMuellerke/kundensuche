package de.cmuellerke.demo.security;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8866069118117135310L;

	private final String token;

	public JwtResponse(String jwttoken) {
		this.token = jwttoken;
	}
}
