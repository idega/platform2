/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.util;

import java.util.Date;

import com.idega.util.IWTimestamp;

/**
 * A class containing some common functions for working with Swedish social 
 * security numbers.
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class PIDChecker {
	private static PIDChecker _instance = null;
	
	/**
	 * Does very little today.....
	 */
	private PIDChecker() {
		
	}

	/**
	 * A method to get an instance of this class.
	 * 
	 * @return An instance of the SSNChecker class.
	 */	
	public static PIDChecker getInstance() {
		if (_instance == null)
			_instance = new PIDChecker();
			
		return _instance;
	}
	
	
	/**
	 * A method to check if a Swedish social security number is valid.
	 * 
	 * @param ssn A string representation of the ssn. Can be of the form
	 *            [XX]XXXXXX[-]XXXX
	 * @return true if the ssn is valid, false otherwise.
	 */
	public boolean isValid(String personalID) {
		personalID = trimSSN(personalID);
		
		if (personalID.length() != 10)
			return false;
	
		StringBuffer buffer = new StringBuffer(personalID);
	
		int values[] = {0,0,0,0,0,0,0,0,0,0};
		int sum = 0;
		for (int i = 0; i < 10; i++) {
			values[i] = personalID.charAt(i) - '0';
			
			if (i % 2 == 0) {
				values[i] *= 2;
				if (values[i] > 9)
					values[i] -= 9;
			}
			
			sum += values[i];
		}
		
		if (sum % 10 == 0)			
			return true;
		else
			return false;
	}
	
	/**
	 * A method that removes all minus signs from the ssn, and also removes the 
	 * first two digits if the ssn length equals 12.
	 * 
	 * @return A trimmed down version of the ssn.
	 */
	private String trimSSN(String personalID) {
		StringBuffer localSSN = new StringBuffer(personalID);
		int i = personalID.indexOf('-');
		if (i != -1) {
			localSSN.deleteCharAt(i);
		}
		
		if (localSSN.length() == 12)
			personalID = localSSN.substring(2);
		else
			personalID = localSSN.toString();
		
		return personalID;
	}	
	
	/**
	 * A method that checks if the owner of a ssn is female.
	 * 
	 * @param ssn A string representation of the ssn. Can be of the form
	 *            [XX]XXXXXX[-]XXXX
	 * @return true if the owner is female, false otherwise. Also returns false if the ssn is invalid.
	 */
	public boolean isFemale(String personalID) {
		personalID = trimSSN(personalID);
		if (personalID.length() != 10) 
			return false;
	
		int sex = personalID.charAt(8) - '0';
			
		if (sex % 2 == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * A method that checks if the owner of a ssn is male.
	 * 
	 * @param ssn A string representation of the ssn. Can be of the form
	 *            [XX]XXXXXX[-]XXXX
	 * @return true if the owner is male, false otherwise. Also returns false if the ssn is invalid.
	 */
	public boolean isMale(String personalID) {
		return !isFemale(personalID);
	}
	
	public static void main(String blabla[]) {
		PIDChecker checker = PIDChecker.getInstance();
		
		System.out.println("Checking ssn = " + blabla[0]);
		if (checker.isValid(blabla[0])) {
			System.out.println(blabla[0] + " is valid");
			if (checker.isFemale(blabla[0]))
				System.out.println(blabla[0] + " belongs to a female");
			else
				System.out.println(blabla[0] + " belongs to a male");
		}
		else
			System.out.println(blabla[0] + " is not valid");
	}
	
	/**
	 * A method to convert a personal ID string to <code>Date</code>.
	 * @param personalID	The personal ID to convert to date.
	 * @return Date	Returns null if personal ID is not valid.
	 */
	public Date getDateFromPersonalID(String personalID) {
  	if ( isValid(personalID) ) {
      int year = Integer.parseInt(personalID.substring(0, 2));
      int month = Integer.parseInt(personalID.substring(2, 4));
      int day = Integer.parseInt(personalID.substring(4, 6));
				
			IWTimestamp stamp = new IWTimestamp(day,month,year);
			return stamp.getDate();
  	}
  	return null;
	}
}