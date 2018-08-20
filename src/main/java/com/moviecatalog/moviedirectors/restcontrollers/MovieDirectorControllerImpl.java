package com.moviecatalog.moviedirectors.restcontrollers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.moviedirectors.model.MovieDirectorDetails;
import com.moviecatalog.moviedirectors.services.MovieDirectorService;

@RestController
@RequestMapping("/moviedirectors")
public class MovieDirectorControllerImpl implements MovieDirectorController {
	
	@Autowired
	private MovieDirectorService moviedirectorsServiceImpl;
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public MovieDirectorDetails addMovieDirector(@Valid @RequestBody MovieDirectorDetails directorsDetails) {
		return  moviedirectorsServiceImpl.addMovieDirector(directorsDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public MovieDirectorDetails updateMovieDirector(@Valid @RequestBody MovieDirectorDetails movieDirectorDetails) {
		return moviedirectorsServiceImpl.updateMovieDirector(movieDirectorDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.DELETE, value="/delete/{directorId}")
	public void deleteMovieDirector(@PathVariable Integer directorId) {
		moviedirectorsServiceImpl.deleteDirector(directorId);
	}
}