package com.moviecatalog.movies.restcontrollers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.moviedirectors.model.MovieDirectorDetails;
import com.moviecatalog.movies.model.MovieDetails;
import com.moviecatalog.movies.services.MovieService;

@RestController
public class MovieControllerImpl implements MovieController {
	
	@Autowired
	private MovieService movieServiceImpl;
	
	@RequestMapping("/moviedirectors/{moviedirectorId}/movies/all")
	public List<MovieDetails> getFilmography(@PathVariable Integer moviedirectorId) {
		return movieServiceImpl.getFilmography(moviedirectorId);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/moviedirectors/{moviedirectorId}/movies/add")
	public MovieDetails addMovie(@PathVariable Integer moviedirectorId, @Valid @RequestBody MovieDetails movieDetails) {
		MovieDirectorDetails movieDirectorDetails = new MovieDirectorDetails();
		movieDirectorDetails.setMoviedirectorId(moviedirectorId);
		movieDetails.setMovieDirectorDetails(movieDirectorDetails);
		return movieServiceImpl.addMovie(movieDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/moviedirectors/{moviedirectorId}/movies/update")
	public MovieDetails updateMovie(@PathVariable Integer moviedirectorId,@Valid @RequestBody MovieDetails movieDetails) {
		MovieDirectorDetails movieDirectorDetails = new MovieDirectorDetails();
		movieDirectorDetails.setMoviedirectorId(moviedirectorId);
		movieDetails.setMovieDirectorDetails(movieDirectorDetails);
		return movieServiceImpl.updateMovie(movieDetails);
	}

	@Override
	@RequestMapping(method=RequestMethod.DELETE, value="/moviedirectors/{moviedirectorId}/movies/delete/{movieId}")
	public void deleteMovie(@PathVariable Integer movieId) {
		movieServiceImpl.deleteMovie(movieId);
	}
}