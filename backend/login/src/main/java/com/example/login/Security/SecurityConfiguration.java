package com.example.login.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	// configure filter chain
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			// TODO: update csrf disable
			.csrf().disable() // disable csrf to allow post request
			.authorizeHttpRequests((authz)-> authz
					.requestMatchers("/auth/**").permitAll()
					.anyRequest().authenticated()
					);
		return http.build();
	}
}
