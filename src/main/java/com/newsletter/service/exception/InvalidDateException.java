package com.newsletter.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Date is not valid")
public class InvalidDateException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public InvalidDateException(String d) {
		super("Invalid Date="+ d);
	}

}
