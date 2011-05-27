/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * RemoteError
 *
 * Used to parse error messages returned from invocations to the 
 * SOAMetricsQueryService. Use is to display as an error dialog
 * to the user.
 */
public class RemoteError extends JavaScriptObject {

    /**
	 * Instantiates a new remote error.
	 */
    protected RemoteError () {
    }
    
    /**
	 * Gets the error id.
	 * 
	 * @return the error id
	 */
    public  final native String getErrorId() /*-{
        return this["ms.errorId"];
    }-*/;
    
    /**
	 * Gets the severity.
	 * 
	 * @return the severity
	 */
    public  final native String getSeverity() /*-{
        return this["ms.severity"];
    }-*/;
    
    /**
	 * Gets the category.
	 * 
	 * @return the category
	 */
    public  final native String getCategory() /*-{
        return this["ms.category"];
    }-*/;
    
    /**
	 * Gets the message.
	 * 
	 * @return the message
	 */
    public  final native String getMessage() /*-{
        return this["ms.message"];
    }-*/;
}
