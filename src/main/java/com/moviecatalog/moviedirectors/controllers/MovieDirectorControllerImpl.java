package com.moviecatalog.moviedirectors.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.moviedirectors.models.MovieDirectorDetails;
import com.moviecatalog.moviedirectors.services.MovieDirectorService;

@RestController
@RequestMapping("/moviedirectors")
public class MovieDirectorControllerImpl implements MovieDirectorController {
	
	@Autowired
	private MovieDirectorService moviedirectorsServiceImpl;
	
	@Override
	@PostMapping("/add")
	public MovieDirectorDetails addMovieDirector(@Valid @RequestBody MovieDirectorDetails directorsDetails) {
		return  moviedirectorsServiceImpl.addMovieDirector(directorsDetails);
	}
	
	@Override
	@PostMapping("/update")
	public MovieDirectorDetails updateMovieDirector(@Valid @RequestBody MovieDirectorDetails movieDirectorDetails) {
		return moviedirectorsServiceImpl.updateMovieDirector(movieDirectorDetails);
	}
	
	@Override
	@DeleteMapping("/delete/{moviedirectorId}")
	public void deleteMovieDirector(@PathVariable Integer moviedirectorId) {
		moviedirectorsServiceImpl.deleteDirector(moviedirectorId);
	}
}