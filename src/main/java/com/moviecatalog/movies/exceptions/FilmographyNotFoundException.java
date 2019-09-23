package com.moviecatalog.movies.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Filmography not found in this catalog for this moviedirectorId")
public class FilmographyNotFoundException extends RuntimeException{
	private static final long serialVersionUID = -8414135665713113740L;
}
