/*
 * $Id: NoticeException.java,v 1.1 2003/09/01 11:08:28 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.message.business;

import se.idega.idegaweb.commune.accounting.business.AccountingException;

/**
 * Exception for data input errors and technical errors in NoticeBusiness.  
 * <p>
 * Last modified: $Date: 2003/09/01 11:08:28 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class NoticeException extends AccountingException {
	
	/**
	 * @see se.idega.idegaweb.commune.accounting.business.AccountingException
	 */
	public NoticeException(String textKey, String defaultText) {
		super(textKey, defaultText);
	}
}
