package com.example.login.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class BasicController {
	
	// testing security config
	@CrossOrigin
	@GetMapping("/")
	public ResponseEntity<String> helloWorld(){
		return new ResponseEntity<>("Hello world", HttpStatus.OK);
	}
}
