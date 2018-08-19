package com.moviecatalog.movies.restcontrollers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.movies.model.MovieDetails;
import com.moviecatalog.movies.services.MovieCatalogService;

@RestController
@RequestMapping("/moviecatalog")
public class MovieCatalogControllerImpl implements MovieCatalogController {
	
	@Autowired
	private MovieCatalogService movieCatalogServiceImpl;
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public MovieDetails addToCatalog(@Valid @RequestBody MovieDetails movieDetails) {
		return movieCatalogServiceImpl.addToCatalog(movieDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public MovieDetails updateCatalog(@Valid @RequestBody MovieDetails movieDetails) {
		return movieCatalogServiceImpl.updateCatalog(movieDetails);
	}

	@Override
	@RequestMapping(method=RequestMethod.DELETE, value="/delete/{id}")
	public void deleteFromCatalog(@PathVariable Integer id) {
		movieCatalogServiceImpl.deleteFromCatalog(id);
	}
}