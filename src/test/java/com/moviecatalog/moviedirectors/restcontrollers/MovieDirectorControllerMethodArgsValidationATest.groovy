package com.moviecatalog.moviedirectors.restcontrollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.repo.testconfig.RepoTestConfig
import com.moviecatalog.moviedirectors.restcontrollers.MovieDirectorControllerImpl
import com.moviecatalog.moviedirectors.services.MovieDirectorServiceImpl

import groovy.swing.factory.ImageIconFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.http.MediaType;

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

@Import(RepoTestConfig)
@ContextConfiguration(classes=[
	MovieCatalogApplication,
	MovieDirectorServiceImpl])
@WebMvcTest(MovieDirectorControllerImpl)
class MovieDirectorControllerMethodArgsValidationATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Unroll
	def "Requests should be rejected if they don't pass validation requirements #description"(){
		given: "a request containing no valid data"
		def invalidRequest =
		'''{
			}'''
		
		when: "I make the request"
		def response = mockMvc.perform(post(endpointURI)
			.contentType(MediaType.APPLICATION_JSON)
			.content(invalidRequest))
		
		then: "the request should be rejected as a bad request"
		response.andExpect(status().isBadRequest())
		
		and: "the response should contain all the error messages for the failed validations"
		MethodArgumentNotValidException ex = response.andReturn().resolvedException
		def validationErrors = ex.getBindingResult().allErrors
		validationErrors.size() == 2
		Set actualErrorMsgs = []
		validationErrors.forEach({error -> actualErrorMsgs += error.getDefaultMessage()})
		Set expectedErrorMsgs = ["Name must not be empty", "Dob should not be null"]
		actualErrorMsgs == expectedErrorMsgs
		
		where:
		description                                         | endpointURI
		"when adding a new director to this catalog"        | "/moviedirectors/add"
		"when updating an existing director in this catalog"| "/moviedirectors/update"
	}
}