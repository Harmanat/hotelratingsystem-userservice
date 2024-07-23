package com.mannat.userservice.config;

//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

	/*
		@Bean: This is used as and when we auto-wire RestTemplate, then no bean is found by Spring Boot, this is how we create bean.
		@LoadBalanced: It is used load balancing and accessing service by their names instead of ip and port
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
