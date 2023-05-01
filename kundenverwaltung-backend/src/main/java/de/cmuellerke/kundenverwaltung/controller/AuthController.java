package de.cmuellerke.kundenverwaltung.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.cmuellerke.kundenverwaltung.models.ERole;
import de.cmuellerke.kundenverwaltung.models.RefreshToken;
import de.cmuellerke.kundenverwaltung.models.Role;
import de.cmuellerke.kundenverwaltung.models.UserEntity;
import de.cmuellerke.kundenverwaltung.payload.request.LoginRequest;
import de.cmuellerke.kundenverwaltung.payload.request.SignupRequest;
import de.cmuellerke.kundenverwaltung.payload.request.TokenRefreshRequest;
import de.cmuellerke.kundenverwaltung.payload.response.JwtResponse;
import de.cmuellerke.kundenverwaltung.payload.response.MessageResponse;
import de.cmuellerke.kundenverwaltung.payload.response.TokenRefreshResponse;
import de.cmuellerke.kundenverwaltung.repository.RoleRepository;
import de.cmuellerke.kundenverwaltung.repository.UserRepository;
import de.cmuellerke.kundenverwaltung.security.jwt.JwtUtils;
import de.cmuellerke.kundenverwaltung.security.services.RefreshTokenService;
import de.cmuellerke.kundenverwaltung.security.services.TokenRefreshException;
import de.cmuellerke.kundenverwaltung.security.services.UserDetailsImpl;
import de.cmuellerke.kundenverwaltung.tenancy.TenantContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
	private final AuthenticationManager authenticationManager;

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PasswordEncoder encoder;

	private final JwtUtils jwtUtils;

	private final RefreshTokenService refreshTokenService;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		TenantContext.setTenantId(loginRequest.getTenantId());
		
		log.debug("/api/auth/signin for tenant {}", TenantContext.getTenantId());
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

		JwtResponse jwtResponse = JwtResponse.builder()//
				.token(jwt) //
				.id(userDetails.getId())
				.refreshToken(refreshToken.getToken()) //
				.username(userDetails.getUsername()) //
				.email(userDetails.getEmail()) //
				.roles(roles) //
				.build();

		return ResponseEntity.ok(jwtResponse);
	}

	@PostMapping("/signup")
	@Transactional
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		TenantContext.setTenantId(signUpRequest.getTenantId());
		
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		UserEntity user = UserEntity.builder()
				.username(signUpRequest.getUsername())
				.email(signUpRequest.getEmail())
				.password(encoder.encode(signUpRequest.getPassword()))
				.build();

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		UserEntity savedUser = userRepository.save(user);

		log.debug("User {} on Tenant {} registered successfully!", user.getUsername(), user.getTenantId());
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser)
				.map(user -> {
					String token = jwtUtils.generateTokenFromUsername(user.getUsername(), user.getTenantId());

					
					TokenRefreshResponse tokenRefreshResponse = TokenRefreshResponse.builder()//
							.accessToken(token)//
							.refreshToken(requestRefreshToken)//
							.build(); 
					
					return ResponseEntity.ok(tokenRefreshResponse);
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
						"Refresh token is not in database!"));
	}
}
