/*
 * $Id: AccountingException.java,v 1.3 2004/01/06 14:03:15 tryggvil Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.business;


/**
 * Exception for errors in accounting business logic.
 * The exception encapsulates a text key and default text for
 * localization in the presentation layer.  
 * <p>
 * Last modified: $Date: 2004/01/06 14:03:15 $
 *
 * @author Anders Lindman
 * @version $Revision: 1.3 $
 */
public class AccountingException extends Exception {

	private String textKey = null;
	private String defaultText = null;
	
	/**
	 * Constructs an accounting exception with the specified text key and default text.
	 * @param textKey the text key for the error message
	 * @param defaultText the default text for the error message
	 */
	public AccountingException(String textKey, String defaultText) {
		super(textKey);
		this.textKey = textKey;
		this.defaultText = defaultText;
	}
	
	/**
	 * Constructs an accounting exception with the specified text key and default text.
	 * @param textKey the text key for the error message
	 * @param defaultText the default text for the error message
	 */
	public AccountingException(String textKey, String defaultText,Exception cause) {
		super(textKey,cause);
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
