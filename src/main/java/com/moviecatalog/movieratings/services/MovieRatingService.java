package com.moviecatalog.movieratings.services;

import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;

/**
* The <code>MovieRatingService</code> interface provides
* an API to allow clients to perform CRUD operations
* on a MovieRepositoryRepository
*/
public interface MovieRatingService {
	
	/**
	* Adds a new movie rating to this catalog with these 
	* movieRatingDetails
	*
	* @param movieRatingDetails of the new movie rating
	* @return An instance of MovieRatingDetails for the movie rating 
	* just added to this catalog including the generated movieratingId
	* @exception Throws MovieRatingExistsException if a movie rating already
	* exists in this catalog with these details
	*/
	MovieRatingDetails addMovieRating(MovieRatingDetails movieRatingDetails);
	
	/**
	* Updates an existing movie rating in this catalog with these 
	* movieRatingDetails
	*
	* @param updated movieRatingDetails for an existing movie rating 
	* @return An instance of MovieRatingDetails for the movie rating just 
	* updated
	* @exception Throws MovieRatingNotFoundException if a movie rating with 
	* this moviedirectorId does not exist in this catalog
	*/
	MovieRatingDetails updateMovieRating(MovieRatingDetails movieRatingDetails);
	
	/**
	* Deletes a movie rating from this catalog with this movieratingId
	*
	* @param movieratingId of the movie rating to delete from this catalog
	*/
	void deleteMovieRating(Integer movieratingId);
}