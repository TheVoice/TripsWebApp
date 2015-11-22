package com.trips.model;

public class TripDetails {

	private String startRoad;
	private String startX;
	private String startY;
	private Integer minLength;
	private Integer maxLength;

	public String getStartRoad() {
		return startRoad;
	}

	public void setStartRoad(String startRoad) {
		this.startRoad = startRoad;
	}

	public String getStartX() {
		return startX;
	}

	public void setStartX(String startX) {
		this.startX = startX;
	}

	public String getStartY() {
		return startY;
	}

	public void setStartY(String startY) {
		this.startY = startY;
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


}
