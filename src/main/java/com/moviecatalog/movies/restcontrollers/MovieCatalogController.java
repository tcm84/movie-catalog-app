package com.moviecatalog.movies.restcontrollers;

import com.moviecatalog.movies.model.MovieDetails;

/**
* The <code>MovieCatalogController</code> interface provides
* an API to allow clients to perform CRUD operations 
* on a MovieCatalog
*/
public interface MovieCatalogController {

	/** 
	* @see MovieCatalogService#addToCatalog(MovieDetails)
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails addToCatalog(MovieDetails movieDetails);

	/**
	* @see MovieCatalogService#updateCatalog(MovieDetails)
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails updateCatalog(MovieDetails movieDetails);

	/**
	* @see MovieCatalogService#deleteFromCatalog(Integer)
	*/
	void deleteFromCatalog(Integer movieId);
}