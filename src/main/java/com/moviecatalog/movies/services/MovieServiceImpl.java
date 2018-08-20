package com.moviecatalog.movies.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalog.movies.exceptions.MovieExistsException;
import com.moviecatalog.movies.exceptions.MovieNotFoundException;
import com.moviecatalog.movies.model.MovieDetails;
import com.moviecatalog.movies.repo.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository movieRepository;
	
	@Override
	public MovieDetails addMovie(MovieDetails movieDetails) {
		if (movieRepository.existsById(movieDetails.getMovieId())) {
			throw new MovieExistsException();
		}
		return movieRepository.save(movieDetails);
	}

	@Override
	public MovieDetails updateMovie(MovieDetails movieDetails) {		
		if(!movieRepository.existsById(movieDetails.getMovieId())) {
			throw new MovieNotFoundException();
		}
		return movieRepository.save(movieDetails);
	}

	@Override
	public void deleteMovie(Integer moveiId) {
		movieRepository.deleteById(moveiId);
	}
}
