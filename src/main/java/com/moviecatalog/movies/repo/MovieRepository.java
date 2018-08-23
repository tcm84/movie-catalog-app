package com.moviecatalog.movies.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moviecatalog.moviedirectors.model.dto.entities.MovieDirectorDetails;
import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;
import com.moviecatalog.movies.model.dto.entities.MovieDetails;

/**
* The <code>MovieRepository</code> represents a MovieCatalog
*/
@Repository
public interface MovieRepository extends CrudRepository<MovieDetails,Integer>  {
	Optional<List<MovieDetails>> findByMovieDirectorDetails(MovieDirectorDetails movieDirectorDetails);
	Optional<List<MovieDetails>> findByMovieRatingDetails(MovieRatingDetails movieRatingDetails);
}
