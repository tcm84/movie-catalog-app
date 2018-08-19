package com.moviecatalog.movies.repo;

import org.springframework.data.repository.CrudRepository;

import com.moviecatalog.movies.model.MovieDetails;

/**
* The <code>MovieRepository</code> represents a MovieCatalog
*/
public interface MovieRepository extends CrudRepository<MovieDetails,Integer>  {
}
