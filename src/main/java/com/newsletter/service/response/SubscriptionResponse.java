package com.newsletter.service.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionResponse {
	
	private String userId;
	private boolean subscribed;
	
	@JsonProperty(required = false)
	private String date;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isSubscribed() {
		return subscribed;
	}
	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	

}
