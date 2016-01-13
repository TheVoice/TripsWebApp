package com.trips.connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trips.model.Line;
import com.trips.model.RouteResults;
import com.trips.model.StartDetails;
import com.trips.model.TripDetails;

public class TripsConnector {
	LinkedList<Route> listOfRoutes;
	TripDetails details;
	StartDetails startDet;

	public TripsConnector(TripDetails details,StartDetails startDet){
		this.details = details;
		this.startDet = startDet;
	}
	
	public List<RouteResults> connectAndSearch(){
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
	    
	    List<RouteResults> res = generateResults();
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
		    ResultSet rs;
		    if(details.getStrategy().equals("byBike"))
			    rs = st.executeQuery("SELECT id, type, length, quality, ST_AsText(range) AS range, ST_AsText(route) AS route from routes "
			    		  							+ "where ST_Distance(ST_GeomFromText('POINT("+startDet.getLatitudeStart()+" "+startDet.getLongitudeStart()+")'), route) < 0.01 and length < "+details.getMaxLength()+" and length > "+details.getMinLength()+" and type ='Bicycle';");
		    else if(details.getStrategy().equals("byFoot"))
		    	rs = st.executeQuery("SELECT id, type, length, quality, ST_AsText(range) AS range, ST_AsText(route) AS route from routes "
							+ "where ST_Distance(ST_GeomFromText('POINT("+startDet.getLatitudeStart()+" "+startDet.getLongitudeStart()+")'), route) < 0.01 and length < "+details.getMaxLength()+" and length > "+details.getMinLength()+" and type='Walk';");
		    else
		    	rs = st.executeQuery("SELECT id, type, length, quality, ST_AsText(range) AS range, ST_AsText(route) AS route from routes "
							+ "where ST_Distance(ST_GeomFromText('POINT("+startDet.getLatitudeStart()+" "+startDet.getLongitudeStart()+")'), route) < 0.01 and length < "+details.getMaxLength()+" and length > "+details.getMinLength()+";");
		    while ( rs.next() ){
		    	Route route = new Route();
		    	route.id        = rs.getString("id");
		    	route.type   	= rs.getString("type");
		    	route.length 	= rs.getString("length");
		    	route.quality   = rs.getString("quality");
		    	route.bounds 	= rs.getString("range");
		    	route.route     = rs.getString("route");
		    	//System.out.println(route.route);
		    	Statement st2 = conn.createStatement();
		    	ResultSet distanceSet = st2.executeQuery("SELECT ST_Distance("
		    			+ "ST_GeomFromText('POINT("+startDet.getLatitudeStart()+" "+startDet.getLongitudeStart()+")'),"
		    			+ "ST_GeomFromText('"+route.route+"')"
		    			+ ");");
		    	distanceSet.next();
		    	route.distance = distanceSet.getString("st_distance");
		    	distanceSet.close();
		    	//System.out.println(route.distance);
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
	
	private List<RouteResults> generateResults(){
		Iterator<Route> it = listOfRoutes.iterator();
		//System.out.println(listOfRoutes.size());
		RouteResults results;
		List<RouteResults> listResults = new ArrayList<RouteResults>();
		Route route;
		//route.generateIntoResults(results);
		
	    while (it.hasNext()){
	    	results = new RouteResults();
	    	route = it.next();
	    	route.generateIntoResults(results);
	    	List<Line> list = results.getLines();
	    	String json ="";
			ObjectMapper mapper = new ObjectMapper();
			try{
				json = mapper.writeValueAsString(list);
			}catch(Exception e){
				e.printStackTrace();
			}
			results.setJsonLines(json);
	    	listResults.add(results);
		}
		
	    return listResults;
	}
}
