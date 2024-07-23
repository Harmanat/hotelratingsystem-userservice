package com.mannat.userservice.services;

import java.util.List;

import com.mannat.userservice.entities.User;


public interface UserService {
	
	User saveUser(User user);

	List<User> getAllUser();
	
	User getUser(String userId);

	//TODO: delete
	//TODO: update
}
