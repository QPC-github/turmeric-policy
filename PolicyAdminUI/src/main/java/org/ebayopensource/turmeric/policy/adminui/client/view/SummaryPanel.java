/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class SummaryPanel extends Composite {
    
    private FlowPanel panel;
    private ScrollPanel scroller;
    private Label headingLabel;
    private Image downloadImg;
    private String downloadUrl;
    private Image infoImg;
    private String info;
    private Image downloadImgLight;
    private Image infoImgLight;
    
    
    public SummaryPanel () {
        panel = new FlowPanel();
        panel.addStyleName("summary-panel-item"); 
        
        Grid headingGrid = new Grid(1, 2);
        
        headingLabel = new Label("");
        headingGrid.setWidget(0, 0, headingLabel);
        headingGrid.setWidth("100%");

        Grid buttonGrid = new Grid(1,2);
        headingGrid.setWidget(0,1, buttonGrid);
        headingGrid.getCellFormatter().setHorizontalAlignment(0,1, HasHorizontalAlignment.ALIGN_RIGHT);
        
        infoImg = new Image();
        infoImg.setUrl("images/info.png");
        
        infoImgLight = new Image();
        infoImgLight.setUrl("images/info-light.png");
        
        PushButton ib = new PushButton(infoImg, infoImgLight);
        ib.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (info != null) {
                    InfoDialog dialog = new InfoDialog(false);
                    dialog.setMessage(info);
                    int x = infoImg.getAbsoluteLeft();
                    int y = infoImg.getAbsoluteTop() + infoImg.getOffsetHeight();
                    dialog.getDialog().setAutoHideEnabled(true);
                    dialog.getDialog().setPopupPosition(x, y);
                    dialog.getDialog().show();
                }
            }
        });
        
        
        downloadImg = new Image();
        downloadImg.setUrl("images/dwnld.png");
        downloadImg.addStyleName("dwnld");
        
        downloadImgLight = new Image();
        downloadImgLight.setUrl("images/dwnld-light.png");
        
        PushButton db = new PushButton(downloadImg, downloadImgLight);
        db.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent arg0) {
                final Element downloadIframe = RootPanel.get("__download").getElement(); 
                if (downloadIframe == null)
                   Window.open(downloadUrl,"_blank", "");
                else
                    DOM.setElementAttribute(downloadIframe, "src", downloadUrl); 
            }
        });
       
        buttonGrid.setWidget(0,0,ib);
        buttonGrid.setWidget(0,1,db);
       
        panel.add(headingGrid);
        panel.setWidth("50em");
        scroller = new ScrollPanel();
        scroller.addStyleName("summary-scroll");
        panel.add(scroller);
        initWidget(panel);
    }

    public void setHeading (String text) {
        headingLabel.setText(text);
    }
    
    public void setContents (Widget widget) {
        scroller.clear();
        scroller.add(widget);
    }
    
    public HasClickHandlers getInfoButton () {
        return infoImg;
    }
    
    public void setInfo (String info) {
        this.info = info;
    }
    
    public void setContentContainerWidth (String width) {
        scroller.setWidth(width);
    }
    
    public void setContentContainerHeight (String height) {
        scroller.setHeight(height);
    }
    
    public Panel getContentContainer () {
        return scroller;
    }
    
    public void setDownloadUrl (String url) {
        downloadUrl = url;
    }
    
}
