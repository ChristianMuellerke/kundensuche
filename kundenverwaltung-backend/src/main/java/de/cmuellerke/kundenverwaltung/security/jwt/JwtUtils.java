package de.cmuellerke.kundenverwaltung.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import de.cmuellerke.kundenverwaltung.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	private String jwtSecret;
	private int jwtExpirationMs;

	@Autowired
	public JwtUtils(@Value("${jwt.secret.value}") String secret, @Value("${jwt.secret.expiration}") int expirationInMillis) {
		this.jwtSecret = secret;
		this.jwtExpirationMs = expirationInMillis;
	}
	
	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()//
				.setSubject((userPrincipal.getUsername()))//
				.setIssuedAt(new Date()) //
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))//
				.claim("tenant", userPrincipal.getTenantId()) //
				.signWith(SignatureAlgorithm.HS512, jwtSecret)//
				.compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public String generateTokenFromUsername(String username, String tenantId) {
		return Jwts.builder()//
				.setSubject(username)//
				.setIssuedAt(new Date()) //
				.claim("tenant", tenantId) //
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) //
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public Date getExpiration(String authToken) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().getExpiration();
	}

	public String getTenant(String authToken) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().get("tenant", String.class);
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser()//
					.setSigningKey(jwtSecret)//
					.parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
