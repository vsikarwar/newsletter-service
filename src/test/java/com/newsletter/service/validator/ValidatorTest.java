package com.newsletter.service.validator;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.newsletter.service.exception.InvalidUserIdException;
@RunWith(SpringRunner.class)
public class ValidatorTest {
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUserId() throws InvalidUserIdException {
		assertTrue(Validator.validateUserId("23223"));
	}
	
	@Test(expected=InvalidUserIdException.class)
	public void testInvalidUserId() throws InvalidUserIdException {
		assertFalse(Validator.validateUserId("123abc"));
	}

}
