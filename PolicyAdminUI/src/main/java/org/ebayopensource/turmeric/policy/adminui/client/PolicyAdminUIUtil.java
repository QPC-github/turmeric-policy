/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.Dictionary;

public class PolicyAdminUIUtil {

    public static final DateTimeFormat timeFormat = DateTimeFormat.getFormat("dd MMM yyyy HH:mm");
    public static final DateTimeFormat tzDateFormat = DateTimeFormat.getFormat("dd MM yyyy ZZZZ");
    public static final DateTimeFormat tzTimeFormat = DateTimeFormat.getFormat("dd MM yyyy HH:mm");
    public static final DateTimeFormat xsDateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd'T'HH:mm:ss.SZZZ");
	public static final PolicyAdminUIConstants constants = GWT.create(PolicyAdminUIConstants.class);
	public static final PolicyAdminUIMessageConstants messages = GWT.create(PolicyAdminUIMessageConstants.class);
	public static final PolicyAdminConstants policyAdminConstants = GWT.create(PolicyAdminConstants.class);
	public static final PolicyAdminMessageConstants policyAdminMessages = GWT.create(PolicyAdminMessageConstants.class);
	
	private static final Map<String,String> configMap = new HashMap<String,String>();
	
	public static Map<String,String> getConfig () {
	    if (configMap.isEmpty()) {
	        
	        try {
	            Dictionary configDictionary = Dictionary.getDictionary("turmericConfig");
	            Set<String> keys = configDictionary.keySet();
	            for (String key:keys)
	                configMap.put(key, configDictionary.get(key));
	        } catch (MissingResourceException e) {
	            //TODO log it?
	        }
	        
	    }
	        
	    return Collections.unmodifiableMap(configMap);
	}
	  
    public static String convertConsumerFromMissing (String name) {
        if (name == null)
            return null;
        if ("Missing".equalsIgnoreCase(name))
            return constants.unknown();
        return name;
            
    }
    
    public static String convertConsumerToMissing (String name) {
        if (name == null)
            return null;
        if (constants.unknown().equalsIgnoreCase(name))
            return "Missing";
        return name;
    }
}
