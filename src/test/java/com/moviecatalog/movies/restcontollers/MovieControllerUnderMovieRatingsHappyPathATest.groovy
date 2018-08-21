package com.moviecatalog.movies.restcontollers

import groovy.json.JsonOutput

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
@ContextConfiguration(classes=[
	MovieCatalogApplication,
	MovieServiceImpl,
	MovieRatingServiceImpl])
@WebMvcTest(controllers=[MovieControllerImpl,
						 MovieRatingControllerImpl])
class MovieControllerUnderMovieRatingsHappyPathATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def movieRating =
	'''{
				"movieratingId": 1,
				"movieClassification": "_18",
				"description": "Suitable only for adults"
		}'''
	
	/**
	 * movieId is generated automatically but we just include it
	 * here (its returned by the endpoint anyway) so that we can check
	 * if a movie exists by Id when calling the endpoints
	 * */
	@Shared
	def movieList =
		[
			'''{
				"movieId": 1,
				"title": "Jackie Brown",
				"genre": "CRIME_FICTION",
				"releasedate": "11/10/2003",
				"cast": [
					"Samuel L Jackson"
				]
			}''',

			'''{
				"movieId": 2,
				"title": "Predator",
				"genre": "ACTION",
				"releasedate": "10/10/1987",
				"cast": [
					"Arnold Schwazenegger"
				]
			}''',

			'''{
				"movieId": 3,
				"title": "Die Hard",
				"genre": "ACTION",
				"releasedate": "16/04/1988",
				"cast": [
					"Bruce Willis"
				]
			}'''
		]
	
	def setup() {
		mockMvc.perform(post("/movieratings/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieRating))
	}

	def "Adding new movies to this catalog under an existing movie rating"(){
		when: "I add request to add a new movie under a movie rating"
		def response = mockMvc.perform(post("/movieratings/1/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(movieList[0]))
		
		then: "it should be added to this catalog under the rating"
		response.andExpect(status().isOk())		
				.andExpect(content().json(movieList[0]))
	}
	
	def "Updating movies in this catalog"(){		
		given:"a movie exists in this catalog under a rating that needs updated"
		mockMvc.perform(post("/movieratings/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieList[1]))
		when: "I make a request to update the movies' details"
		movieList[1] =
		'''{
				"movieId": 2,
				"title": "Predator",
				"genre": "SCIENCE_FICTION",
				"releasedate": "04/10/1987",
				"cast": [
					"Arnold Schwazenegger"
				]
			}'''
		def response = mockMvc.perform(post("/movieratings/1/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieList[1]))
		
		then: "the movies details should be updated in this catalog under this rating"
		response.andExpect(status().isOk())
				.andExpect(content().json(movieList[1]))
	}
	
	def "Deleting movies from this catalog"(){		
		given:"a movie exists in this catalog under this rating that I want to delete"
		mockMvc.perform(post("/movieratings/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieList[2]))
			
		when: "I make a request to delete the movie from this catalog"
		def response = mockMvc.perform(delete("/movies/delete/3"))
		
		then: "the movies details should be deleted from this catalog"
		response.andExpect(status().isOk())
	}
	
	def "Should return all movies of a particular rating when a search is done with its id"(){
		given: "all this ratings movies have been added to this catalog under it"
		//movieList[0] and movieList[1] have been added in previous test cases
		mockMvc.perform(post("/movieratings/1/movies/add")
		.contentType(MediaType.APPLICATION_JSON)
		.content(movieList[2]))
		
		when: "I search for all the ratings movies"
		def response = mockMvc.perform(post("/movieratings/1/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "all the ratings movies should have been returned"
		def expectedMovieList =
		'''[
			{
				"movieId": 1,
				"title": "Jackie Brown",
				"genre": "CRIME_FICTION",
				"releasedate": "11/10/2003",
				"cast": [
					"Samuel L Jackson"
				]
			},
			{
				"movieId": 2,
				"title": "Predator",
				"genre": "SCIENCE_FICTION",
				"releasedate": "04/10/1987",
				"cast": [
					"Arnold Schwazenegger"
				]
			},{
				"movieId": 3,
				"title": "Die Hard",
				"genre": "ACTION",
				"releasedate": "16/04/1988",
				"cast": [
					"Bruce Willis"
				]
			}
		]'''
	
		response.andExpect(status().isOk())
				.andExpect(content().json(expectedMovieList))
	}
}