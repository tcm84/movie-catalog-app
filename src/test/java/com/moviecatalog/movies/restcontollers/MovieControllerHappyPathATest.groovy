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
class MovieControllerHappyPathATest extends Specification {
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
	
	@Shared
	def filmography =
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
			.content(movieDirector))
	}

	def "Adding new movies to the catalog under an existing director"(){
		when: "I add one of their movies to the catalog"
		def response = mockMvc.perform(post("/moviedirectors/1/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(filmography[0]))
		
		then: "it should be added to the catalog under their id"
		response.andExpect(status().isOk())		
				.andExpect(content().json(filmography[0]))
	}
	
	def "Updating movies in the catalog"(){		
		given:"a movie exists in the catalog under that director that needs updated"
		mockMvc.perform(post("/moviedirectors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(filmography[2]))
		when: "I make a request to update the movies' details"
		filmography[2] =
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
							  .content(filmography[2]))
		
		then: "the movies details should be updated in the catalog under that director"
		response.andExpect(status().isOk())
				.andExpect(content().json(filmography[2]))
	}
	
	def "Deleting movies from the catalog"(){		
		given:"a movie exists in the catalog under that director that I want to delete"
		mockMvc.perform(post("/moviedirectors/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(filmography[1]))
			
		when: "I make a request to delete the movie from the catalog"
		def response = mockMvc.perform(delete("/moviedirectors/1/movies/delete/2"))
		
		then: "the movies details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
	
	def "Should return all a directors movies when a search is done with their id"(){
		given: "all a directors movies have been added to the catalog under them"
		filmography.forEach({movieDetails ->
			mockMvc.perform(post("/moviedirectors/1/movies/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(movieDetails))
		})
		
		when: "I search for all the directors movies"
		def response = mockMvc.perform(post("/moviedirectors/1/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "all the directors movies should have been returned"
		def expectedFilmography =
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
				.andExpect(content().json(expectedFilmography))
	}
}