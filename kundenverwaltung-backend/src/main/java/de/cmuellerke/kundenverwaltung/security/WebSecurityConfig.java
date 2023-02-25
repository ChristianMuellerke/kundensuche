package de.cmuellerke.kundenverwaltung.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.cmuellerke.kundenverwaltung.security.jwt.AuthEntryPointJwt;
import de.cmuellerke.kundenverwaltung.security.jwt.AuthTokenFilter;
import de.cmuellerke.kundenverwaltung.security.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final AuthEntryPointJwt unauthorizedHandler;
	
	private final UserDetailsServiceImpl userDetailsService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(PathRequest.toH2Console());
		http.authorizeHttpRequests((requests) -> customAuthorization(requests));
        http.csrf((csrf) -> csrf.disable());
        http.headers((headers) -> headers.frameOptions().sameOrigin());
        return http.build();
    }
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	return http.cors()//
    			.and()//
    			.csrf().disable()//
    			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler) //
    			.and() //
    			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
    			.and().headers().frameOptions().disable() //
    			.and() //
    			.authorizeHttpRequests((requests) -> customAuthorization(requests))//
    			.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class) //
    			.authenticationProvider(authenticationProvider()) //
//    			.cors().disable() // TODO nicht so richtig cool oder?
    			.build();
	}

	private void customAuthorization(
			AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests) {
		requests//
			.requestMatchers("/api/auth/**", "/api/test/**", "/tenants/**", "/h2-console/**").permitAll()//
			.requestMatchers(PathRequest.toH2Console()).permitAll() //
			.anyRequest().authenticated();
	}

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    	
    	authProvider.setUserDetailsService(userDetailsService);
    	authProvider.setPasswordEncoder(passwordEncoder());
    	
    	return authProvider;
    }

    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    	return authenticationConfiguration.getAuthenticationManager();
    }
    
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
