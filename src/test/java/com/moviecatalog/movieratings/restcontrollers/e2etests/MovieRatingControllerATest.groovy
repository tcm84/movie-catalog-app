package com.moviecatalog.movieratings.restcontrollers.e2etests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.movieratings.restcontrollers.MovieRatingControllerImpl
import com.moviecatalog.movieratings.services.MovieRatingServiceImpl
import com.moviecatalog.movies.repo.MovieRepository
import com.moviecatalog.repo.testconfig.RepoTestConfig
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

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
@ContextConfiguration(classes=[MovieCatalogApplication,MovieRatingServiceImpl])
@WebMvcTest(MovieRatingControllerImpl)
class MovieRatingControllerATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def addEndpointURI = "/movieratings/add"
	
	def "Should not beable to add a movie rating that already exists in this catalog"(){
		given: "a movie rating that already exists in this catalog"
		def newMovieRating = '''{
				"movieClassification": "_18A",
				"description": "Suitable only for adults"
			}'''
		def addResponse = mockMvc.perform(post(addEndpointURI)
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovieRating))
			
		when: "I try to add the same movie rating again a second time in a row"
		def response = mockMvc.perform(post(addEndpointURI)
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(addResponse.andReturn().getResponse().getContentAsString()))
		
		then: "an exception should be returned in the response"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie rating already exists in this catalog"))
	}
	
	def "Should not beable to update a movie rating that doesn't exist in this catalog"(){
		when: "I try to update a movie ratings' details that are not in this catalog"
		def nonexistentMovieRating = '''{
				"movieClassification": "_12",
				"description": "Suitable for 12 years and over"
			}'''
		def response = mockMvc.perform(post("/movieratings/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentMovieRating))
		
		then: "an exception should be returned in the response"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie rating not found in this catalog"))
	}
	
	def "Should be able to add new movie ratings to this catalog"(){
		when: "I make a request to add a new movie rating to this catalog"
		def newMovieRating = '''{
				"movieClassification": "_18A",
				"description": "Suitable only for adults"
			}'''
		def response = mockMvc.perform(post(addEndpointURI)
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(newMovieRating))
		
		then: "it should be added to this catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(newMovieRating))
	}
	
	def "Should be able to update movie ratings that exist in this catalog"(){		
		given:"a movie rating already exists in this catalog"
		def newMovingRating = '''{
				"movieClassification": "_12A",
				"description": "Suitable for 12 years and over"
			}'''
		def addResponse = mockMvc.perform(post(addEndpointURI)
								 .contentType(MediaType.APPLICATION_JSON)
								 .content(newMovingRating))
		when: "I make a request to update the movies ratings' details"
		def movieRatingDetailsMap = 
					new JsonSlurper().parseText(addResponse.andReturn()
		 									               .getResponse()
														   .getContentAsString())
		//We are updating the movie ratings description in this test
		movieRatingDetailsMap["description"] = "Suitable for 12 years and over - optional accompanying adult"
		
		def updatedMovieRatingsDetails =
							new JsonOutput().toJson(movieRatingDetailsMap)
							
		def response = mockMvc.perform(post("/movieratings/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedMovieRatingsDetails))
		
		then: "the movie ratings details should be updated"
		response.andExpect(status().isOk())
				.andExpect(content().json(updatedMovieRatingsDetails))
	}
	
	def "Should be able to delete movie ratings that exist in this catalog"(){
		given:"a movie rating exists in this catalog"
		def newMovieRating = '''{
				"movieClassification": "_15",
				"description": "Suitable for 15 years and over "
			}'''
		def addResponse = mockMvc.perform(post(addEndpointURI)
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovieRating))
			
		when: "I make a request to delete the movie ratings' details"
		def movieRatingDetailsMap =
			new JsonSlurper().parseText(addResponse.andReturn()
												   .getResponse()
												   .getContentAsString())
		def movieratingId =  movieRatingDetailsMap["movieratingId"]
			
		def response = mockMvc.perform(delete("/movieratings/delete/" + movieratingId))
		
		then: "the movie ratings details should be deleted"
		response.andExpect(status().isOk())
	}
}