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
public class MissingCustodianException extends AccountingException {
	public MissingCustodianException(String key, String text) {
		super(key,text);
	}
}
