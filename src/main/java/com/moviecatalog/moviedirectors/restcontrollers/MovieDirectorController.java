package com.moviecatalog.moviedirectors.restcontrollers;

import com.moviecatalog.moviedirectors.model.MovieDirectorDetails;

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
	* @see MovieDirectorController#updateMovieDirector(MovieDirectorDetails)
	* @exception Throws MethodArgumentNotValidException if movieDirectorDetails
	* fails the validation requirements
	*/
	MovieDirectorDetails updateMovieDirector(MovieDirectorDetails movieDirectorDetails);
	
	/**
	* @see MovieDirectorController#deleteMovieDirector(Integer)
	*/
	void deleteMovieDirector(Integer moviedirectorId);
}