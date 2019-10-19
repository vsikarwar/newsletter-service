package com.newsletter.service.utils;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.newsletter.service.exception.InvalidDateException;

public class UtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDateString() throws InvalidDateException {
		String testDate = "10/11/2009";
		Date date = Utils.getDate(testDate);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		assertTrue(testDate.equals(sdf.format(date)));
	}
	
	@Test(expected = InvalidDateException.class)
	public void testInvalidDateString() throws InvalidDateException {
		String testDate = "abc";
		Date date = Utils.getDate(testDate);
		assertTrue(date == null);
	}

	@Test
	public void testGetDateDate() throws ParseException {
		String testDate = "10/11/2009";
		Date date = new SimpleDateFormat("dd/MM/yyyy").parse(testDate);
		String result = Utils.getDate(date);
		assertTrue(testDate.equals(result));
	}

}
