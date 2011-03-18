/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.view.policy;

import java.util.List;

import org.ebayopensource.turmeric.policy.adminui.client.PolicyAdminUIUtil;
import org.ebayopensource.turmeric.policy.adminui.client.model.UserAction;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.ExtraField;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class BLPolicyEditView extends PolicyEditView {

	protected static  final UserAction SELECTED_ACTION = UserAction.BL_POLICY_EDIT;
	private static final String TITLE_FORM= PolicyAdminUIUtil.policyAdminConstants.policyInformationBLEdit();
	

	@Override
	public String getTitleForm(){
		return TITLE_FORM;
	}
	
	
	@Override
	public UserAction getSelectedAction(){
		return SELECTED_ACTION;
	}


    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.policy.adminui.client.Display#getAssociatedId()
     */
    @Override
    public String getAssociatedId() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.ebayopensource.turmeric.policy.adminui.client.Display#setAssociatedId(java.lang.String)
     */
    @Override
    public void setAssociatedId(String id) {
        // TODO Auto-generated method stub
        
    }


	@Override
	protected void initializeExtraFields() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setExtraFieldList(List<ExtraField> extraFieldList) {
		// TODO Auto-generated method stub
		
	}




}
