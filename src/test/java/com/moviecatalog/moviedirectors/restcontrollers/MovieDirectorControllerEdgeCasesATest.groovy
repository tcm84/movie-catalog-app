package com.moviecatalog.moviedirectors.restcontrollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.Rollback
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
@Transactional
class MovieDirectorControllerEdgeCasesATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	/**
	* moviedirectorId is generated automatically but we just include it 
	* here (its returned by the endpoint anyway) so that we can check 
	* if a director exists by Id when calling the endpoints 
	* */
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
			}'''
	]
	@Rollback
	def "Should not beable to add a director that already exists in this catalog"(){
		given: "a director that already exists in this catalog"
	   mockMvc.perform(post("/moviedirectors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieDirectors[0]))
			
		when: "I request that it be added again when it already is in this catalog"
		def response = mockMvc.perform(post("/moviedirectors/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieDirectors[0]))
		
		then: "I should not be able to added it again"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie director already exists in this catalog"))
	}
	
	def "Should not beable to update a movie director that doesn't exist in this catalog"(){
		when: "I make a request to update a nonexistent movie directors' details"
		def response = mockMvc.perform(post("/moviedirectors/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieDirectors[1]))
		
		then: "an exception should be returned in the response indicating the movie director was not found"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie director not found in this catalog"))
	}
}