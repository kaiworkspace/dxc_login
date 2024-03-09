package com.example.login.Controllers;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.login.Dto.RegisterDTO;
import com.example.login.Models.RoleEntity;
import com.example.login.Models.UserEntity;
import com.example.login.Repository.RoleRepository;
import com.example.login.Repository.UserRepository;

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
	public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto){
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
	
	
}
