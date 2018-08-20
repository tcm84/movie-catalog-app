package com.moviecatalog.moviedirectors.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="MovieDirector not found in the catalog")
public class MovieDirectorNotFoundException extends RuntimeException{
}
