package com.example.login.Controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.login.Dto.ResponseDTO;
import com.example.login.Dto.UserDTO;
import com.example.login.Models.UserEntity;
import com.example.login.Repository.UserRepository;
import com.example.login.Security.CustomUserDetailsService;
import com.example.login.Security.JwtGenerator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@CrossOrigin(origins = "http://localhost:4200", allowCredentials="true")
@RestController
public class BasicController {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	@Autowired
	private JwtGenerator jwtGenerator;
	@Autowired
	private UserRepository userRepository;
	
	Logger logger = LoggerFactory.getLogger(BasicController.class);
	
	@CrossOrigin
	@GetMapping("/dashboard")
	public ResponseEntity<ResponseDTO> dashboard(@CookieValue(name="jwtToken", required=true) String jwtToken){
		// check cookies
		if(jwtToken==null) {
			ResponseDTO responseDto = new ResponseDTO("Missing token", null, 403);
		}
		logger.info(jwtToken);
		// fetch user information
		String username = this.jwtGenerator.getUsernameFromJWT(jwtToken);
		Optional<UserEntity> userOptional = userRepository.findByUsername(username);
		UserEntity user = userOptional.get();
		UserDTO userDto = new UserDTO(user.getUsername(), user.getName(), user.getRoles());
		ResponseDTO responseDto = new ResponseDTO("Retrieved user info", userDto, 200);
//		logger.info("name: ", user.getName());
//		logger.info("username: ", user.getUsername());
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping("/dashboard/logout")
	public ResponseEntity<String> logout(HttpServletResponse response){
		// clear cookie
		ResponseCookie cookie = ResponseCookie.from("jwtToken", "")
			.sameSite("None")
			.secure(false)
			.httpOnly(true)
			.path("/dashboard")
			.build();
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		return new ResponseEntity<>("Logout successful", HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping("/dashboard/restricted")
	public ResponseEntity<ResponseDTO> getRestrictedContent(HttpServletResponse response){
		String content = "The top secret content is:  P@55w0rd!";
		ResponseDTO responseDto = new ResponseDTO(content, null, 200);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
