package com.nextflix.app.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> {
					session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
				}).authorizeHttpRequests(auth -> {
					auth.requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll();
					auth.requestMatchers("/api/v1/server/internal/**").permitAll();
					auth.requestMatchers(HttpMethod.GET, "/api/v1/admin/**").hasRole("ADMIN");
					auth.requestMatchers("/api/v1/movie/**").hasAnyRole("USER", "ADMIN");
					auth.requestMatchers("/api/v1/subscription/**").hasAnyRole("USER", "ADMIN");
					auth.anyRequest().authenticated();
				})
				.build();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(
				Collections.singletonList(
						new DaoAuthenticationProvider() {
							{
								setUserDetailsService(userDetailsService);
							}
						}));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

}