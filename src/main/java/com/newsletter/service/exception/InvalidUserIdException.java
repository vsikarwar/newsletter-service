package com.newsletter.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="User Id is not valid")
public class InvalidUserIdException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public InvalidUserIdException(String id) {
		super("Invalid user id="+ id);
	}

}
