package com.moviecatalog.movieratings.restcontrollers;

import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;

/**
* The <code>MovieRatingController</code> interface provides
* an API to allow clients to perform CRUD operations 
* on a MovieRatingCatalog
*/
public interface MovieRatingController {

	/** 
	* @see MovieRatingService#addMovieRating(MovieRatingDetails)
	* @exception Throws MethodArgumentNotValidException if movieRatingDetails
	* fails the validation requirements
	*/
	MovieRatingDetails addMovieRating(MovieRatingDetails movieRatingDetails);
	
	/**
	* @see MovieRatingService#updateMovieRating(MovieRatingDetails)
	* @exception Throws MethodArgumentNotValidException if movieRatingDetails
	* fails the validation requirements
	*/
	MovieRatingDetails updateMovieRating(MovieRatingDetails movieRatingDetails);
}