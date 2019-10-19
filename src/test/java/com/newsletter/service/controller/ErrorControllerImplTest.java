package com.newsletter.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ErrorControllerImplTest {
	
	@Autowired
    private ErrorController controller;
	
	@LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

	@Test
	public void testInvalidPath() {
		String expectedResponse = "{\"ERROR\":\"Something went wrong\"}";
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/abc",
                String.class)).contains(expectedResponse);
	}
	
	@Test
	public void testInvalidUserId() {
		String expectedResponse = "{\"ERROR\":\"User Id is not valid\"}";
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + 
				"/api/subscription/user/" + "123abc", 
				String.class)).contains(expectedResponse);
	}

}
