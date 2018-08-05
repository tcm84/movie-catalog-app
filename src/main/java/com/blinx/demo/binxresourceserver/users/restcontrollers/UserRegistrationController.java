package com.blinx.demo.binxresourceserver.users.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blinx.demo.binxresourceserver.users.model.entities.UserDetails;
import com.blinx.demo.binxresourceserver.users.services.UserRegistrationService;

@RestController
@RequestMapping("/users")
public class UserRegistrationController {
	@Autowired
	private UserRegistrationService userRegistrationService;
	
	@RequestMapping("/all")
	public List<UserDetails> getAllUserDetails() {
		return userRegistrationService.getAllUserDetails();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/register")
	public void addUserDetails(@RequestBody UserDetails newUserDetails) {
		userRegistrationService.addUserDetails(newUserDetails);
	}
}
