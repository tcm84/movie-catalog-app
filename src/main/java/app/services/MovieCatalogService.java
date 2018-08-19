package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.MovieDetails;
import app.repo.MovieCatalogRepository;

@Service
public class MovieCatalogService {

	@Autowired
	private MovieCatalogRepository movieCatalogRepository;
	
	public MovieDetails addToCatalog(MovieDetails movieDetails) {
		return movieCatalogRepository.save(movieDetails);
	}

	public MovieDetails updateCatalog(MovieDetails movieDetails) {
		return movieCatalogRepository.save(movieDetails);
	}

	public void deleteFromCatalog(Integer id) {
		movieCatalogRepository.deleteById(id);
	}
}
