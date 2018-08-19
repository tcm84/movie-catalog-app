package com.moviecatalog.movies.restcontrollers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.movies.model.MovieDetails;
import com.moviecatalog.movies.services.MovieService;

@RestController
@RequestMapping("/movies")
public class MovieControllerImpl implements MovieController {
	
	@Autowired
	private MovieService movieServiceImpl;
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public MovieDetails addMovie(@Valid @RequestBody MovieDetails movieDetails) {
		return movieServiceImpl.addMovie(movieDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public MovieDetails updateMovie(@Valid @RequestBody MovieDetails movieDetails) {
		return movieServiceImpl.updateMovie(movieDetails);
	}

	@Override
	@RequestMapping(method=RequestMethod.DELETE, value="/delete/{movieId}")
	public void deleteMovie(@PathVariable Integer movieId) {
		movieServiceImpl.deleteMovie(movieId);
	}
}