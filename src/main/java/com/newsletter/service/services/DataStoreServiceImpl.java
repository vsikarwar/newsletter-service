package com.newsletter.service.services;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newsletter.service.datastore.DataStore;
import com.newsletter.service.entity.Subscription;
import com.newsletter.service.entity.SubscriptionBuilder;
import com.newsletter.service.exception.UserNotFoundException;
import com.newsletter.service.factory.DataStoreFactory;

@Service
public class DataStoreServiceImpl implements DataStoreService{
	
	//private DataStore dataStore = DataStoreFactory.instance().getDataStore("INMEMORY");
	
	@Autowired
	private DataStoreFactory dataStoreFactory;
	
	private DataStore dataStore;
	
	@PostConstruct
	private void init() {
		dataStore = dataStoreFactory.getDataStore("INMEMORY");
	}

	@Override
	public boolean isUserSubscribed(Long userId) {
		return dataStore.isSubscribed(userId);
	}

	@Override
	public void subscribe(Subscription subscription) {
		dataStore.subscribe(subscription);
	}

	@Override
	public Subscription unsubscribe(Long userId) throws UserNotFoundException {
		Subscription subs = dataStore.unsubscribe(userId);
		Subscription subscription = new SubscriptionBuilder().
									setUserId(subs.getUserId()).
									setSubscribed(false).
									setDate(subs.getDate()).
									getSubscription();
		return subscription;
	}

	@Override
	public List<Subscription> getSubscriptionBefore(Date date) {
		return dataStore.getSubscriptionBefore(date);
	}

	@Override
	public List<Subscription> getSubscriptionAfter(Date date) {
		return dataStore.getSubscriptionAfter(date);
	}

	@Override
	public Subscription getSubscription(Long userId) throws UserNotFoundException {
		return dataStore.getSubscription(userId);
	}

}
