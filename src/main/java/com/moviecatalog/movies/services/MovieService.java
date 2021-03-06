package com.moviecatalog.movies.services;

import java.util.List;

import com.moviecatalog.movies.enums.MovieClassification;
import com.moviecatalog.movies.models.MovieDetails;

/**
* The <code>MovieService</code> interface provides
* an API to allow clients to perform CRUD operations
* on a MovieRepository
*/
public interface MovieService {
	
	/** 
	* Returns the filmography from this catalog for this moviemoviedirectorId
	* 
	* @param moviedirectorId of the movie director the search is done for
	*/
	List<MovieDetails> getFilmography(Integer moviedirectorId);
	
	/** 
	* Returns the combined list of movies for every rating at and above movieClassification 
	* from this catalog
	* 
	* @param movieClassification is the lower movieclassification of the movieList
	*/
	List<MovieDetails> getMovieListAboveMovieRating(MovieClassification movieClassification);
	
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
	MovieDetails addMovie(MovieDetails movieDetails);

	/**
	* Updates an existing movie in this catalog with these 
	* movieDetails
	*
	* @param updated moviesDetails for an existing movie 
	* @return An instance of MoviesDetails for the movie just 
	* updated
	* @exception Throws MovieNotFoundException if a movie with 
	* this movieId does not exist in this catalog
	*/
	MovieDetails updateMovie(MovieDetails movieDetails);
	
	/** 
	* Returns the movie list from this catalog for this movieratingId
	* 
	* @param movieratingId of the movie rating
	*/
	List<MovieDetails> getMovieList(Integer movieratingId);

	/**
	* Deletes a movie from this catalog with this movieId
	*
	* @param movieId of the movie to delete from this catalog
	*/
	void deleteMovie(Integer movieId);
}