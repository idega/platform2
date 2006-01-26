/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.export.business;

import se.idega.idegaweb.commune.accounting.business.AccountingException;

/**
 * @author palli
 */
public class MissingPostalCodeException extends AccountingException {

	/**
	 * @param textKey
	 * @param defaultText
	 */
	public MissingPostalCodeException(String textKey, String defaultText) {
		super(textKey, defaultText);
		// TODO Auto-generated constructor stub
	}

}
