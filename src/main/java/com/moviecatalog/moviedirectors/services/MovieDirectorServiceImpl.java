package com.moviecatalog.moviedirectors.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalog.moviedirectors.exceptions.MovieDirectorExistsException;
import com.moviecatalog.moviedirectors.exceptions.MovieDirectorNotFoundException;
import com.moviecatalog.moviedirectors.model.MovieDirectorDetails;
import com.moviecatalog.moviedirectors.repo.MovieDirectorRepository;

@Service
public class MovieDirectorServiceImpl implements MovieDirectorService {

	@Autowired
	private MovieDirectorRepository movieDirectorRepository;
	
	@Override
	public MovieDirectorDetails addMovieDirector(MovieDirectorDetails movieDirectorDetails) {
		if(movieDirectorRepository.existsById(movieDirectorDetails.getMoviedirectorId())) {
			throw new MovieDirectorExistsException();
		}
		return movieDirectorRepository.save(movieDirectorDetails);
	}
	
	@Override
	public MovieDirectorDetails updateDirector(MovieDirectorDetails movieDirectorDetails) {
		if(!movieDirectorRepository.existsById(movieDirectorDetails.getMoviedirectorId())) {
			throw new MovieDirectorNotFoundException();
		}
		return movieDirectorRepository.save(movieDirectorDetails);
	}
	
	@Override
	public void deleteDirector(Integer directorId) {
		movieDirectorRepository.deleteById(directorId);
	}
}