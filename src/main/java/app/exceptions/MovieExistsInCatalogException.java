package app.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Movie already exists in the catalog")
public class MovieExistsInCatalogException extends RuntimeException{
}
