package com.trips.controller;

import java.util.Map;

public class OSMNode {
	private String id;
	private String lat;
	private String lon;

	
	public OSMNode(String _id,String _lat,String _lon){
		id = _id;
		lon = _lon;
		lat = _lat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}
}
