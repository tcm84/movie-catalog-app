package com.moviecatalog.movies.restcontrollers;

import java.util.List;

import com.moviecatalog.movies.model.MovieDetails;

/**
* The <code>MovieController</code> interface provides
* an API to allow clients to perform CRUD operations 
* on a MovieCatalog
*/
public interface MovieController {

	/** 
	* @see MovieService#getFilmography(Integer)
	* @param directorId of director
	*/
	List<MovieDetails> getFilmography(Integer directorId);
	
	/** 
	* @see MovieService#addMovie(MovieDetails)
	* @param directorId of director of the new movie
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails addMovie(Integer directorId, MovieDetails movieDetails);

	/**
	* @see MovieService#updateMovie(MovieDetails)
	* @param directorId of director of the movie
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails updateMovie(Integer directorId, MovieDetails movieDetails);

	/**
	* @see MovieService#deleteMovie(Integer)
	*/
	void deleteMovie(Integer movieId);
}