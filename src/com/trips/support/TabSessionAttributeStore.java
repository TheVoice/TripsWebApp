package com.trips.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class TabSessionAttributeStore implements SessionAttributeStore,InitializingBean{

	@Inject
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
	
	private int keepAliveConversations = 10;
	
	public final static String CID_FIELD = "_cid";
	public final static String SESSION_MAP = "sessionConversationMap";
	
	public int getKeepAliveConversations(){
		return keepAliveConversations;
	}
	
	public void setKeepAliveConversations(int _conversationsMax){
		keepAliveConversations = _conversationsMax;
	}
	
	@Override
	public void storeAttribute(WebRequest request, String attributeName,
			Object attributeValue) {
		Assert.notNull(request, "WebRequest must not be null");
		Assert.notNull(attributeName, "AttributeName must not be null");
		Assert.notNull(attributeValue, "AttributeValue must not be null");
		
		String cId = getConversationId(request);

		if(cId == null || cId.trim().length()==0){
			cId = UUID.randomUUID().toString();
		}
		
		request.setAttribute(CID_FIELD, cId, WebRequest.SCOPE_SESSION);
		store(request,attributeName,attributeValue,cId);
	}

	//get conversation id from web request
	private String getConversationId(WebRequest request) {
		String cId = request.getParameter(CID_FIELD);
		if(cId == null){
			cId = (String) request.getAttribute(CID_FIELD, WebRequest.SCOPE_SESSION);
		}
		return cId;
	}

	//store object on session
	//if max conversations reached -> delete oldest conversation
	//IMPORTANT - configure maxConvs as 0 to always keep all
	private void store(WebRequest request, String attributeName,
			Object attributeValue, String cId) {
		LinkedHashMap<String,Map<String,Object>> sessionConversationsMap = getSessionConversationsMap(request);
		if(keepAliveConversations > 0 && sessionConversationsMap.size() >= keepAliveConversations && !sessionConversationsMap.containsKey(cId)){
			//delete oldest
			String key = sessionConversationsMap.keySet().iterator().next();
			sessionConversationsMap.remove(key);
		}
		getConversationStore(request,cId).put(attributeName, attributeValue);
	}

	//get a conversation map from session
	private Map<String, Object> getConversationStore(
			WebRequest request, String cId) {
		Map<String,Object> conversationMap = getSessionConversationsMap(request).get(cId);
		//create if doesnt exist
		if(cId != null && conversationMap == null){
			conversationMap = new HashMap<String,Object>();
			getSessionConversationsMap(request).put(cId, conversationMap);
		}
		return conversationMap;
	}

	private LinkedHashMap<String, Map<String, Object>> getSessionConversationsMap(
			WebRequest request) {
		LinkedHashMap<String,Map<String,Object>> sessionMap = (LinkedHashMap<String, Map<String, Object>>) request.getAttribute(SESSION_MAP, WebRequest.SCOPE_SESSION);
		if(sessionMap == null){
			sessionMap = new LinkedHashMap<String,Map<String,Object>>();
			request.setAttribute(SESSION_MAP, sessionMap, WebRequest.SCOPE_SESSION);
		}
		return sessionMap;
	}

	@Override
	public Object retrieveAttribute(WebRequest request, String attributeName) {
		
		Assert.notNull(request, "WebRequest must not be null");
		Assert.notNull(attributeName, "AttributeName must not be null");
		if(getConversationId(request) != null){
			return getConversationStore(request,getConversationId(request)).get(attributeName);
		}else{
			return null;
		}
	}

	@Override
	public void cleanupAttribute(WebRequest request, String attributeName) {
		Assert.notNull(request, "WebRequest must not be null");
		Assert.notNull(attributeName, "AttributeName must not be null");
		
		Map<String,Object> conversationStore = getConversationStore(request,getConversationId(request));
		conversationStore.remove(attributeName);
		
		//remove if conv store now empty
		if(conversationStore.isEmpty()){
			getSessionConversationsMap(request).remove(getConversationId(request));
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		requestMappingHandlerAdapter.setSessionAttributeStore(this);
	}

}
