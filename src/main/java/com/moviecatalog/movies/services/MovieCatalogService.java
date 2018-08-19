package com.moviecatalog.movies.services;

import com.moviecatalog.movies.model.MovieDetails;

/**
* The <code>MovieCatalogService</code> interface provides
* an API to allow clients to perform CRUD operations
* on a MovieCatalogRepository
*/
public interface MovieCatalogService {

	MovieDetails addToCatalog(MovieDetails movieDetails);

	MovieDetails updateCatalog(MovieDetails movieDetails);

	void deleteFromCatalog(Integer movieId);
}