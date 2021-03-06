/*
 * $Id: VATException.java,v 1.5 2004/01/06 14:03:15 tryggvil Exp $
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
 * Last modified: $Date: 2004/01/06 14:03:15 $
 *
 * @author Anders Lindman
 * @version $Revision: 1.5 $
 */
public class VATException extends AccountingException {
	
	/**
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public VATException(String textKey, String defaultText) {
		super(textKey, defaultText);
	}
	/**
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public VATException(String textKey, String defaultText,Exception cause) {
		super(textKey, defaultText,cause);
	}
	
	
}
