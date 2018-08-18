package app.repo;

import org.springframework.data.repository.CrudRepository;

import app.model.MovieDetails;

public interface MovieCatalogRepository extends CrudRepository<MovieDetails,String>  {
}
