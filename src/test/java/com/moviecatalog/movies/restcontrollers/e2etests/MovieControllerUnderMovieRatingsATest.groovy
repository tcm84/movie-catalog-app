package com.moviecatalog.movies.restcontrollers.e2etests

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.transaction.annotation.Propagation

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.movieratings.restcontrollers.MovieRatingControllerImpl
import com.moviecatalog.movieratings.services.MovieRatingServiceImpl
import com.moviecatalog.movies.repo.MovieRepository
import com.moviecatalog.movies.restcontrollers.MovieControllerImpl
import com.moviecatalog.movies.services.MovieServiceImpl
import com.moviecatalog.repo.testconfig.RepoTestConfig

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.junit.After
import org.springframework.http.MediaType;

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Unroll

@Import(RepoTestConfig)
@ContextConfiguration(classes=[MovieCatalogApplication,MovieServiceImpl,MovieRatingServiceImpl])
@WebMvcTest(controllers=[MovieControllerImpl,MovieRatingControllerImpl])
class MovieControllerUnderMovieRatingsATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	def movieRatingId
	
	/**
	 * setup runs before each test is run. A new instance of MovieRatingDetails is created
	 * in the DB for each test. This is nice as the tests are more independent and what is 
	 * getting test is more atomic in nature and less fragile
	 **/
	def setup() {
		def movieRating =
		'''{
				"movieClassification": "_18",
				"description": "Suitable only for adults"
		}'''
		def addResponse = mockMvc.perform(post("/movieratings/add")
							.contentType(MediaType.APPLICATION_JSON)
							.content(movieRating))
		def movieRatingMap =
			new JsonSlurper().parseText(addResponse.andReturn()
												   .getResponse()
												   .getContentAsString())
		movieRatingId =  movieRatingMap["movieratingId"]
		
	}

	def "Should be able to add new movies to this catalog under an existing movie rating"(){
		when: "I make a request to add a new movie to this catalog under this movie rating"
		def newMovie = '''{
				"title": "Jackie Brown",
				"genre": "CRIME_FICTION",
				"releasedate": "11/10/2003",
				"cast": [
					"Samuel L Jackson"
				]
			}'''
		def response = mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(newMovie))
		
		then: "it should be added to this catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(newMovie))
	}
	
	def "Should not beable to add a movie that already exists in this catalog for this movie rating"(){
		given: "a movie that already exists in this catalog under this movie rating"
		def newMovie = '''{
				"title": "Alien",
				"genre": "SCIENCE_FICTION",
				"releasedate": "11/10/1979",
				"cast": [
					"S Weaver"
				]
			}'''
		def addResponse = mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/add")
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovie))
			
		when: "I retry to add the same movie again a second time in a row"
		def response = mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(addResponse.andReturn().getResponse().getContentAsString()))
		
		then: "an exception should be returned"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie with this movieId already exists in this catalog"))
	}

	
	def "Should be able to update movies that exist in this catalog under this movie rating"(){		
		setup:"a movie already exists in this catalog under this movie rating"
		def newMovie = '''{
				"title": "Predator",
				"genre": "SCIENCE_FICTION",
				"releasedate": "04/10/1987",
				"cast": [
					"Arnold Schwazenegger"
				]
			}'''
		def addResponse = mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/add")
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovie))
		when: "I make a request to update the movies' details"
		def movieDetailsMap =
			new JsonSlurper().parseText(addResponse.andReturn()
												   .getResponse()
												   .getContentAsString())
		//We are updating the movies releasedate in this test
		movieDetailsMap["releasedate"] = "01/09/2003"
		
		def updatedMovieDetails =
						new JsonOutput().toJson(movieDetailsMap)
						
		def response = mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedMovieDetails))
		
		then: "the movie details should be updated under this movie rating"
		response.andExpect(status().isOk())
				//.andExpect(content().json(updatedMovieDetails)) TODO: Need serializer for movieRatingsPart
	}
	
	def "Should not beable to update a movie that doesn't exist in this catalog"(){
		given:"a movie that doesn't exist under this rating in this catalog"
		def nonexistentMovie =
		'''{
				"title": "Die Hard 3",
				"genre": "ACTION",
				"releasedate": "01/01/1995",
				"cast": [
					"Bruce Willis"
				]
			}'''
		
		when: "I make a request to update the movies' details under this movie rating"
		def response = mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentMovie))
		
		then: "an exception should be returned"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie not found in this catalog for this movieId"))
	}
	
	def "Should be able to delete movie that exist in this catalog under this movie rating"(){		
		setup:"a movie exists in this catalog under this movie rating"
		def newMovie = '''{
				"title": "Die Hard",
				"genre": "ACTION",
				"releasedate": "16/04/1988",
				"cast": [
					"Bruce Willis"
				]
			}'''
		def addResponse = mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/add")
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovie))			
			
		when: "I make a request to delete the movies' details"
		def movieDetailsMap =
			new JsonSlurper().parseText(addResponse.andReturn()
												   .getResponse()
												   .getContentAsString())
	    def movieId =  movieDetailsMap["movieId"]
		
		def response = mockMvc.perform(delete("/movies/delete/" + movieId))
		
		then: "the movies details should be deleted"
		response.andExpect(status().isOk())
	}
	
	def "Should not get a movie list for a movie rating that doesn't exist in this catalog"(){
		when: "I search for a movie list for a movie rating that that doesn't exist in this catalog"
		def response = mockMvc.perform(post("/movieratings/999/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "an exception should be returned"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie list not found in this catalog for this rating"))
	}
	
	def "Should return movie list for an existing movie rating in this catalog"(){		
		given: "a movie list that exists in this catalog for this movie rating"
		def movielist =
		[
			'''{
				"title": "Jackie Brown",
				"genre": "CRIME_FICTION",
				"releasedate": "11/10/2003",
				"cast": [
					"Samuel L Jackson"
				]
			}''',
			'''{
				"title": "Predator",
				"genre": "SCIENCE_FICTION",
				"releasedate": "01/09/2003",
				"cast": [
					"Arnold Schwazenegger"
				]
			}''',
			'''{
				"title": "Alien",
				"genre": "SCIENCE_FICTION",
				"releasedate": "11/10/1979",
				"cast": [
					"S Weaver"
				]
			}'''
		]
		
		movielist.forEach({movieDetails ->
			mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/add")
					.contentType(MediaType.APPLICATION_JSON)
					.content(movieDetails))
		})
		when: "I search for the movie list for this movie rating"
		def response = mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "all the ratings movies should have been returned"
		def expectedMovieList =
		'''
		[
			{
				"title": "Jackie Brown",
				"genre": "CRIME_FICTION",
				"releasedate": "11/10/2003",
				"cast": [
					"Samuel L Jackson"
				]
			},

			{
				"title": "Predator",
				"genre": "SCIENCE_FICTION",
				"releasedate": "01/09/2003",
				"cast": [
					"Arnold Schwazenegger"
				]
			},
			{
				"title": "Alien",
				"genre": "SCIENCE_FICTION",
				"releasedate": "11/10/1979",
				"cast": [
					"S Weaver"
				]
			}
		]'''
		response.andExpect(status().isOk())
				.andExpect(content().json(expectedMovieList))
	}
}