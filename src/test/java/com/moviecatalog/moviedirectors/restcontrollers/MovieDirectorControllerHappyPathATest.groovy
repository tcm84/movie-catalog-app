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
class MovieDirectorControllerHappyPathATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def movieDirectors = [
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
						"moviedirectorId": 2,
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
	
	def "Deleting movie directors from the catalog"(){
		given:"a movie director exists in the catalog that I want to delete"
		mockMvc.perform(post("/moviedirectors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieDirectors[2]))
			
		
		when: "I make a request to delete the movie director from the catalog"
		def response = mockMvc.perform(delete("/moviedirectors/delete/4"))
		
		then: "the movie directors details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
}