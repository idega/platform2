/*
 * $Id: NoAbbreviationException.java,v 1.1 2005/05/31 11:45:34 eiki Exp $
 * Created on May 16, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.business;

import javax.ejb.FinderException;


public class NoAbbreviationException extends FinderException {

	public NoAbbreviationException(String groupName) {
		super("No abbreviation found for group "+groupName);
	}
}
