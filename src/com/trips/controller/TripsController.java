package com.trips.controller;
 
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.trips.connectionDB.TripsConnector;
import com.trips.model.TripDetails;
 
@Controller
public class TripsController {
 
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public ModelAndView viewForm(){
		return new ModelAndView("form","command",new TripDetails());
	}
	
	@RequestMapping(value = "/results",method = RequestMethod.POST)
	public ModelAndView searchDatabase(@ModelAttribute("TripsWeb") TripDetails details,Map<String,Object> model) {
		/*
		System.out.println(details.getStartX());
		System.out.println(details.getStartY());
		System.out.println(details.getMinLength());
		System.out.println(details.getMaxLength());
		*/
		
		TripsConnector connector = new TripsConnector(details);
		connector.connectAndSearch();
		String message =  details.getStartRoad();
		return new ModelAndView("results", "message", message);
	}
}