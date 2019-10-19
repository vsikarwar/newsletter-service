package com.newsletter.service.services;

import java.util.Date;
import java.util.List;

import com.newsletter.service.entity.Subscription;
import com.newsletter.service.exception.UserNotFoundException;
import com.newsletter.service.request.SubscriptionRequest;

public interface SubscriptionService {
		
	Subscription isUserSubscribed(Long userId);
	
	Subscription subscribe(SubscriptionRequest request);
	
	Subscription unsubscribe(Long userId) throws UserNotFoundException;
	
	List<Subscription> getSubscriptionBefore(Date date);
	
	List<Subscription> getSubscriptionAfter(Date date);
	
	List<Subscription> getSubscriptions();
	
	List<Subscription> getSubscriptions(Date after, Date before);

}
