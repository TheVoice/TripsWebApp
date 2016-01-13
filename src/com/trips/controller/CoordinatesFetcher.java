package com.trips.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.trips.model.StartDetails;
import com.trips.model.TripDetails;

public class CoordinatesFetcher {
	private static final String NOMINATIM_API = "http://nominatim.openstreetmap.org/";
	private static final String OVERPASS_API = "http://www.overpass-api.de/api/interpreter";
	//private static final String OPENSTREETMAP_API_06 = "http://www.openstreetmap.org/api/0.6/";
	private static final String OPENSTREETMAP_API_06 = "http://api.openstreetmap.org/api/0.6/";

	public static boolean fetch(StartDetails details,String startRoad) throws IOException, SAXException, ParserConfigurationException{
		
		//String streetName = new String(startRoad.getBytes(),"ISO-8859-2");
		//System.out.println(startRoad);
		int spaceIndex = startRoad.lastIndexOf(" ");
		if(spaceIndex==-1) return false;
		String startStreet = startRoad.substring(spaceIndex+1, startRoad.length())+"%20"+startRoad.substring(0, spaceIndex).replaceAll(" ", "%20");
		//System.out.println(startStreet);
		OSMNode osmNode = getFromLocationName(startStreet,1,19.7922355,49.9676668,20.2173455,50.1261383,true);;
		if(osmNode!=null){
			details.setLatitudeStart(Double.parseDouble(osmNode.getLat()));
			details.setLongitudeStart(Double.parseDouble(osmNode.getLon()));
			
			return true;
		}
		return false;
	}
	
	private static String convertFromUnicode(String str){
		String result = str
				.replaceAll("&#261;", "a")
				.replaceAll("&#281;", "e")
				.replaceAll("&#347;", "s")
				.replaceAll("&#263;", "c")
				.replaceAll("&#243;", "o")
				.replaceAll("&#324;", "n")
				.replaceAll("&#380;", "z")
				.replaceAll("&#378;", "z")
				.replaceAll("&#322;", "l")
				.replaceAll("&#260;", "A")
				.replaceAll("&#280;", "E")
				.replaceAll("&#346;", "S")
				.replaceAll("&#262;", "C")
				.replaceAll("&#211;", "O")
				.replaceAll("&#323;", "N")
				.replaceAll("&#379;", "Z")
				.replaceAll("&#377;", "Z")
				.replaceAll("&#321;", "L")
				.replaceAll("ó", "o")
				.replaceAll("Ó", "O");
		return result;
	}
	
	private static OSMNode getFromLocationName(String locationName,int maxResults,double lowerLeftLon,double lowerLeftLat,double upperRightLon,double upperRightLat,boolean bounded) throws IOException, ParserConfigurationException, SAXException{
		String url;
		if(bounded){
			url = NOMINATIM_API +
					"search?"+
					"format=xml"+
					"&addresdetails=1"+
					"&viewboxlbrt="+
					lowerLeftLon+","+lowerLeftLat+","+upperRightLon+","+upperRightLat+
					"&bounded=1"+
					"&street="+
					locationName;
		}else{
			url = NOMINATIM_API +
					"search?"+
					"format=xml"+
					"&addresdetails=1"+
					"&viewboxlbrt="+
					lowerLeftLon+","+lowerLeftLat+","+upperRightLon+","+upperRightLat+
					"&bounded=0"+
					"&street="+
					locationName;
		}
		URI uri = URI.create(url);
		url = uri.toASCIIString();
		//System.out.println(url);
		URL osm = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) osm.openConnection();
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document xmlDoc = docBuilder.parse(connection.getInputStream());
		connection.disconnect();
		
		
		xmlDoc.getDocumentElement().normalize();
		NodeList placeList = xmlDoc.getElementsByTagName("place");
		if(placeList.getLength()!=0){
			Element elem = (Element) placeList.item(0);
			OSMNode mapNode = new OSMNode(elem.getAttribute("osm_id"),elem.getAttribute("lat"),elem.getAttribute("lon"));
			return mapNode;
		}else{
			System.err.println("No street with that name found");
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
		
		//cracow's boundingbox=19.7922355,49.9676668,20.2173455,50.1261383
		getFromLocationName("2%20Bulwarowa",1,19.7922355,49.9676668,20.2173455,50.1261383,true);
	}
}
