package com.moviecatalog.movies.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Movie not found in this catalog")
public class MovieNotFoundException extends RuntimeException{
}
