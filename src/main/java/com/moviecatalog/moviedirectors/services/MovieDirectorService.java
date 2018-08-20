package com.moviecatalog.moviedirectors.services;

import com.moviecatalog.moviedirectors.model.dto.entities.MovieDirectorDetails;

/**
* The <code>MovieDirectorService</code> interface provides
* an API to allow clients to perform CRUD operations
* on a MovieDirectorRepository
*/
public interface MovieDirectorService {
	
	/**
	* Adds a new movie director to this catalog with these 
	* movieDirectorDetails
	*
	* @param movieDirectorDetails of the new movie director
	* @return An instance of MovieDirectorDetails for the movie director 
	* just added to this catalog including the generated directorId
	* @exception Throws MovieDirectorExistsException if a movie director already
	* exists in this catalog with these details
	*/
	MovieDirectorDetails addMovieDirector(MovieDirectorDetails movieDirectorDetails);
	
	/**
	* Updates an existing movie director in this catalog with these 
	* movieDirectorDetails
	*
	* @param updated movieDirectorDetails for an existing movie director 
	* @return An instance of MovieDirectorDetails for the movie director just 
	* updated
	* @exception Throws MovieDirectorNotFoundException if a movie director with 
	* this moviedirectorId does not exist in this catalog
	*/
	MovieDirectorDetails updateMovieDirector(MovieDirectorDetails movieDirectorDetails);
	
	/**
	* Deletes a movie director from this catalog with this moviedirectorId
	*
	* @param moviedirectorId of the movie director to delete from this catalog
	*/
	void deleteDirector(Integer moviedirectorId);
}