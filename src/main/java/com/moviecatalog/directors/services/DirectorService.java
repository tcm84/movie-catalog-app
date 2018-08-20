package com.moviecatalog.directors.services;

import com.moviecatalog.directors.model.DirectorDetails;

/**
* The <code>DirectorService</code> interface provides
* an API to allow clients to perform CRUD operations
* on a DirectorRepository
*/
public interface DirectorService {
	
	/**
	* Adds a new director to this catalog with these 
	* directorDetails
	*
	* @param directorDetails of the new director
	* @return An instance of DirectorDetails for the director just 
	* added to this catalog including the generated directorId
	* @exception Throws MovieExistsException if a director already
	* exists in this catalog with these details
	*/
	DirectorDetails addDirector(DirectorDetails directorDetails);
	
	/**
	* Updates an existing director in this catalog with these 
	* directorDetails
	*
	* @param updated directorDetails for an existing director 
	* @return An instance of DirectorDetails for the director just 
	* updated
	* @exception Throws DirectorNotFoundException if a director with 
	* this directorId does not exist in this catalog
	*/
	DirectorDetails updateDirector(DirectorDetails directorDetails);
	
	/**
	* Deletes a director from this catalog with this directorId
	*
	* @param directorId of the director to delete from this catalog
	*/
	void deleteDirector(Integer directorId);
}