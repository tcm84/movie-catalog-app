package com.moviecatalog.movies.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Movie not found in this catalog for this movieId")
public class MovieNotFoundException extends RuntimeException{
	private static final long serialVersionUID = -4125215371637718145L;
}
