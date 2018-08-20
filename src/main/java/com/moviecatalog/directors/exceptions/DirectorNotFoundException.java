package com.moviecatalog.directors.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Director not found in the catalog")
public class DirectorNotFoundException extends RuntimeException{
}
