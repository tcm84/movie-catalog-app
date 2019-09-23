package com.moviecatalog.movies.controllers;

import java.util.List;

import com.moviecatalog.movies.enums.MovieClassification;
import com.moviecatalog.movies.models.MovieDetails;

/**
* The <code>MovieController</code> interface provides
* an API to allow clients to perform CRUD operations 
* on a MovieCatalog
*/
public interface MovieController {

	/** 
	* @see MovieService#getFilmography(Integer)
	*/
	List<MovieDetails> getFilmography(Integer moviedirectorId);
	
	/** 
	* @see MovieService#getMovieListAboveMovieRating(MovieClassification)
	*/
	List<MovieDetails> getMovieListAboveMovieRating(MovieClassification movieClassification);
	
	/** 
	* @see MovieService#addMovieUnderMovieDirector(Integer, MovieDetails)
	* @param moviedirectorId of the movie director for the new movie
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails addMovieUnderMovieDirector(Integer moviedirectorId, MovieDetails movieDetails);

	/**
	* @see MovieService#updateMovieUnderMovieDirector(Integer, MovieDetails)
	* @param moviedirectorId of the movie director of the movie
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails updateMovieUnderMovieDirector(Integer moviedirectorId, MovieDetails movieDetails);
	
	
	/** 
	* @see MovieService#getMovieList(Integer)
	*/
	List<MovieDetails> getMovieList(Integer movieratingId);
	
	/** 
	* @see MovieService#addMovieUnderMovieRating(Integer, MovieDetails)
	* @param movieratingId of the moving rating for the new movie
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails addMovieUnderMovieRating(Integer movieratingId, MovieDetails movieDetails);

	/**
	* @see MovieService#updateMovieUnderMovieRating(Integer, MovieDetails)
	* @param movieratingId of the movie rating of the movie
	* @exception Throws MethodArgumentNotValidException if movieDetails
	* fails the validation requirements
	*/
	MovieDetails updateMovieUnderMovieRating(Integer movieratingId, MovieDetails movieDetails);

	/**
	* @see MovieService#deleteMovie(Integer)
	*/
	void deleteMovie(Integer movieId);
}