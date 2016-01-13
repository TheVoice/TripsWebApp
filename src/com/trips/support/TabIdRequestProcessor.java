package com.trips.support;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestDataValueProcessor;

public class TabIdRequestProcessor implements RequestDataValueProcessor{

	@Override
	public String processAction(HttpServletRequest request, String action,
			String httpMethod) {
		return action;
	}

	@Override
	public String processFormFieldValue(HttpServletRequest request,
			String name, String value, String type) {
		return value;
	}

	@Override
	public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
		Map<String,String> hiddenFields = new HashMap<String,String>();
		if(request.getAttribute(TabSessionAttributeStore.CID_FIELD)!=null){
			hiddenFields.put(TabSessionAttributeStore.CID_FIELD, request.getAttribute(TabSessionAttributeStore.CID_FIELD).toString());
		}
		return hiddenFields;
	}

	@Override
	public String processUrl(HttpServletRequest request, String url) {
		return url;
	}

}
