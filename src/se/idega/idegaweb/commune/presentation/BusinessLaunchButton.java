/*
 * $Id: BusinessLaunchButton.java,v 1.4 2003/06/02 23:11:55 palli Exp $
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
import com.idega.presentation.text.Text;
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
	protected static final String SUBMIT2 = "checks";
	
	protected void control(IWContext iwc) {
		if (iwc.isParameterSet(SUBMIT)) {
			getBusiness().convertOldQueue();
		}
		else if (iwc.isParameterSet(SUBMIT2)) {
			getBusiness().addMissingGrantedChecks();
		}
		
		displayForm();			
	}

	protected void displayForm() {
		Form form = new Form();
		SubmitButton button = new SubmitButton(SUBMIT,"Kill application");
		SubmitButton button2 = new SubmitButton(SUBMIT2,"Add missing checks");
		form.add(button);
		form.add(Text.BREAK);		
		form.add(button2);
		add(form);	
	}

	public void init(IWContext iwc) {
		control(iwc);
	}
}
