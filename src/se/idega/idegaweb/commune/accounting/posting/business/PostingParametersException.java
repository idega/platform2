/*
 * $Id: PostingParametersException.java,v 1.1 2003/08/27 07:35:53 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.posting.business;
import se.idega.idegaweb.commune.accounting.business.AccountingException;

/**
 * @author Kelly
 * 
 */
public class PostingParametersException extends AccountingException {
	/**
	 * Exception to handle Posting paramerters exceptions 
	 */
	public PostingParametersException(String textKey, String defaultText) {
		super(textKey, defaultText);
	}
}
