package com.moviecatalog.movies.restcontollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.directors.restcontrollers.DirectorControllerImpl
import com.moviecatalog.directors.services.DirectorServiceImpl
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
	DirectorServiceImpl])
@WebMvcTest(controllers=[MovieControllerImpl,
						 DirectorControllerImpl])
class MovieControllerATest extends Specification {
	@Autowired
	private MockMvc mockMvc

	def "Adding new movies to the catalog under an existing director"(){
		setup: "a director exists in the catalog"
		def quentinTarantino =
		'''{
				"directorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"

			}'''
		mockMvc.perform(post("/directors/add")
			  .contentType(MediaType.APPLICATION_JSON)
			  .content(quentinTarantino))
		
		when: "I add one of their movies to the catalog"
		def hatefulEight =
		'''{
				"title": "Hateful Eight",
				"rating": "_18",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/10/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			}'''
		def response = mockMvc.perform(post("/directors/1/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(hatefulEight))
		
		then: "it should be added to the catalog under their id"
		response.andExpect(status().isOk())		
				.andExpect(content().json(hatefulEight))
	}
	
	def "Should not beable to add a movie that already exists in the catalog"(){
		setup: "a director exists in the catalog"
		def quentinTarantino =
		'''{
				"directorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"

			}'''
		mockMvc.perform(post("/directors/add")
			  .contentType(MediaType.APPLICATION_JSON)
			  .content(quentinTarantino))
		
		and: "a movie has been already added to the catalog under that director"
		def killBillVol1 =
		'''{
				"movieId": 1,
				"title": "Kill Bill Volume 1",
				"director": "Quentin Tarantino",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "10/10/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}'''
	   mockMvc.perform(post("/directors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(killBillVol1))
			
		when: "I request that it be added again to the catalog under that director"
		def response = mockMvc.perform(post("/directors/1/movies/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(killBillVol1))
		
		then: "an exception should be returned indicating that the movie already exists"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie already exists in the catalog"))
	}
	
	def "Updating movies in the catalog"(){
		setup: "a director exists in the catalog"
		def quentinTarantino =
		'''{
				"directorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"

			}'''
		mockMvc.perform(post("/directors/add")
			  .contentType(MediaType.APPLICATION_JSON)
			  .content(quentinTarantino))
		
		and:"a movie exists in the catalog under that director that needs updated"
		def killBillVol2 =
		'''{
				"movieId": 1,
				"title": "Kill Bill Volume 2",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "16/04/2004",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}'''
		mockMvc.perform(post("/directors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(killBillVol2))
		when: "I make a request to update the movies' details"
		def updatedkillBillVol2 =
		'''{
				"movieId": 1,
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
		def response = mockMvc.perform(post("/directors/1/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedkillBillVol2))
		
		then: "the movies details should be updated in the catalog under that director"
		response.andExpect(status().isOk())
				.andExpect(content().json(updatedkillBillVol2))
	}
	
	def "Should not beable to update a movie that doesn't exist in the catalog"(){
		given: "a director exists in the catalog"
		def quentinTarantino =
		'''{
				"directorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"

			}'''
		mockMvc.perform(post("/directors/add")
			  .contentType(MediaType.APPLICATION_JSON)
			  .content(quentinTarantino))
		
		and:"a movie that doesn't exist under the director in the catalog"
		def nonexistentMovie =
		'''{
				"movieId": 2,
				"title": "Kill Bill Volume 4",
				"rating": "_18A",
				"genre": "ACTION",
				"releasedate": "10/10/2008",
				"cast": [
					"Urma Thurman"
				]
			}'''
		
		when: "I make a request to update the movies' details under that director"
		def response = mockMvc.perform(post("/directors/1/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentMovie))
		
		then: "an exception should be returned indicating that the movie was not found"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie not found in the catalog"))
	}
	
	def "Deleting movies from the catalog"(){
		given: "a director exists in the catalog"
		def quentinTarantino =
		'''{
				"directorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"

			}'''
		mockMvc.perform(post("/directors/add")
			  .contentType(MediaType.APPLICATION_JSON)
			  .content(quentinTarantino))
		
		and:"a movie exists in the catalog under that director that I want to delete"
		def killBillVol3 =
		'''{
				"title": "Kill Bill Volume 3",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "11/10/2006",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}'''
		mockMvc.perform(post("/directors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(killBillVol3))
			
		
		when: "I make a request to delete the movie from the catalog"
		def response = mockMvc.perform(delete("/directors/1/movies/delete/1"))
		
		then: "the movies details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
	
	@Unroll
	def "Requests should be rejected if they don't pass validation requirements #description"(){
		given: "a director exists in the catalog"
		def quentinTarantino =
		'''{
				"directorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"

			}'''
		mockMvc.perform(post("/directors/add")
			  .contentType(MediaType.APPLICATION_JSON)
			  .content(quentinTarantino))
		
		and: "a request containing no valid data"
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
		"for adding a new movie under a director to the catalog"         | "/directors/1/movies/add"
		"for updating an existing movie under a director in the catalog" | "/directors/1/movies/update"
	}
}