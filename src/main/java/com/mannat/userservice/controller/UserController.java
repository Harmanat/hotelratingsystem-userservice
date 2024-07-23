package com.mannat.userservice.controller;

import java.util.List;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mannat.userservice.entities.User;
import com.mannat.userservice.services.UserService;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user){
		User createdUser = userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}
	
	@GetMapping("/{userId}")
	@CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingFallback")
	public ResponseEntity<User> getSingleUser(@PathVariable String userId){
		User userById = userService.getUser(userId);
		return ResponseEntity.ok(userById);
	}

	// creating fallback method for circuit breaker
	public ResponseEntity<User> ratingFallback(String userId, Exception ex){
		log.info("Fallback is executed because service is down. ", ex.getMessage());

		User user = User.builder()
				.email("dummy@email.com")
				.name("John Doe")
				.about("This is a dummy user.")
				.build();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<User>> getAllUsers(){
		List<User> allUsers = userService.getAllUser();
		return ResponseEntity.ok(allUsers);
	}
	
}
