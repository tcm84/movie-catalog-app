package com.blinx.demo.binxresourceserver.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
																  HttpHeaders headers, 
																  HttpStatus status, 
																  WebRequest request) {
	    List<String> errors = new ArrayList<>();
	    ex.getBindingResult().getFieldErrors()
	    					  .forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
	    ex.getBindingResult().getGlobalErrors()
	    					 .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));
	    ErrorDetails errorDetails = 
	      new ErrorDetails(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
	    return handleExceptionInternal(ex, errorDetails, headers, errorDetails.getStatus(), request);
	}
}
