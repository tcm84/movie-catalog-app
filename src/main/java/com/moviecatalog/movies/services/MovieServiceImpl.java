package com.moviecatalog.movies.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalog.moviedirectors.model.dto.entities.MovieDirectorDetails;
import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;
import com.moviecatalog.movies.enums.MovieClassification;
import com.moviecatalog.movies.exceptions.FilmographyNotFoundException;
import com.moviecatalog.movies.exceptions.MovieExistsException;
import com.moviecatalog.movies.exceptions.MovieListNotFoundException;
import com.moviecatalog.movies.exceptions.MovieNotFoundException;
import com.moviecatalog.movies.model.dto.entities.MovieDetails;
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
		 List<MovieDetails> combinedList = new ArrayList<>();
		 movieRepository.findAll().forEach(md -> {
			 MovieRatingDetails movieRatingDetails = md.getMovieRatingDetails();
			 if(movieRatingDetails != null && movieRatingDetails.getMovieClassification().getMinAge() >= movieClassification.getMinAge()) {
				 combinedList.add(md);
			 }
		 });
				     
		 return combinedList;
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
