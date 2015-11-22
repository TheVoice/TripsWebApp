package com.trips.connectionDB;



import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

public class TestPrint {
	
	LinkedList<Route> listOfRoutes;
	
	public static void main(String[] args){
	    new TestPrint();
	}
	
	public TestPrint() {
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
	    
	    generateHTMLs();
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
		    		  							+ "where ST_Distance(ST_GeomFromText('POINT(50.1764891 19.9696849)'), route) < 0.1 and length < 25 and length > 15;");
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
	
	private void printTopics(){
	    Iterator<Route> it = listOfRoutes.iterator();
	    while (it.hasNext()){
	    	Route route = it.next();
	    	System.out.println("id: " + route.id + ", type: " + route.type + ", length" + route.length + ", route.quality");
	    	System.out.println(route.bounds);
	    	System.out.println(route.route);
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
	
	private void generateHTMLs() {
			Iterator<Route> it = listOfRoutes.iterator();
			int i = 0;
		    while (it.hasNext()){
		    	Route route = it.next();
		    	i++;
		    	route.generateLeafletHtmlView("DatabaseRouteG" + i +".html");
			}
	}
}