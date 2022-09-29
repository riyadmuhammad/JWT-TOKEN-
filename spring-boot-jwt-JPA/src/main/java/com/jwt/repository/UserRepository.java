package com.jwt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jwt.model.entity.Users;


@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
	
	Users findByUsername(String username);
	
}