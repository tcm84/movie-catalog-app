package com.blinx.demo.binxresourceserver.users.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="User has already registered")
public class UserAlreadyRegisteredException extends RuntimeException{

}
