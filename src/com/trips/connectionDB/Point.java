package com.trips.connectionDB;

class Point {
	public int sequenceId; // probably not needed
	public double x;
	public double y;
	
	Point(int sequenceId, double x, double y){
		this.sequenceId = sequenceId;
		this.x = x;
		this.y = y;
	}

	public Point() {
		// TODO Auto-generated constructor stub
	}
}