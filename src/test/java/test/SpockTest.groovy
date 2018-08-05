package test

import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc

import com.blinx.demo.binxresourceserver.BlinxResourceServerApplication
import com.blinx.demo.binxresourceserver.restcontrollers.TestController

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

@ContextConfiguration(classes=BlinxResourceServerApplication)
@WebMvcTest(TestController)
class SpockTest extends Specification  {
	
	 @Autowired 
	 private MockMvc mockMvc
	
	def "Should return test"() {
		when:
		def response = mockMvc.perform(get("/rest/test"))
		
		then:
			response.andExpect(status().isOk())
					.andExpect(content().string("test"))
	}
}