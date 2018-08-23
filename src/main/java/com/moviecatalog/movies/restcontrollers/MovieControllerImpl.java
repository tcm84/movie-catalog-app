package com.moviecatalog.movies.restcontrollers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.moviedirectors.model.dto.entities.MovieDirectorDetails;
import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;
import com.moviecatalog.movies.enums.MovieClassification;
import com.moviecatalog.movies.model.dto.entities.MovieDetails;
import com.moviecatalog.movies.services.MovieService;

@RestController
public class MovieControllerImpl implements MovieController {
	
	@Autowired
	private MovieService movieService;
	
	@RequestMapping("/moviedirectors/{moviedirectorId}/movies/all")
	public List<MovieDetails> getFilmography(@PathVariable Integer moviedirectorId) {
		return movieService.getFilmography(moviedirectorId);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/moviedirectors/{moviedirectorId}/movies/add")
	public MovieDetails addMovieUnderMovieDirector(@PathVariable Integer moviedirectorId, @Valid @RequestBody MovieDetails movieDetails) {
		MovieDirectorDetails movieDirectorDetails = new MovieDirectorDetails();
		movieDirectorDetails.setMoviedirectorId(moviedirectorId);
		movieDetails.setMovieDirectorDetails(movieDirectorDetails);
		return movieService.addMovie(movieDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/moviedirectors/{moviedirectorId}/movies/update")
	public MovieDetails updateMovieUnderMovieDirector(@PathVariable Integer moviedirectorId,@Valid @RequestBody MovieDetails movieDetails) {
		return movieService.updateMovie(movieDetails);
	}
	
	@RequestMapping("/movieratings/{movieratingId}/movies/all")
	public List<MovieDetails> getMovieList(@PathVariable Integer movieratingId) {
		return movieService.getMovieList(movieratingId);
	}
	
	@RequestMapping("/movieratings/movies/above/{movieClassification}")
	public List<MovieDetails> getMovieListAboveMovieRating(@PathVariable MovieClassification movieClassification) {
		return movieService.getMovieListAboveMovieRating(movieClassification);
	}

	@Override
	@RequestMapping(method=RequestMethod.POST, value="/movieratings/{movieratingId}/movies/add")
	public MovieDetails addMovieUnderMovieRating(@PathVariable Integer movieratingId, @Valid @RequestBody MovieDetails movieDetails) {
		MovieRatingDetails movieRatingDetails = new MovieRatingDetails();
		movieRatingDetails.setMovieratingId(movieratingId);
		movieDetails.setMovieRatingDetails(movieRatingDetails);
	    return movieService.addMovie(movieDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/movieratings/{movieratingId}/movies/update")
	public MovieDetails updateMovieUnderMovieRating(@PathVariable Integer movieratingId,@Valid @RequestBody MovieDetails movieDetails) {
		return movieService.updateMovie(movieDetails);
	}

	@Override
	@RequestMapping(method=RequestMethod.DELETE, value="/movies/delete/{movieId}")
	public void deleteMovie(@PathVariable Integer movieId) {
		movieService.deleteMovie(movieId);
	}
}