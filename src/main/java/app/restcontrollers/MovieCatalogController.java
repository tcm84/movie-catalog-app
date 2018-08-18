package app.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
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
}
