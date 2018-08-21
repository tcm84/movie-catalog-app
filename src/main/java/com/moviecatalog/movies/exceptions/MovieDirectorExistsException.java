package com.moviecatalog.movies.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Movie director already exists in this catalog")
public class MovieDirectorExistsException extends RuntimeException{
}
