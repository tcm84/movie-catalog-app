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
class MovieControllerUnderMovieRatingsEdgeCasesATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def movieRating =
	'''{
				"movieratingId": 1,
				"movieClassification": "_18",
				"description": "Suitable only for adults",
				"moviewarnings": [
					{
						"title": "Strong violence",
						"summary": "Detailed portrayal of violent or dangerous acts"
					},
					{
						"title": "Strong language",
						"summary": "Strong language is used throughout"
					}
					
				]
			}'''
	
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
		mockMvc.perform(post("/movieratings/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieRating))
	}
	
	def "Should not beable to add a movie that already exists in the catalog"(){
		given: "a movie has been already added to the catalog under that rating"
	   mockMvc.perform(post("/movieratings/1/movies/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(filmography[1]))
			
		when: "I request that it be added again to the catalog under that rating"
		def response = mockMvc.perform(post("/movieratings/1/movies/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(filmography[1]))
		
		then: "an exception should be returned indicating that the movie already exists"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Movie already exists in the catalog"))
	}
	
	def "Should not beable to update a movie that doesn't exist in the catalog"(){
		given:"a movie that doesn't exist under this rating in the catalog"
		def nonexistentMovie =
		'''{
				"movieId": 10,
				"title": "Kill Bill Volume 4",
				"genre": "ACTION",
				"releasedate": "10/10/2008",
				"cast": [
					"Urma Thurman"
				]
			}'''
		
		when: "I make a request to update the movies' details under this rating"
		def response = mockMvc.perform(post("/movieratings/1/movies/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentMovie))
		
		then: "an exception should be returned indicating that the movie was not found"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie not found in the catalog"))
	}
	
	def "Should not beable to search for a list of movies under a rating if the rating doesnt exist in the catalog"(){
		when: "I search for a list of movies for a rating that doesn't exist in the catalog"
		def response = mockMvc.perform(post("/movieratings/99/movies/all")
				.contentType(MediaType.APPLICATION_JSON))
		
		then: "an exception should be returned indicating that list of movies under this rating was not found"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Movie list not found in the catalog for this rating"))
	}
}