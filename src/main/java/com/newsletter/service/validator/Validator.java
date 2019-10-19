package com.newsletter.service.validator;

import org.apache.commons.lang3.StringUtils;

import com.newsletter.service.exception.InvalidUserIdException;

public class Validator {
	public static boolean validateUserId(String id) throws InvalidUserIdException {
		if(!StringUtils.isNumeric(id)) {
			throw new InvalidUserIdException(id);
		}
		return true;
	}
}
