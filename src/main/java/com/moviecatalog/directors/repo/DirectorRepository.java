package com.moviecatalog.directors.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moviecatalog.directors.model.DirectorDetails;

/**
* The <code>DirectorRepository</code> represents a DirectorCatalog
*/
@Repository
public interface DirectorRepository extends CrudRepository<DirectorDetails,Integer>  {
}
