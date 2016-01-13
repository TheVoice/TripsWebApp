package com.trips.controller;
 
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trips.connectionDB.TripsConnector;
import com.trips.model.Line;
import com.trips.model.RouteResults;
import com.trips.model.StartDetails;
import com.trips.model.TripDetails;
 
@Controller
@SessionAttributes(value = {"resultList","startPoint"})
@Scope("request")
public class TripsController {
	@Autowired
    ServletContext context; 

 
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public ModelAndView viewForm(){
		return new ModelAndView("form","command",new TripDetails());
	}
	
	@RequestMapping(value = "/results",method = RequestMethod.POST)
	public String searchDatabase(@ModelAttribute("TripsWeb") TripDetails details,Model model,HttpServletRequest request) throws UnsupportedEncodingException {
		
		StartDetails startFrom = new StartDetails();
		try {
			if(CoordinatesFetcher.fetch(startFrom,details.getStartRoad())==false){
				//return new ModelAndView("form","command",new TripDetails("Trip start point not found."));
				model.addAttribute("command",new TripDetails("Trip start point not found."));
				return "form";
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		
		//System.out.println(startFrom.getLatitudeStart()+"_"+startFrom.getLongitudeStart());
		TripsConnector connector = new TripsConnector(details,startFrom);
		List<RouteResults> results;
		
		results = connector.connectAndSearch();
		
		if(results.size()==0){
			model.addAttribute("command",new TripDetails("Nie znaleziono trasy spe³niaj¹cej podane kryteria."));
			return "form";
		}
		Collections.sort(results);
		Collections.reverse(results);
		results = results.size()>=10 ? results.subList(0, 10) : results.subList(0, results.size());
		/*
		ModelAndView mdl = new ModelAndView("list");
		mdl.addObject("resultList", results);
		mdl.addObject("startPoint", startFrom);
		*/
		String id = UUID.randomUUID().toString();
		model.addAttribute("resultList",results);
		//model.addAttribute(id+"&resultList", results);
		request.getSession().setAttribute(id+"&resultList", results);
		//model.addAttribute(id+"&startPoint",startFrom);
		request.getSession().setAttribute(id+"&startPoint", startFrom);
		model.addAttribute("requestID",id);
		
		return "list";
	}
	
	@RequestMapping(value = "/{id}&map{number}",method = RequestMethod.GET)
	public ModelAndView displayMap(@PathVariable("id") String id,@PathVariable("number") int n,Model model,HttpServletRequest request){
		
		List<RouteResults> results = (List<RouteResults>) request.getSession().getAttribute(id+"&resultList");
		ModelAndView mdl = new ModelAndView("results");
		mdl.addObject("resultObj",results.get(n));
		mdl.addObject("startPoint", request.getSession().getAttribute(id+"&startPoint"));
		return mdl;
	}
}