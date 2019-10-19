package com.newsletter.service.entity;

import java.util.Date;

public class SubscriptionBuilder {
	
	private Long userId;
	private boolean subscribed;
	private Date date;
	
	public SubscriptionBuilder setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	public SubscriptionBuilder setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
		return this;
	}
	public SubscriptionBuilder setDate(Date date) {
		this.date = date;
		return this;
	}
	
	public Subscription getSubscription(Subscription subs) {
		Subscription subscription = new Subscription(subs.getUserId(), 
										subs.getDate(), subs.isSubscribed());
		return subscription;
	}
	
	public Subscription getSubscription() {
		
		if(date == null) {
			date = new Date();
		}
		Subscription subs = new Subscription(userId, date, subscribed);
		return subs;
		
	}
	

}
