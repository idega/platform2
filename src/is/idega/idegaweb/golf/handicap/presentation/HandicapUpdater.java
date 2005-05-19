/*
 * $Id: HandicapUpdater.java,v 1.1 2005/05/19 07:32:43 laddi Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.golf.handicap.presentation;


import is.idega.idegaweb.golf.handicap.business.HandicapService;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * This class does something very clever.....
 * 
 * @author <a href="laddi@idega.is">Thorhallur Helgason</a>
 * @version 1.0
 */
public class HandicapUpdater extends Block {

	protected static final String SUBMIT = "handicap_update";
	protected static final String DATE = "date";
	
	public void main(IWContext iwc) throws Exception {
		if (iwc.isParameterSet(SUBMIT)) {
			if (iwc.isParameterSet(DATE)) {
				IWTimestamp stamp = new IWTimestamp(iwc.getParameter(DATE));
				getService(iwc).updateAllHandicaps(stamp);
			}
			else {
				getService(iwc).updateAllHandicaps();
			}
		}
		
		displayForm();			
	}

	protected void displayForm() {
		Form form = new Form();
		DateInput date = new DateInput(DATE);
		SubmitButton button = new SubmitButton(SUBMIT,"Update all handicaps");
		form.add(date);
		form.add(button);
		add(form);	
	}

	private HandicapService getService(IWContext iwc) {
		try {
			return (HandicapService) IBOLookup.getServiceInstance(iwc, HandicapService.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}