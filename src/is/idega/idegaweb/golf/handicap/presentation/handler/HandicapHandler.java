/*
 * $Id: HandicapHandler.java,v 1.2 2005/02/07 13:56:06 laddi Exp $
 * Created on 7.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.handicap.presentation.handler;

import java.util.Collection;
import java.util.Collections;

import com.idega.business.InputHandler;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.TextInput;


/**
 * Last modified: $Date: 2005/02/07 13:56:06 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class HandicapHandler extends TextInput implements InputHandler {

	protected static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setAsFloat(iwrb.getLocalizedString("handicap_report.not_a_valid_handicap", "Not a valid handicap."), 1);
		setLength(4);
		setMaxlength(4);
		
		super.main(iwc);
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#getBundleIdentifier()
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.lang.String, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
		setName(name);
		if (value != null) {
			setValue(value);
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getHandlerObject(java.lang.String, java.util.Collection, com.idega.presentation.IWContext)
	 */
	public PresentationObject getHandlerObject(String name, Collection values, IWContext iwc) {
		String value = (String) Collections.min(values);
		return getHandlerObject(name, value, iwc);
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getResultingObject(java.lang.String[], com.idega.presentation.IWContext)
	 */
	public Object getResultingObject(String[] value, IWContext iwc) throws Exception {
		if (value != null && value.length > 0) {
			return new Float(value[0].toString());
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#getDisplayForResultingObject(java.lang.Object, com.idega.presentation.IWContext)
	 */
	public String getDisplayForResultingObject(Object value, IWContext iwc) {
		return value.toString();
	}

	/* (non-Javadoc)
	 * @see com.idega.business.InputHandler#convertSingleResultingObjectToType(java.lang.Object, java.lang.String)
	 */
	public Object convertSingleResultingObjectToType(Object value, String className) {
		if (value != null) {
			return new Float(value.toString());
		}
		return null;
	}
}