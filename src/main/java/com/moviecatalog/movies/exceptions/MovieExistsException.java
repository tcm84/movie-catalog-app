package com.moviecatalog.movies.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Movie with this movieId already exists in this catalog")
public class MovieExistsException extends RuntimeException{
	private static final long serialVersionUID = 3891051261813723785L;
}
