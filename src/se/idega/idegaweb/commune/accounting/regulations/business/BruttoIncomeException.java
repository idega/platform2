/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

import se.idega.idegaweb.commune.accounting.business.AccountingException;

/**
 * @author palli
 */
public class BruttoIncomeException extends AccountingException {
	/**
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public BruttoIncomeException(String textKey, String defaultText) {
		super(textKey, defaultText);
	}
}
