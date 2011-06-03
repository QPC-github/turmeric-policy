/********************************************************************
 * Copyright (c) 2010 eBay Inc., and others. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.model.policy;

import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyEnforcementService.VerifyAccessResponse;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * VerifyAccessResponseJS.
 */
public class VerifyAccessResponseJS  extends JavaScriptObject implements VerifyAccessResponse {
    
    /** The Constant NAME. */
    public static final String NAME = "ns1.verifyAccessResponse";

    /**
	 * Instantiates a new verify access response js.
	 */
    protected VerifyAccessResponseJS () {}
    
    /**
	 * From json.
	 * 
	 * @param json
	 *            the json
	 * @return the verify access response js
	 */
    public static final native VerifyAccessResponseJS fromJSON (String json) /*-{
        try {
            return eval('(' + json + ')');
        } catch (err) {
        return null;
        }
    }-*/;
    

    /**
	 * Checks if is errored.
	 * 
	 * @return true, if is errored
	 * @see org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyEnforcementService.VerifyAccessResponse#isErrored()
	 */
    @Override
    public final native boolean isErrored() /*-{
    
       if (this["verifyAccessResponse"]) {
           var response;
           
           if (this["verifyAccessResponse"] == Array) {
             response = this["verifyAccessResponse"][0];
           }  else
             response = this["verifyAccessResponse"];
       }
       
       if (response["ack"]) {
          var ack;
          if (response["ack"] == Array) {
            ack = response["ack"][0];
          } else
            ack = response["ack"];
       }
       
       if (!ack)
         return true;
         
       if (ack  === "Success")
         return false;
       else
         return true;
     
    }-*/;
    
  
    
}
