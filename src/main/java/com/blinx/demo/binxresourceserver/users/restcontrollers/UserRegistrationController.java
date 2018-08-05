package com.blinx.demo.binxresourceserver.users.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blinx.demo.binxresourceserver.users.exceptions.UserAlreadyRegisteredException;
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
	
	@RequestMapping("registered/{id}")
	public boolean isRegistered(@PathVariable String id) {
		return userRegistrationService.getUserDetails(id) != null;
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/register")
	public void addUserDetails(@RequestBody UserDetails newUserDetails) {
		if (userRegistrationService.exists(newUserDetails)) {
			throw new UserAlreadyRegisteredException();
		}
		
		userRegistrationService.addUserDetails(newUserDetails);
	}
}
