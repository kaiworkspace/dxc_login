package com.example.login.Controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

import com.example.login.Dto.AuthResponseDTO;
import com.example.login.Dto.RegisterDTO;
import com.example.login.Dto.ResponseDTO;
import com.example.login.Dto.UserDTO;
import com.example.login.Models.RoleEntity;
import com.example.login.Models.UserEntity;
import com.example.login.Repository.RoleRepository;
import com.example.login.Repository.UserRepository;
import com.example.login.Security.JwtGenerator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@CrossOrigin(origins="http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private AuthenticationManager authenticationManager;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private JwtGenerator jwtGenerator;
	
	Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager,
			UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder,
			JwtGenerator jwtGenerator) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtGenerator = jwtGenerator;
	}
	
	@CrossOrigin
	@GetMapping("/")
	public ResponseEntity<String> helloWorld(){
		return new ResponseEntity<>("Hello World", HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/register")
	public ResponseEntity<ResponseDTO> register(@RequestBody @Valid RegisterDTO registerDto){
		// check if user alr exists
		if(userRepository.existsByUsername(registerDto.getUsername())) {
			ResponseDTO responseDto = new ResponseDTO("Username taken",null, 400);
			return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
		}
		
		UserEntity user = new UserEntity();
		user.setName(registerDto.getName());
		user.setUsername(registerDto.getUsername());
		logger.info("Encoded Password: ", passwordEncoder.encode(registerDto.getPassword()));
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		RoleEntity roles = roleRepository.findByName("user").get();
		user.setRoles(Collections.singletonList(roles));
		
		userRepository.save(user);
		
		ResponseDTO responseDto = new ResponseDTO("User registered successfully",null, 200);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestHeader(value="Authorization", required=true) String authorizationHeader, HttpServletResponse response){
		logger.info("Reached api");
		// check auth header
		if(authorizationHeader == null && !authorizationHeader.startsWith("Basic")) {
//			ResponseDTO responseDto = new ResponseDTO("Missing or invalid headers", null, 400);
//			return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
			AuthResponseDTO responseDto = new AuthResponseDTO(null);
			return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
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
//			ResponseDTO responseDto = new ResponseDTO("Missing credentials", null, 400);
//			return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
			AuthResponseDTO responseDto = new AuthResponseDTO(null);
			return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
		}
		
		// check if password matches
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// retrieve user details
//			Optional<UserEntity> userOptional = userRepository.findByUsername(username);
//			UserEntity user = userOptional.get();
//			UserDTO userDto = new UserDTO(user.getUsername(), user.getName(), user.getRoles());
//			ResponseDTO responseDto = new ResponseDTO("User signed in successfully", userDto, 200);
//			return new ResponseEntity<>(responseDto, HttpStatus.OK);
			
			String token = jwtGenerator.generateToken(authentication);
			// set cookies
			ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
				.sameSite("None")
				.secure(false)
				.httpOnly(true)
				.path("/dashboard")
				.build();
			response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
			AuthResponseDTO responseDto = new AuthResponseDTO(token);
			return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
		}catch(BadCredentialsException e) {
//			ResponseDTO responseDto = new ResponseDTO("Invalid username or password", null, 401);
//			return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
			AuthResponseDTO responseDto = new AuthResponseDTO(null);
			return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
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
