/*
 * $Id: VATException.java,v 1.3 2003/08/25 14:41:24 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

/**
 * Exception for data input errors in VATBusiness.  
 * <p>
 * Last modified: $Date: 2003/08/25 14:41:24 $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class VATException extends Exception {


	private String textKey = null;
	private String defaultText = null;
	
	/**
	 * Constructs a VAT exception with the specified text key and default text.
	 * @param textKey the text key for the error message
	 * @param defaultText the default text for the error message
	 */
	public VATException(String textKey, String defaultText) {
		this.textKey = textKey;
		this.defaultText = defaultText;
	}
	
	/**
	 * Returns the error message text key.
	 */
	public String getTextKey() {
		return textKey; 
	}
	
	/**
	 * Returns the default error message text.
	 */
	public String getDefaultText() {
		return defaultText; 
	}
}
