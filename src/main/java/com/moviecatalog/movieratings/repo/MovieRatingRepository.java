package com.moviecatalog.movieratings.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;

/**
* The <code>MovieRatingRepository</code> represents a MovieRatingCatalog
*/
@Repository
public interface MovieRatingRepository extends CrudRepository<MovieRatingDetails,Integer>  {
}
