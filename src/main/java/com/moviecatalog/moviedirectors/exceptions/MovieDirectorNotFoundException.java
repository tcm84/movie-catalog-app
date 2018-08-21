package com.moviecatalog.moviedirectors.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Movie director not found in this catalog")
public class MovieDirectorNotFoundException extends RuntimeException{
}
