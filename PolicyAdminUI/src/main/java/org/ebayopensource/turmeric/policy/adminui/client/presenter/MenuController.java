/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.presenter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.policy.adminui.client.PolicyAdminUIUtil;
import org.ebayopensource.turmeric.policy.adminui.client.Container;
import org.ebayopensource.turmeric.policy.adminui.client.Controller;
import org.ebayopensource.turmeric.policy.adminui.client.SupportedService;
import org.ebayopensource.turmeric.policy.adminui.client.event.LogoutEvent;
import org.ebayopensource.turmeric.policy.adminui.client.model.PolicyAdminUIService;
import org.ebayopensource.turmeric.policy.adminui.client.model.HistoryToken;
import org.ebayopensource.turmeric.policy.adminui.client.model.UserAction;
import org.ebayopensource.turmeric.policy.adminui.client.presenter.policy.PolicyController;
import org.ebayopensource.turmeric.policy.adminui.client.shared.AppUser;
import org.ebayopensource.turmeric.policy.adminui.client.view.DashboardContainer;
import org.ebayopensource.turmeric.policy.adminui.client.view.PolicyContainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * This presenter contains top level menu of the application.
 * (1) Monitoring Console
 * (2) Policy Admin
 * 
 * @author nuy
 *
 */
public class MenuController implements Presenter, Controller {
	
	public final static String PRESENTER_ID = "Menu";

	protected HandlerManager eventBus;
	protected MenuControllerDisplay view;
	protected HasWidgets rootContainer;
	protected boolean added;
	
	protected Map<String, Presenter> presenters = new HashMap<String, Presenter>();
	protected Map<UserAction, Presenter> actionMap = new HashMap<UserAction, Presenter>();
	protected Map<SupportedService, PolicyAdminUIService> serviceMap;
	
	
	public interface MenuControllerDisplay extends Container {
	    HasClickHandlers getLogoutComponent();
	    void setUserName(String name);
	}
	
	public MenuController(HandlerManager eventBus, HasWidgets rootContainer, MenuControllerDisplay view, Map<SupportedService, PolicyAdminUIService> serviceMap) {
		this.eventBus = eventBus;
		this.view = view;
		this.serviceMap = serviceMap;
		this.rootContainer = rootContainer;
		
		initPresenters();
		bind();
	}

	public void go(HasWidgets container, HistoryToken token) {
	    //only add ourselves to the root window the first time we are activated
	    if (!added) {
	        added =true;
	        rootContainer.add(this.view.asWidget());
	        this.view.setUserName((AppUser.getUser()==null?"":AppUser.getUser().getUsername()));
	    }
	    
	    
	    //try my sub presenters
	    String id = token.getPresenterId();
	    //Window.alert("MenuController: presenter id = "+id);
	    if (id != null && !PRESENTER_ID.equals(id)){
	        selectPresenter(token);
	    }else{
	    	HistoryToken tok = HistoryToken.newHistoryToken(PolicyController.PRESENTER_ID, null);
            History.newItem(tok.toString());
            selectPresenter(tok);
	    }

	}

	public String getId() {
		return PRESENTER_ID;
	}

	public void bind() {
		
		//listen for logout
		this.view.getLogoutComponent().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {    
                eventBus.fireEvent(new LogoutEvent());
            }
		});
	}

	public void addPresenter(String id, Presenter p) {
		presenters.put(id, p);
	}

	public Presenter getPresenter(String id) {
		return presenters.get(id);
	}

	public void selectPresenter(HistoryToken token) {
		String presenterId = token != null ? token.getPresenterId() : null;
		
		Presenter presenter = presenters.get(presenterId);
        if (presenter != null) {
           
            UserAction ua = getActionForPresenter(presenter);
            
           //Pass in this view so that all presenter/view pairs are children
           //of the MenuController's view (get constant header/footer wrapping)
            presenter.go(view, token);
        }
	}
	
	public void initPresenters() {
		Presenter pp = new PolicyController(eventBus, new PolicyContainer(), this.serviceMap);
		addPresenter(pp.getId(), pp);
		actionMap.put(UserAction.POLICY_MAIN, pp);
	}

	private UserAction getActionForPresenter (Presenter presenter) {
	    UserAction ua = null;
	    for (Map.Entry<UserAction, Presenter> e: actionMap.entrySet()) {
	        if (e.getValue().getId().equals(presenter.getId()))
	            ua = e.getKey();
	    }
	    return ua;
	}
}
