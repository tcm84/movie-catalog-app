package app.restcontollers.acceptancetests

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete

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
class MovieCatalogControllerExceptionsATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	def "Should not beable to add a movie that already exists in the catalog"(){
		given: "a movie that already exists in the catalog"
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
		mockMvc.perform(post("/moviecatalog/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(newMovie))
			
		when: "I request that it be added again when it already is in the catalog"
		def response = mockMvc.perform(post("/moviecatalog/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(newMovie))
		
		then: "I should not be able to added it again"
		response.andExpect(status().isConflict())		
				.andExpect(status().reason("Movie already exists in the catalog"))
	}
}
