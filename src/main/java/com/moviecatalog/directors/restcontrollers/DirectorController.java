package com.moviecatalog.directors.restcontrollers;

import com.moviecatalog.directors.model.DirectorDetails;

/**
* The <code>DirectorController</code> interface provides
* an API to allow clients to perform CRUD operations 
* on a MovieCatalog
*/
public interface DirectorController {

	/** 
	* @see DirectorService#addDirector(DirectorsDetails)
	* @exception Throws MethodArgumentNotValidException if directorsDetails
	* fails the validation requirements
	*/
	DirectorDetails addDirector(DirectorDetails directorDetails);
}