package com.moviecatalog.movieratings.restcontrollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.movieratings.services.MovieRatingServiceImpl
import com.moviecatalog.movies.repo.MovieRepository
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
	MovieRatingServiceImpl])
@WebMvcTest(MovieRatingControllerImpl)
class MovieRatingControllerHappyPathATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	def movieRatings =
	[
		'''{
				"movieratingId": 2,
				"movieClassification": "_18A",
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
		,
		'''{
				"movieratingId": 3,
				"movieClassification": "_12A",
				"description": "Suitable for 12 years and over - optional accompanying adult",
				"moviewarnings": [
					{
						"title": "Imitable behaviour",
						"summary": "No promotion of potentially dangerous behaviour which children are likely to copy"
					},
					{
						"title": "Threat",
						"summary": "There may be moderate physical and psychological threat and horror sequences"
					},
					{
						"title": "Violence",
						"summary": "There may be moderate violence but it should not dwell on detail"
					}
					
				]
			}''',
			,
			'''{
				"movieratingId": 4,
				"movieClassification": "_15",
				"description": "Suitable for 15 years and over ",
				"moviewarnings": [
					{
						"title": "Violence",
						"summary": "There may be moderate violence with occasional more serious violence"
					}
					
				]
			}'''
	]
	
	def "Adding new movie ratings to the catalog"(){
		when: "I request to add a new movie rating"
		def response = mockMvc.perform(post("/movieratings/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(movieRatings[0]))
		
		then: "it should be added to the catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(movieRatings[0]))
	}
	
	def "Updating movie ratings in the catalog"(){		
		given:"a movie rating exists in the catalog"
		mockMvc.perform(post("/movieratings/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieRatings[1]))
		when: "I make a request to update the movies ratings' details"
		movieRatings[1] =
		'''{
				"movieratingId": 3,
				"movieClassification": "_12A",
				"description": "Suitable for 12 years and over - optional accompanying adult",
				"moviewarnings": [
					{
						"title": "Imitable behaviour",
						"summary": "No promotion of potentially dangerous behaviour which children are likely to copy"
					},
					{
						"title": "Threat",
						"summary": "There may be more moderate physical and psychological threat and horror sequences"
					},
					{
						"title": "Violence",
						"summary": "There may be more moderate violence but it should not dwell on detail"
					}
					
				]
			}'''
		def response = mockMvc.perform(post("/movieratings/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(movieRatings[1]))
		
		then: "the movie ratings details should be updated in the catalog"
		response.andExpect(status().isOk())
				.andExpect(content().json(movieRatings[1]))
	}
	
	def "Deleting movie ratings from the catalog"(){
		given:"a movie rating exists in the catalog that I want to delete"
		mockMvc.perform(post("/movieratings/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(movieRatings[2]))
			
		
		when: "I make a request to delete the movie rating from the catalog"
		def response = mockMvc.perform(delete("/movieratings/delete/4"))
		
		then: "the movie ratings details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
}