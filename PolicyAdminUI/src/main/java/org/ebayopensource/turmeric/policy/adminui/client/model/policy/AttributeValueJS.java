/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.model.policy;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * AttributeValueJS.
 */
public class AttributeValueJS extends JavaScriptObject implements AttributeValue {

    /**
	 * Instantiates a new attribute value js.
	 */
    protected AttributeValueJS() {}

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.policy.adminui.client.model.policy.AttributeValue#getDataType()
     */
    @Override
    public native final String getDataType() /*-{
        return this["@DataType"];
    }-*/;
    
    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.policy.adminui.client.model.policy.AttributeValue#getValue()
     */
    @Override
	public native final String getValue() /*-{
		return this["__value__"];
	}-*/;
    
  
}
