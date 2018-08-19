package com.moviecatalog.movies.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalog.movies.exceptions.MovieExistsException;
import com.moviecatalog.movies.exceptions.MovieNotFoundException;
import com.moviecatalog.movies.model.MovieDetails;
import com.moviecatalog.movies.repo.MovieCatalogRepository;

@Service
public class MovieCatalogServiceImpl implements MovieCatalogService {

	@Autowired
	private MovieCatalogRepository movieCatalogRepository;
	
	@Override
	public MovieDetails addToCatalog(MovieDetails movieDetails) {
		if (movieCatalogRepository.existsById(movieDetails.getMovieId())) {
			throw new MovieExistsException();
		}
		return movieCatalogRepository.save(movieDetails);
	}

	@Override
	public MovieDetails updateCatalog(MovieDetails movieDetails) {		
		if(!movieCatalogRepository.existsById(movieDetails.getMovieId())) {
			throw new MovieNotFoundException();
		}
		return movieCatalogRepository.save(movieDetails);
	}

	@Override
	public void deleteFromCatalog(Integer id) {
		movieCatalogRepository.deleteById(id);
	}
}
