/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.view.common;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

/**
 * The Class FooterWidget.
 */
public class FooterWidget extends Composite {

	/**
	 * Instantiates a new footer widget.
	 */
	public FooterWidget() {
		Panel panel = new FlowPanel();
		initWidget(panel);
		
		panel.setWidth("100%");
		panel.addStyleName("footer");
	}
}
