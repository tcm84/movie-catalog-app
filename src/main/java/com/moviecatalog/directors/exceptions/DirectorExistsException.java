package com.moviecatalog.directors.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Director already exists in the catalog")
public class DirectorExistsException extends RuntimeException{
}
