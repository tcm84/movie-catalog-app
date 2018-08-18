package app.restcontollers.acceptancetests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.springframework.http.MediaType;

import app.MovieCatalogApplication
import app.restcontrollers.MovieCatalogController
import app.services.MovieCatalogService
import app.repo.testconfig.RepoTestConfig
import spock.lang.Shared
import spock.lang.Specification

@Import(RepoTestConfig)
@ContextConfiguration(classes=[
	MovieCatalogApplication,
	MovieCatalogService])
@WebMvcTest(MovieCatalogController)
class MovieCatalogControllerATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	@Shared
	def newMovie =
	'''{
				"movieId": 1,
				"title": "Hateful Eight",
				"director": "Quentin Tarantino",
				"rating": "_18",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "11/10/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel"
				]
			}'''
	
	
	def "Adding new movies to the catalog"(){
		when: "I request a new movie gets added to the catalog"
		def response = mockMvc.perform(post("/moviecatalog/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(newMovie))
		
		then: "it should be added to the catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(newMovie))
	}
	
	def "Updating movies in the catalog"(){
		setup:"A movie exists in the catalog that needs updated"
		mockMvc.perform(post("/moviecatalog/update")
			.contentType(MediaType.APPLICATION_JSON)
			.content(newMovie))
		def updatedMovie =
		'''{
				"movieId": 1,
				"title": "Hateful Eight",
				"director": "Quentin Tarantino",
				"rating": "_18A",
				"genre": "HISTORICAL_FICTION",
				"releasedate": "12/12/2016",
				"cast": [
					"Samuel L Jackson",
					"Kurt Russel",
					"Jennifer Jason Lee"
				]
			}'''
		
		when: "I make a request to update the movies' details"
		def response = mockMvc.perform(put("/moviecatalog/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedMovie))
		
		then: "the movies details should be updated in the catalog"
		response.andExpect(status().isOk())
				.andExpect(content().json(updatedMovie))
	}
}
