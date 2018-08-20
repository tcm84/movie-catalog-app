package com.moviecatalog.directors.services;

import com.moviecatalog.directors.model.DirectorDetails;

/**
* The <code>DirectorService</code> interface provides
* an API to allow clients to perform CRUD operations
* on a MovieRepository
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
}