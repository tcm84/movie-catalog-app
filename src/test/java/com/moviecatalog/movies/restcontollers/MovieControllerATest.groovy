package com.moviecatalog.movies.restcontollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
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
	MovieServiceImpl])
@WebMvcTest(MovieControllerImpl)
class MovieControllerATest extends Specification {
	@Autowired
	private MockMvc mockMvc

	def "Adding new movies to the catalog"(){
		when: "I request a new movie gets added to the catalog"
		def hatefulEight =
		'''{
				"title": "Hateful Eight",
				"director": "Quentin Tarantino",
				"rating": "_18",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/10/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			}'''
		def response = mockMvc.perform(post("/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(hatefulEight))
		
		then: "it should be added to the catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(hatefulEight))
	}
	
	def "Should not beable to add a movie that already exists in the catalog"(){
		given: "a movie that already exists in the catalog"
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
	   mockMvc.perform(post("/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(killBillVol1))
			
		when: "I request that it be added again when it already is in the catalog"
		def response = mockMvc.perform(post("/movies/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(killBillVol1))
		
		then: "I should not be able to added it again"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie already exists in the catalog"))
	}
	
	def "Updating movies in the catalog"(){
		setup:"A movie exists in the catalog that needs updated"
		def killBillVol2 =
		'''{
				"title": "Kill Bill Volume 2",
				"director": "Quentin Tarantino",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "16/04/2004",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}'''
		mockMvc.perform(post("/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(killBillVol2))
		def updatedkillBillVol2 =
		'''{
				"movieId": 1,
				"title": "Kill Bill Volume 2",
				"director": "Quentin Tarantino",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "16/04/2004",
				"cast": [
					"Uma Thurman",
					"David Carradine",
					"Michael Madsen"
				]
			}'''
		
		when: "I make a request to update the movies' details"
		def response = mockMvc.perform(post("/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedkillBillVol2))
		
		then: "the movies details should be updated in the catalog"
		response.andExpect(status().isOk())
				.andExpect(content().json(updatedkillBillVol2))
	}
	
	def "Deleting movies from the catalog"(){
		given:"a movie exists in the catalog that I want to delete"
		def killBillVol3 =
		'''{
				"title": "Kill Bill Volume 3",
				"director": "Quentin Tarantino",
				"rating": "_18",
				"genre": "ACTION",
				"releasedate": "11/10/2006",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}'''
		mockMvc.perform(post("/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(killBillVol3))
			
		
		when: "I make a request to delete the movie from the catalog"
		def response = mockMvc.perform(delete("/movies/delete/1"))
		
		then: "the movies details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
	
	def "Should not beable to update a movie that doesn't exist in the catalog"(){
		given:"A movie that doesn't exist in the catalog"
		def nonexistentMovie =
		'''{
				"movieId": 1,
				"title": "Kill Bill Volume 1",
				"director": "Quentin Tarantino",
				"rating": "_18A",
				"genre": "ACTION",
				"releasedate": "10/10/2003",
				"cast": [
					"Urma Thurman"
				]
			}'''
		
		when: "I make a request to update the movies' details"
		def response = mockMvc.perform(post("/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentMovie))
		
		then: "I should not beable to update them as the movie is not in the catalog"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie not found in the catalog"))
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
		validationErrors.size() == 4
		Set actualErrorMsgs = []
		validationErrors.forEach({error -> actualErrorMsgs += error.getDefaultMessage()})
		Set expectedErrorMsgs = ["Movie title must not be empty", "Director must not be empty", 
			 				     "Cast must contains at least one actor","Releasedate should not be null"]
		actualErrorMsgs == expectedErrorMsgs
		
		where:
		description                                     | endpointURI
		"when adding a new movie to the catalog"        | "/movies/add"
		"when updating an existing movie in the catalog"| "/movies/update"
	}
}