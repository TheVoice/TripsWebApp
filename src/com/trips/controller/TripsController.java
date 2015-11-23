package com.trips.controller;
 
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trips.connectionDB.TripsConnector;
import com.trips.model.Line;
import com.trips.model.RouteResults;
import com.trips.model.TripDetails;
 
@Controller
public class TripsController {
	@Autowired
    ServletContext context; 
 
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public ModelAndView viewForm(){
		return new ModelAndView("form","command",new TripDetails());
	}
	
	@RequestMapping(value = "/results",method = RequestMethod.POST)
	public ModelAndView searchDatabase(@ModelAttribute("TripsWeb") TripDetails details,Map<String,Object> model) {
		
		
		
		TripsConnector connector = new TripsConnector(details);
		RouteResults results = connector.connectAndSearch();
		List<Line> list = results.getLines();
		String json ="";
		//ObjectMapper mapper = new ObjectMapper();
		//try{
			//json
		//}
		return new ModelAndView("results", "resultObj", results);
	}
}