package com.example.login.Controllers;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.login.Dto.RegisterDTO;
import com.example.login.Models.RoleEntity;
import com.example.login.Models.UserEntity;
import com.example.login.Repository.RoleRepository;
import com.example.login.Repository.UserRepository;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	
	Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager,
			UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@CrossOrigin
	@GetMapping("/")
	public ResponseEntity<String> helloWorld(){
		return new ResponseEntity<>("Hello World", HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO registerDto){
		// check if user alr exists
		if(userRepository.existsByUsername(registerDto.getUsername())) {
			return new ResponseEntity<>("Username taken", HttpStatus.BAD_REQUEST);
		}
		
		UserEntity user = new UserEntity();
		user.setName(registerDto.getName());
		user.setUsername(registerDto.getUsername());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		RoleEntity roles = roleRepository.findByName("user").get();
		user.setRoles(Collections.singletonList(roles));
		
		userRepository.save(user);
		
		return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestHeader(value="Authorization", required=true) String authorizationHeader){
		// check auth header
		if(authorizationHeader == null && !authorizationHeader.startsWith("Basic")) {
			return new ResponseEntity<>("Missing or invalid headers", HttpStatus.BAD_REQUEST);
		}
		
		// extract and decode
		String credentialBased64 = authorizationHeader.substring(6);
		String decodedCredentials = new String(java.util.Base64.getDecoder().decode(credentialBased64));
		String[] credentialsArr = decodedCredentials.split(":");
		String username = "";
		String password = "";
		
		// check if both username and password is present
		try {
			username = credentialsArr[0];
			password = credentialsArr[1];
		}catch(Exception e) {
			return new ResponseEntity<>("Missing Credentials", HttpStatus.BAD_REQUEST);
		}
		
		// check if password matches
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			return new ResponseEntity<>("User signed in successfully", HttpStatus.OK);
		}catch(BadCredentialsException e) {
			return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e){
		BindingResult result = e.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		
		StringBuilder errorMessage = new StringBuilder("Validation failed. ");
		for (FieldError fieldError: fieldErrors) {
			errorMessage.append(fieldError.getDefaultMessage()).append("; ");
		}
		return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
	}
}
