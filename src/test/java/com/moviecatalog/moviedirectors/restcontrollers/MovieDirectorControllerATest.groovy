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
class MovieDirectorControllerATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def movieDirectors = [
		'''{
				"moviedirectorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"

			}''',
			'''{
				"moviedirectorId": 2,
				"name": "stevenSpielberg",
				"dob": "18/12/46",
				"nationality": "AMERICAN"
			}''',
			'''{
				"moviedirectorId": 3,
				"name": "Clint Eastwood",
				"dob": "31/05/30",
		        "nationality": "AMERICAN"
			}''',
			'''{
				"moviedirectorId": 4,
				"name": "Eli Roth",
				"dob": "18/04/72",
		        "nationality": "AMERICAN"
			}'''
	]
	
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
		description                                        | endpointURI
		"when adding a new director to the catalog"        | "/moviedirectors/add"
		"when updating an existing director in the catalog"| "/moviedirectors/update"
	}
	
	def "Should not beable to add a director that already exists in the catalog"(){
		given: "a director that already exists in the catalog"
	   mockMvc.perform(post("/moviedirectors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieDirectors[1]))
			
		when: "I request that it be added again when it already is in the catalog"
		def response = mockMvc.perform(post("/moviedirectors/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieDirectors[1]))
		
		then: "I should not be able to added it again"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("MovieDirector already exists in the catalog"))
	}
	
	def "Should not beable to update a movie director that doesn't exist in the catalog"(){
		when: "I make a request to update a nonexistent movie directors' details"
		def response = mockMvc.perform(post("/moviedirectors/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieDirectors[2]))
		
		then: "an exception should be returned in the response indicating the movie director was not found"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("MovieDirector not found in the catalog"))
	}
	
	def "Adding new movie directors to the catalog"(){
		when: "I request a new director gets added to the catalog"
		def response = mockMvc.perform(post("/moviedirectors/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(movieDirectors[0]))
		
		then: "it should be added to the catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(movieDirectors[0]))
	}
	
	def "Updating movie directors in the catalog"(){
		setup:"A director exists in the catalog that needs updated"
	   mockMvc.perform(post("/moviedirectors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieDirectors[1]))
		when: "I make a request to update the directors' details"
		movieDirectors[1] =
		'''{
						"moviedirectorId": 1,
						"name": "Steven Spielberg",
						"dob": "18/12/46",
				        "nationality": "AMERICAN"
					}'''
		
		def response = mockMvc.perform(post("/moviedirectors/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieDirectors[1]))
		
		then: "the director details should be updated in the catalog"
		response.andExpect(status().isOk())
				.andExpect(content().json(movieDirectors[1]))
	}
	
	def "Deleting directors from the catalog"(){
		given:"a movie director exists in the catalog that I want to delete"
		mockMvc.perform(post("/moviedirectors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieDirectors[3]))
			
		
		when: "I make a request to delete the director from the catalog"
		def response = mockMvc.perform(delete("/moviedirectors/delete/4"))
		
		then: "the directors details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
}