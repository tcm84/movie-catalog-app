package com.blinx.demo.binxresourceserver.users.restcontrollers.integrationtests

import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

import com.blinx.demo.binxresourceserver.BlinxResourceServerApplication
import com.blinx.demo.binxresourceserver.users.restcontrollers.UserRegistrationController
import com.blinx.demo.binxresourceserver.users.services.UserRegistrationService
import com.blinx.demo.binxresourceserver.users.model.entities.UserDetails
import com.blinx.demo.binxresourceserver.users.repo.UserDetailsRepository

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

import com.blinx.demo.binxresourceserver.users.restcontrollers.testconfig.RepoTestConfig
import org.springframework.context.annotation.Import

import org.springframework.http.MediaType;

@Import(RepoTestConfig)
@ContextConfiguration(classes=[ 
	BlinxResourceServerApplication,
	UserRegistrationService])
@WebMvcTest(UserRegistrationController)
class UserRegistrationControllerTest extends Specification  {
	
	def newUserDetailsJSON =
	'''{
			"username": "username",
			"password": "password",
			"company": "company",
			"address": {
				"streetAddress": "streetaddress",
				"city": "city",
				"postCode": "postcode"
			},
			"phoneNumbers": [
				"phoneNumber1",
				"phoneNumber2"
			]
		}'''
	

	@Autowired
	private MockMvc mockMvc

	def "Should return an empty list when no users have been registered yet"() {
		when:
		def response = mockMvc.perform(get("/users/all"))

		then:
		response.andExpect(status().isOk())
		response.andExpect(content().string("[]"))
	}
	
	def "Should beable to register a new user"(){
		when:"a new user tries to register"
		def response = this.mockMvc.perform(post("/users/register")
			.contentType(MediaType.APPLICATION_JSON).content(newUserDetailsJSON))
		
		then: "We expect a HTTP status of OK"
		response.andExpect(status().isOk())
		
		and: "the new user should be present in the repo"
		mockMvc.perform(get("/users/registered/1"))
				.andExpect(content().string("true"))
	}
	
	def "Should not allow a user to Register again if all ready registered"(){
		given:"A new user registers"
		this.mockMvc.perform(post("/users/register")
			.contentType(MediaType.APPLICATION_JSON).content(newUserDetailsJSON))
		
		when:"The same user tries to register again after already having just registered"
		def response = this.mockMvc.perform(post("/users/register")
								   .contentType(MediaType.APPLICATION_JSON).content(newUserDetailsJSON))
		
		then:"the registration endpoint should return a HttpStatus of CONFLICT"
	    response.andExpect(status().isConflict())
		
		and:"the error response reason is User has already registered"
		response.andExpect(status().reason("User has already registered"))
	}
}