/*
 * Created on 2.4.2004
 */
package se.idega.idegaweb.commune.childcare.presentation.inputhandler;

import java.util.Collection;
import java.util.Collections;

import com.idega.business.InputHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DateInput;
import com.idega.util.IWTimestamp;


/**
 * @author laddi
 */
public class DateInputHandler implements InputHandler {

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayForResultingObject(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		if (value != null) {
			return value.toString();
		}
		return "";
	}
	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		DateInput input = new DateInput(name);
		input.setToDisplayDayLast(true);
		if (value != null) {
			input.setDate(new IWTimestamp(value).getDate());
		}
		IWTimestamp stamp = new IWTimestamp();
		input.setYearRange(stamp.getYear(), stamp.getYear() - 7);
		return input;
	}
	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getResultingObject(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public Object getResultingObject(String[] value, IWContext iwc) throws Exception {
		return value[0];
	}
	
	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}


	public Object convertSingleResultingObjectToType(Object value, String className) {
		return value;
	}
}
