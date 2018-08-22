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
@ContextConfiguration(classes=[MovieCatalogApplication,MovieServiceImpl,MovieDirectorServiceImpl])
@WebMvcTest(controllers=[MovieControllerImpl, MovieDirectorControllerImpl])
class MovieControllerUnderMovieDirectorsATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	def movieDirectorId
	
    /**
     * setup runs before each test is run. A new instance of MovieDirectorDetails is created
	 * in the DB for each test. This is nice as the tests are more independent and what is 
	 * getting test is more atomic in nature and less fragile
	 * **/
	def setup() {
		def movieDirector =
		'''{
				"name": "Quentin Tarantino",
				"dob": "27/03/65",
				"nationality": "AMERICAN"
		}'''
		def addResponse = mockMvc.perform(post("/moviedirectors/add")
							.contentType(MediaType.APPLICATION_JSON)
							.content(movieDirector))
		def movieDirectorMap =
			new JsonSlurper().parseText(addResponse.andReturn()
												   .getResponse()
												   .getContentAsString())
		movieDirectorId =  movieDirectorMap["moviedirectorId"]
		
	}

	def "Should be able to add new movies to this catalog under an existing movie director"(){
		when: "I make a request to add a new movie to this catalog under this movie director"
		def newMovie = '''{
				"title": "Hateful Eight",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/12/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			}'''

		def response = mockMvc.perform(post("/moviedirectors/" + movieDirectorId + "/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(newMovie))
		
		then: "it should be added to this catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(newMovie))
	}
	
	def "Should not beable to add a movie that already exists in this catalog for this movie director"(){
		given: "a movie that already exists in this catalog under this movie director"
		def newMovie = '''{
				"title": "Jurassic World",
				"genre": "SCIENCE_FICTION",
				"releasedate": "11/12/2016",
				"cast": [
					"Chris Pratt"
				]
			}'''
		def addResponse = mockMvc.perform(post("/moviedirectors/" + movieDirectorId + "/movies/add")
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovie))
			
		when: "I try to add the same movie again a second time in a row"
		def response = mockMvc.perform(post("/moviedirectors/" + movieDirectorId + "/movies/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(addResponse.andReturn().getResponse().getContentAsString()))
		
		then: "an exception should be returned in the response"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie with this movieId already exists in this catalog"))
	}
	
	def "Should be able to update movies that exist in this catalog under this movie director"(){		
		setup:"a movie already exists in this catalog under this movie director"
		def newMovie = '''{
				"title": "Kill Bill Volume 1",
				"genre": "ACTION",
				"releasedate": "10/12/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}'''
		def addResponse = mockMvc.perform(post("/moviedirectors/" + movieDirectorId + "/movies/add")
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
						
		def response = mockMvc.perform(post("/moviedirectors/" + movieDirectorId + "/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedMovieDetails))
	
		
		then: "the movie details should be updated under this movie rating"
		response.andExpect(status().isOk())
			  //.andExpect(content().json(updatedMovieDetails)) TODO: Need serializer for movieRatingsPart
	}
	
	def "Should not beable to update a movie that doesn't exist in this catalog"(){
		given:"a movie that doesn't exist under the director in this catalog"
		def nonexistentMovie =
		'''{
				"title": "Die Hard 4.0",
				"movieclassification": "_18A",
				"genre": "ACTION",
				"releasedate": "01/01/2017",
				"cast": [
					"Bruce Willis"
				]
			}'''
		
		when: "I make a request to update the movies' details under this movie director"
		def response = mockMvc.perform(post("/moviedirectors/" + movieDirectorId + "/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentMovie))
		
		then: "an exception should be returned"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie not found in this catalog for this movieId"))
	}
	
	
	def "Should be able to delete movie that exist in this catalog under this movie director"(){		
		setup:"a movie exists in this catalog under this movie director"
		def newMovie = '''{
				"title": "Kill Bill Volume 2",
				"genre": "ACTION",
				"releasedate": "16/04/2004",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}'''
		def addResponse = mockMvc.perform(post("/moviedirectors/" + movieDirectorId + "/movies/add")
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

	def "Should return the filmography for an existing movie director in this catalog"(){	
		given: "a filmograpy that exists in this catalog for this movie director"
		def filmography =
		[
			'''{
				"title": "Hateful Eight",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/12/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			}''',
			'''{
				"title": "Kill Bill Volume 1",
				"genre": "ACTION",
				"releasedate": "01/09/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}''',
			'''{
				"title": "Jurassic World",
				"genre": "SCIENCE_FICTION",
				"releasedate": "11/12/2016",
				"cast": [
					"Chris Pratt"
				]
			}'''
		]
		
		filmography.forEach({movieDetails ->
			mockMvc.perform(post("/moviedirectors/" + movieDirectorId + "/movies/add")
					.contentType(MediaType.APPLICATION_JSON)
					.content(movieDetails))
		})
		when: "I search for the filmography for this movie director"
		def response = mockMvc.perform(post("/moviedirectors/" + movieDirectorId + "/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "the movie directors filmography should have been returned"
		def expectedFilmography =
		'''
		[
			{
				"title": "Hateful Eight",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/12/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			},
			{
				"title": "Kill Bill Volume 1",
				"genre": "ACTION",
				"releasedate": "01/09/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			},
			{
				"title": "Jurassic World",
				"genre": "SCIENCE_FICTION",
				"releasedate": "11/12/2016",
				"cast": [
					"Chris Pratt"
				]
			}
		]'''
		response.andExpect(status().isOk())
				.andExpect(content().json(expectedFilmography))
	}
	
	def "Should not get a filmography for a movie director that doesn't exist in this catalog"(){
		when: "I search for a filmography for a movie director that that doesn't exist in this catalog"
		def response = mockMvc.perform(post("/moviedirectors/999/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "an exception should be returned"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Filmography not found in this catalog for this moviedirectorId"))
	}
}