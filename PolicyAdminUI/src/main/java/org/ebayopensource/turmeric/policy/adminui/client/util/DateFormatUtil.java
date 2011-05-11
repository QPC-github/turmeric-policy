/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.policy.adminui.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;

public class DateFormatUtil {
	public static DateBox.DefaultFormat SHORT_DATE_FORMAT = new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy MMM dd"));
	private static final String CONSOLE_DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";
	private static final DateTimeFormat CONSOLE_DATE_FORMATTER = DateTimeFormat.getFormat(CONSOLE_DATE_FORMAT);
	
	public static String toConsoleDateFormat(Date date) {
		if (date == null) {
			return "";
		}
		return CONSOLE_DATE_FORMATTER.format(date);
	}
	
	public static Date resetTo12am(Date date){
		Date result = new Date(date.getTime());
		result.setHours(00);
		result.setMinutes(00);
		result.setSeconds(00);
		return result;
	}

	public static Date resetTo1159pm(Date value) {
		Date result = new Date(value.getTime());
		result.setHours(23);
		result.setMinutes(59);
		result.setSeconds(59);
		return result;
	}
}
