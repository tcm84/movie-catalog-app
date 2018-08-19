package com.moviecatalog.movies.restcontrollers;

import com.moviecatalog.movies.model.MovieDetails;

/**
* The <code>MovieController</code> interface provides
* an API to allow clients to perform CRUD operations 
* on a MovieCatalog
*/
public interface MovieController {

	/** 
	* @see MovieService#addMovie(MovieDetails)
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails addMovie(MovieDetails movieDetails);

	/**
	* @see MovieService#updateMovie(MovieDetails)
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails updateMovie(MovieDetails movieDetails);

	/**
	* @see MovieService#deleteMovie(Integer)
	*/
	void deleteMovie(Integer movieId);
}