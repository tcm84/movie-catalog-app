package com.moviecatalog.movieratings.restcontrollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.movieratings.services.MovieRatingServiceImpl
import com.moviecatalog.movies.repo.MovieRepository
import com.moviecatalog.repo.testconfig.RepoTestConfig

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
	MovieRatingServiceImpl])
@WebMvcTest(MovieRatingControllerImpl)
class MovieRatingControllerHappyPathATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	/**
	 * movieratingId is generated automatically but we just include it
	 * here (its returned by the endpoint anyway) so that we can check
	 * if a director exists by Id when calling the endpoints
	 * */
	@Shared
	def movieRatings =
	[
		'''{
				"movieratingId": 1,
				"movieClassification": "_18A",
				"description": "Suitable only for adults"
			}'''
		,
		'''{
				"movieratingId": 2,
				"movieClassification": "_12A",
				"description": "Suitable for 12 years and over - optional accompanying adult"
			}''',
			,
			'''{
				"movieratingId": 3,
				"movieClassification": "_15",
				"description": "Suitable for 15 years and over "
			}'''
	]
	
	def "Adding new movie ratings to this catalog"(){
		when: "I request to add a new movie rating"
		def response = mockMvc.perform(post("/movieratings/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(movieRatings[0]))
		
		then: "it should be added to this catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(movieRatings[0]))
	}
	
	def "Updating movie ratings in this catalog"(){		
		given:"a movie rating exists in this catalog"
		mockMvc.perform(post("/movieratings/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieRatings[1]))
		when: "I make a request to update the movies ratings' details"
		movieRatings[1] =
		'''{
				"movieratingId": 2,
				"movieClassification": "_12A",
				"description": "Suitable for 12 years and over - optional accompanying adult"
			}'''
		def response = mockMvc.perform(post("/movieratings/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieRatings[1]))
		
		then: "the movie ratings details should be updated in this catalog"
		response.andExpect(status().isOk())
				.andExpect(content().json(movieRatings[1]))
	}
	
	def "Deleting movie ratings from this catalog"(){
		given:"a movie rating exists in this catalog that I want to delete"
		mockMvc.perform(post("/movieratings/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieRatings[2]))
			
		when: "I make a request to delete the movie rating from this catalog"
		def response = mockMvc.perform(delete("/movieratings/delete/3"))
		
		then: "the movie ratings details should be deleted this this catalog"
		response.andExpect(status().isOk())
	}
}