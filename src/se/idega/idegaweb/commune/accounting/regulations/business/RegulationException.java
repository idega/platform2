/*
 * $Id: RegulationException.java,v 1.1 2003/08/29 00:53:16 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

import se.idega.idegaweb.commune.accounting.business.AccountingException;

/**
 * Exception for data input errors and technical errors in VATBusiness.  
 * <p>
 * Last modified: $Date: 2003/08/29 00:53:16 $
 *
 * @author Kjell Lindman
 * @version $Revision: 1.1 $
 */
public class RegulationException extends AccountingException {
	/**
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public RegulationException(String textKey, String defaultText) {
		super(textKey, defaultText);
	}
}
