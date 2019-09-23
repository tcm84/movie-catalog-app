package com.moviecatalog.movies.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.moviedirectors.models.MovieDirectorDetails;
import com.moviecatalog.movieratings.models.MovieRatingDetails;
import com.moviecatalog.movies.enums.MovieClassification;
import com.moviecatalog.movies.models.MovieDetails;
import com.moviecatalog.movies.services.MovieService;

@RestController
public class MovieControllerImpl implements MovieController {
	
	@Autowired
	private MovieService movieService;
	
	@GetMapping("/moviedirectors/{moviedirectorId}/movies/all")
	public List<MovieDetails> getFilmography(@PathVariable Integer moviedirectorId) {
		return movieService.getFilmography(moviedirectorId);
	}
	
	@Override
	@PostMapping("/moviedirectors/{moviedirectorId}/movies/add")
	public MovieDetails addMovieUnderMovieDirector(@PathVariable Integer moviedirectorId, @Valid @RequestBody MovieDetails movieDetails) {
		MovieDirectorDetails movieDirectorDetails = new MovieDirectorDetails();
		movieDirectorDetails.setMoviedirectorId(moviedirectorId);
		movieDetails.setMovieDirectorDetails(movieDirectorDetails);
		return movieService.addMovie(movieDetails);
	}
	
	@Override
	@PostMapping("/moviedirectors/{moviedirectorId}/movies/update")
	public MovieDetails updateMovieUnderMovieDirector(@PathVariable Integer moviedirectorId,@Valid @RequestBody MovieDetails movieDetails) {
		return movieService.updateMovie(movieDetails);
	}
	
	@GetMapping("/movieratings/{movieratingId}/movies/all")
	public List<MovieDetails> getMovieList(@PathVariable Integer movieratingId) {
		return movieService.getMovieList(movieratingId);
	}
	
	@GetMapping("/movieratings/movies/above/{movieClassification}")
	public List<MovieDetails> getMovieListAboveMovieRating(@PathVariable MovieClassification movieClassification) {
		return movieService.getMovieListAboveMovieRating(movieClassification);
	}

	@Override
	@PostMapping("/movieratings/{movieratingId}/movies/add")
	public MovieDetails addMovieUnderMovieRating(@PathVariable Integer movieratingId, @Valid @RequestBody MovieDetails movieDetails) {
		MovieRatingDetails movieRatingDetails = new MovieRatingDetails();
		movieRatingDetails.setMovieratingId(movieratingId);
		movieDetails.setMovieRatingDetails(movieRatingDetails);
	    return movieService.addMovie(movieDetails);
	}
	
	@Override
	@PostMapping("/movieratings/{movieratingId}/movies/update")
	public MovieDetails updateMovieUnderMovieRating(@PathVariable Integer movieratingId,@Valid @RequestBody MovieDetails movieDetails) {
		return movieService.updateMovie(movieDetails);
	}

	@Override
	@DeleteMapping("/movies/delete/{movieId}")
	public void deleteMovie(@PathVariable Integer movieId) {
		movieService.deleteMovie(movieId);
	}
}