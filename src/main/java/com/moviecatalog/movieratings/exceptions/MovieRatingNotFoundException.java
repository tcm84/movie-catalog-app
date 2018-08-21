package com.moviecatalog.movieratings.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Movie rating not found in this catalog")
public class MovieRatingNotFoundException extends RuntimeException{
}
