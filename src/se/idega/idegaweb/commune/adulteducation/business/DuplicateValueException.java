/*
 * $Id: DuplicateValueException.java,v 1.1 2005/05/11 07:16:22 laddi Exp $
 * Created on 27.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;


public class DuplicateValueException extends Exception {

	public DuplicateValueException() {
		super("");
	}

	public DuplicateValueException(String s) {
		super("[DuplicateValueException]: Value already exists in the database\n"+s);
	}
}