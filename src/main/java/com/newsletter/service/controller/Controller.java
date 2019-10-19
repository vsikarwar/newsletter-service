package com.newsletter.service.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.newsletter.service.entity.Subscription;
import com.newsletter.service.exception.InvalidDateException;
import com.newsletter.service.exception.InvalidUserIdException;
import com.newsletter.service.exception.UserNotFoundException;
import com.newsletter.service.request.SubscriptionRequest;
import com.newsletter.service.response.SubscriptionResponse;
import com.newsletter.service.services.SubscriptionService;
import com.newsletter.service.utils.Utils;
import com.newsletter.service.validator.Validator;

@RestController
public class Controller {
	
	@Autowired
	private SubscriptionService service;
	
	@GetMapping("/")
	public String defaultMessage() {
		return "Welcome to newsletter service";
	}
	
	@PostMapping(path="/api/subscription/user",
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<SubscriptionResponse> subscribe(@RequestBody SubscriptionRequest request) 
			throws InvalidUserIdException{
		Validator.validateUserId(request.getUserId());
		
		Subscription subs = service.subscribe(request);
		SubscriptionResponse response = generateResponse(subs);
		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	@DeleteMapping(path="/api/subscription/user/{id}",
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<SubscriptionResponse> removeSubscription(@PathVariable String id) 
			throws InvalidUserIdException, UserNotFoundException{
		Validator.validateUserId(id);
		
		Long userId = Long.valueOf(id);
		
		Subscription unsubs = service.unsubscribe(userId);
		SubscriptionResponse response = generateResponse(unsubs);
		return ResponseEntity.status(HttpStatus.OK).
					body(response);
		
	}
	
	@GetMapping(path="/api/subscription/user/{id}", 
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<SubscriptionResponse> subscription(@PathVariable String id) 
			throws InvalidUserIdException {
		
		Validator.validateUserId(id);
		Long userId = Long.valueOf(id);
		
		Subscription subs = service.isUserSubscribed(userId);
		
		if(subs.isSubscribed()) {
			return ResponseEntity.status(HttpStatus.OK).
					body(generateResponse(subs));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).
					body(generateResponseWithoutDate(subs));
		}
	}
	
	@GetMapping(path="/api/subscription/user/after/{year}/{month}/{day}",
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<SubscriptionResponse>> afterSubscription(@PathVariable String year,
													@PathVariable String month,
													@PathVariable String day) throws InvalidDateException{
		String dateStr = day + "/" + month + "/" + year;
		Date date = Utils.getDate(dateStr);
		List<Subscription> subs = service.getSubscriptionAfter(date);
		List<SubscriptionResponse> responses = new ArrayList<>();
		for(Subscription sub: subs) {
			responses.add(generateResponse(sub));
		}
		if(responses.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).
					body(responses);
		}else {
			return ResponseEntity.status(HttpStatus.OK).
					body(responses);
		}
		
	}
	
	@GetMapping(path="/api/subcription/user", produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<SubscriptionResponse>> dateSubscription(@RequestParam(required=false) String before, 
									@RequestParam(required=false) String after) throws InvalidDateException{
		List<SubscriptionResponse> responses = new ArrayList<>();
		if(before == null && after == null) {
			for(Subscription sub: service.getSubscriptions()) {
				responses.add(generateResponse(sub));
			}
		}else if(before != null && after != null){
			for(Subscription sub: service.getSubscriptions(Utils.getDate(after), Utils.getDate(before))) {
				responses.add(generateResponse(sub));
			}
		} else if(before != null) {
			for(Subscription sub: service.getSubscriptionBefore(Utils.getDate(before))) {
				responses.add(generateResponse(sub));
			}
		} else if(after != null) {
			for(Subscription sub: service.getSubscriptionAfter(Utils.getDate(after))) {
				responses.add(generateResponse(sub));
			}
		}
		if(responses.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).
					body(responses);
		}else {
			return ResponseEntity.status(HttpStatus.OK).
					body(responses);
		}
	}
	
	@GetMapping(path="/api/subscription/user/before/{year}/{month}/{day}",
			produces= {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<SubscriptionResponse>> beforeSubscription(@PathVariable String year,
													@PathVariable String month,
													@PathVariable String day) throws InvalidDateException{
		String dateStr = day + "/" + month + "/" + year;
		Date date = Utils.getDate(dateStr);
		List<Subscription> subs = service.getSubscriptionBefore(date);
		List<SubscriptionResponse> responses = new ArrayList<>();
		for(Subscription sub: subs) {
			responses.add(generateResponse(sub));
		}
		if(responses.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).
					body(responses);
		}else {
			return ResponseEntity.status(HttpStatus.OK).
					body(responses);
		}
	}
	
	
	private SubscriptionResponse generateResponse(Subscription subs) {
		SubscriptionResponse response = new SubscriptionResponse();
		response.setUserId(String.valueOf(subs.getUserId()));
		response.setDate(Utils.getDate(subs.getDate()));
		response.setSubscribed(subs.isSubscribed());
		return response;
	}
	
	private SubscriptionResponse generateResponseWithoutDate(Subscription subs) {
		SubscriptionResponse response = new SubscriptionResponse();
		response.setUserId(String.valueOf(subs.getUserId()));
		response.setSubscribed(subs.isSubscribed());
		return response;
	}

}
