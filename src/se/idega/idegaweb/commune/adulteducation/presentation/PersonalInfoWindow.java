/*
 * $Id: PersonalInfoWindow.java,v 1.1 2005/05/25 13:06:38 laddi Exp $
 * Created on May 25, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;


/**
 * Last modified: $Date: 2005/05/25 13:06:38 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class PersonalInfoWindow extends Window {

	public PersonalInfoWindow() {
		setWidth(540);
		setHeight(700);
		setScrollbar(true);
		setResizable(true);
	}
	
	public void main(IWContext iwc) throws Exception {
		PersonalInfo info = new PersonalInfo();
		info.setInWindow(true);
		add(info);
	}
}