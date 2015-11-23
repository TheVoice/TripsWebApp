package com.trips.model;

import java.util.List;

public class RouteResults {

	private Double latitudeCenter;
	private Double longitudeCenter;
	private List<Line> lines;
	private String jsonLines;
	
	public Double getLatitudeCenter() {
		return latitudeCenter;
	}
	public void setLatitudeCenter(Double latitudeCenter) {
		this.latitudeCenter = latitudeCenter;
	}
	public Double getLongitudeCenter() {
		return longitudeCenter;
	}
	public void setLongitudeCenter(Double longitudeCenter) {
		this.longitudeCenter = longitudeCenter;
	}
	public List<Line> getLines() {
		return lines;
	}
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}
	public String getJsonLines() {
		return jsonLines;
	}
	public void setJsonLines(String jsonLines) {
		this.jsonLines = jsonLines;
	}
	
	
}
