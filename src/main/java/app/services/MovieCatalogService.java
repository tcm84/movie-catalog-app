package app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.exceptions.MovieExistsInCatalogException;
import app.model.MovieDetails;
import app.repo.MovieCatalogRepository;

@Service
public class MovieCatalogService {

	@Autowired
	private MovieCatalogRepository movieCatalogRepository;
	
	public MovieDetails addToCatalog(MovieDetails movieDetails) {
		if (movieCatalogRepository.existsById(movieDetails.getMovieId())) {
			throw new MovieExistsInCatalogException();
		}
		return movieCatalogRepository.save(movieDetails);
	}

	public MovieDetails updateCatalog(MovieDetails movieDetails) {
		return movieCatalogRepository.save(movieDetails);
	}

	public void deleteFromCatalog(Integer id) {
		movieCatalogRepository.deleteById(id);
	}
}
