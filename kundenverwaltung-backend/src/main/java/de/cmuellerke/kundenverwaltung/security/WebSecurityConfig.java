package de.cmuellerke.kundenverwaltung.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.cmuellerke.kundenverwaltung.security.jwt.AuthEntryPointJwt;
import de.cmuellerke.kundenverwaltung.security.jwt.AuthTokenFilter;
import de.cmuellerke.kundenverwaltung.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//@EnableGlobalAuthentication
public class WebSecurityConfig {

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	/*
	 * Diese Konfiguration muss komplett neu gemacht werden, da sich alles gedreht hat
	 * TODO umstellen auf das neue aus Spring
	 */

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
    			.authorizeHttpRequests((requests) -> {
    				requests//
	    				.requestMatchers("/api/auth/**", "/api/test/**", "/tenants/**", "/h2-console/**").permitAll()//
	    				.anyRequest().authenticated();
    			}).addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
    			.authenticationProvider(authenticationProvider()) //
    			.build();
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
