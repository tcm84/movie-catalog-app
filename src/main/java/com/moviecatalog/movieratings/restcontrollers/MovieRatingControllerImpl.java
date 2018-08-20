package com.moviecatalog.movieratings.restcontrollers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;
import com.moviecatalog.movieratings.services.MovieRatingService;

@RestController
@RequestMapping("/movieratings")
public class MovieRatingControllerImpl implements MovieRatingController {
	
	@Autowired
	private MovieRatingService  movieRatingService;
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public MovieRatingDetails addMovieRating(@Valid @RequestBody MovieRatingDetails movieRatingDetails) {
		return  movieRatingService.addMovieRating(movieRatingDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public MovieRatingDetails updateMovieRating(@Valid @RequestBody MovieRatingDetails movieRatingDetails) {
		return movieRatingService.updateMovieRating(movieRatingDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.DELETE, value="/delete/{movieratingId}")
	public void deleteMovieRating(@PathVariable Integer movieratingId) {
		movieRatingService.deleteMovieRating(movieratingId);
	}
}