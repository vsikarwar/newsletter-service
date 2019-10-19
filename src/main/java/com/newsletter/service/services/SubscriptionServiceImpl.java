package com.newsletter.service.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newsletter.service.entity.Subscription;
import com.newsletter.service.entity.SubscriptionBuilder;
import com.newsletter.service.exception.UserNotFoundException;
import com.newsletter.service.request.SubscriptionRequest;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
	
	@Autowired
	private DataStoreService storeService;
	
	@Override
	public Subscription subscribe(SubscriptionRequest request) {
		
		Subscription subs = new SubscriptionBuilder().
									setUserId(Long.valueOf(request.getUserId())).
									setSubscribed(true).
									getSubscription();
		
		storeService.subscribe(subs);
		return subs;
	}

	@Override
	public Subscription unsubscribe(Long userId) throws UserNotFoundException {
		return storeService.unsubscribe(userId);
	}

	@Override
	public List<Subscription> getSubscriptionBefore(Date date) {
		return storeService.getSubscriptionBefore(date);
	}

	@Override
	public List<Subscription> getSubscriptionAfter(Date date) {
		return storeService.getSubscriptionAfter(date);
	}

	@Override
	public Subscription isUserSubscribed(Long userId) {
		boolean isSubs = storeService.isUserSubscribed(userId);
		Subscription subs = new SubscriptionBuilder().
										setUserId(userId).
										getSubscription();
		if(isSubs) {
			try {
				subs = storeService.getSubscription(userId);
			}catch(UserNotFoundException unfe) {
				return subs;
			}
		}
		return subs;
	}

}
