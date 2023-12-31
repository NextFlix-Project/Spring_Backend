package com.nextflix.app.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nextflix.app.component.AuthEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthEntryPoint authEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http
				.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> {
					session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
				}).authorizeHttpRequests(auth -> {
					auth.requestMatchers("/api/v1/auth/**").permitAll();
					auth.requestMatchers("/api/v1/server/internal/**").permitAll();
					auth.requestMatchers("/api/v1/file/image/**").permitAll();
					auth.requestMatchers("/api/v1/admin/user/**").hasRole("ADMIN");
					auth.requestMatchers("/api/v1/admin/movie/**").hasRole("ADMIN");
					auth.requestMatchers("/api/v1/movie/**").hasAnyRole("USER", "ADMIN");
					auth.requestMatchers("/api/v1/rating/**").hasAnyRole("USER", "ADMIN");
					auth.requestMatchers("/api/v1/movie/streammovie**").hasAnyRole("USER", "ADMIN");
					auth.requestMatchers("/api/v1/subscription/**").hasAnyRole("USER", "ADMIN");
					auth.requestMatchers("/api/v1/customer/**").hasAnyRole("USER", "ADMIN");
					auth.requestMatchers("/api/v1/user/**").hasAnyRole("USER", "ADMIN");
					auth.anyRequest().authenticated();
				})
				.httpBasic(Customizer.withDefaults()).logout(logout -> {
					logout.logoutUrl("/api/v1/user/logout");
				})
				.exceptionHandling(excptHand -> excptHand.authenticationEntryPoint(authEntryPoint))
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

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}