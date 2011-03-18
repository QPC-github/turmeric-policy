package org.ebayopensource.turmeric.policy.adminui.client.view.policy;

import java.util.List;

import org.ebayopensource.turmeric.policy.adminui.client.PolicyAdminUIUtil;
import org.ebayopensource.turmeric.policy.adminui.client.model.UserAction;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.ExtraField;

public class BLPolicyCreateView extends PolicyCreateView {

	protected static  final UserAction SELECTED_ACTION = UserAction.BL_POLICY_CREATE;
	private static final String TITLE_FORM= PolicyAdminUIUtil.policyAdminConstants.policyInformationBLCreate();
	

	@Override
	public UserAction getSelectedAction(){
		return SELECTED_ACTION;
	}
	
	@Override
	public String getTitleForm(){
		return TITLE_FORM;
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
	public void setPolicyDesc(String policyDesc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPolicyName(String policyName) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setExtraFieldList(List<ExtraField> extraFieldList) {
		// TODO Auto-generated method stub
		
	}


}
