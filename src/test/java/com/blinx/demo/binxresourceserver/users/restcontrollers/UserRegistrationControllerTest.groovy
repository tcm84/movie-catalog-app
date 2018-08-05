package com.blinx.demo.binxresourceserver.users.restcontrollers

import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

import com.blinx.demo.binxresourceserver.BlinxResourceServerApplication
import com.blinx.demo.binxresourceserver.users.restcontrollers.UserRegistrationController
import com.blinx.demo.binxresourceserver.users.services.UserRegistrationService
import com.blinx.demo.binxresourceserver.users.repo.UserDetailsRepository

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

import com.blinx.demo.binxresourceserver.users.restcontrollers.testconfig.RepoTestConfig
import org.springframework.context.annotation.Import

@Import(RepoTestConfig)
@ContextConfiguration(classes=[ 
	BlinxResourceServerApplication,
	UserRegistrationService])
@WebMvcTest(UserRegistrationController)
class UserRegistrationControllerTest extends Specification  {

	@Autowired
	private MockMvc mockMvc

	def "Should return an empty list when no users have been registered yet"() {
		when:
		def response = mockMvc.perform(get("/users/all"))

		then:
		response.andExpect(status().isOk())
	}
}