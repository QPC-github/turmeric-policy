/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ebayopensource.turmeric.policy.adminui.client.model.UserAction;
import org.ebayopensource.turmeric.policy.adminui.client.shared.AppUserRole;

/**
 * The Class PolicyMenuUtil.
 */
public class PolicyMenuUtil {

	
	/** The Constant MAIN_MENU_ACTIONS. */
	public final static UserAction[] MAIN_MENU_ACTIONS = new UserAction[] {
		UserAction.CONSOLE_MAIN,
		UserAction.POLICY_MAIN
	};
	
	private final static UserAction[] POLICY_ADMIN_MENU_ACTIONS = new UserAction[] {
		UserAction.SUBJECT_GROUP_SUMMARY,
		UserAction.SUBJECT_GROUP_CREATE,
		UserAction.SUBJECT_GROUP_IMPORT,
		UserAction.POLICY_SUMMARY,
		UserAction.POLICY_IMPORT,
		UserAction.AUTHZ_POLICY_CREATE,
		UserAction.RL_POLICY_CREATE,
		UserAction.BL_POLICY_CREATE,
		UserAction.WL_POLICY_CREATE,
		UserAction.CHANGE_HISTORY_SUMMARY
	};
	
	private final static UserAction[] POLICY_ADMIN_SUBMENU_ACTIONS = new UserAction[] {
		UserAction.POLICY_VIEW,
		UserAction.POLICY_ENABLE,
		UserAction.POLICY_DISABLE,
		UserAction.POLICY_SUBMIT_TICKET_TRACE,
		UserAction.POLICY_EDIT,
		UserAction.POLICY_DELETE,
		UserAction.POLICY_EXPORT
	};
	
	private final static UserAction[] RESOURCE_ADMIN_SUBMENU_ACTIONS = new UserAction[] {
		UserAction.RESOURCE_VIEW,
		UserAction.RESOURCE_EDIT,
		UserAction.RESOURCE_DELETE
	};
	
	/**
	 * Gets the main menu support actions.
	 * 
	 * @return the main menu support actions
	 */
	public static UserAction[] getMainMenuSupportActions() {
		return MAIN_MENU_ACTIONS;
	}
	
	/**
	 * Gets the policy admin menu supported actions.
	 * 
	 * @return the policy admin menu supported actions
	 */
	public static UserAction[] getPolicyAdminMenuSupportedActions() {
		return POLICY_ADMIN_MENU_ACTIONS;
	}
	
	/**
	 * Gets the policy admin sub menu supported actions.
	 * 
	 * @return the policy admin sub menu supported actions
	 */
	public static UserAction[] getPolicyAdminSubMenuSupportedActions() {
		return POLICY_ADMIN_SUBMENU_ACTIONS;
	}
	
	
	/**
	 * Gets the permitted user actions.
	 * 
	 * @param role
	 *            the role
	 * @return the permitted user actions
	 */
	public static List<UserAction> getPermittedUserActions(AppUserRole role) {
	    if (role.isAdmin()) {
	        return Arrays.asList(UserAction.values());
	    }
	    else
	        return Collections.emptyList();
	}

}
