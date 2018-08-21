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
	
	@Shared
	def filmography =
		[
			'''{
				"movieId": 5,
				"title": "Hateful Eight",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/10/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			}''',

			'''{
				"movieId": 6,
				"title": "Kill Bill Volume 1",
				"genre": "ACTION",
				"releasedate": "10/10/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine"
				]
			}''',

			'''{
				"movieId": 7,
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
		mockMvc.perform(post("/movieratings/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieRating))
	}

	def "Adding new movies to the catalog under an existing movie rating"(){
		when: "I add request to add a new movie under a movie rating"
		def response = mockMvc.perform(post("/movieratings/1/movies/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(filmography[0]))
		
		then: "it should be added to the catalog under the rating"
		response.andExpect(status().isOk())		
				.andExpect(content().json(filmography[0]))
	}
	
	def "Updating movies in the catalog"(){		
		given:"a movie exists in the catalog under a rating that needs updated"
		mockMvc.perform(post("/movieratings/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(filmography[1]))
		when: "I make a request to update the movies' details"
		filmography[1] =
		'''{
				"movieId": 6,
				"title": "Kill Bill Volume 1",
				"genre": "ACTION",
				"releasedate": "10/10/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine",
					"Michael Madsen"
				]
			}'''
		def response = mockMvc.perform(post("/movieratings/1/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(filmography[1]))
		
		then: "the movies details should be updated in the catalog under that rating"
		response.andExpect(status().isOk())
				.andExpect(content().json(filmography[1]))
	}
	
	def "Deleting movies from the catalog"(){		
		given:"a movie exists in the catalog under that rating that I want to delete"
		mockMvc.perform(post("/movieratings/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(filmography[2]))
			
		when: "I make a request to delete the movie from the catalog"
		def response = mockMvc.perform(delete("/movies/delete/7"))
		
		then: "the movies details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
	
	def "Should return all movies of a particular rating when a search is done with its id"(){
		given: "all a ratings movies have been added to the catalog under them"
		filmography.forEach({movieDetails ->
			mockMvc.perform(post("/movieratings/1/movies/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(movieDetails))
		})
		
		when: "I search for all the ratings movies"
		def response = mockMvc.perform(post("/movieratings/1/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "all the ratings movies should have been returned"
		def expectedMovieList =
		'''[
			{
				"movieId": 5,
				"title": "Hateful Eight",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/10/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			},
			{
				"movieId": 6,
				"title": "Kill Bill Volume 1",
				"genre": "ACTION",
				"releasedate": "10/10/2003",
				"cast": [
					"Uma Thurman",
					"David Carradine",
					"Michael Madsen"
				]
			},

			{
				"movieId": 7,
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
				.andExpect(content().json(expectedMovieList))
	}
}