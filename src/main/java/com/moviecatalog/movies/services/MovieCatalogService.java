package com.moviecatalog.movies.services;

import com.moviecatalog.movies.model.MovieDetails;

/**
* The <code>MovieCatalogService</code> interface provides
* an API to allow clients to perform CRUD operations
* on a MovieCatalogRepository
*/
public interface MovieCatalogService {
	
	/**
	* Adds a new movie to this catalog with these 
	* movieDetails
	*
	* @param moviesDetails of the new movie
	* @return An instance of MoviesDetails for the movie just 
	* added to this catalog including the generated movieId
	* @exception Throws MovieExistsException if a movie already
	* exists in this catalog with these details
	*/
	MovieDetails addToCatalog(MovieDetails movieDetails);

	/**
	* Updates an existing movie in this catalog with these 
	* movieDetails
	*
	* @param updated moviesDetails for an existing movie 
	* @return An instance of MoviesDetails for the movie just 
	* updated
	* @exception Throws MovieNotFoundException the movie with 
	* this movieId does not exist in this catalog
	*/
	MovieDetails updateCatalog(MovieDetails movieDetails);

	/**
	* Deletes a movie from this catalog with this movieId
	*
	* @param movieId of the movie to delete from this catalog
	*/
	void deleteFromCatalog(Integer movieId);
}