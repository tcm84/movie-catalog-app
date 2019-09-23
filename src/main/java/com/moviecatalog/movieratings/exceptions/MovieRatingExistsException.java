package com.moviecatalog.movieratings.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Movie rating already exists in this catalog")
public class MovieRatingExistsException extends RuntimeException{
	private static final long serialVersionUID = 2898688649745969487L;
}
