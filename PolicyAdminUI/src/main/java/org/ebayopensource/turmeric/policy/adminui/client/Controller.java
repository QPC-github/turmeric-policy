/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client;

import org.ebayopensource.turmeric.policy.adminui.client.model.HistoryToken;
import org.ebayopensource.turmeric.policy.adminui.client.presenter.Presenter;

/**
 * The Interface Controller.
 */
public interface Controller {

    /**
	 * Adds the presenter.
	 * 
	 * @param id
	 *            the id
	 * @param p
	 *            the p
	 */
    void addPresenter(String id, Presenter p);
    
    /**
	 * Gets the presenter.
	 * 
	 * @param id
	 *            the id
	 * @return the presenter
	 */
    Presenter getPresenter(String id);
    
    /**
	 * Select presenter.
	 * 
	 * @param token
	 *            the token
	 */
    void selectPresenter(HistoryToken token);
}
