package com.newsletter.service.entity;

import java.util.Date;

public class Subscription {
	
	private Long userId;
	private boolean subscribed;
	private Date date;
	
	public Subscription(Long userId, Date date, boolean subscribed) {
		this.userId = userId;
		this.date = date;
		this.subscribed = subscribed;
	}
	
	public Long getUserId() {
		return userId;
	}
	public boolean isSubscribed() {
		return subscribed;
	}
	public Date getDate() {
		return date;
	}
	
}
