package com.newsletter.service.datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.newsletter.service.entity.Subscription;
import com.newsletter.service.exception.UserNotFoundException;

public class InMemoryDataStore implements DataStore{
	
	private Map<Long, Subscription> store = new HashMap<>();
	
	@Override
	public boolean isSubscribed(Long userId) {
		return store.containsKey(userId);
	}

	@Override
	public void subscribe(Subscription subscription) {
		Long key = subscription.getUserId();
		store.put(key, subscription);
	}

	@Override
	public Subscription unsubscribe(Long userId) throws UserNotFoundException {
		if(!store.containsKey(userId)) {
			throw new UserNotFoundException(String.valueOf(userId));
		}
		Subscription subs = store.get(userId);
		store.remove(userId);
		return subs;
	}

	@Override
	public List<Subscription> getSubscriptionBefore(Date date) {
		List<Subscription> beforeSubs = new ArrayList<>();
		for(Long key: store.keySet()) {
			if(store.get(key).getDate().before(date)) {
				beforeSubs.add(store.get(key));
			}
		}
		return beforeSubs;
	}

	@Override
	public List<Subscription> getSubscriptionAfter(Date date) {
		List<Subscription> afterSubs = new ArrayList<>();
		for(Long key: store.keySet()) {
			if(store.get(key).getDate().after(date)) {
				afterSubs.add(store.get(key));
			}
		}
		return afterSubs;
	}

	@Override
	public Subscription getSubscription(Long userId) throws UserNotFoundException {
		if(!store.containsKey(userId)) {
			throw new UserNotFoundException(String.valueOf(userId));
		}
		Subscription subs = store.get(userId);
		return subs;
	}

}
