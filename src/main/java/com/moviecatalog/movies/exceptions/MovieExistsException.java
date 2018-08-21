package com.moviecatalog.movies.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Movie already exists in this catalog")
public class MovieExistsException extends RuntimeException{
}
