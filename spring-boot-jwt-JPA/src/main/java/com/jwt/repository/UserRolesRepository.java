package com.jwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.model.entity.UserRoles;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long>{

	public List<UserRoles> findByUsername(String username);
}
