/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.presentation;

import is.idega.idegaweb.campus.block.application.business.CampusApplicationFormHelper;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationConfirmationReminderEmail extends Block {
	protected static final String SUBMIT = "send_email";
	
	protected void control(IWContext iwc) {
		if (iwc.isParameterSet(SUBMIT)) {
			CampusApplicationFormHelper.sendApplicationConfirmationReminderEmail(iwc);
		}
		
		displayForm();			
	}

	protected void displayForm() {
		Form form = new Form();
		SubmitButton button = new SubmitButton(SUBMIT,"Send emails");
		form.add(button);
		add(form);	
	}

	public void main(IWContext iwc) {
		control(iwc);
	}
}
