/*
 * $Id: StyleMultivaluedException.java,v 1.1 2004/09/16 10:25:24 laddi Exp $
 * Created on 13.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.style;


/**
 * 
 *  Last modified: $Date: 2004/09/16 10:25:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class StyleMultivaluedException extends IllegalAccessException {

	/**
	 * 
	 */
	public StyleMultivaluedException() {
		super("");
	}

	/**
	 * @param s
	 */
	public StyleMultivaluedException(String s) {
		super("StyleMultivaluedException: Style attribute has multiple values\n"+s);
	}

}
