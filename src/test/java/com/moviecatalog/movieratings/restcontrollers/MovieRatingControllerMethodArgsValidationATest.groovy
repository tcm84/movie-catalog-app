package com.moviecatalog.movieratings.restcontrollers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.repo.testconfig.RepoTestConfig
import com.moviecatalog.movieratings.restcontrollers.MovieRatingControllerImpl
import com.moviecatalog.movieratings.services.MovieRatingServiceImpl

import groovy.swing.factory.ImageIconFactory

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
@ContextConfiguration(classes=[MovieCatalogApplication,MovieRatingServiceImpl])
@WebMvcTest(MovieRatingControllerImpl)
class MovieRatingControllerMethodArgsValidationATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Unroll
	def "Requests should be rejected if they don't pass validation requirements #description"(){
		given: "a request containing no valid data"
		def invalidRequest =
		'''{
			}'''
		
		when: "I make the request to #endpointURI"
		def response = mockMvc.perform(post(endpointURI)
			.contentType(MediaType.APPLICATION_JSON)
			.content(invalidRequest))
		
		then: "the request should be rejected as a bad request"
		response.andExpect(status().isBadRequest())
		
		and: "the response should contain all the error messages for the failed validations"
		MethodArgumentNotValidException ex = response.andReturn().resolvedException
		def validationErrors = ex.getBindingResult().allErrors
		validationErrors.size() == 1
		validationErrors.get(0).getDefaultMessage() == "Description should not be empty"
		
		where: "the following endpointURI's will be hit"
		description                                                  | endpointURI
		"when adding a new movie rating to this catalog"             | "/movieratings/add"
		"when updating an existing moving rating in this catalog"    | "/movieratings/update"
	}
}