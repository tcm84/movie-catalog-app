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
class MovieControllerUnderMovieRatingsIT extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	def postMovieListUnderMovieRating(def movieList, def movieRatingId) {
		movieList.forEach({movieDetails ->
			mockMvc.perform(post("/movieratings/" + movieRatingId + "/movies/add")
					.contentType(MediaType.APPLICATION_JSON)
					.content(movieDetails))
		})
	}
	
	def createMovieRatingId(def movieRatingDetails) {
		def addResponse = mockMvc.perform(post("/movieratings/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieRatingDetails))
		def movieRatingDetailsMap =
		new JsonSlurper().parseText(addResponse.andReturn()
										   .getResponse()
										   .getContentAsString())
		return movieRatingDetailsMap["movieratingId"]
	}
	
	def _18movieRatingId
	
	/**
	 * setup runs before each test is run. A new instance of MovieRatingDetails is created
	 * in the DB for each test. This is nice as the tests are more independent and what is 
	 * getting test is more atomic in nature and less fragile. Some tests need to be rolledback.
	 * An alternative approach would be to setup the db with an initial configuration so this is
	 * not necessary
	 **/
	def setup() {
		def movieRatingDetails =
		'''{
				"movieClassification": "_18",
				"description": "Suitable only for adults"
		}'''
		_18movieRatingId = createMovieRatingId(movieRatingDetails)
	}

	@Transactional
	@Rollback
	def "Should be able to add new movies to this catalog under an existing movie rating"(){
		when: "I make a request to add a new movie to this catalog under this movie rating"
		def newMovieDetails = '''{
				"title": "Jackie Brown",
				"genre": "CRIME_FICTION",
				"releasedate": "11/10/2003",
				"cast": [
					"Samuel L Jackson"
				]
			}'''
		def response = mockMvc.perform(post("/movieratings/" + _18movieRatingId + "/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(newMovieDetails))
		
		then: "it should be added to this catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(newMovieDetails))
	}
	
	@Transactional
	@Rollback
	def "Should not beable to add a movie that already exists in this catalog for this movie rating"(){
		given: "a movie that already exists in this catalog under this movie rating"
		def newMovieDetails = '''{
				"title": "Alien",
				"genre": "SCIENCE_FICTION",
				"releasedate": "11/10/1979",
				"cast": [
					"S Weaver"
				]
			}'''
		def addResponse = mockMvc.perform(post("/movieratings/" + _18movieRatingId + "/movies/add")
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovieDetails))
			
		when: "I retry to add the same movie again a second time in a row"
		def response = mockMvc.perform(post("/movieratings/" + _18movieRatingId + "/movies/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(addResponse.andReturn().getResponse().getContentAsString()))
		
		then: "an exception should be returned"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie with this movieId already exists in this catalog"))
	}

	@Transactional
	@Rollback
	def "Should be able to update movies that exist in this catalog under this movie rating"(){		
		given:"a movie already exists in this catalog under this movie rating"
		def newMovieDetails = '''{
				"title": "Predator",
				"genre": "SCIENCE_FICTION",
				"releasedate": "04/10/1987",
				"cast": [
					"Arnold Schwazenegger"
				]
			}'''
		def addResponse = mockMvc.perform(post("/movieratings/" + _18movieRatingId + "/movies/add")
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovieDetails))
		when: "I make a request to update the movies' details"
		def movieDetailsMap =
			new JsonSlurper().parseText(addResponse.andReturn()
												   .getResponse()
												   .getContentAsString())
		//We are updating the movies releasedate in this test
		movieDetailsMap["releasedate"] = "01/09/2003"
		
		def updatedMovieDetails =
						new JsonOutput().toJson(movieDetailsMap)
						
		def response = mockMvc.perform(post("/movieratings/" + _18movieRatingId + "/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedMovieDetails))
		def updatedMovieDetailsMap = new JsonSlurper().parseText(response.andReturn()
														.getResponse()
														.getContentAsString())
		def updatedReleaseDate = updatedMovieDetailsMap["releasedate"]
		
		then: "the movie details should be updated under this movie rating"
		response.andExpect(status().isOk())
		updatedReleaseDate == "01/09/2003"
	}
	
	def "Should not beable to update a movie that doesn't exist in this catalog"(){
		given:"a movie that doesn't exist under this rating in this catalog"
		def nonexistentMovieDetails =
		'''{
				"title": "Die Hard 3",
				"genre": "ACTION",
				"releasedate": "01/01/1995",
				"cast": [
					"Bruce Willis"
				]
			}'''
		
		when: "I make a request to update the movies' details under this movie rating"
		def response = mockMvc.perform(post("/movieratings/" + _18movieRatingId + "/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentMovieDetails))
		
		then: "an exception should be returned"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie not found in this catalog for this movieId"))
	}
	
	def "Should be able to delete movie that exists in this catalog under this movie rating"(){		
		given:"a movie exists in this catalog under this movie rating"
		def newMovieDetails = '''{
				"title": "Die Hard",
				"genre": "ACTION",
				"releasedate": "16/04/1988",
				"cast": [
					"Bruce Willis"
				]
			}'''
		def addResponse = mockMvc.perform(post("/movieratings/" + _18movieRatingId + "/movies/add")
								.contentType(MediaType.APPLICATION_JSON)
								.content(newMovieDetails))			
			
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
	
	@Transactional
	@Rollback
	def "Should return movie list for an existing movie rating in this catalog"(){		
		given: "a movie list that exists in this catalog for this movie rating"
		def movieList =
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
		postMovieListUnderMovieRating(movieList, _18movieRatingId)
		
		when: "I search for the movie list for this movie rating"
		def response = mockMvc.perform(post("/movieratings/" + _18movieRatingId + "/movies/all")
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
	
	def "Should return all movies that have a movie rating equal to or greater than a supplied movie rating" (){
		given: "a list of _12 movies under a _12 movie rating"
		def _12movieRatingDetails =
		'''{
				"movieClassification": "_12",
				"description": "Suitable only for 12 year olds"
		}'''
		def _12movieRatingId = createMovieRatingId(_12movieRatingDetails)
		
		def _12moviesList =
		[
			'''{
				"title": "Mission Impossible Fallout",
				"genre": "ACTION",
				"releasedate": "11/10/2018",
				"cast": [
					"Tom Cruise"
				]
			}''',
			'''{
				"title": "Avengers Infinity War",
				"genre": "CRIME_FICTION",
				"releasedate": "01/09/2018",
				"cast": [
					"Robert Downey Jr",
					"Josh Brolin"
				]
			}'''
		]
		
		postMovieListUnderMovieRating(_12moviesList, _12movieRatingId)
		
		and: "a list of _15 movies under a _15 movie rating"

		def _15movieRatingDetails =
		'''{
				"movieClassification": "_15",
				"description": "Suitable only for 15 and over"
		}'''
		def _15movieRatingId = createMovieRatingId(_15movieRatingDetails)
		
		def _15moviesList =
		[
			'''{
				"title": "Abraham Lincoln Vampire Hunter",
				"genre": "HORROR",
				"releasedate": "11/10/2012",
				"cast": [
					"Benjamin Walker",
					"Rufus Sewell"
				]
			}''',
			'''{
				"title": "The Actors",
				"genre": "CRIME_FICTION",
				"releasedate": "01/09/2003",
				"cast": [
					"Michael Cain"
				]
			}''',
			'''{
				"title": "Airplane",
				"genre": "COMEDY",
				"releasedate": "11/10/1980",
				"cast": [
					"Leslie Nielsen",
					"Robery Hays",
					"Julie Hagerty"
				]
			}'''
		]
		
		postMovieListUnderMovieRating(_15moviesList, _15movieRatingId)
		
		and: "a list of _18 movies under a _18 movie rating"
		def _18moviesList =
		[
			'''{
				"title": "Black Mass",
				"genre": "CRIME_FICTION",
				"releasedate": "11/10/2016",
				"cast": [
					"Johnny Depp"
				]
			}''',
			'''{
				"title": "Predator 2",
				"genre": "SCIENCE_FICTION",
				"releasedate": "01/09/2003",
				"cast": [
					"Danny Glover"
				]
			}''',
			'''{
				"title": "Alien 3",
				"genre": "SCIENCE_FICTION",
				"releasedate": "11/10/1993",
				"cast": [
					"S Weaver"
				]
			}'''
		]
		
		postMovieListUnderMovieRating(_18moviesList, _18movieRatingId)

		when: "I search for a list of movies for a moving rating of _15 or over"
		def response = mockMvc.perform(post("/movieratings/movies/above/_15")
			.contentType(MediaType.APPLICATION_JSON))
		
		then: "all the movies for the movie rating of _15 or over should be returned"
		def expectedMovieList =
		'''
		[
			{
				"title": "Abraham Lincoln Vampire Hunter",
				"genre": "HORROR",
				"releasedate": "11/10/2012",
				"cast": [
					"Benjamin Walker",
					"Rufus Sewell"
				]
			},

			{
				"title": "The Actors",
				"genre": "CRIME_FICTION",
				"releasedate": "01/09/2003",
				"cast": [
					"Michael Cain"
				]
			},
			{
				"title": "Airplane",
				"genre": "COMEDY",
				"releasedate": "11/10/1980",
				"cast": [
					"Leslie Nielsen",
					"Robery Hays",
					"Julie Hagerty"
				]
			},
			{
				"title": "Black Mass",
				"genre": "CRIME_FICTION",
				"releasedate": "11/10/2016",
				"cast": [
					"Johnny Depp"
				]
			},
			{
				"title": "Predator 2",
				"genre": "SCIENCE_FICTION",
				"releasedate": "01/09/2003",
				"cast": [
					"Danny Glover"
				]
			},
			{
				"title": "Alien 3",
				"genre": "SCIENCE_FICTION",
				"releasedate": "11/10/1993",
				"cast": [
					"S Weaver"
				]
			}
		]'''
		
		response.andExpect(status().isOk())
		.andExpect(content().json(expectedMovieList))	
	}
}