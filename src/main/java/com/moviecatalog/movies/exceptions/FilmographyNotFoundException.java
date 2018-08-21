package com.moviecatalog.movies.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Filmography not found in this catalog for this director")
public class FilmographyNotFoundException extends RuntimeException{
}
