package com.blinx.demo.binxresourceserver.users.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blinx.demo.binxresourceserver.users.model.entities.UserDetails;
import com.blinx.demo.binxresourceserver.users.repo.UserDetailsRepository;

@Service
public class UserRegistrationService {
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	public List<UserDetails> getAllUserDetails(){
		List<UserDetails> userDetails = new ArrayList<>();
		userDetailsRepository.findAll().forEach(userDetails::add);
		return userDetails;
	}
	
	public UserDetails getUserDetails(String id) {
		return userDetailsRepository.findByUserId(Integer.valueOf(id));
	}
	
	public int addUserDetails(UserDetails userDetails) {
		return userDetailsRepository.save(userDetails).getUserId();
	}
	
	public boolean exists(UserDetails userDetails) {
		return userDetailsRepository.
				findByUsernameAndEmailAddress(userDetails.getUsername(),userDetails.getEmailAddress()).isPresent();
	}
}
