package com.moviecatalog.moviedirectors.controllers;

import com.moviecatalog.moviedirectors.models.MovieDirectorDetails;

/**
* The <code>MovieDirectorController</code> interface provides
* an API to allow clients to perform CRUD operations 
* on a MovieDirectorCatalog
*/
public interface MovieDirectorController {

	/** 
	* @see MovieDirectorService#addMovieDirector(MovieDirectorDetails)
	* @exception Throws MethodArgumentNotValidException if movieDirectorDetails
	* fails the validation requirements
	*/
	MovieDirectorDetails addMovieDirector(MovieDirectorDetails movieDirectorDetails);
	
	/**
	* @see MovieDirectorService#updateMovieDirector(MovieDirectorDetails)
	* @exception Throws MethodArgumentNotValidException if movieDirectorDetails
	* fails the validation requirements
	*/
	MovieDirectorDetails updateMovieDirector(MovieDirectorDetails movieDirectorDetails);
	
	/**
	* @see MovieDirectorController#deleteMovieDirector(Integer)
	*/
	void deleteMovieDirector(Integer moviedirectorId);
}