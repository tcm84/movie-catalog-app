package com.moviecatalog.movies.restcontollers

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
class MovieControllerUnderMovieDirectorsEdgeCasesATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def movieDirector =
	'''{
				"moviedirectorId": 1,
				"name": "Quentin Tarantino",
				"dob": "27/03/1965",
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
	@Rollback
	def "Should not beable to add a movie that already exists in this catalog"(){
		given: "a movie has been already added to this catalog under that director"
	   mockMvc.perform(post("/moviedirectors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(filmography[0]))
			
		when: "I request that it be added again to this catalog under that director"
		def response = mockMvc.perform(post("/moviedirectors/1/movies/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(filmography[0]))
		
		then: "an exception should be returned indicating that the movie already exists"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie with this movieId already exists in this catalog"))
	}
	
	def "Should not beable to update a movie that doesn't exist in this catalog"(){
		given:"a movie that doesn't exist under the director in this catalog"
		def nonexistentMovie =
		'''{
				"movieId": 10,
				"title": "Kill Bill Volume 4",
				"movieclassification": "_18A",
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
				.andExpect(status().reason("Movie not found in this catalog"))
	}
	
	def "Should not beable to search for a directors filmography if the director doesnt exist in this catalog"(){
		when: "I search for a filmography for a director that doesn't in this catalog"
		def response = mockMvc.perform(post("/moviedirectors/99/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "all the directors movies should have been returned"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Filmography not found in this catalog for this director"))
	}
}