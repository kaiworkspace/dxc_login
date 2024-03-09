package com.example.login.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@CrossOrigin
	@GetMapping("/")
	public ResponseEntity<String> helloWorld(){
		return new ResponseEntity<>("Hello World", HttpStatus.OK);
	}
}
