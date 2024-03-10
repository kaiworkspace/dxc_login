package com.example.login.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	private JwtAuthEntry authEntryPoint;
	private CustomUserDetailsService userDetailsService;
	private AuthenticationConfiguration authenticationConfiguration;
	
	@Autowired
	public SecurityConfiguration(JwtAuthEntry authEntryPoint, CustomUserDetailsService userDetailsService, 
			AuthenticationConfiguration authenticationConfiguration) {
		this.authEntryPoint = authEntryPoint;
		this.userDetailsService = userDetailsService;
		this.authenticationConfiguration = authenticationConfiguration;
	}
	
	// configure filter chain
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			// TODO: update csrf disable
			.csrf().disable() // disable csrf to allow post request
			.exceptionHandling()
			.authenticationEntryPoint(authEntryPoint)
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests((authz)-> authz
					.requestMatchers("/auth/**").permitAll()
					.requestMatchers("/dashboard/restricted").hasRole("manager")
					.anyRequest().authenticated()
					);
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public JWTAuthenticationFilter jwtAuthenticationFilter() {
		return new JWTAuthenticationFilter();
	}
}
