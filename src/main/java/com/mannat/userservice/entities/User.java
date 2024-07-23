package com.mannat.userservice.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "micro_users")
@Builder
public class User {

	@Id
	private String userId;

	@Column(name = "name", length = 20)
	private String name;
	private String email;
	private String about;
	
	@Transient // this annotation stops the property from being saved into the database
	private List<Rating> rating = new ArrayList<>();
}
