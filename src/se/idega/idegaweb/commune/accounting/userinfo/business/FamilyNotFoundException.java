/*
 * Created on Dec 18, 2003
 *
 */
package se.idega.idegaweb.commune.accounting.userinfo.business;

import com.idega.user.data.User;

/**
 * FamilyNotFoundException
 * @author aron 
 * @version 1.0
 */
public class FamilyNotFoundException extends Exception {
	
	
	/**
	 * 
	 */
	public FamilyNotFoundException(User user) {
		this("Family for user "+user.getPrimaryKey());
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public FamilyNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
