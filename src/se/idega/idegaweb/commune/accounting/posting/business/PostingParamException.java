/*
 * $Id: PostingParamException.java,v 1.1 2003/08/21 15:25:27 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.posting.business;

/**
 * @author Kelly
 * 
 */
public class PostingParamException extends Exception {
	/**
	 * Exception to handle Posting paramerters exceptions 
	 */
	public PostingParamException(String message) {
		super(message);
	}
}
