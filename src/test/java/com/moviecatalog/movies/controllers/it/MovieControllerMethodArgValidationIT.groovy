package com.moviecatalog.movies.controllers.it

import groovy.json.JsonOutput

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.MethodArgumentNotValidException

import com.moviecatalog.MovieCatalogApplication
import com.moviecatalog.moviedirectors.controllers.MovieDirectorControllerImpl
import com.moviecatalog.moviedirectors.services.MovieDirectorServiceImpl
import com.moviecatalog.movies.controllers.MovieControllerImpl
import com.moviecatalog.movies.repo.MovieRepository
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
@WebMvcTest(controllers=[MovieControllerImpl,MovieDirectorControllerImpl])
class MovieControllerMethodArgValidationIT extends Specification {
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
		validationErrors.size() == 3
		Set actualErrorMsgs = []
		validationErrors.forEach({error -> actualErrorMsgs += error.getDefaultMessage()})
		Set expectedErrorMsgs = ["Movie title must not be empty", "Cast must contains at least one actor",
			"Releasedate should not be null"]
		actualErrorMsgs == expectedErrorMsgs
		
		where:
		description                                                      | endpointURI
		"for adding a new movie under a director to this catalog"         | "/moviedirectors/1/movies/add"
		"for updating an existing movie under a director in this catalog" | "/moviedirectors/1/movies/update"
		"for adding a new movie under a rating to this catalog"           | "/movieratings/1/movies/add"
		"for updating an existing movie under a rating in this catalog"   | "/movieratings/1/movies/update"
	}
}