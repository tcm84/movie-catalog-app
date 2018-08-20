package com.moviecatalog.directors.restcontrollers

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
import com.moviecatalog.directors.restcontrollers.DirectorControllerImpl
import com.moviecatalog.directors.services.DirectorServiceImpl

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
@ContextConfiguration(classes=[
	MovieCatalogApplication,
	DirectorServiceImpl])
@WebMvcTest(DirectorControllerImpl)
class DirectorControllerATest extends Specification {
	@Autowired
	private MockMvc mockMvc
	
	def "Adding new directors to the catalog"(){
		when: "I request a new director gets added to the catalog"
		def quentinTarantino =
		'''{
				"directorId": 1,
				"name": "Quentin Tarantino"
			}'''
		def response = mockMvc.perform(post("/directors/add")
						      .contentType(MediaType.APPLICATION_JSON)
							  .content(quentinTarantino))
		
		then: "it should be added to the catalog"
		response.andExpect(status().isOk())		
				.andExpect(content().json(quentinTarantino))
	}
	
	def "Should not beable to add a director that already exists in the catalog"(){
		given: "a director that already exists in the catalog"
		def stevenSpielberg =
		'''{
				"directorId": 1,
				"name": "stevenSpielberg"
			}'''
	   mockMvc.perform(post("/directors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(stevenSpielberg))
			
		when: "I request that it be added again when it already is in the catalog"
		def response = mockMvc.perform(post("/directors/add")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(stevenSpielberg))
		
		then: "I should not be able to added it again"
		response.andExpect(status().isConflict())
				.andExpect(status().reason("Director already exists in the catalog"))
	}
	
	def "Updating directors in the catalog"(){
		setup:"A director exists in the catalog that needs updated"
		def stevenSpielberg =
				'''{
						"directorId": 1,
						"name": "stevenSpielberg"
					}'''
	   mockMvc.perform(post("/directors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(stevenSpielberg))
		def updatedStevenSpielberg =
		'''{
						"directorId": 1,
						"name": "Steven Spielberg"
					}'''
		
		when: "I make a request to update the directors' details"
		def response = mockMvc.perform(post("/directors/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(updatedStevenSpielberg))
		
		then: "the director details should be updated in the catalog"
		response.andExpect(status().isOk())
				.andExpect(content().json(updatedStevenSpielberg))
	}
	
	def "Should not beable to update a director that doesn't exist in the catalog"(){
		given:"A director that doesn't exist in the catalog"
		def nonexistentDirecror =
				'''{
						"directorId": 2,
						"name": "Clint Eastwood"
					}'''
		
		when: "I make a request to update the directors' details"
		def response = mockMvc.perform(post("/directors/update")
							  .contentType(MediaType.APPLICATION_JSON)
							  .content(nonexistentDirecror))
		
		then: "I should not beable to update them as the director is not in the catalog"
		response.andExpect(status().isNotFound())
				.andExpect(status().reason("Director not found in the catalog"))
	}
	
	def "Deleting directors from the catalog"(){
		given:"a director exists in the catalog that I want to delete"
		def eliRoth =
				'''{
						"directorId": 1,
						"name": "Eli Roth"
					}'''
		mockMvc.perform(post("/directors/add")
			.contentType(MediaType.APPLICATION_JSON)
			.content(eliRoth))
			
		
		when: "I make a request to delete the director from the catalog"
		def response = mockMvc.perform(delete("/directors/delete/1"))
		
		then: "the directors details should be deleted from the catalog"
		response.andExpect(status().isOk())
	}
}