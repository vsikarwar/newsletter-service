package com.newsletter.service.services;

import java.util.ArrayList;
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
	public List<Subscription> getSubscriptions() {
		return dataStore.getSubscriptions();
	}
	
	@Override
	public List<Subscription> getSubscriptions(Date after, Date before) {
		if(after.compareTo(before) == 0) {
			return getSubscriptions();
		}
		List<Subscription> result = new ArrayList<>();
		if(before.before(after)) {
			for(Subscription subs: dataStore.getSubscriptionAfter(after)) {
				result.add(subs);
			}
			for(Subscription subs: dataStore.getSubscriptionBefore(before)) {
				result.add(subs);
			}
		}else {
			List<Subscription> afterSubs = dataStore.getSubscriptionAfter(after);
			for(Subscription subs: afterSubs) {
				if(subs.getDate().before(before)) {
					result.add(subs);
				}
			}
		}
		return result;
	}

	@Override
	public Subscription getSubscription(Long userId) throws UserNotFoundException {
		return dataStore.getSubscription(userId);
	}

}
