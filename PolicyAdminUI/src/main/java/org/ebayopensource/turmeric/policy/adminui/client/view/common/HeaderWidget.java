/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.view.common;

import org.ebayopensource.turmeric.policy.adminui.client.PolicyAdminUIUtil;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class HeaderWidget.
 */
public class HeaderWidget extends Composite {
	
	private HasClickHandlers logo;
	private Button logoutButton;

	/**
	 * Instantiates a new header widget.
	 * 
	 * @param width
	 *            the width
	 */
	public HeaderWidget(String width) {
		Panel panel = new FlowPanel();
	    panel.addStyleName("header");
		initWidget(panel);

		panel.setWidth(width);
		Grid headerGrid = new Grid(1,2);
		headerGrid.setWidth("100%");

		
		logo = new Image("images/turmeric-small.png");
		headerGrid.setWidget(0, 0, (Widget) logo);
		headerGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		
		Grid actionGrid = new Grid(1,2);
		headerGrid.setWidget(0,1, actionGrid);
		logoutButton = new Button(PolicyAdminUIUtil.constants.logout());	
//		actionGrid.setWidget(0, 0, apps);
		actionGrid.setWidget(0, 1, logoutButton);
		headerGrid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(headerGrid);
	}

	

	/**
	 * Sets the user name.
	 * 
	 * @param username
	 *            the new user name
	 */
	public void setUserName (String username) {
	    if (username == null  || "".equals(username))
	        username = "";
	    logoutButton.setText(PolicyAdminUIUtil.constants.logout()+":"+username);
	}

	/**
	 * Gets the logo component.
	 * 
	 * @return the logo component
	 */
	public HasClickHandlers getLogoComponent() {
		return logo;
	}
	
	/**
	 * Gets the logout component.
	 * 
	 * @return the logout component
	 */
	public HasClickHandlers getLogoutComponent() {
		return logoutButton;
	}
	
}
