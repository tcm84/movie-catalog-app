package com.moviecatalog.movies.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moviecatalog.directors.model.DirectorDetails;
import com.moviecatalog.movies.model.MovieDetails;

/**
* The <code>MovieRepository</code> represents a MovieCatalog
*/
@Repository
public interface MovieRepository extends CrudRepository<MovieDetails,Integer>  {
	Optional<List<MovieDetails>> findByDirectorDetails(DirectorDetails directorDetails);
}
