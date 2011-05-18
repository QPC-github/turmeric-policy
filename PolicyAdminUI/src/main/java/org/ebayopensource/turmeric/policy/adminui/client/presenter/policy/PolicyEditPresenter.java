/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.presenter.policy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.ebayopensource.turmeric.policy.adminui.client.PolicyAdminUIUtil;
import org.ebayopensource.turmeric.policy.adminui.client.SupportedService;
import org.ebayopensource.turmeric.policy.adminui.client.model.HistoryToken;
import org.ebayopensource.turmeric.policy.adminui.client.model.PolicyAdminUIService;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.ExtraField;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.GenericPolicy;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.GenericPolicyImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyKey;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyQueryService;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyQueryService.GetPoliciesResponse;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicySubjectAssignment;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.QueryCondition;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Resource;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Rule;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.RuleAttribute;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Subject;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public abstract class PolicyEditPresenter extends PolicyCreatePresenter {

	protected String originalPolicyId;
	protected String originalPolicyType;

	// protected final List<Rule> rules = new ArrayList<Rule>();

	public PolicyEditPresenter(HandlerManager eventBus, PolicyEditDisplay view,
			Map<SupportedService, PolicyAdminUIService> serviceMap) {
		super(eventBus, view, serviceMap);

	}

	/*
	 * Interface definitions
	 */
	public interface PolicyEditDisplay extends PolicyCreateDisplay {
	}

	@Override
	public void go(HasWidgets container, final HistoryToken token) {
		super.go(container, token);
		validateEnablingPrivileges(token);
		loadAssignments(token);
	}

	//TURMERIC-1402 - To check privileges for updating Status at edition time
	private void validateEnablingPrivileges(final HistoryToken token) {
		final String statusEditable = token.getValue(HistoryToken.POLICY_STATUS_EDITABLE);
		this.view.setStatusListboxEnabled(Boolean.parseBoolean(statusEditable));
	}

	private void loadAssignments(final HistoryToken token) {

		originalPolicyId = token
				.getValue(HistoryToken.SELECTED_POLICY_TOKEN_ID);
		originalPolicyType = token
				.getValue(HistoryToken.SELECTED_POLICY_TOKEN_TYPE);

		PolicyKey pKey = new PolicyKey();
		pKey.setId(Long.valueOf(originalPolicyId));
		pKey.setType(originalPolicyType);
		ArrayList<PolicyKey> poKeys = new ArrayList<PolicyKey>();
		poKeys.add(pKey);
		QueryCondition condition = new QueryCondition();
		condition.addQuery(new QueryCondition.Query(
				QueryCondition.ActivePoliciesOnlyValue.FALSE));

		service.findPolicies(null, poKeys, null, null, null, null, null,
				condition,
				new AsyncCallback<PolicyQueryService.GetPoliciesResponse>() {

					public void onFailure(Throwable arg) {
						if (arg.getLocalizedMessage().contains("500")) {
							view.error(PolicyAdminUIUtil.messages
									.serverError(PolicyAdminUIUtil.policyAdminConstants
											.genericErrorMessage()));
						} else {
							view.error(PolicyAdminUIUtil.messages.serverError(arg
									.getLocalizedMessage()));
						}
						GWT.log("findPolicies:Fail");
					}

					public void onSuccess(GetPoliciesResponse result) {
						GWT.log("findPolicies:Success");

						Collection<GenericPolicy> policies = result
								.getPolicies();

						for (GenericPolicy policy : policies) {
							resourceAssignments = new ArrayList<Resource>();
							resourceAssignments.addAll(policy.getResources());
							
							if(assignedUniqueResources == null){
								assignedUniqueResources = new HashSet<String>();
							}
							for (Resource assignment : resourceAssignments){
								assignedUniqueResources.add(assignment
										.getResourceType()
										+ assignment.getResourceName());
							}
							
							view.getResourceContentView().setAssignments(
									resourceAssignments);

							subjectAssignments = new ArrayList<PolicySubjectAssignment>();
							subjectAssignments
									.addAll(fetchSubjectAndSGAssignment(policy));
							
							for(PolicySubjectAssignment assignment : subjectAssignments){
								subjectTypes.remove(assignment.getSubjectType());
							}
							view.getSubjectContentView().setAvailableSubjectTypes(subjectTypes);
														
							view.getSubjectContentView().setAssignments(
									subjectAssignments);
							view.setPolicyName(policy.getName());
							view.setPolicyDesc(policy.getDescription());
							view.setPolicyType(policy.getType());
							view.setPolicyStatus(policy.getEnabled());
							// TODO improve this
							if ("RL".equalsIgnoreCase(policy.getType())) {
								setExtraFieldView(policy);
							}

							break;
						}

					}
				});
	}

	// TODO make it abstract and move its content to an specific RL policy
	// View Presenter extends from PolicyViewPresenter
	protected void setExtraFieldView(GenericPolicy policy) {
		List<ExtraField> rlExtraFields = new ArrayList<ExtraField>();
		// TODO JOSE load from xml file from an Util class

		if (policy.getRules() != null && policy.getRules().size() > 0) {
			Rule rule = policy.getRules().get(0);

			if (rule.getAttributeList() != null
					&& rule.getAttributeList().size() > 0) {
				for (RuleAttribute attribute : rule.getAttributeList()) {
					if (RuleAttribute.NotifyKeys.NotifyEmails.name().equals(
							attribute.getKey())) {
						// Policy Based Email Address
						view.setExtraFieldValue(1, attribute.getValue(), false);
					}
					if (RuleAttribute.NotifyKeys.NotifyActive.name().equals(
							attribute.getKey())) {
						// Subject Based Email Address
						view.setExtraFieldValue(2, attribute.getValue(), false);
					}
				}
			}

			// Effect Duration
			view.setExtraFieldValue(3, rule.getEffectDuration().toString(),
					false);
			// Rollover period
			view.setExtraFieldValue(4, rule.getRolloverPeriod().toString(),
					false);
			// Priority
			view.setExtraFieldValue(5, rule.getPriority().toString(), false);
			// Priority
			view.setExtraFieldValue(6, rule.getEffect().toString(), false);

			// Condition
			try {

				view.setExtraFieldValue(7, rule.getCondition().getExpression()
						.getPrimitiveValue().getValue(), false);

			} catch (NullPointerException ex) {
				// do nothing...no condition value
				GWT.log("No condition Values ");
			}

		}

	}

	private List<PolicySubjectAssignment> fetchSubjectAndSGAssignment(
			GenericPolicy policy) {
				HashMap<String, List<Subject>> sAssignMap = new HashMap<String, List<Subject>>();
		for (Subject subject : policy.getSubjects()) {
			String type = subject.getType();
			
//			if(subject.getName() != null){

				if (!sAssignMap.containsKey(type)) {
					List list = new ArrayList();
					list.add(subject);
					sAssignMap.put(type, list);
				} else {
					List list = (List) sAssignMap.get(type);
					list.add(subject);
					sAssignMap.put(type, list);
				}
//			}else{
//				//means it has selectAllSubject activated
//				SubjectImpl allSb = new SubjectImpl();
//				allSb.setType(type);
//				allSb.setName(PolicyAdminUIUtil.policyAdminConstants.all());
//				if (!sAssignMap.containsKey(type)) {
//					List list = new ArrayList();
//					list.add(allSb);
//					sAssignMap.put(type, list);
//				} else {
//					List list = (List) sAssignMap.get(type);
//					list.add(allSb);
//					sAssignMap.put(type, list);
//				}
//				break;
//			}
		}

		HashMap<String, List<Subject>> exclSAssignMap = new HashMap<String, List<Subject>>();
		for (Subject subject : policy.getExclusionSubjects()) {
			String type = subject.getType();
			if (!exclSAssignMap.containsKey(type)) {
				List list = new ArrayList();
				list.add(subject);
				exclSAssignMap.put(type, list);
			} else {
				List list = (List) exclSAssignMap.get(type);
				list.add(subject);
				exclSAssignMap.put(type, list);
			}
		}

		HashMap<String, List<SubjectGroup>> sgAssignMap = new HashMap<String, List<SubjectGroup>>();
		for (SubjectGroup subjectGroup : policy.getSubjectGroups()) {
			String type = subjectGroup.getType();
			if (!sgAssignMap.containsKey(type)) {
				List list = new ArrayList();
				list.add(subjectGroup);
				sgAssignMap.put(type, list);
			} else {
				List list = (List) sgAssignMap.get(type);
				list.add(subjectGroup);
				sgAssignMap.put(type, list);
			}
		}

		HashMap<String, List<SubjectGroup>> exclSGAssignMap = new HashMap<String, List<SubjectGroup>>();
		for (SubjectGroup subjectGroup : policy.getExclusionSG()) {
			String type = subjectGroup.getType();
			if (!exclSGAssignMap.containsKey(type)) {
				List list = new ArrayList();
				list.add(subjectGroup);
				exclSGAssignMap.put(type, list);
			} else {
				List list = (List) exclSGAssignMap.get(type);
				list.add(subjectGroup);
				exclSGAssignMap.put(type, list);
			}
		}

		// Generates the PolicySubjectAssignment objects
		List<PolicySubjectAssignment> polSubAssignmentList = new ArrayList<PolicySubjectAssignment>();

		//there are 4 subject types according the metadata info
		for(String subjectType : SubjectType.getValues()){
			PolicySubjectAssignment polSubAssignment = null;

			if(sAssignMap.containsKey(subjectType)){
				if(polSubAssignment == null){
					polSubAssignment = new PolicySubjectAssignment();
					polSubAssignment.setSubjectType(subjectType);
				}
				polSubAssignment.setSubjects(sAssignMap.get(subjectType));
				

			}
	
			if(exclSAssignMap.containsKey(subjectType)){
				if(polSubAssignment == null){
					polSubAssignment = new PolicySubjectAssignment();
					polSubAssignment.setSubjectType(subjectType);
				}
				polSubAssignment.setExclusionSubjects(exclSAssignMap.get(subjectType));
			}
			
			if(sgAssignMap.containsKey(subjectType)){
				if(polSubAssignment == null){
					polSubAssignment = new PolicySubjectAssignment();
					polSubAssignment.setSubjectType(subjectType);
				}
				polSubAssignment.setSubjectGroups(sgAssignMap.get(subjectType));
			}
			
			if(exclSGAssignMap.containsKey(subjectType)){
				if(polSubAssignment == null){
					polSubAssignment = new PolicySubjectAssignment();
					polSubAssignment.setSubjectType(subjectType);
				}
				polSubAssignment.setExclusionSubjectGroups(exclSGAssignMap.get(subjectType));
			}
			
			if(polSubAssignment != null){
				polSubAssignmentList.add(polSubAssignment);	
			}
			
		}
		return polSubAssignmentList;

	}

	public GenericPolicy getPolicy(String name, String type,
			String description, List<Resource> resources,
			List<PolicySubjectAssignment> subjectAssignments, boolean enabled,
			long id, List<Rule> rules) {
		GenericPolicyImpl p = new GenericPolicyImpl();
		p.setName(name);
		p.setType(type);
		p.setDescription(description);

		// update existing one
		p.setId(id);
		p.setEnabled(enabled);

		if (rules != null) {
			p.setRules(rules);
		}

		if (resources != null)
			p.setResources(new ArrayList<Resource>(resources));

		if (subjectAssignments != null) {
			List<Subject> subjects = new ArrayList<Subject>();
			List<Subject> exclusionSubjects = new ArrayList<Subject>();

			List<SubjectGroup> groups = new ArrayList<SubjectGroup>();
			List<SubjectGroup> exclusionGroups = new ArrayList<SubjectGroup>();

			for (PolicySubjectAssignment a : subjectAssignments) {
				if (a.getSubjects() != null) {
					subjects.addAll(a.getSubjects());
				}
				
				if (a.getExclusionSubjects() != null) {
					exclusionSubjects.addAll(a.getExclusionSubjects());
				}
				

				if (a.getSubjectGroups() != null) {
					groups.addAll(a.getSubjectGroups());
				}

				if (a.getExclusionSubjectGroups() != null) {
					exclusionGroups.addAll(a.getExclusionSubjectGroups());
				}
			}
			p.setSubjects(subjects);
			p.setExclusionSubjects(exclusionSubjects);
			p.setSubjectGroups(groups);
			p.setExclusionSG(exclusionGroups);
		}
		return p;
	}

	private void foreach(List<Subject> subjects) {
		// TODO Auto-generated method stub
		
	}

}
