package com.moviecatalog.movies.repo;

import org.springframework.data.repository.CrudRepository;

import com.moviecatalog.movies.model.MovieDetails;

public interface MovieCatalogRepository extends CrudRepository<MovieDetails,Integer>  {
}
