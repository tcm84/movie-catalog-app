package com.moviecatalog.moviedirectors.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Movie director already exists in this catalog")
public class MovieDirectorExistsException extends RuntimeException{
	private static final long serialVersionUID = 1L;
}
