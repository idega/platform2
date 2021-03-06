/*
 * $Id: Date.java,v 1.3.2.1 2007/01/12 19:32:39 idegaweb Exp $
 * Created on 14.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.idegaweb.widget.date;

import com.idega.idegaweb.widget.Widget;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.util.IWCalendar;


/**
 * Shows the current date as a Text object.  Has a set method to change the style (details) of the date shown.
 * Available styles:<br>
 * 		IWCalendar.SHORT (default): completely numeric, such as 12.13.52<br>
 * 		IWCalendar.MEDIUM: is longer, such as Jan 12, 1952<br>
 * 		IWCalendar.LONG: is longer, such as January 12, 1952<br>
 * 		IWCalendar.FULL: is pretty completely specified, such as Tuesday, April 12, 1952 AD
 * 
 * Last modified: 14.10.2004 11:18:49 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3.2.1 $
 */
public class Date extends Widget {
	
	private int style = IWCalendar.SHORT;

	protected PresentationObject getWidget(IWContext iwc) {
		IWCalendar calendar = new IWCalendar();
		
		Text text = new Text(calendar.getLocaleDate(getLocale(), this.style));
		return text;
	}
	
	/**
	 * Sets the style of the date.
	 * @param style	The style to set
	 */
	public void setDateStyle(int style) {
		this.style = style;
	}
}