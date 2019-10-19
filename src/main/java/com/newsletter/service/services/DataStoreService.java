package com.newsletter.service.services;

import java.util.Date;
import java.util.List;

import com.newsletter.service.entity.Subscription;
import com.newsletter.service.exception.UserNotFoundException;

public interface DataStoreService {
	
	Subscription getSubscription(Long userId) throws UserNotFoundException;
	
	boolean isUserSubscribed(Long userId);
	
	void subscribe(Subscription request);
	
	Subscription unsubscribe(Long userId) throws UserNotFoundException;
	
	List<Subscription> getSubscriptionBefore(Date date);
	
	List<Subscription> getSubscriptionAfter(Date date);

}
