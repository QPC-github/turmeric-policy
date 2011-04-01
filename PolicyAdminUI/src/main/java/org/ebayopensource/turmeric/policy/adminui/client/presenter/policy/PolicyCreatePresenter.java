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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.ebayopensource.turmeric.policy.adminui.client.PolicyAdminUIUtil;
import org.ebayopensource.turmeric.policy.adminui.client.Display;
import org.ebayopensource.turmeric.policy.adminui.client.SupportedService;
import org.ebayopensource.turmeric.policy.adminui.client.model.PolicyAdminUIService;
import org.ebayopensource.turmeric.policy.adminui.client.model.HistoryToken;
import org.ebayopensource.turmeric.policy.adminui.client.model.UserAction;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.AttributeValueImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.ExtraField;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.GenericPolicy;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.GenericPolicyImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Operation;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.OperationImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyQueryService;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicySubjectAssignment;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Resource;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.ResourceImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.ResourceKey;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.ResourceType;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Rule;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.Subject;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectAttributeDesignator;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectAttributeDesignatorImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectGroup;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectGroupImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectGroupKey;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectGroupQuery;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectKey;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectMatchType;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectMatchTypeImpl;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectQuery;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.SubjectType;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyQueryService.CreateSubjectsResponse;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyQueryService.FindExternalSubjectsResponse;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyQueryService.FindSubjectGroupsResponse;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyQueryService.FindSubjectsResponse;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyQueryService.GetResourcesResponse;
import org.ebayopensource.turmeric.policy.adminui.client.model.policy.PolicyQueryService.ResourceLevel;
import org.ebayopensource.turmeric.policy.adminui.client.presenter.AbstractGenericPresenter;
import org.ebayopensource.turmeric.policy.adminui.client.util.PolicyKeysUtil;
import org.ebayopensource.turmeric.policy.adminui.client.util.SubjectUtil;
import org.ebayopensource.turmeric.policy.adminui.client.view.common.SelectBoxesWidget;
import org.ebayopensource.turmeric.policy.adminui.client.view.common.PolicyTemplateDisplay.PolicyPageTemplateDisplay;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public abstract class PolicyCreatePresenter extends AbstractGenericPresenter {

	protected HandlerManager eventBus;
	protected PolicyCreateDisplay view;
	protected Map<SupportedService, PolicyAdminUIService> serviceMap;
	protected List<UserAction> permittedActions = new ArrayList<UserAction>();
	protected List<Resource> availableResourcesByType;
	protected List<Subject> allSubjects;

	protected List<SubjectGroup> allSubjectGroups;

	protected List<Resource> allResources;
	protected List<Subject> internalSubjects;

	protected HashSet<String> assignedUniqueResources = new HashSet<String>();
	protected List<String> subjectTypes;
	protected List<PolicySubjectAssignment> subjectAssignments;
	protected List<Resource> resourceAssignments;
	protected List<Rule> rules = new ArrayList<Rule>();;

	protected Resource editResourceAssignment;

	protected PolicySubjectAssignment editSubjectAssignment;
	protected PolicyQueryService service;

	public PolicyCreatePresenter(HandlerManager eventBus,
			PolicyCreateDisplay view,
			Map<SupportedService, PolicyAdminUIService> serviceMap) {
		this.eventBus = eventBus;
		this.view = view;
		this.view.setAssociatedId(getId());
		this.serviceMap = serviceMap;
		bindResourceSection();
		bindSubjectSection();
		bindSaveButton();
		bind();
	}

	protected abstract List<String> getResourceLevels();

	protected abstract void bindSaveButton();

	private void bindSubjectSection() {

		// search for matching SubjectGroup
		this.view.getSubjectContentView().getGroupSearchButton()
				.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// do a lookup of all the matching SubjectGroups
						SubjectGroupKey key = new SubjectGroupKey();
						key.setType(view.getSubjectContentView()
								.getSubjectType());
						String name = view.getSubjectContentView()
								.getGroupSearchTerm();
						if (name != null && !name.trim().equals(""))
							key.setName(name);
						SubjectGroupQuery query = new SubjectGroupQuery();
						query.setGroupKeys(Collections.singletonList(key));

						service.findSubjectGroups(query,
								new AsyncCallback<FindSubjectGroupsResponse>() {

									public void onFailure(Throwable arg) {
										if (arg.getLocalizedMessage().contains(
												"500")) {
											view.error(PolicyAdminUIUtil.messages
													.serverError(PolicyAdminUIUtil.policyAdminConstants
															.genericErrorMessage()));
										} else {
											view.error(PolicyAdminUIUtil.messages.serverError(arg
													.getLocalizedMessage()));
										}
									}

									public void onSuccess(
											FindSubjectGroupsResponse response) {
										List<SubjectGroup> subjects = response
												.getGroups();
										List<String> names = new ArrayList<String>();
										if (subjects != null) {
											for (SubjectGroup s : subjects)
												names.add(s.getName());
										}
										view.getSubjectContentView()
												.setAvailableSubjectGroups(
														names);
										view.getSubjectContentView()
												.setAvailableExclusionSG(names);
									}

								});
					}
				});

		// search for matching Subject
		this.view.getSubjectContentView().getSubjectSearchButton()
				.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// do a lookup of all the matching Subjects
						SubjectKey key = new SubjectKey();
						String name = view.getSubjectContentView()
								.getSubjectSearchTerm();
						if (name != null && !name.trim().equals(""))
							key.setName(name);
						key.setType(view.getSubjectContentView()
								.getSubjectType());

						SubjectQuery query = new SubjectQuery();
						query.setSubjectKeys(Collections.singletonList(key));

						if ("USER".equals(key.getType())) {
							service.findExternalSubjects(
									query,
									new AsyncCallback<FindExternalSubjectsResponse>() {

										public void onFailure(Throwable arg) {
											if (arg.getLocalizedMessage()
													.contains("500")) {
												view.error(PolicyAdminUIUtil.messages
														.serverError(PolicyAdminUIUtil.policyAdminConstants
																.genericErrorMessage()));
											} else {
												view.error(PolicyAdminUIUtil.messages.serverError(arg
														.getLocalizedMessage()));
											}
										}

										public void onSuccess(
												FindExternalSubjectsResponse response) {
											List<Subject> subjects = response
													.getSubjects();
											List<String> names = new ArrayList<String>();
											if (subjects != null) {
												for (Subject s : subjects)
													names.add(s.getName());
											}

											view.getSubjectContentView()
													.setAvailableSubjects(
															getSubjectNames(subjects));
											view.getSubjectContentView()
													.setAvailableExclusionSubjects(
															getSubjectNames(subjects));
										}

									});

						} else {
							service.findSubjects(query,
									new AsyncCallback<FindSubjectsResponse>() {

										public void onFailure(Throwable arg) {
											if (arg.getLocalizedMessage()
													.contains("500")) {
												view.error(PolicyAdminUIUtil.messages
														.serverError(PolicyAdminUIUtil.policyAdminConstants
																.genericErrorMessage()));
											} else {
												view.error(PolicyAdminUIUtil.messages.serverError(arg
														.getLocalizedMessage()));
											}
										}

										public void onSuccess(
												FindSubjectsResponse response) {
											List<Subject> subjects = response
													.getSubjects();
											List<String> names = new ArrayList<String>();
											if (subjects != null) {
												for (Subject s : subjects)
													names.add(s.getName());
											}

											view.getSubjectContentView()
													.setAvailableSubjects(
															getSubjectNames(subjects));

											view.getSubjectContentView()
													.setAvailableExclusionSubjects(
															getSubjectNames(subjects));
										}

									});

						}

					}
				});

		// edit an assignment
		this.view.getSubjectContentView().getEditButton()
				.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if (view.getSubjectContentView()
								.getSelectedSubjectAssignments().size() != 1)
							return;
						/**
						 * edit the existing assignment - set the available
						 * SubjectTypes as only the one in the assignment, and
						 * set the availables vs selected Subjects and
						 * SubjectGroups based on the assignment
						 */
						editSubjectAssignment = view.getSubjectContentView()
								.getSelectedSubjectAssignments().get(0);

						// remove the assignment from the list of assignments
						// while it is being edited
						subjectAssignments.remove(editSubjectAssignment);
						// update the display
						view.getSubjectContentView().setAssignments(
								subjectAssignments);
						// set the available subject types to just the type
						// being edited
						List<String> typeList = new ArrayList<String>();
						typeList.add(editSubjectAssignment.getSubjectType());
						view.getSubjectContentView().setAvailableSubjectTypes(
								typeList);

						// get all the subjects of this type
						SubjectKey skey = new SubjectKey();
						skey.setType(editSubjectAssignment.getSubjectType());

						SubjectQuery query = new SubjectQuery();
						query.setSubjectKeys(Collections.singletonList(skey));

						service.findSubjects(query,
								new AsyncCallback<FindSubjectsResponse>() {

									public void onFailure(Throwable arg) {
										if (arg.getLocalizedMessage().contains(
												"500")) {
											view.error(PolicyAdminUIUtil.messages
													.serverError(PolicyAdminUIUtil.policyAdminConstants
															.genericErrorMessage()));
										} else {
											view.error(PolicyAdminUIUtil.messages.serverError(arg
													.getLocalizedMessage()));
										}
									}

									public void onSuccess(
											FindSubjectsResponse response) {
										List<Subject> subjects = response
												.getSubjects();
										view.getSubjectContentView()
												.setSelectedSubjects(
														getSubjectNames(editSubjectAssignment
																.getSubjects()));
										subjects.removeAll(editSubjectAssignment
												.getSubjects());
										view.getSubjectContentView()
												.setAvailableSubjects(
														getSubjectNames(subjects));

										List<Subject> exclusionSubjects = response
												.getSubjects();
										view.getSubjectContentView()
												.setSelectedExclusionSubjects(
														getSubjectNames(editSubjectAssignment
																.getExclusionSubjects()));
										exclusionSubjects
												.removeAll(editSubjectAssignment
														.getExclusionSubjects());
										view.getSubjectContentView()
												.setAvailableExclusionSubjects(
														getSubjectNames(exclusionSubjects));

									}
								});

						// get the available SubjectGroups of the same type as
						// we've elected to edit
						SubjectGroupKey gkey = new SubjectGroupKey();
						gkey.setType(editSubjectAssignment.getSubjectType());
						SubjectGroupQuery gquery = new SubjectGroupQuery();
						gquery.setGroupKeys(Collections.singletonList(gkey));

						service.findSubjectGroups(gquery,
								new AsyncCallback<FindSubjectGroupsResponse>() {

									public void onFailure(Throwable arg) {
										if (arg.getLocalizedMessage().contains(
												"500")) {
											view.error(PolicyAdminUIUtil.messages
													.serverError(PolicyAdminUIUtil.policyAdminConstants
															.genericErrorMessage()));
										} else {
											view.error(PolicyAdminUIUtil.messages.serverError(arg
													.getLocalizedMessage()));
										}
									}

									public void onSuccess(
											FindSubjectGroupsResponse response) {
										List<SubjectGroup> subjectGroups = response
												.getGroups();

										view.getSubjectContentView()
												.setSelectedSubjectGroups(
														getGroupNames(editSubjectAssignment
																.getSubjectGroups()));
										// if there are subject groups already
										// assigned, take them out of the list
										// of available ones
										if (editSubjectAssignment
												.getSubjectGroups() != null) {

											for (SubjectGroup existing : editSubjectAssignment
													.getSubjectGroups()) {
												ListIterator<SubjectGroup> itor = subjectGroups
														.listIterator();
												while (itor.hasNext()) {
													SubjectGroup g = itor
															.next();
													if (existing.getId() != null
															&& existing
																	.getId()
																	.equals(g
																			.getId()))
														itor.remove();
													else if (existing.getName() != null
															&& existing
																	.getName()
																	.equals(g
																			.getName()))
														itor.remove();
												}
											}

										}
										view.getSubjectContentView()
												.setAvailableSubjectGroups(
														getGroupNames(subjectGroups));

										// ExclusionSG
										List<SubjectGroup> exclSG = response
												.getGroups();

										view.getSubjectContentView()
												.setSelectedExclusionSG(
														getGroupNames(editSubjectAssignment
																.getExclusionSubjectGroups()));
										// if there are exclusion subject groups
										// already
										// assigned, take them out of the list
										// of available ones
										if (editSubjectAssignment
												.getExclusionSubjectGroups() != null) {

											for (SubjectGroup existing : editSubjectAssignment
													.getExclusionSubjectGroups()) {
												ListIterator<SubjectGroup> itor = exclSG
														.listIterator();
												while (itor.hasNext()) {
													SubjectGroup g = itor
															.next();
													if (existing.getId() != null
															&& existing
																	.getId()
																	.equals(g
																			.getId()))
														itor.remove();
													else if (existing.getName() != null
															&& existing
																	.getName()
																	.equals(g
																			.getName()))
														itor.remove();
												}
											}

										}
										view.getSubjectContentView()
												.setAvailableExclusionSG(
														getGroupNames(exclSG));

										view.getSubjectContentView().show();
									}

								});
					}
				});

		// delete an assignment
		this.view.getSubjectContentView().getDelButton()
				.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {

						if (view.getSubjectContentView()
								.getSelectedSubjectAssignments().size() == 0) {

							return;
						} else {
							if(Window.confirm(PolicyAdminUIUtil.policyAdminConstants
									.deleteSelected())){
								for (PolicySubjectAssignment assignment : view
										.getSubjectContentView()
										.getSelectedSubjectAssignments()) {
									subjectAssignments.remove(assignment);
									subjectTypes.add(assignment.getSubjectType());
								}
								view.getSubjectContentView()
										.getSelectedSubjectAssignments().clear();
	
								view.getSubjectContentView().setAssignments(
										subjectAssignments);
								// add back in the subject type as being available
								view.getSubjectContentView()
										.setAvailableSubjectTypes(subjectTypes);
							}
						}
					}
				});

		// assign a new group of Subject/SubjectGroup to the policy
		this.view.getSubjectContentView().getAddButton()
				.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// Add a new set of subjects and groups for a given
						// SubjectType
						if (subjectAssignments == null) {
							subjectAssignments = new ArrayList<PolicySubjectAssignment>();
						}
						PolicySubjectAssignment assignment = null;
						if (editSubjectAssignment != null) {
							// we were editing an existing assignment
							assignment = editSubjectAssignment;
							editSubjectAssignment = null;
						} else {
							assignment = new PolicySubjectAssignment();
						}

						assignment.setSubjectType(view.getSubjectContentView()
								.getSubjectType());
						// subjects
						List<Subject> subjects = new ArrayList<Subject>();

						for (String s : view.getSubjectContentView()
								.getSelectedSubjects()) {
							Subject assignedSubject = getSubject(s, assignment.getSubjectType(), false);
							//let's do a second loading, external subjects should stored in local now
							if(assignedSubject.getSubjectMatchTypes() == null){
								assignedSubject = getSubject(s, assignment.getSubjectType(), true);
							}
							subjects.add(assignedSubject);
						
						}
						// exclusionSubjects
						List<Subject> exclusionSubjects = new ArrayList<Subject>();
						for (String s : view.getSubjectContentView()
								.getSelectedExclusionSubjects()) {
							Subject assignedSubject = getSubject(s, assignment.getSubjectType(), true);
							
							//let's do a second loading, external subjects should stored in local now
							if(assignedSubject.getSubjectMatchTypes() == null){
								assignedSubject = getSubject(s, assignment.getSubjectType(), true);
							}
							exclusionSubjects.add(assignedSubject);
						
						}
						// groups
						List<SubjectGroup> groups = new ArrayList<SubjectGroup>();
						for (String s : view.getSubjectContentView()
								.getSelectedSubjectGroups()) {
							groups.add(getGroup(s, assignment.getSubjectType(), false));
						}
						// exclusion Subjectgroups
						List<SubjectGroup> exclSg = new ArrayList<SubjectGroup>();
						for (String s : view.getSubjectContentView()
								.getSelectedExclusionSG()) {
							exclSg.add(getGroup(s, assignment.getSubjectType(), true));
						}

						assignment.setSubjects(subjects);
						assignment.setExclusionSubjects(exclusionSubjects);
						assignment.setSubjectGroups(groups);
						assignment.setExclusionSubjectGroups(exclSg);
						subjectAssignments.add(assignment);
						view.getSubjectContentView().setAssignments(
								subjectAssignments);
						// take the SubjectType of the assignment out of the
						// list of available types
						subjectTypes.remove(assignment.getSubjectType());
						view.getSubjectContentView().setAvailableSubjectTypes(
								subjectTypes);
						view.getSubjectContentView().clearAssignmentWidget();
					}
				});

		view.getSubjectContentView().getCancelButton()
				.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if (editSubjectAssignment != null) {
							subjectAssignments.add(editSubjectAssignment);
							editSubjectAssignment = null;
							view.getSubjectContentView().setAssignments(
									subjectAssignments);
							view.getSubjectContentView().hide();
							// fix the subjecttypes
							view.getSubjectContentView()
									.setAvailableSubjectTypes(subjectTypes);
							return;
						}

						view.getSubjectContentView().clearAssignmentWidget();
						view.getSubjectContentView().hide();
					}
				});

	}

	private void bindResourceSection() {

		this.view.getResourceContentView().getResourceLevelBox()
				.addChangeHandler(new ChangeHandler() {

					public void onChange(ChangeEvent event) {

						if (ResourceLevel.GLOBAL.name().equals(
								view.getResourceContentView()
										.getResourceLevel())) {

							view.getResourceContentView()
									.getResourceTypeLabel().setVisible(false);
							view.getResourceContentView().getResourceTypeBox()
									.setVisible(false);
							view.getResourceContentView()
									.getResourceNameLabel().setVisible(false);
							view.getResourceContentView().getResourceNameBox()
									.setVisible(false);

							view.getResourceContentView()
									.getSelectBoxesWidget().setVisible(false);

						} else {

							if (ResourceLevel.RESOURCE.name().equals(
									view.getResourceContentView()
											.getResourceLevel())) {
								view.getResourceContentView()
										.getResourceTypeLabel()
										.setVisible(true);
								view.getResourceContentView()
										.getResourceTypeBox().setVisible(true);
								view.getResourceContentView()
										.getResourceNameLabel()
										.setVisible(true);
								view.getResourceContentView()
										.getResourceNameBox().setVisible(true);
								view.getResourceContentView()
										.getSelectBoxesWidget()
										.setVisible(false);
							} else if (ResourceLevel.OPERATION.name().equals(
									view.getResourceContentView()
											.getResourceLevel())) {
								view.getResourceContentView()
										.getResourceTypeLabel()
										.setVisible(true);
								view.getResourceContentView()
										.getResourceTypeBox().setVisible(true);
								view.getResourceContentView()
										.getResourceNameLabel()
										.setVisible(true);
								view.getResourceContentView()
										.getResourceNameBox().setVisible(true);
								view.getResourceContentView()
										.getSelectBoxesWidget()
										.setVisible(true);
							}

							view.getResourceContentView().setResourceTypes(
									getResourceTypes());
							view.getResourceContentView().getResourceTypeBox()
									.setSelectedIndex(-1);
						}

					}

				});

		// edit an assignment
		this.view.getResourceContentView().getEditButton()
				.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						if (view.getResourceContentView().getSelections()
								.size() != 1)
							return;
						view.getResourceContentView().getResourceLevelLabel()
								.setVisible(false);
						view.getResourceContentView().getResourceLevelBox()
								.setVisible(false);

						view.getResourceContentView().getResourceTypeLabel()
								.setVisible(true);
						view.getResourceContentView().getResourceTypeBox()
								.setVisible(true);

						view.getResourceContentView().getResourceNameLabel()
								.setVisible(true);
						view.getResourceContentView().getResourceNameBox()
								.setVisible(true);

						view.getResourceContentView().getSelectBoxesWidget()
								.setVisible(true);

						// edit the existing assignment - set the available
						// SubjectTypes as only the one
						// in the assignment, and set the availables vs selected
						// Subjects and SubjectGroups
						// based on the assignment
						editResourceAssignment = view.getResourceContentView()
								.getSelections().get(0);
						// remove the assignment from the list of assignments
						// while it is being edited
						resourceAssignments.remove(editResourceAssignment);
						// update the display
						view.getResourceContentView().setAssignments(
								resourceAssignments);
						// set the available resource types to just the type
						// being edited
						List<String> typeList = new ArrayList<String>();
						typeList.add(editResourceAssignment.getResourceType());
						view.getResourceContentView()
								.setResourceTypes(typeList);

						// get all the resources of this type
						ResourceKey rsKey = new ResourceKey();
						rsKey.setType(editResourceAssignment.getResourceType());

						List<ResourceKey> rsKeys = new ArrayList<ResourceKey>();
						rsKeys.add(rsKey);
						service.getResources(rsKeys,
								new AsyncCallback<GetResourcesResponse>() {

									public void onFailure(Throwable arg) {
										if (arg.getLocalizedMessage().contains(
												"500")) {
											view.error(PolicyAdminUIUtil.messages
													.serverError(PolicyAdminUIUtil.policyAdminConstants
															.genericErrorMessage()));
										} else {
											view.error(PolicyAdminUIUtil.messages.serverError(arg
													.getLocalizedMessage()));
										}
									}

									public void onSuccess(
											GetResourcesResponse response) {
										List<Resource> resources = new ArrayList<Resource>(
												response.getResources());
										view.getResourceContentView()
												.setResourceNames(
														getResourceNames(resources));

										int index = getResourceIndex(resources,
												editResourceAssignment
														.getResourceName());
										view.getResourceContentView()
												.getResourceNameBox()
												.setSelectedIndex(index);

										view.getResourceContentView()
												.setSelectedOperations(
														getOperationNames(editResourceAssignment
																.getOpList()));

										List<Operation> operations = new ArrayList<Operation>(
												resources.get(index)
														.getOpList());
										operations
												.removeAll(editResourceAssignment
														.getOpList());
										view.getResourceContentView()
												.setAvailableOperations(
														getOperationNames(operations));
									}
								});
						view.getResourceContentView().show();

					}

				});

		view.getResourceContentView().getCancelResourceButton()
				.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if (editResourceAssignment != null) {
							resourceAssignments.add(editResourceAssignment);
							editResourceAssignment = null;
							view.getResourceContentView().setAssignments(
									resourceAssignments);
							view.getResourceContentView().hide();
							// // fix the resource types + name key
							// .setasiigmentUnique resource
							// AvailableSubjectTypes(subjectTypes);
							return;
						}
						assignedUniqueResources.clear();
						view.getResourceContentView().clearAssignmentWidget();
						view.getResourceContentView().hide();
					}
				});

		// retrieve resource names based on selected type
		this.view.getResourceContentView().getResourceTypeBox()
				.addChangeHandler(new ChangeHandler() {

					public void onChange(ChangeEvent event) {

						getRemainingResourceNames();
					}

				});

		// retrieve available operations based on selected name
		this.view.getResourceContentView().getResourceNameBox()
				.addClickHandler(new ClickHandler() {

					public void onClick(ClickEvent event) {
						getAvailableOperations();
					}

				});

		// assign a new rs to the policy
		this.view.getResourceContentView().getAddResourceButton()
				.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {

						/*
						 * Resources could be assigned at 3 levels GLOBAL: means
						 * all resources and its operations apply RESOURCE: All
						 * operations of the selected resource are included
						 * OPERATION: The policy just apply to the specific
						 * operation of the selected resources
						 */
						if (resourceAssignments == null)
							resourceAssignments = new ArrayList<Resource>();

						ResourceImpl assignment = null;
						// if (editResourceAssignment != null) {
						// // we were editing an existing assignment
						// assignment = editResourceAssignment;
						// editResourceAssignment = null;
						// } else
						assignment = new ResourceImpl();

						if (ResourceLevel.RESOURCE.name().equals(
								view.getResourceContentView()
										.getResourceLevel())) {
							assignment
									.setResourceName(view
											.getResourceContentView()
											.getResourceName());
							assignment
									.setResourceType(view
											.getResourceContentView()
											.getResourceType());

							List<Operation> operations = new ArrayList<Operation>();
							for (String s : view.getResourceContentView()
									.getAvailableOperations()) {
								OperationImpl op = new OperationImpl();
								op.setOperationName(s);
								operations.add(op);
							}
							assignment.setOpList(operations);
							resourceAssignments.add(assignment);

						} else if (ResourceLevel.GLOBAL.name().equals(
								view.getResourceContentView()
										.getResourceLevel())) {
							getAllResources();
							resourceAssignments.clear();
							resourceAssignments.addAll(allResources);

						} else {

							assignment
									.setResourceName(view
											.getResourceContentView()
											.getResourceName());
							assignment
									.setResourceType(view
											.getResourceContentView()
											.getResourceType());

							List<Operation> operations = new ArrayList<Operation>();
							for (String s : view.getResourceContentView()
									.getSelectedOperations()) {
								OperationImpl op = new OperationImpl();
								op.setOperationName(s);
								operations.add(op);
							}
							assignment.setOpList(operations);
							resourceAssignments.add(assignment);

						}

						view.getResourceContentView().setAssignments(
								resourceAssignments);
						// add the ResourceType & name as already assidned
						if (ResourceLevel.GLOBAL.name().equals(
								view.getResourceContentView()
										.getResourceLevel())) {
							assignedUniqueResources.add("allall");
						} else {
							assignedUniqueResources.add(assignment
									.getResourceType()
									+ assignment.getResourceName());
						}

						view.getResourceContentView().clearAssignmentWidget();
					}

				});

		// delete an assignment
		this.view.getResourceContentView().getDelButton()
				.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if (view.getResourceContentView().getSelections()
								.size() == 0) {
							return;
						} else {
							if(Window.confirm(PolicyAdminUIUtil.policyAdminConstants
									.deleteSelected())){
								for (Resource selectedAssignment : view
										.getResourceContentView().getSelections()) {
									resourceAssignments.remove(selectedAssignment);
	
									assignedUniqueResources
											.remove(selectedAssignment
													.getResourceType()
													+ selectedAssignment
															.getResourceName());
	
								}
								view.getResourceContentView().getSelections()
										.clear();
								view.getResourceContentView().setAssignments(
										resourceAssignments);
							}
						}
					}
				});

	}

	/*
	 * Interface definitions
	 */
	public interface PolicyCreateDisplay extends PolicyPageTemplateDisplay {
		HasValue<String> getPolicyName();

		HasValue<String> getPolicyDesc();

		boolean getPolicyEnabled();

		Button getSaveButton();

		Button getCancelButton();

		ResourcesContentDisplay getResourceContentView();

		SubjectContentDisplay getSubjectContentView();

		String getExtraFieldValue(int order);

		void setExtraFieldValue(int order, String value, boolean append);

		void setUserActions(List<UserAction> permittedActions);

		void clear();

		void error(String msg);

		void setPolicyDesc(String policyDesc);

		void setPolicyName(String policyName);

		void setPolicyStatus(boolean enabled);

		void setPolicyType(String policyType);

		void setExtraFieldList(List<ExtraField> extraFieldList);

		/*
		 * Condition Builder methods
		 */
		void setConditionBuilderVisible(boolean visible);

		void setExclusionListsVisible(boolean visible);
		
		HasClickHandlers getAddConditionButton();

		HasChangeHandlers getRsListBox();

		String getRsNameSelected();

		String getAritmSignSelected();

		String getConditionSelected();

		String getQuantityBox();

		String getOpNameSelected();

		String getLogicOpSelected();

		void setRsNames(List<String> names);

		void setOpNames(List<String> names);

		void setConditionNames(List<String> conditions);

		void clearConditionBuilder();

		boolean validAllConditionFields();
	}

	public interface ResourcesContentDisplay extends Display {
		void setUserActions(List<UserAction> permittedActions);

		void setAssignments(List<Resource> assignments);

		void setAvailableOperations(List<String> operations);

		void setSelectedOperations(List<String> operations);

		void setResourceLevel(List<String> resourceLevels);

		void setResourceTypes(List<String> resourceTypes);

		void setResourceNames(List<String> resourceNames);

		List<Resource> getSelections();

		SelectBoxesWidget getSelectBoxesWidget();

		List<String> getSelectedOperations();

		List<String> getAvailableOperations();

		String getResourceName();

		String getResourceLevel();

		Label getResourceLevelLabel();

		Label getResourceNameLabel();

		Label getResourceTypeLabel();

		String getResourceType();

		ListBox getResourceTypeBox();

		ListBox getResourceLevelBox();

		ListBox getResourceNameBox();

		HasClickHandlers getAddResourceButton();

		HasClickHandlers getCancelResourceButton();

		void error(String msg);

		void clearAssignmentWidget();

		HasClickHandlers getEditButton();

		HasClickHandlers getDelButton();

		void show();

		void hide();
	}

	public interface SubjectContentDisplay extends Display {
		String getSubjectType();

		HasClickHandlers getGroupSearchButton();

		HasClickHandlers getSubjectSearchButton();

		HasClickHandlers getAddButton();

		HasClickHandlers getCancelButton();

		HasClickHandlers getEditButton();

		HasClickHandlers getDelButton();

		String getSubjectSearchTerm();

		String getGroupSearchTerm();

		List<String> getSelectedSubjectGroups();

		List<String> getSelectedExclusionSG();

		List<String> getSelectedSubjects();

		List<String> getSelectedExclusionSubjects();

		List<PolicySubjectAssignment> getSelectedSubjectAssignments();

		List<PolicySubjectAssignment> getAssignments();

		void setAvailableSubjectGroups(List<String> list);

		void setAvailableExclusionSG(List<String> list);

		void setAvailableSubjects(List<String> list);

		void setAvailableExclusionSubjects(List<String> list);

		void setAvailableSubjectTypes(List<String> list);

		void setAssignments(List<PolicySubjectAssignment> assignments);

		void setSelectedSubjects(List<String> list);

		void setSelectedExclusionSubjects(List<String> list);

		void setSelectedSubjectGroups(List<String> list);

		void setSelectedExclusionSG(List<String> list);

		void setUserActions(List<UserAction> permissions);

		void clearAssignmentWidget();

		void show();

		void hide();
	}

	protected void clearLists() {
		if (assignedUniqueResources != null) {
			assignedUniqueResources.clear();
		}
		if (availableResourcesByType != null) {
			availableResourcesByType.clear();
		}
		 if (allSubjects != null) {
			 allSubjects.clear();
		 }
		 if(allSubjectGroups != null){
			 allSubjectGroups.clear();
		 }
		if (subjectAssignments != null) {
			subjectAssignments.clear();
		}
		if (rules != null) {
			rules.clear();
		}
		if (internalSubjects != null) {
			internalSubjects.clear();
		}
	}

	@Override
	protected PolicyCreateDisplay getView() {
		return view;
	}

	public void bind() {

		this.view.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				PolicyCreatePresenter.this.view.clear();

				try {
					PolicyCreatePresenter.this.subjectAssignments.clear();
					PolicyCreatePresenter.this.resourceAssignments.clear();
					PolicyCreatePresenter.this.rules.clear();
					PolicyCreatePresenter.this.allResources.clear();
					PolicyCreatePresenter.this.assignedUniqueResources.clear();
					PolicyCreatePresenter.this.availableResourcesByType.clear();
				    PolicyCreatePresenter.this.allSubjects.clear();
				    PolicyCreatePresenter.this.allSubjectGroups.clear();
					PolicyCreatePresenter.this.editResourceAssignment = null;

				} catch (NullPointerException ex) {
					// do nothing
				}

				HistoryToken token = makeToken(PolicyController.PRESENTER_ID,
						PolicySummaryPresenter.PRESENTER_ID, null);
				History.newItem(token.toString(), true);
			}
		});

	}

	@Override
	public void go(HasWidgets container, final HistoryToken token) {
		container.clear();
		// this.view.clear();

		this.view.activate();
		container.add(this.view.asWidget());

		service = (PolicyQueryService) serviceMap
				.get(SupportedService.POLICY_QUERY_SERVICE);

		// TODO fetch from server
		permittedActions = Arrays.asList(UserAction.values());
		this.view.setUserActions(permittedActions);

		subjectTypes = new ArrayList(SubjectType.getValues());

		fetchResources();
		fetchSubjects();
		fetchSubjectGroups();
		
		this.view.getResourceContentView()
				.setResourceLevel(getResourceLevels());

		this.view.getResourceContentView().getResourceLevelBox()
				.setSelectedIndex(-1);

		this.view.getSubjectContentView()
				.setAvailableSubjectTypes(subjectTypes);

		this.view.setPolicyStatus(false);
	}

	private List<String> getResourceTypes() {
		return ResourceType.getValues();
	}

	private void getAvailableOperations() {
		PolicyCreatePresenter.this.view.getResourceContentView()
				.setAvailableOperations(Collections.EMPTY_LIST);
		String rsName = PolicyCreatePresenter.this.view
				.getResourceContentView().getResourceName();

		List<String> opNames = new ArrayList<String>();

		for (Resource rs : availableResourcesByType) {
			if (rsName.equals(rs.getResourceName())) {
				if (!rs.getOpList().isEmpty()) {
					for (Operation operation : rs.getOpList()) {
						opNames.add(operation.getOperationName());
					}

				}
				break;
			}
		}
		PolicyCreatePresenter.this.view.getResourceContentView()
				.setAvailableOperations(opNames);

	}

	private void getAllResources() {

		service.getResources(PolicyKeysUtil.getAllResourceKeyList(),
				new AsyncCallback<GetResourcesResponse>() {
					public void onSuccess(GetResourcesResponse response) {
						allResources = new ArrayList<Resource>(response
								.getResources());
					}

					public void onFailure(Throwable arg) {
						if (arg.getLocalizedMessage().contains("500")) {
							view.getResourceContentView()
									.error(PolicyAdminUIUtil.messages
											.serverError(PolicyAdminUIUtil.policyAdminConstants
													.genericErrorMessage()));
						} else {
							view.getResourceContentView().error(
									PolicyAdminUIUtil.messages.serverError(arg
											.getLocalizedMessage()));
						}
					}
				});

	}

	private void getRemainingResourceNames() {
		ResourceKey key = new ResourceKey();
		key.setType(PolicyCreatePresenter.this.view.getResourceContentView()
				.getResourceType());

		service.getResources(Collections.singletonList(key),
				new AsyncCallback<GetResourcesResponse>() {
					public void onSuccess(GetResourcesResponse response) {
						availableResourcesByType = new ArrayList<Resource>(
								response.getResources());
						List<String> resourceNames = new ArrayList<String>();

						for (Resource rs : availableResourcesByType) {

							if (!assignedUniqueResources.contains(rs
									.getResourceType() + rs.getResourceName())) {
								resourceNames.add(rs.getResourceName());
							}
						}
						PolicyCreatePresenter.this.view
								.getResourceContentView().setResourceNames(
										resourceNames);
						PolicyCreatePresenter.this.view
								.getResourceContentView().getResourceNameBox()
								.setSelectedIndex(-1);
					}

					public void onFailure(Throwable arg) {
						if (arg.getLocalizedMessage().contains("500")) {
							view.getResourceContentView()
									.error(PolicyAdminUIUtil.messages
											.serverError(PolicyAdminUIUtil.policyAdminConstants
													.genericErrorMessage()));
						} else {
							view.getResourceContentView().error(
									PolicyAdminUIUtil.messages.serverError(arg
											.getLocalizedMessage()));
						}
					}
				});
	}

	private List<String> getResourceNames(List<Resource> resources) {

		List<String> resourceNames = new ArrayList<String>();

		for (Resource rs : resources) {
			resourceNames.add(rs.getResourceName());
		}
		return resourceNames;
	}

	protected void fetchSubjects() {

		SubjectQuery query = new SubjectQuery();

		query.setSubjectKeys(PolicyKeysUtil.getAllSubjectKeyList());
		service.findSubjects(query,
				new AsyncCallback<PolicyQueryService.FindSubjectsResponse>() {

					public void onSuccess(FindSubjectsResponse result) {
						allSubjects = new ArrayList<Subject>(result.getSubjects());
					}

					public void onFailure(Throwable arg) {
						if (arg.getLocalizedMessage().contains("500")) {
							view.error(PolicyAdminUIUtil.messages
									.serverError(PolicyAdminUIUtil.policyAdminConstants
											.genericErrorMessage()));
						} else {
							view.error(PolicyAdminUIUtil.messages.serverError(arg
									.getLocalizedMessage()));
						}
					}
				});
	}
	
	protected void fetchSubjectGroups() {

		SubjectGroupQuery query = new SubjectGroupQuery();

		query.setGroupKeys(PolicyKeysUtil.getAllSubjectGroupKeyList());
		service.findSubjectGroups(query,
				new AsyncCallback<PolicyQueryService.FindSubjectGroupsResponse>() {

					public void onSuccess(FindSubjectGroupsResponse result) {
						allSubjectGroups = new ArrayList<SubjectGroup>(result.getGroups());
					}

					public void onFailure(Throwable arg) {
						if (arg.getLocalizedMessage().contains("500")) {
							view.error(PolicyAdminUIUtil.messages
									.serverError(PolicyAdminUIUtil.policyAdminConstants
											.genericErrorMessage()));
						} else {
							view.error(PolicyAdminUIUtil.messages.serverError(arg
									.getLocalizedMessage()));
						}
					}
				});
	}

	public Subject getSubject(String name, String type,
			final boolean exclusionList) {
		final SubjectImpl s = new SubjectImpl();
		for (Subject subject : allSubjects) {
			if (subject.getType().equals(type)
					&& subject.getName().equals(name)) {
				s.setName(subject.getName());
				s.setType(subject.getType());
				SubjectMatchTypeImpl smt = new SubjectMatchTypeImpl();
				
				AttributeValueImpl attr = new AttributeValueImpl();
				SubjectAttributeDesignatorImpl designator = new SubjectAttributeDesignatorImpl();
				if (!exclusionList) {
					smt.setMatchId("urn:oasis:names:tc:xacml:1.0:function:integer-equal");
					attr.setDataType("http://www.w3.org/2001/XMLSchema#integer");
					attr.setValue(SubjectUtil.getSubjectId(subject).toString());
					designator.setAttributeId("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
					designator.setDataType("http://www.w3.org/2001/XMLSchema#integer");
					
				} else {
					smt.setMatchId("urn:oasis:names:tc:xacml:1.0:function:string-regexp-match");
					attr.setDataType("http://www.w3.org/2001/XMLSchema#string");
					attr.setValue("(?!"
							+ SubjectUtil.getSubjectId(subject).toString()
							+ ")");
					designator.setAttributeId("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
					designator.setDataType("http://www.w3.org/2001/XMLSchema#string");
					
				}
				smt.setAttributeValue(attr);
				smt.setSubjectAttributeDesignator(designator);

			 
				s.setSubjectMatch(new ArrayList<SubjectMatchType>(Collections
						.singletonList(smt)));
				break;
			}

		}
		
		if(s.getSubjectMatchTypes()==null){
			s.setType(type);
			s.setName(name);
			createInternalSubject(new ArrayList<Subject>(Collections.singletonList(s)));
			fetchSubjects();
			fetchSubjectGroups();	
		}
		 	
		 
		return s;
	}

	public SubjectGroup getGroup(String name, String type, final boolean exclusionList) {
		SubjectGroupImpl group = new SubjectGroupImpl();
		for (SubjectGroup subjectGroup : allSubjectGroups) {
			if (subjectGroup.getType().equals(type)
					&& subjectGroup.getName().equals(name)) {
				group.setType(type);
				group.setName(name);
				SubjectMatchTypeImpl smt = new SubjectMatchTypeImpl();
				AttributeValueImpl attr = new AttributeValueImpl();
				SubjectAttributeDesignatorImpl designator = new SubjectAttributeDesignatorImpl();

				if (!exclusionList) {
					smt.setMatchId("urn:oasis:names:tc:xacml:1.0:function:integer-equal");
					attr.setDataType("http://www.w3.org/2001/XMLSchema#integer");
					attr.setValue(SubjectUtil.getSubjectGroupId(subjectGroup).toString());
					designator.setAttributeId("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
					designator.setDataType("http://www.w3.org/2001/XMLSchema#integer");
					
				} else {
					smt.setMatchId("urn:oasis:names:tc:xacml:1.0:function:string-regexp-match");
					attr.setDataType("http://www.w3.org/2001/XMLSchema#string");
					attr.setValue("(?!"
							+ SubjectUtil.getSubjectGroupId(subjectGroup).toString()
							+ ")");
					designator.setAttributeId("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
					designator.setDataType("http://www.w3.org/2001/XMLSchema#string");
				
				}
				smt.setAttributeValue(attr);
				smt.setSubjectAttributeDesignator(designator);

				group.setSubjectMatch(new ArrayList<SubjectMatchType>(Collections
						.singletonList(smt)));
				break;
			}
		}
		return group;
	}

	protected GenericPolicy getPolicy(String name, String type,
			String description, List<Resource> resources,
			List<PolicySubjectAssignment> subjectAssignments, List<Rule> rules) {
		GenericPolicyImpl p = new GenericPolicyImpl();
		p.setName(name);
		p.setType(type);
		p.setDescription(description);

		if (rules != null) {
			p.setRules(rules);
		}

		if (resources != null)
			p.setResources(new ArrayList<Resource>(resources));

		if (subjectAssignments != null) {
			List<Subject> subjects = new ArrayList<Subject>();
			List<Subject> exclusionSubjects = new ArrayList<Subject>();
			List<SubjectGroup> groups = new ArrayList<SubjectGroup>();
			List<SubjectGroup> exclSGroups = new ArrayList<SubjectGroup>();

			for (PolicySubjectAssignment a : subjectAssignments) {

				if (a.getSubjects() != null && a.getSubjects().size() > 0) {
					// external subjects todays are only USER types
//					if ("USER".equals(a.getSubjectType())) {
//						createInternalSubject(a.getSubjects());
//					}

					// adding the created subjects (now as internal ones)
//					List<PolicySubjectAssignment> internalAssignments = view
//							.getSubjectContentView().getAssignments();

					for (PolicySubjectAssignment policySubjectAssignment :  view.getSubjectContentView().getAssignments()) {
						if (policySubjectAssignment.getSubjects() != null) {
							subjects.addAll(policySubjectAssignment
									.getSubjects());
							break;
						}
					}

				}

				if (a.getExclusionSubjects() != null
						&& a.getExclusionSubjects().size() > 0) {
					// external subjects todays are only USER types
//					if ("USER".equals(a.getSubjectType())) {
//						createInternalSubject(a.getExclusionSubjects());
//					}

					// adding the created subjects (now as internal ones)
//					List<PolicySubjectAssignment> internalAssignments = view
//							.getSubjectContentView().getAssignments();

					for (PolicySubjectAssignment policySubjectAssignment : view.getSubjectContentView().getAssignments()) {
						if (policySubjectAssignment.getExclusionSubjects() != null) {
							exclusionSubjects.addAll(policySubjectAssignment
									.getExclusionSubjects());
							break;
						}
					}

				}

				
				if (a.getSubjectGroups() != null) {
					groups.addAll(a.getSubjectGroups());
				}

				if (a.getExclusionSubjectGroups() != null) {
					exclSGroups.addAll(a.getExclusionSubjectGroups());
				}
			}
			p.setSubjects(subjects);
			p.setExclusionSubjects(exclusionSubjects);
			p.setSubjectGroups(groups);
			p.setExclusionSG(exclSGroups);
		}
		return p;
	}

	protected void createInternalSubject(final List<Subject> subjects) {
		List<SubjectKey> keys = new ArrayList<SubjectKey>();
		for (Subject subj : subjects) {
			SubjectKey key = new SubjectKey();
			key.setName(subj.getName());
			// today external subject supported are USER types
			key.setType("USER");
			keys.add(key);
		}

		final SubjectQuery query = new SubjectQuery();
		query.setSubjectKeys(keys);
		service.findSubjects(query,
				new AsyncCallback<PolicyQueryService.FindSubjectsResponse>() {

					public void onSuccess(FindSubjectsResponse result) {
						subjects.removeAll(result.getSubjects());
						if (subjects.size() > 0) {
							service.createSubjects(
									subjects,
									new AsyncCallback<PolicyQueryService.CreateSubjectsResponse>() {

										public void onSuccess(
												final CreateSubjectsResponse result) {
											// do nothing, subjects has been
											// stored,
											// we can continue...
										}

										public void onFailure(
												final Throwable arg) {
											if (arg.getLocalizedMessage()
													.contains("500")) {
												view.error(PolicyAdminUIUtil.messages
														.serverError(PolicyAdminUIUtil.policyAdminConstants
																.genericErrorMessage()));
											} else {
												view.error(PolicyAdminUIUtil.messages.serverError(arg
														.getLocalizedMessage()));
											}
										}
									});
						}
					}

					public void onFailure(Throwable arg) {
						if (arg.getLocalizedMessage().contains("500")) {
							view.error(PolicyAdminUIUtil.messages
									.serverError(PolicyAdminUIUtil.policyAdminConstants
											.genericErrorMessage()));
						} else {
							view.error(PolicyAdminUIUtil.messages.serverError(arg
									.getLocalizedMessage()));
						}
					}

				});

	}

	protected void fetchResources() {
		service.getResources(null, new AsyncCallback<GetResourcesResponse>() {
			public void onSuccess(GetResourcesResponse response) {
				availableResourcesByType.addAll(response.getResources());

			}

			public void onFailure(Throwable arg) {
				if (arg.getLocalizedMessage().contains("500")) {
					view.getResourceContentView()
							.error(PolicyAdminUIUtil.messages
									.serverError(PolicyAdminUIUtil.policyAdminConstants
											.genericErrorMessage()));
				} else {
					view.getResourceContentView().error(
							PolicyAdminUIUtil.messages.serverError(arg
									.getLocalizedMessage()));
				}
			}
		});

	}

	private List<String> getSubjectNames(List<Subject> subjects) {
		List<String> names = new ArrayList<String>();
		if (subjects != null) {
			for (Subject s : subjects) {
				names.add(s.getName());
			}
		}
		return names;
	}

	private List<String> getGroupNames(List<SubjectGroup> groups) {
		List<String> names = new ArrayList<String>();
		if (groups != null) {
			for (SubjectGroup s : groups) {
				names.add(s.getName());
			}
		}
		return names;
	}

	private List<String> getOperationNames(List<Operation> operations) {
		List<String> names = new ArrayList<String>();
		if (operations != null) {
			for (Operation op : operations) {
				names.add(op.getOperationName());
			}
		}
		return names;
	}

	private int getResourceIndex(List<Resource> resources, String rsName) {
		int index = -1;
		for (int i = 0; i < resources.size(); i++) {
			if (resources.get(i).getResourceName().equals(rsName)) {
				index = i;
				break;
			}
		}
		return index;
	}

}
