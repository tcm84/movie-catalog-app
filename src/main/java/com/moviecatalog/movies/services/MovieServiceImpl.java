package com.moviecatalog.movies.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalog.moviedirectors.models.MovieDirectorDetails;
import com.moviecatalog.movieratings.models.MovieRatingDetails;
import com.moviecatalog.movies.enums.MovieClassification;
import com.moviecatalog.movies.exceptions.FilmographyNotFoundException;
import com.moviecatalog.movies.exceptions.MovieExistsException;
import com.moviecatalog.movies.exceptions.MovieListNotFoundException;
import com.moviecatalog.movies.exceptions.MovieNotFoundException;
import com.moviecatalog.movies.models.MovieDetails;
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
			throw new FilmographyNotFoundException();
		}
	}
	
	@Override
	public List<MovieDetails> getMovieList(Integer movieratingId) {
		MovieRatingDetails movieRatingsDetails = new MovieRatingDetails();
		movieRatingsDetails.setMovieratingId(movieratingId);

		Optional<List<MovieDetails>> optionalMovieList = movieRepository.findByMovieRatingDetails(movieRatingsDetails);
		if (optionalMovieList.isPresent()) {
			return optionalMovieList.get();
		} else {
			throw new MovieListNotFoundException();
		}
	}
	
	@Override
	public List<MovieDetails> getMovieListAboveMovieRating(MovieClassification movieClassification) {				     
		 return StreamSupport.stream(movieRepository.findAll().spliterator(), false)
	 			  .filter(md -> Objects.nonNull(md.getMovieRatingDetails()))
	 			  .filter(md -> md.getMovieRatingDetails().getMovieClassification().getMinAge() >= movieClassification.getMinAge())
	 			  .collect(Collectors.toList());
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
