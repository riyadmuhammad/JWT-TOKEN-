package com.jwt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.model.entity.Users;
import com.jwt.model.response.ItemResponse;
import com.jwt.model.response.UserListViewResponse;
import com.jwt.model.response.UserViewResponse;
import com.jwt.repository.UserRepository;

@RestController
public class UserInfoController {
	
	@Autowired
	private UserRepository userRepository; 

	@SuppressWarnings("unchecked")
	@RequestMapping({ "/user/info" })
	public ResponseEntity<ItemResponse> userInfo() {
		
		ItemResponse itemResponse = new ItemResponse();
		
		Users user = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserViewResponse response = new UserViewResponse();
		
		response.setUsername(user.getUsername());
		response.setUserId(user.getId());
		response.setPassword(user.getPassword());
		response.setUserRoles(user.getRoles());
		
		itemResponse.setItem(response);
		itemResponse.setMessage("OK");
		itemResponse.setMessageType(1);
		
		return new ResponseEntity<>(itemResponse, HttpStatus.OK);
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping({ "/user/list" })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ItemResponse> userList() {
		
		ItemResponse itemResponse = new ItemResponse();
		
		List<Users> users = (List<Users>) userRepository.findAll();
		
		List<UserListViewResponse> userList = new ArrayList<>();
		
		for(Users u : users) {
		
			UserListViewResponse response = new UserListViewResponse();
			
			response.setUsername(u.getUsername());
			response.setUserId(u.getId());
			userList.add(response);
			
		}
		
		itemResponse.setItem(userList);
		itemResponse.setMessage("OK");
		itemResponse.setMessageType(1);
		
		return new ResponseEntity<>(itemResponse, HttpStatus.OK);
	}

}