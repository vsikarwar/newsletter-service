package com.newsletter.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.newsletter.service.exception.InvalidDateException;

public class Utils {
	
	public static final String DATE_FORMAT = "dd/MM/yyyy"; 
	
	public static Date getDate(String dStr) throws InvalidDateException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		sdf.setLenient(false);
		try {
			return sdf.parse(dStr);
		}catch(ParseException pe) {
			throw new InvalidDateException(dStr);
		}
		
	}
	
	public static String getDate(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);  
		return sdf.format(d);
	}

}
