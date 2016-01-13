package com.trips.model;

import java.util.List;

public class RouteResults implements Comparable<RouteResults>{

	private Double latitudeCenter;
	private Double longitudeCenter;
	private List<Line> lines;
	private String jsonLines;
	private Double distance;
	private Long quality;
	private Integer length;
	
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
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Long getQuality() {
		return quality;
	}
	public void setQuality(Long quality) {
		this.quality = quality;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
	@Override
	public int compareTo(RouteResults o) {
		return Long.compare(this.quality, o.quality);
	}
	
	
}
