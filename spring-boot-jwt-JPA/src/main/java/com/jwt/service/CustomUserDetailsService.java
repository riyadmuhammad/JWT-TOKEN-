package com.jwt.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwt.model.entity.UserRoles;
import com.jwt.model.entity.Users;
import com.jwt.model.request.UserCreateRequest;
import com.jwt.model.response.BaseResponse;
import com.jwt.repository.UserRepository;
import com.jwt.repository.UserRolesRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserRolesRepository userRolesRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user = userRepository.findByUsername(username);
		
		if (user == null) {
			
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		List<UserRoles> userRoles=userRolesRepository.findByUsername(username);
		List<String> roles=new ArrayList<>();
		for(UserRoles r: userRoles) {
			roles.add(r.getRoleName());	
		}
		user.setRoles(roles);
		
		return user;
	}
	
	
	@Transactional
	public BaseResponse save(UserCreateRequest request) {
		
		BaseResponse baseResponse = new BaseResponse();
		Users newUser = userRepository.findByUsername(request.getUsername());
		if(newUser!=null) {
			baseResponse.setMessage("User Already Exists");
			baseResponse.setMessageType(0);
			return baseResponse;
		}
		
		newUser = new Users();
		newUser.setUsername(request.getUsername());
		newUser.setPassword(bcryptEncoder.encode(request.getPassword()));
		newUser.setEnabled(true);
		
		List<UserRoles> userRoles = new ArrayList<>();
		
		for(String role : request.getUserRoles()) {
			
			UserRoles userRole=new UserRoles();	
			userRole.setUsername(request.getUsername());
			userRole.setRoleName(role);
			
			userRoles.add(userRole);
		}
				
		userRepository.save(newUser);
		userRolesRepository.saveAll(userRoles);
		
		baseResponse.setMessage("User Successfully Created.");
		baseResponse.setMessageType(1);
		return baseResponse;
	}
}