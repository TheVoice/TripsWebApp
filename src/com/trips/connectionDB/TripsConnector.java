package com.trips.connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.trips.model.RouteResults;
import com.trips.model.TripDetails;

public class TripsConnector {
	LinkedList<Route> listOfRoutes;
	TripDetails details;

	public TripsConnector(TripDetails details){
		this.details = details;
	}
	
	public RouteResults connectAndSearch(){
		Connection conn = null;
	    listOfRoutes = new LinkedList<Route>();

	    // connect to the database
	    conn = connectToDatabase();

	    // get the data
	    getRoutesFromDatabase(conn);
	    
	    // print the results Print everything
	    // printTopics();
	    
	    // parse Linestring into Route (LinkedList of points)
	    parseLineString();
	    
	    RouteResults res = generateResults();
	    return res;
	}
	
	private Connection connectToDatabase() {
		Connection conn = null;
		try{
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://localhost:5432/ToursRoutesDB";
			conn = DriverManager.getConnection(url,"postgres", "admin");
		} 
		
		catch (ClassNotFoundException e){
			e.printStackTrace();
			System.exit(1);
		}
		
		catch (SQLException e){
			e.printStackTrace();
			System.exit(2);
		}
		return conn;
	}
	
	private void getRoutesFromDatabase(Connection conn){
	    try {
		    Statement st = conn.createStatement();
		    ResultSet rs = st.executeQuery("SELECT id, type, length, quality, ST_AsText(range) AS range, ST_AsText(route) AS route from routes "
		    		  							+ "where ST_Distance(ST_GeomFromText('POINT("+details.getStartX()+" "+details.getStartY()+")'), route) < 0.1 and length < "+details.getMaxLength()+" and length > "+details.getMinLength()+";");
		    while ( rs.next() ){
		    	Route route = new Route();
		    	route.id        = rs.getString("id");
		    	route.type   	= rs.getString("type");
		    	route.length 	= rs.getString("length");
		    	route.quality   = rs.getString("quality");
		    	route.bounds 	= rs.getString("range");
		    	route.route     = rs.getString("route");
		    	listOfRoutes.add(route);
		    }
		    rs.close();
		    st.close();
	    }
	    catch (SQLException se) {
		    System.err.println("Threw a SQLException creating the list of blogs.");
		    System.err.println(se.getMessage());
	    }
	}
	
	private void parseLineString() {
	    Iterator<Route> it = listOfRoutes.iterator();
	    while (it.hasNext()){
	    	Route route = it.next();
	    	String token1 = route.route.substring(11, route.route.length() -1);  // eliminate Linestring(  and )
	    	String[] tokens1 = token1.split(",");
	    	
	    	for(int i = 0; i < tokens1.length; i++){
	    		String[] cordinates = tokens1[i].split(" ");
	    		route.ParsedLinestring.add(new Point(i, Double.parseDouble(cordinates[0]), Double.parseDouble(cordinates[1])));
	    	}	   
	    	
//	    	Iterator<Point> its = route.ParsedLinestring.iterator();
//		    while (its.hasNext()){
//		    	Point point = its.next();
//		    	System.out.println(point.x + " : " + point.y);
//		    }
	    }
	}
	
	private RouteResults generateResults() {
		Iterator<Route> it = listOfRoutes.iterator();
		RouteResults results = new RouteResults();
		Route route = listOfRoutes.getFirst();
		route.generateIntoResults(results);
		/*
	    while (it.hasNext()){
	    	Route route = it.next();
	    	route.generateIntoResults(results);
		}
		*/
	    return results;
	}
}
