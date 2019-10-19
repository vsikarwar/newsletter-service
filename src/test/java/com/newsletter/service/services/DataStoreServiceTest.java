package com.newsletter.service.services;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.newsletter.service.datastore.DataStore;
import com.newsletter.service.datastore.InMemoryDataStore;
import com.newsletter.service.entity.Subscription;
import com.newsletter.service.exception.InvalidDateException;
import com.newsletter.service.exception.UserNotFoundException;
import com.newsletter.service.utils.Utils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataStoreServiceTest {
	
	@Autowired
	private DataStoreService service;
	
	private List<Subscription> testSubs;
	
	private DataStore store;

	@Before
	public void setUp() throws Exception {
		
		store = new InMemoryDataStore();

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
		
		
		for(Subscription subs : testSubs) {
			store.subscribe(subs);
		}
		
		ReflectionTestUtils.setField(service, "dataStore", store);
		
	}

	@After
	public void tearDown() throws Exception {
		for(Subscription subs : testSubs) {
			if(store.isSubscribed(subs.getUserId()))
				store.unsubscribe(subs.getUserId());
		}
	}
	
	@Test
    public void contexLoads() throws Exception {
        assertThat(service).isNotNull();
    }

	@Test
	public void testIsUserSubscribed() {
		//valid path
		Subscription subs = testSubs.get(0);
		boolean result = service.isUserSubscribed(subs.getUserId());
		assertTrue(result);
		
		//Invalid path
		result = service.isUserSubscribed(Long.valueOf(9999));
		assertFalse(result);
	}

	@Test(expected = UserNotFoundException.class)
	public void testUnsubscribe() throws UserNotFoundException {
		//valid path
		Subscription subs = testSubs.get(1);
		boolean result = service.isUserSubscribed(subs.getUserId());
		assertTrue(result);
		service.unsubscribe(subs.getUserId());
		result = service.isUserSubscribed(subs.getUserId());
		assertFalse(result);
		
		//Invalid Path
		Long invalidUser = Long.valueOf(9999);
		result = service.isUserSubscribed(invalidUser);
		assertFalse(result);
		service.unsubscribe(invalidUser);
		result = service.isUserSubscribed(invalidUser);
		assertFalse(result);
	}

	@Test
	public void testGetSubscriptionBefore() throws ParseException, InvalidDateException {
		Date testDate = Utils.getDate("13/02/2008");
		List<Subscription> results = service.getSubscriptionBefore(testDate);
		
		for(Subscription result: results) {
			assertTrue(result.getDate().before(testDate));
		}
	}

	@Test
	public void testGetSubscriptionAfter() throws ParseException, InvalidDateException {
		Date testDate = Utils.getDate("13/02/2008");
		List<Subscription> results = service.getSubscriptionAfter(testDate);
		
		for(Subscription result: results) {
			assertTrue(result.getDate().after(testDate));
		}
	}
	
	@Test
	public void testGetAllSubscriptions() throws ParseException, InvalidDateException {
		List<Subscription> results = service.getSubscriptions();
		for(Subscription result: results) {
			assertTrue(store.isSubscribed(result.getUserId()));
		}
	}
	
	@Test
	public void testGetBeforeAfterSubscriptions1() throws ParseException, InvalidDateException {
		Date afterDate = Utils.getDate("13/02/2008");
		Date beforeDate = Utils.getDate("13/10/2008");
		List<Subscription> results = service.getSubscriptions(afterDate, beforeDate);
		for(Subscription result: results) {
			assertTrue(result.getDate().before(beforeDate) && result.getDate().after(afterDate));
		}
	}
	
	@Test
	public void testGetBeforeAfterSubscriptions2() throws ParseException, InvalidDateException {
		Date afterDate = Utils.getDate("13/10/2008");
		Date beforeDate = Utils.getDate("13/02/2008");
		List<Subscription> results = service.getSubscriptions(afterDate, beforeDate);
		for(Subscription result: results) {
			assertTrue(result.getDate().before(beforeDate) || result.getDate().after(afterDate));
		}
	}

}
