/*
 * $Id: BusinessLaunchButton.java,v 1.2 2003/05/23 08:06:28 laddi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.presentation;


import se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class BusinessLaunchButton extends ChildCareBlock {
	protected static final String SUBMIT = "detonate";
	
	protected void control(IWContext iwc) {
		if (iwc.isParameterSet(SUBMIT)) {
			getBusiness().convertOldQueue();
		}
		
		displayForm();			
	}

	protected void displayForm() {
		Form form = new Form();
		SubmitButton button = new SubmitButton(SUBMIT,"Kill application");
		form.add(button);
		add(form);	
	}

	public void init(IWContext iwc) {
		control(iwc);
	}
}
