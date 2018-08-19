package com.moviecatalog.movies.repo;

import org.springframework.data.repository.CrudRepository;

import com.moviecatalog.movies.model.MovieDetails;

/**
* The <code>MovieCatalogRepository</code> represents a MovieCatalog
*/
public interface MovieCatalogRepository extends CrudRepository<MovieDetails,Integer>  {
}
