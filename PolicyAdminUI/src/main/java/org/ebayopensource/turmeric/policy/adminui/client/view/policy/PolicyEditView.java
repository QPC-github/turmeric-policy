/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.view.policy;

import org.ebayopensource.turmeric.policy.adminui.client.PolicyAdminUIUtil;
import org.ebayopensource.turmeric.policy.adminui.client.presenter.policy.PolicyEditPresenter.PolicyEditDisplay;

public abstract class PolicyEditView extends PolicyCreateView implements
		PolicyEditDisplay {
	@Override
	public void setPolicyDesc(final String policyDesc) {
		this.policyDesc.setText(policyDesc);
	}
	
	@Override
	public void setPolicyName(final String policyName) {
		this.policyName.setText(policyName);
	}
	
	@Override
	public void setPolicyStatus(final boolean enabled) {
		if(enabled){
			this.policyStatus.setText(PolicyAdminUIUtil.policyAdminConstants.enable());
		}else{
			this.policyStatus.setText(PolicyAdminUIUtil.policyAdminConstants.disable());
		}
	}


}
