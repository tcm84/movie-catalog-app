package com.moviecatalog.movies.restcontrollers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.directors.model.DirectorDetails;
import com.moviecatalog.movies.model.MovieDetails;
import com.moviecatalog.movies.services.MovieService;

@RestController
public class MovieControllerImpl implements MovieController {
	
	@Autowired
	private MovieService movieServiceImpl;
	
	@RequestMapping("/directors/{directorId}/movies/all")
	public List<MovieDetails> getFilmography(@PathVariable Integer directorId) {
		return movieServiceImpl.getFilmography(directorId);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/directors/{directorId}/movies/add")
	public MovieDetails addMovie(@PathVariable Integer directorId, @Valid @RequestBody MovieDetails movieDetails) {
		DirectorDetails directorDetails = new DirectorDetails();
		directorDetails.setDirectorId(directorId);
		movieDetails.setDirectorDetails(directorDetails);
		return movieServiceImpl.addMovie(movieDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/directors/{directorId}/movies/update")
	public MovieDetails updateMovie(@PathVariable Integer directorId,@Valid @RequestBody MovieDetails movieDetails) {
		DirectorDetails directorDetails = new DirectorDetails();
		directorDetails.setDirectorId(directorId);
		movieDetails.setDirectorDetails(directorDetails);
		return movieServiceImpl.updateMovie(movieDetails);
	}

	@Override
	@RequestMapping(method=RequestMethod.DELETE, value="/directors/{directorId}/movies/delete/{movieId}")
	public void deleteMovie(@PathVariable Integer movieId) {
		movieServiceImpl.deleteMovie(movieId);
	}
}