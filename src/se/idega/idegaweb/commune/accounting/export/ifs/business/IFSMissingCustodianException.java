/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.export.ifs.business;

import se.idega.idegaweb.commune.accounting.business.AccountingException;

/**
 * @author palli
 */
public class IFSMissingCustodianException extends AccountingException {
	public IFSMissingCustodianException(String key, String text) {
		super(key,text);
	}
}
