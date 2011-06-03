/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.model;

/**
 * ErrorDetail.
 */
public interface ErrorDetail {
    
    /**
	 * Gets the id.
	 * 
	 * @return the id
	 */
    String getId();

    /**
	 * Gets the name.
	 * 
	 * @return the name
	 */
    String getName();

    /**
	 * Gets the domain.
	 * 
	 * @return the domain
	 */
    String getDomain();

    /**
	 * Gets the sub domain.
	 * 
	 * @return the sub domain
	 */
    String getSubDomain();

    /**
	 * Gets the severity.
	 * 
	 * @return the severity
	 */
    String getSeverity();

    /**
	 * Gets the category.
	 * 
	 * @return the category
	 */
    String getCategory();
}
