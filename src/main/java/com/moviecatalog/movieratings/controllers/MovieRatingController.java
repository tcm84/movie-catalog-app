package com.moviecatalog.movieratings.controllers;

import com.moviecatalog.movieratings.models.MovieRatingDetails;

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
	
	/**
	* @see MovieRatingService#deleteMovieRating(Integer)
	*/
	void deleteMovieRating(Integer movieRatingId);
}