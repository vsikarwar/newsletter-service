package com.newsletter.service.datastore;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.newsletter.service.entity.Subscription;
import com.newsletter.service.exception.UserNotFoundException;

public class InMemoryDataStoreTest {
	
	private DataStore store;
	private List<Subscription> testSubs;

	@Before
	public void setUp() throws Exception {
		testSubs = new ArrayList<>();
		
		List<String> dates  = new ArrayList<>();
		dates.add("10/09/2007");
		dates.add("13/10/2008");
		dates.add("13/06/2008");
		dates.add("13/02/2008");
		dates.add("15/02/2008");
		dates.add("18/02/2008");
		dates.add("13/02/2010");
		
		Long userId = Long.valueOf(1234);
		
		for(String date : dates) {
			Date d =new SimpleDateFormat("dd/MM/yyyy").parse(date); 
			Subscription sub = new Subscription(userId++, d, true);
			testSubs.add(sub);
		}
		
		store = new InMemoryDataStore();
		
		for(Subscription subs : testSubs) {
			store.subscribe(subs);
		}
		
	}

	@After
	public void tearDown() throws Exception {
		for(Subscription subs : testSubs) {
			if(store.isSubscribed(subs.getUserId()))
				store.unsubscribe(subs.getUserId());
		}
	}

	@Test
	public void testIsSubscribedAndSubscribe() {
		
		//valid path
		Subscription subs = testSubs.get(0);
		boolean result = store.isSubscribed(subs.getUserId());
		assertTrue(result);
		
		//Invalid path
		result = store.isSubscribed(Long.valueOf(9999));
		assertFalse(result);
	}

	@Test(expected = UserNotFoundException.class)
	public void testUnsubscribe() throws UserNotFoundException {
		//valid path
		Subscription subs = testSubs.get(1);
		boolean result = store.isSubscribed(subs.getUserId());
		assertTrue(result);
		store.unsubscribe(subs.getUserId());
		result = store.isSubscribed(subs.getUserId());
		assertFalse(result);
		
		//Invalid Path
		Long invalidUser = Long.valueOf(9999);
		result = store.isSubscribed(invalidUser);
		assertFalse(result);
		store.unsubscribe(invalidUser);
		result = store.isSubscribed(invalidUser);
		assertFalse(result);
		
		
	}

	@Test
	public void testGetSubscriptionBefore() throws ParseException {
		Date testDate = new SimpleDateFormat("dd/MM/yyyy").parse("13/02/2008");
		List<Subscription> results = store.getSubscriptionBefore(testDate);
		
		for(Subscription result: results) {
			assertTrue(result.getDate().before(testDate));
		}
	}

	@Test
	public void testGetSubscriptionAfter() throws ParseException {
		Date testDate = new SimpleDateFormat("dd/MM/yyyy").parse("13/02/2008");
		List<Subscription> results = store.getSubscriptionAfter(testDate);
		
		for(Subscription result: results) {
			assertTrue(result.getDate().after(testDate));
		}
	}


}
