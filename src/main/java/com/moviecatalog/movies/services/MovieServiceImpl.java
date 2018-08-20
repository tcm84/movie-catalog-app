package com.moviecatalog.movies.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalog.moviedirectors.exceptions.MovieDirectorNotFoundException;
import com.moviecatalog.moviedirectors.model.MovieDirectorDetails;
import com.moviecatalog.movies.exceptions.MovieExistsException;
import com.moviecatalog.movies.exceptions.MovieNotFoundException;
import com.moviecatalog.movies.model.MovieDetails;
import com.moviecatalog.movies.repo.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository movieRepository;
	
	@Override
	public List<MovieDetails> getFilmography(Integer moviedirectorId) {
		MovieDirectorDetails movieDirectorDetails = new MovieDirectorDetails();
		movieDirectorDetails.setMoviedirectorId(moviedirectorId);

		Optional<List<MovieDetails>> optionalFilmography = movieRepository.findByMovieDirectorDetails(movieDirectorDetails);
		if (optionalFilmography.isPresent()) {
			return optionalFilmography.get();
		} else {
			throw new MovieDirectorNotFoundException();
		}
	}
	
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
