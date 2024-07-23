package com.mannat.userservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mannat.userservice.entities.User;

public interface UserRepository extends JpaRepository<User, String> {

}
