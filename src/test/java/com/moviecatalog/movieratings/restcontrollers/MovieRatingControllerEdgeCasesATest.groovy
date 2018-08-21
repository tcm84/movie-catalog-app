package com.moviecatalog.movieratings.restcontrollers

import groovy.json.JsonOutput

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.moviedirectors.restcontrollers.MovieDirectorControllerImpl
import com.moviecatalog.movieratings.services.MovieRatingServiceImpl
import com.moviecatalog.movies.repo.MovieRepository
import com.moviecatalog.movieratings.restcontrollers.MovieRatingControllerImpl
import com.moviecatalog.movies.services.MovieServiceImpl
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
@Transactional
class MovieRatingControllerEdgeCasesATest extends Specification {
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
				"movieClassification": "_18",
				"description": "Suitable only for adults"
			}'''
			,
			'''{
				"movieratingId": 2,
				"movieClassification": "_12",
				"description": "Suitable for 12 years and over"
			}'''
		]
	@Rollback
	def "Should not beable to add a movie rating that already exists in this catalog"(){
		given: "a movie rating has been already added to this catalog"
	   mockMvc.perform(post("/movieratings/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieRatings[0]))
			
		when: "I request that it be added again to this catalog"
		def response = mockMvc.perform(post("/movieratings/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieRatings[0]))
		
		then: "an exception should be returned indicating that the movie already exists"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie rating already exists in this catalog"))
	}
	
	def "Should not beable to update a movie rating that doesn't exist in this catalog"(){
		when: "I make a request to update a nonexistent movie ratings' details"
		def response = mockMvc.perform(post("/movieratings/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieRatings[1]))
		
		then: "an exception should be returned in the response indicating the movie rating was not found"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie rating not found in this catalog"))
	}
}