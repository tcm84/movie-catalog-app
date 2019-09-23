package com.moviecatalog.moviedirectors.restcontrollers.it

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.repo.testconfig.RepoTestConfig
import com.moviecatalog.moviedirectors.model.dto.entities.MovieDirectorDetails
import com.moviecatalog.moviedirectors.restcontrollers.MovieDirectorControllerImpl
import com.moviecatalog.moviedirectors.services.MovieDirectorServiceImpl
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.swing.factory.ImageIconFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

@Import(RepoTestConfig)
@ContextConfiguration(classes=[MovieCatalogApplication,MovieDirectorServiceImpl])
@WebMvcTest(MovieDirectorControllerImpl)
class MovieDirectorControllerIT extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def addEndpointURI = "/moviedirectors/add"
	
	def "Should be able to add new movie directors to this catalog"(){
		when: "I make a request to add a new movie director to this catalog"
		def newMovieDirectorDetails = 
		'''{
				"name": "Jony Carson",
				"dob": "18/12/1926",
				"nationality": "AMERICAN"
			}'''
		def response = mockMvc.perform(post(addEndpointURI)
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(newMovieDirectorDetails))
		
		then: "it should be added to this catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(newMovieDirectorDetails))
	}
	
	def "Should not beable to add a director that already exists in this catalog"(){
		given: "a movie director that already exists in this catalog"
		def newMovieDirectorDetails = '''{
				"name": "Jim Smith",
				"dob": "27/03/1955",
				"nationality": "AMERICAN"

			}'''
		def addResponse = mockMvc.perform(post(addEndpointURI)
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovieDirectorDetails))
		
		when: "I try to add the same movie director again a second time in a row"
		def response = mockMvc.perform(post(addEndpointURI)
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(addResponse.andReturn().getResponse().getContentAsString()))
		
		then: "an exception should be returned in the response"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie director already exists in this catalog"))
	}
	
	def "Should be able to update movie directors that exist in this catalog"(){
		given:"a movie director already exists in this catalog"
		def movieDirectorDetails =
		'''{
				"name": "Clint Eastwood",
				"dob": "31/05/1930",
		        "nationality": "AMERICAN"
			}'''
	    def addresponse = mockMvc.perform(post(addEndpointURI)
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieDirectorDetails))
		
		when: "I make a request to update the movie directors' details"
		def movieDirectorDetailsMap = 
					new JsonSlurper().parseText(addresponse.andReturn()
		 									            .getResponse()
														.getContentAsString())
					
		//We are updating the movie directors dob in this test
		movieDirectorDetailsMap["dob"] = "25/05/1930"
		
		def updatedMovieDirectorDetails = 
							new JsonOutput().toJson(movieDirectorDetailsMap)
							
		def response = mockMvc.perform(post("/moviedirectors/update/")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedMovieDirectorDetails))
		
		then: "the movie director details should be updated"
		response.andExpect(status().isOk())
				.andExpect(content().json(updatedMovieDirectorDetails))
	}
	
	def "Should not beable to update a movie director that doesn't exist in this catalog"(){
		when: "I try to update a movie directors' details that are not in this catalog"
		def nonexistentMovieDirectorDetails = '''{
				"moviedirectorId": 999,
				"name": "stevenSpielberg",
				"dob": "18/12/1946",
				"nationality": "AMERICAN"
			}'''
		def response = mockMvc.perform(post("/moviedirectors/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentMovieDirectorDetails))
		
		then: "an exception should be returned in the response"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie director not found in this catalog"))
	}
	
	def "Should be able to delete movie directors that exist in this catalog"(){
		given:"a movie director exists in this catalog"
		def newMovieDirectorDetails =
		'''{
				"name": "Soup Broth",
				"dob": "18/04/1972",
		        "nationality": "AMERICAN"
			}'''
		def addResponse = mockMvc.perform(post(addEndpointURI)
								 .contentType(MediaType.APPLICATION_JSON)
								 .content(newMovieDirectorDetails))
		
		when: "I make a request to delete the movie directors' details"
		def movieDirectorDetailsMap =
			new JsonSlurper().parseText(addResponse.andReturn()
												   .getResponse()
												   .getContentAsString())
		
		def moviedirectorId =  movieDirectorDetailsMap["moviedirectorId"]
		
		def response = mockMvc.perform(delete("/moviedirectors/delete/" + moviedirectorId))
		
		then: "the movie directors details should be deleted"
		response.andExpect(status().isOk())
	}
}