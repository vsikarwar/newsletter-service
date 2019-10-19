package com.newsletter.service.datastore;

import java.util.Date;
import java.util.List;

import com.newsletter.service.entity.Subscription;
import com.newsletter.service.exception.UserNotFoundException;

public interface DataStore {
	
	Subscription getSubscription(Long userId) throws UserNotFoundException;
	
	boolean isSubscribed(Long userId);
	
	void subscribe(Subscription subscription);
	
	Subscription unsubscribe(Long userId) throws UserNotFoundException;
	
	List<Subscription> getSubscriptionBefore(Date date);
	
	List<Subscription> getSubscriptionAfter(Date date);
	
	List<Subscription> getSubscriptions();

}
