/*
 * $Id: ProviderException.java,v 1.1 2003/09/17 09:29:04 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.school.business;

import se.idega.idegaweb.commune.accounting.business.AccountingException;

/**
 * Exception for data input errors and technical errors in ProviderBusiness.  
 * <p>
 * Last modified: $Date: 2003/09/17 09:29:04 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class ProviderException extends AccountingException {
	
	/**
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public ProviderException(String textKey, String defaultText) {
		super(textKey, defaultText);
	}
}
