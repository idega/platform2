/*
 * $Id: ReferenceNumberHandler.java,v 1.12.2.1 2006/10/18 13:53:58 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import com.idega.block.application.presentation.ReferenceNumber;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWBundle;
import com.idega.util.CypherText;

/**
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ReferenceNumberHandler implements IWPageEventListener {
	private static final String REFERENCE_NUMBER = "referenceNumber";

	private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.application";

	/**
	 * 
	 */
	public ReferenceNumberHandler() {
	}

	/**
	 * 
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		String ref = iwc.getParameter(ReferenceNumber.CAM_REF_NUMBER);
		if (ref.length() != 10) {
			System.out.println("Setting upp decyphered reference number :"
					+ ref);
			CypherText cyph = new CypherText(iwc);
			String cypherKey = getCypherKey(iwc);

			ref = cyph.doDeCypher(ref, cypherKey);
			iwc.setSessionAttribute(REFERENCE_NUMBER, ref);
			iwc.removeSessionAttribute("DUMMY_LOGIN");
		} else {
			System.out.println("Saving ssn to session");
			iwc.setSessionAttribute(REFERENCE_NUMBER, ref);
			iwc.setSessionAttribute("DUMMY_LOGIN", "true");
		}
		return true;
	}

	/**
	 * 
	 */
	public static String getCypherKey(IWApplicationContext iwc) {
		IWBundle iwb = iwc.getIWMainApplication().getBundle(
				IW_BUNDLE_IDENTIFIER);
		CypherText cyph = new CypherText(iwc);

		String cypherKey = iwb.getProperty("cypherKey");
		if ((cypherKey == null) || (cypherKey.equalsIgnoreCase(""))) {
			cypherKey = cyph.getKey(100);
			iwb.setProperty("cypherKey", cypherKey);
		}

		return (cypherKey);
	}

	public String getBundleIdentifier() {
		return (IW_BUNDLE_IDENTIFIER);
	}

	/**
	 * Gets the decyphered reference number from the IWContext.
	 * 
	 * @param iwc
	 *            The IWContext for the session
	 * @return
	 */
	public static String getReferenceNumber(IWContext iwc) {
		String ref = (String) iwc.getSessionAttribute(REFERENCE_NUMBER);
		// iwc.removeSessionAttribute(referenceNumber_);
		return (ref);
	}
}
