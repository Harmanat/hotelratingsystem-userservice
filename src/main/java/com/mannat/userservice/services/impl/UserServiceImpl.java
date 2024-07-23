package com.mannat.userservice.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mannat.userservice.external.services.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.mannat.userservice.entities.Hotel;
import com.mannat.userservice.entities.Rating;
import com.mannat.userservice.entities.User;
import com.mannat.userservice.exceptions.ResourceNotFoundException;
import com.mannat.userservice.repositories.UserRepository;
import com.mannat.userservice.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HotelService hotelService;

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public User saveUser(User user) {
		String randomUserId = UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		List<User> allUsers = userRepository.findAll();
//		allUsers.forEach(user -> {
//			String userId = user.getUserId();
//			Rating[] userRating = restTemplate.getForObject("http://localhost:9093/ratings/users/" + userId,
//					Rating[].class);
//			user.setRating(Arrays.stream(userRating).toList());
//		});
		return allUsers;
	}

	@Override
	public User getUser(String userId) {
		// gets user from database with help of user repository
		User user = userRepository.findById(userId).orElseThrow(
				() -> new ResourceNotFoundException("User with given id not found on the server ! " + userId));

		// fetch ratings of user from Rating service
		// http://localhost:9093/ratings/users/7d973e88-2203-40d7-978d-8f088266dd41

		// fetch ratings for given userId from rating service
		Rating[] ratingsOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + userId,
				Rating[].class);
		List<Rating> ratings = Arrays.stream(ratingsOfUser).toList();

		List<Rating> ratingList= ratings.stream().map(rating -> {
			String hotelId = rating.getHotelId();
			// fetch hotel detail from hotel service using resttemplate
//			ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+hotelId, Hotel.class);
//			Hotel hotel = forEntity.getBody();
//			logger.info("**********************", forEntity.getStatusCode());

			// calling hotel service using feign client
			Hotel hotel = hotelService.getHotel(rating.getHotelId());
			rating.setHotel(hotel);
			return rating;
		}).collect(Collectors.toList());


		user.setRating(ratingList);
		return user;
	}

}
