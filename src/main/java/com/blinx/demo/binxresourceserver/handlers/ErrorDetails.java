package com.blinx.demo.binxresourceserver.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorDetails {
	private final HttpStatus status;
	private final String message;
	private final List<String> errors;

	public ErrorDetails(HttpStatus status, String message, String error) {
		super();
		this.status = status;
		this.message = message;
		errors = new ArrayList<>();
		errors.add(error);
	}

	public ErrorDetails(HttpStatus status, String message, List<String> errors) {
		super();
		this.status = status;
		this.message = message;
		this.errors = errors;
	}
	
	public HttpStatus getStatus() {
		return HttpStatus.valueOf(status.value());
	}
}
