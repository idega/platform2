/*
 * Created on 2.4.2004
 */
package se.idega.idegaweb.commune.childcare.presentation.inputhandler;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DateInput;


/**
 * @author laddi
 */
public class DateInputHandler extends DateInput {

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		setToDisplayDayLast(true);
	}
}
