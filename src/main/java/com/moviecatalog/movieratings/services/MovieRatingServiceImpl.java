package com.moviecatalog.movieratings.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalog.movieratings.exceptions.MovieRatingExistsException;
import com.moviecatalog.movieratings.exceptions.MovieRatingNotFoundException;
import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;
import com.moviecatalog.movieratings.repo.MovieRatingRepository;

@Service
public class MovieRatingServiceImpl implements MovieRatingService {

	@Autowired
	private MovieRatingRepository movieRatingRepository;
	
	@Override
	public MovieRatingDetails addMovieRating(MovieRatingDetails movieRatingDetails) {
		if(movieRatingRepository.existsById(movieRatingDetails.getMovieratingId())) {
			throw new MovieRatingExistsException();
		}
		return movieRatingRepository.save(movieRatingDetails);
	}
	
	@Override
	public MovieRatingDetails updateMovieRating(MovieRatingDetails movieRatingDetails) {
		if(!movieRatingRepository.existsById(movieRatingDetails.getMovieratingId())) {
			throw new MovieRatingNotFoundException();
		}
		
		return movieRatingRepository.save(movieRatingDetails);
	}
}