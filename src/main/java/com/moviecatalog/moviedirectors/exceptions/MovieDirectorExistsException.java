package com.moviecatalog.moviedirectors.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="MovieDirector already exists in the catalog")
public class MovieDirectorExistsException extends RuntimeException{
}
