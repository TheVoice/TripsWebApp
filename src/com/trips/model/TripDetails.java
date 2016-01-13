package com.trips.model;

public class TripDetails {

	private String startRoad;
	private Integer minLength;
	private Integer maxLength;
	private String message;
	private String strategy;

	public TripDetails(){}
	
	public TripDetails(String _message){
		message = _message;
	}
	
	public String getStartRoad() {
		return startRoad;
	}

	public void setStartRoad(String startRoad) {
		this.startRoad = startRoad;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}


}
