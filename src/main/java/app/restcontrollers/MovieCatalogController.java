package app.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import app.model.MovieDetails;
import app.services.MovieCatalogService;

@RestController
@RequestMapping("/moviecatalog")
public class MovieCatalogController {
	
	@Autowired
	private MovieCatalogService movieCatalogService;
	
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public MovieDetails addToCatalog(@RequestBody MovieDetails movieDetails) {
		return movieCatalogService.addToCatalog(movieDetails);
	}
	
	
	@RequestMapping(method=RequestMethod.PUT, value="/update")
	public MovieDetails updateCatalog(@RequestBody MovieDetails movieDetails) {
		return movieCatalogService.updateCatalog(movieDetails);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/delete/{id}")
	public void deleteTopic(@PathVariable Integer id) {
		movieCatalogService.deleteFromCatalog(id);
	}
}
