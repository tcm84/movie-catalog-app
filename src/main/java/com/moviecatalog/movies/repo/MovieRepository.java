package com.moviecatalog.movies.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moviecatalog.moviedirectors.models.MovieDirectorDetails;
import com.moviecatalog.movieratings.models.MovieRatingDetails;
import com.moviecatalog.movies.models.MovieDetails;

/**
* The <code>MovieRepository</code> represents a MovieCatalog
*/
@Repository
public interface MovieRepository extends CrudRepository<MovieDetails,Integer>  {
	Optional<List<MovieDetails>> findByMovieDirectorDetails(MovieDirectorDetails movieDirectorDetails);
	Optional<List<MovieDetails>> findByMovieRatingDetails(MovieRatingDetails movieRatingDetails);
}
