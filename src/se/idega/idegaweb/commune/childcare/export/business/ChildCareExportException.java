/*
 * $Id: ChildCareExportException.java,v 1.1 2005/01/12 13:11:54 anders Exp $
 *
 * Copyright (C) 2005 Idega. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf & Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.export.business;

/**
 * Exception for errors in child care export business logic.
 * The exception encapsulates a localization key and default text for
 * localization in the presentation layer.  
 * <p>
 * Last modified: $Date: 2005/01/12 13:11:54 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class ChildCareExportException extends Exception {

	private String _localizationKey = null;
	private String _defaultText = null;

	/**
	 * Constructs a new ChildCareExportException with the specified localization key and default text.
	 * @param localizationKey the localization key for the error message
	 * @param defaultText the default text for the error message
	 */
	public ChildCareExportException(String localizationKey, String defaultText) {
		super(defaultText);
		_localizationKey = localizationKey;
		_defaultText = defaultText;
	}
	
	/**
	 * Returns the error message text key.
	 */
	public String getTextKey() {
		return _localizationKey; 
	}
	
	/**
	 * Returns the default error message text.
	 */
	public String getDefaultText() {
		return _defaultText; 
	}
}
