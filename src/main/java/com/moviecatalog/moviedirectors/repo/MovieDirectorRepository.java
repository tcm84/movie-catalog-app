package com.moviecatalog.moviedirectors.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moviecatalog.moviedirectors.models.MovieDirectorDetails;

/**
* The <code>MovieDirectorRepository</code> represents a MovieDirectorCatalog
*/
@Repository
public interface MovieDirectorRepository extends CrudRepository<MovieDirectorDetails,Integer>  {
}
