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
@Transactional
class MovieControllerUnderMovieDirectorsHappyPathATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def movieDirector =
	'''{
				"moviedirectorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"
		}'''
	
	/**
	 * movieId is generated automatically but we just include it
	 * here (its returned by the endpoint anyway) so that we can check
	 * if a movie exists by Id when calling the endpoints
	 * */
	@Shared
	def filmography =
		[
			'''{
				"movieId": 1,
				"title": "Hateful Eight",
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
			.content(movieDirector))
	}

	def "Adding new movies to this catalog under an existing director"(){
		when: "I add one of their movies to this catalog"
		def response = mockMvc.perform(post("/moviedirectors/1/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(filmography[0]))
		
		then: "it should be added to this catalog under their id"
		response.andExpect(status().isOk())		
				.andExpect(content().json(filmography[0]))
	}
	
	def "Updating movies in this catalog"(){		
		given:"a movie exists in this catalog under this director that needs updated"
		mockMvc.perform(post("/moviedirectors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(filmography[1]))
		when: "I make a request to update the movies' cast"
		filmography[1] =
		'''{
				"movieId": 2,
				"title": "Kill Bill Volume 1",
				"genre": "ACTION",
				"releasedate": "10/10/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine",
					"Samuel L Jackson"
				]
			}'''
		def response = mockMvc.perform(post("/moviedirectors/1/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(filmography[1]))
		
		then: "the movies details should be updated in this catalog under this director"
		response.andExpect(status().isOk())
				.andExpect(content().json(filmography[1]))
	}
	
	def "Deleting movies from this catalog"(){		
		given:"a movie exists in the catalog under this director that I want to delete"
		mockMvc.perform(post("/moviedirectors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(filmography[2]))
			
		when: "I make a request to delete the movie from this catalog"
		def response = mockMvc.perform(delete("/movies/delete/3"))
		
		then: "the movies details should be deleted from this catalog"
		response.andExpect(status().isOk())
	}
	
	@Rollback
	def "Should return all a directors movies when a search is done with their id"(){
		given: "all a directors movies have been added to this catalog under them"
		//filmography[0] and filmography[1] have been added in previous test cases
		mockMvc.perform(post("/moviedirectors/1/movies/add")
		.contentType(MediaType.APPLICATION_JSON)
		.content(filmography[2]))
		
		when: "I search for all the directors movies"
		def response = mockMvc.perform(post("/moviedirectors/1/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "all the directors movies should have been returned"
		def expectedFilmography =
		'''[

			{
				"movieId": 3,
				"title": "Kill Bill Volume 2",
				"genre": "ACTION",
				"releasedate": "16/04/2004",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}
		]'''
		response.andExpect(status().isOk())
				.andExpect(content().json(expectedFilmography))
	}
}