package com.newsletter.service.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.newsletter.service.entity.Subscription;
import com.newsletter.service.entity.SubscriptionBuilder;
import com.newsletter.service.request.SubscriptionRequest;
import com.newsletter.service.services.DataStoreService;
import com.newsletter.service.utils.Utils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerTest {
	
	@Autowired
	private DataStoreService storeService;
	
	private Long testUserId = Long.valueOf(1234);
	private Date testDate; 

	private List<Subscription> testSubsList;
	
	@Before
	public void setUp() throws Exception {
		testDate = Utils.getDate("18/10/2019");
		Subscription testSubs = new SubscriptionBuilder().
									setUserId(testUserId).
									setSubscribed(true).
									setDate(testDate).
									getSubscription();
		storeService.subscribe(testSubs);
		
		testSubsList = new ArrayList<>();
		
		List<String> dates  = new ArrayList<>();
		dates.add("10/09/2007");
		dates.add("13/10/2008");
		dates.add("13/06/2008");
		dates.add("13/02/2008");
		dates.add("15/02/2008");
		dates.add("18/02/2008");
		dates.add("13/02/2010");
		
		Long userId = Long.valueOf(1111);
		
		for(String date : dates) {
			Date d =Utils.getDate(date); 
			Subscription sub = new Subscription(userId++, d, true);
			testSubsList.add(sub);
		}
		
		
		for(Subscription subs : testSubsList) {
			storeService.subscribe(subs);
		}
		
	}

	@After
	public void tearDown() throws Exception {
		
		if(storeService.isUserSubscribed(testUserId)) {
			storeService.unsubscribe(testUserId);
		}
		
		
		for(Subscription subs : testSubsList) {
			if(storeService.isUserSubscribed(subs.getUserId()))
				storeService.unsubscribe(subs.getUserId());
		}
		
	}
	
	@Autowired
    private Controller controller;
	
	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

	@Test
	public void testDefaultMessage() {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("Welcome to newsletter service");
	}

	@Test
	public void testIsSubscribed() {
		String strDate = "18/10/2019";  
		String expectedResponse = "{\"userId\":\"1234\",\"subscribed\":true,\"date\":\""+ strDate+"\"}";
		
		String url = "http://localhost:" + port + "/api/subscription/user/" + testUserId;
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), expectedResponse);
	}
	
	@Test
	public void testIsNotSubscribed() {
		String expectedResponse = "{\"userId\":\"9876\",\"subscribed\":false,\"date\":null}";
		
		String url = "http://localhost:" + port + "/api/subscription/user/9876";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
		assertEquals(response.getBody(), expectedResponse);
	}
	
	@Test
	public void testInvalidIsSubscribed() {
		String expectedResponse = "{\"ERROR\":\"User Id is not valid\"}";
		
		String url = "http://localhost:" + port + "/api/subscription/user/abc";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
		assertEquals(response.getBody(), expectedResponse);
	}

	@Test
	public void testRemoveSubscription() {
		String url = "http://localhost:" + port + 
				"/api/subscription/user/" + testUserId;
		
		String expected = "{\"userId\":\"1234\",\"subscribed\":false,\"date\":\"18/10/2019\"}";
		
		ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
		
		assertEquals(resp.getStatusCode(), HttpStatus.OK);
		assertEquals(expected, resp.getBody());
		assertFalse(storeService.isUserSubscribed(testUserId));
	}
	
	@Test
	public void testInvalidRemoveSubscription() {
		String url = "http://localhost:" + port + "/api/subscription/user/abc";
		String expected = "{\"ERROR\":\"User Id is not valid\"}";
		ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
		
		assertEquals(resp.getStatusCode(), HttpStatus.FORBIDDEN);
		assertEquals(expected, resp.getBody());
	}
	
	@Test
	public void testNotFoundRemoveSubscription() {
		String url = "http://localhost:" + port + "/api/subscription/user/9911";
		String expected = "{\"ERROR\":\"User Not Found\"}";
		ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, String.class);
		
		assertEquals(resp.getStatusCode(), HttpStatus.NOT_FOUND);
		assertEquals(expected, resp.getBody());
	}

	@Test
	public void testSubscribe() {
		SubscriptionRequest request = new SubscriptionRequest();
		request.setUserId("9876");
		
		String url = "http://localhost:" + port + "/api/subscription/user";
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		
		String strDate = Utils.getDate(new Date());  
		
		String expectedResponse = "{\"userId\":\"9876\",\"subscribed\":true,\"date\":\""+ strDate+"\"}";
		
		assertEquals(response.getBody(), expectedResponse);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void testInvalidSubscribe() {
		SubscriptionRequest request = new SubscriptionRequest();
		request.setUserId("abc");
		String url = "http://localhost:" + port + "/api/subscription/user";
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		
		assertEquals(response.getStatusCode(),HttpStatus.FORBIDDEN);
		assertEquals(response.getBody(), "{\"ERROR\":\"User Id is not valid\"}");
	}
	
	@Test
	public void testAfterSubscription() {
		String expectedResponse = "[{\"userId\":\"1234\",\"subscribed\":true,\"date\":\"18/10/2019\"},"
				+ "{\"userId\":\"1117\",\"subscribed\":true,\"date\":\"13/02/2010\"}]";
		String url = "http://localhost:" + port + "/api/subscription/user/after/2009/10/01";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), expectedResponse);
	}
	
	@Test
	public void testInvalidAfterSubscription() {
		String expectedResponse = "{\"ERROR\":\"Date is not valid\"}";
		String url = "http://localhost:" + port + "/api/subscription/user/after/2009/60/01";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
		assertEquals(response.getBody(), expectedResponse);
		
	}
	
	@Test
	public void testEmptyAfterSubscription() {
		String expectedResponse = "[]";
		String url = "http://localhost:" + port + "/api/subscription/user/after/2049/11/01";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
		assertEquals(response.getBody(), expectedResponse);
		
	}
	
	@Test
	public void testBeforeSubscription() {
		String expectedResponse = "[{\"userId\":\"1111\",\"subscribed\":true,\"date\":\"10/09/2007\"},"
				+ "{\"userId\":\"1112\",\"subscribed\":true,\"date\":\"13/10/2008\"},"
				+ "{\"userId\":\"1113\",\"subscribed\":true,\"date\":\"13/06/2008\"},"
				+ "{\"userId\":\"1114\",\"subscribed\":true,\"date\":\"13/02/2008\"},"
				+ "{\"userId\":\"1115\",\"subscribed\":true,\"date\":\"15/02/2008\"},"
				+ "{\"userId\":\"1116\",\"subscribed\":true,\"date\":\"18/02/2008\"}]";
		String url = "http://localhost:" + port + "/api/subscription/user/before/2009/10/01";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody(), expectedResponse);
	}
	
	@Test
	public void testInvalidBeforeSubscription() {
		String expectedResponse = "{\"ERROR\":\"Date is not valid\"}";
		String url = "http://localhost:" + port + "/api/subscription/user/before/2009/60/01";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
		assertEquals(response.getBody(), expectedResponse);
		
	}
	
	@Test
	public void testEmptyBeforeSubscription() {
		String expectedResponse = "[]";
		String url = "http://localhost:" + port + "/api/subscription/user/before/1111/11/01";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
		assertEquals(response.getBody(), expectedResponse);
		
	}
	
	@Test
	public void testAllBeforeAfterSubscription() {
		String expectedResponse = "[{\"userId\":\"1234\",\"subscribed\":true,\"date\":\"18/10/2019\"},"
				+ "{\"userId\":\"1111\",\"subscribed\":true,\"date\":\"10/09/2007\"},"
				+ "{\"userId\":\"1112\",\"subscribed\":true,\"date\":\"13/10/2008\"},"
				+ "{\"userId\":\"1113\",\"subscribed\":true,\"date\":\"13/06/2008\"},"
				+ "{\"userId\":\"1114\",\"subscribed\":true,\"date\":\"13/02/2008\"},"
				+ "{\"userId\":\"1115\",\"subscribed\":true,\"date\":\"15/02/2008\"},"
				+ "{\"userId\":\"1116\",\"subscribed\":true,\"date\":\"18/02/2008\"},"
				+ "{\"userId\":\"1117\",\"subscribed\":true,\"date\":\"13/02/2010\"}]";
		String url = "http://localhost:" + port + "/api/subcription/user?after=10/10/2009&before=10/10/2009";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getBody(),expectedResponse);
	}
	
	@Test
	public void testBeforeAndAfterSubscription1() {
		String expectedResponse = "[{\"userId\":\"1234\",\"subscribed\":true,\"date\":\"18/10/2019\"},"
				+ "{\"userId\":\"1117\",\"subscribed\":true,\"date\":\"13/02/2010\"},"
				+ "{\"userId\":\"1111\",\"subscribed\":true,\"date\":\"10/09/2007\"}]";
		String url = "http://localhost:" + port + "/api/subcription/user?after=13/10/2008&before=13/02/2008";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getBody(),expectedResponse);
	}
	
	@Test
	public void testBeforeAndAfterSubscription2() {
		String expectedResponse = "[{\"userId\":\"1113\",\"subscribed\":true,\"date\":\"13/06/2008\"},"
				+ "{\"userId\":\"1115\",\"subscribed\":true,\"date\":\"15/02/2008\"},"
				+ "{\"userId\":\"1116\",\"subscribed\":true,\"date\":\"18/02/2008\"}]";
		String url = "http://localhost:" + port + "/api/subcription/user?after=13/02/2008&before=13/10/2008";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getBody(),expectedResponse);
	}
	
	@Test
	public void testBeforeOnlySubscription() {
		String expectedResponse = "[{\"userId\":\"1111\",\"subscribed\":true,\"date\":\"10/09/2007\"}]";
		String url = "http://localhost:" + port + "/api/subcription/user?before=10/01/2008";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getBody(),expectedResponse);
	}
	
	@Test
	public void testAfterOnlySubscription() {
		String expectedResponse = "[{\"userId\":\"1234\",\"subscribed\":true,\"date\":\"18/10/2019\"},{\"userId\":\"1117\",\"subscribed\":true,\"date\":\"13/02/2010\"}]";
		String url = "http://localhost:" + port + "/api/subcription/user?after=10/10/2009";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getBody(),expectedResponse);
	}
	
	@Test
	public void testAllSubscription() {
		String expectedResponse = "[{\"userId\":\"1234\",\"subscribed\":true,\"date\":\"18/10/2019\"},"
				+ "{\"userId\":\"1111\",\"subscribed\":true,\"date\":\"10/09/2007\"},"
				+ "{\"userId\":\"1112\",\"subscribed\":true,\"date\":\"13/10/2008\"},"
				+ "{\"userId\":\"1113\",\"subscribed\":true,\"date\":\"13/06/2008\"},"
				+ "{\"userId\":\"1114\",\"subscribed\":true,\"date\":\"13/02/2008\"},"
				+ "{\"userId\":\"1115\",\"subscribed\":true,\"date\":\"15/02/2008\"},"
				+ "{\"userId\":\"1116\",\"subscribed\":true,\"date\":\"18/02/2008\"},"
				+ "{\"userId\":\"1117\",\"subscribed\":true,\"date\":\"13/02/2010\"}]";
		String url = "http://localhost:" + port + "/api/subcription/user";
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertEquals(response.getBody(),expectedResponse);
	}

}
