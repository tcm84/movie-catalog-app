package com.moviecatalog.movieratings.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.movieratings.models.MovieRatingDetails;
import com.moviecatalog.movieratings.services.MovieRatingService;

@RestController
@RequestMapping("/movieratings")
public class MovieRatingControllerImpl implements MovieRatingController {
	
	@Autowired
	private MovieRatingService  movieRatingService;
	
	@Override
	@PostMapping("/add")
	public MovieRatingDetails addMovieRating(@Valid @RequestBody MovieRatingDetails movieRatingDetails) {
		 return movieRatingService.addMovieRating(movieRatingDetails);
	}
	
	@Override
	@PostMapping("/update")
	public MovieRatingDetails updateMovieRating(@Valid @RequestBody MovieRatingDetails movieRatingDetails) {
		return movieRatingService.updateMovieRating(movieRatingDetails);
	}
	
	@Override
	@DeleteMapping("/delete/{movieratingId}")
	public void deleteMovieRating(@PathVariable Integer movieratingId) {
		movieRatingService.deleteMovieRating(movieratingId);
	}
}