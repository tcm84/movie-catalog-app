package com.moviecatalog.movies.restcontollers

import groovy.json.JsonOutput

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.moviedirectors.restcontrollers.MovieDirectorControllerImpl
import com.moviecatalog.moviedirectors.services.MovieDirectorServiceImpl
import com.moviecatalog.movies.repo.MovieRepository
import com.moviecatalog.movies.restcontrollers.MovieControllerImpl
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
	MovieServiceImpl,
	MovieDirectorServiceImpl])
@WebMvcTest(controllers=[MovieControllerImpl,
						 MovieDirectorControllerImpl])
class MovieControllerATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def quentinTarantino =
	'''{
				"moviedirectorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"

			}'''
	
	@Shared
	def quentinTarantinoFilmography =
		[
			'''{
				"movieId": 1,
				"title": "Hateful Eight",
				"rating": "_18",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/10/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			}''',

			'''{
				"movieId": 2,
				"title": "Kill Bill Volume 1",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "10/10/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}''',

			'''{
				"movieId": 3,
				"title": "Kill Bill Volume 2",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "16/04/2004",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}'''
		]
	
	def setup() {
		mockMvc.perform(post("/moviedirectors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(quentinTarantino))
	}

	def "Adding new movies to the catalog under an existing director"(){
		when: "I add one of their movies to the catalog"
		def response = mockMvc.perform(post("/moviedirectors/1/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(quentinTarantinoFilmography[0]))
		
		then: "it should be added to the catalog under their id"
		response.andExpect(status().isOk())		
				.andExpect(content().json(quentinTarantinoFilmography[0]))
	}
	
	def "Should not beable to add a movie that already exists in the catalog"(){
		given: "a movie has been already added to the catalog under that director"
	   mockMvc.perform(post("/moviedirectors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(quentinTarantinoFilmography[1]))
			
		when: "I request that it be added again to the catalog under that director"
		def response = mockMvc.perform(post("/moviedirectors/1/movies/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(quentinTarantinoFilmography[1]))
		
		then: "an exception should be returned indicating that the movie already exists"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie already exists in the catalog"))
	}
	
	def "Updating movies in the catalog"(){		
		given:"a movie exists in the catalog under that director that needs updated"
		mockMvc.perform(post("/moviedirectors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(quentinTarantinoFilmography[2]))
		when: "I make a request to update the movies' details"
		def updatedkillBillVol2 =
		'''{
				"movieId": 3,
				"title": "Kill Bill Volume 2",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "16/04/2004",
				"cast": [
					"Uma Thurman",
					"David Carradine",
					"Michael Madsen"
				]
			}'''
		def response = mockMvc.perform(post("/moviedirectors/1/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedkillBillVol2))
		
		then: "the movies details should be updated in the catalog under that director"
		response.andExpect(status().isOk())
				.andExpect(content().json(updatedkillBillVol2))
	}
	
	def "Should not beable to update a movie that doesn't exist in the catalog"(){
		given:"a movie that doesn't exist under the director in the catalog"
		def nonexistentMovie =
		'''{
				"movieId": 10,
				"title": "Kill Bill Volume 4",
				"rating": "_18A",
				"genre": "ACTION",
				"releasedate": "10/10/2008",
				"cast": [
					"Urma Thurman"
				]
			}'''
		
		when: "I make a request to update the movies' details under that director"
		def response = mockMvc.perform(post("/moviedirectors/1/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentMovie))
		
		then: "an exception should be returned indicating that the movie was not found"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie not found in the catalog"))
	}
	
	def "Deleting movies from the catalog"(){		
		given:"a movie exists in the catalog under that director that I want to delete"
		mockMvc.perform(post("/moviedirectors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(quentinTarantinoFilmography[1]))
			
		when: "I make a request to delete the movie from the catalog"
		def response = mockMvc.perform(delete("/moviedirectors/1/movies/delete/2"))
		
		then: "the movies details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
	
	def "Should not beable to search for a directors filmography if the director doesnt exist in the catalog"(){	
		when: "I search for a filmography for a director that doesn't in the catalog"
		def response = mockMvc.perform(post("/moviedirectors/99/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "all the directors movies should have been returned"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("MovieDirector not found in the catalog"))
	}
	
	def "Should return all a directors movies when a search is done with their id"(){
		given: "all a directors movies have been added to the catalog under them"
		quentinTarantinoFilmography.forEach({movieDetails ->
			mockMvc.perform(post("/moviedirectors/1/movies/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(movieDetails))
		})
		
		when: "I search for all the directors movies"
		def response = mockMvc.perform(post("/moviedirectors/1/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "all the directors movies should have been returned"
		def expectedQuentinTarantinoFilmography =
		'''[
			{
				"movieId": 1,
				"title": "Hateful Eight",
				"rating": "_18",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/10/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			},

			{
				"movieId": 2,
				"title": "Kill Bill Volume 1",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "10/10/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			},

			{
				"movieId": 3,
				"title": "Kill Bill Volume 2",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "16/04/2004",
				"cast": [
					"Uma Thurman",
					"David Carradine",
					"Michael Madsen"
				]
			}
		]'''
		response.andExpect(status().isOk())
				.andExpect(content().json(expectedQuentinTarantinoFilmography))
	}
	
	@Unroll
	def "Requests should be rejected if they don't pass validation requirements #description"(){	
		given: "a request containing no valid data"
		def invalidRequest =
		'''{
			}'''
		
		when: "I make the request to #endpointURI"
		def response = mockMvc.perform(post(endpointURI)
			.contentType(MediaType.APPLICATION_JSON)
			.content(invalidRequest))
		
		then: "the request should be rejected as a bad request"
		response.andExpect(status().isBadRequest())
		
		and: "the response should contain all the error messages for the failed validations"
		MethodArgumentNotValidException ex = response.andReturn().resolvedException
		def validationErrors = ex.getBindingResult().allErrors
		validationErrors.size() == 3
		Set actualErrorMsgs = []
		validationErrors.forEach({error -> actualErrorMsgs += error.getDefaultMessage()})
		Set expectedErrorMsgs = ["Movie title must not be empty", "Cast must contains at least one actor",
			"Releasedate should not be null"]
		actualErrorMsgs == expectedErrorMsgs
		
		where:
		description                                                      | endpointURI
		"for adding a new movie under a director to the catalog"         | "/moviedirectors/1/movies/add"
		"for updating an existing movie under a director in the catalog" | "/moviedirectors/1/movies/update"
	}
}