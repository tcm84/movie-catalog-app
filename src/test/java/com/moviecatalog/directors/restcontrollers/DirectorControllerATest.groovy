package com.moviecatalog.directors.restcontrollers

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
import com.moviecatalog.directors.restcontrollers.DirectorControllerImpl
import com.moviecatalog.directors.services.DirectorServiceImpl

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
	DirectorServiceImpl])
@WebMvcTest(DirectorControllerImpl)
class DirectorControllerATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	def "Adding new directors to the catalog"(){
		when: "I request a new director gets added to the catalog"
		def quentinTarantino =
		'''{
				"directorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"

			}'''
		def response = mockMvc.perform(post("/directors/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(quentinTarantino))
		
		then: "it should be added to the catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(quentinTarantino))
	}
	
	def "Should not beable to add a director that already exists in the catalog"(){
		given: "a director that already exists in the catalog"
		def stevenSpielberg =
		'''{
				"directorId": 1,
				"name": "stevenSpielberg",
				"dob": "18/12/46",
				"nationality": "AMERICAN"
			}'''
	   mockMvc.perform(post("/directors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(stevenSpielberg))
			
		when: "I request that it be added again when it already is in the catalog"
		def response = mockMvc.perform(post("/directors/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(stevenSpielberg))
		
		then: "I should not be able to added it again"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Director already exists in the catalog"))
	}
	
	def "Updating directors in the catalog"(){
		setup:"A director exists in the catalog that needs updated"
		def stevenSpielberg =
				'''{
						"directorId": 1,
						"name": "stevenSpielberg",
						"dob": "18/12/46",
				        "nationality": "AMERICAN"
					}'''
	   mockMvc.perform(post("/directors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(stevenSpielberg))
		when: "I make a request to update the directors' details"
		def updatedStevenSpielberg =
		'''{
						"directorId": 1,
						"name": "Steven Spielberg",
						"dob": "18/12/46",
				        "nationality": "AMERICAN"
					}'''
		
		def response = mockMvc.perform(post("/directors/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedStevenSpielberg))
		
		then: "the director details should be updated in the catalog"
		response.andExpect(status().isOk())
				.andExpect(content().json(updatedStevenSpielberg))
	}
	
	def "Should not beable to update a director that doesn't exist in the catalog"(){
		given:"A director that doesn't exist in the catalog"
		def nonexistentDirecror =
				'''{
						"directorId": 2,
						"name": "Clint Eastwood",
						"dob": "31/05/30",
				        "nationality": "AMERICAN"
					}'''
		
		when: "I make a request to update the directors' details"
		def response = mockMvc.perform(post("/directors/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentDirecror))
		
		then: "I should not beable to update them as the director is not in the catalog"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Director not found in the catalog"))
	}
	
	def "Deleting directors from the catalog"(){
		given:"a director exists in the catalog that I want to delete"
		def eliRoth =
				'''{
						"directorId": 1,
						"name": "Eli Roth",
						"dob": "18/04/72",
				        "nationality": "AMERICAN"
					}'''
		mockMvc.perform(post("/directors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(eliRoth))
			
		
		when: "I make a request to delete the director from the catalog"
		def response = mockMvc.perform(delete("/directors/delete/1"))
		
		then: "the directors details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
	
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
		"when adding a new director to the catalog"        | "/directors/add"
		"when updating an existing director in the catalog"| "/directors/update"
	}
}