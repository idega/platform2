/*
 * $Id: AgeException.java,v 1.1 2003/08/25 15:20:56 anders Exp $
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
 * Exception for data input errors and technical errors in AgeBusiness.  
 * <p>
 * Last modified: $Date: 2003/08/25 15:20:56 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class AgeException extends AccountingException {
	
	/**
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public AgeException(String textKey, String defaultText) {
		super(textKey, defaultText);
	}
}
