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

/**
 * A class containing some common functions for working with Swedish social 
 * security numbers.
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class SSNChecker {
	private static SSNChecker _instance = null;
	
	/**
	 * Does very little today.....
	 */
	private SSNChecker() {
		
	}

	/**
	 * A method to get an instance of this class.
	 * 
	 * @return An instance of the SSNChecker class.
	 */	
	public static SSNChecker getInstance() {
		if (_instance == null)
			_instance = new SSNChecker();
			
		return _instance;
	}
	
	
	/**
	 * A method to check if a Swedish social security number is valid.
	 * 
	 * @param ssn A string representation of the ssn. Can be of the form
	 *            [XX]XXXXXX[-]XXXX
	 * @return true if the ssn is valid, false otherwise.
	 */
	public boolean isValid(String ssn) {
		ssn = trimSSN(ssn);
		
		if (ssn.length() != 10)
			return false;
	
		StringBuffer buffer = new StringBuffer(ssn);
	
		int values[] = {0,0,0,0,0,0,0,0,0,0};
		int sum = 0;
		for (int i = 0; i < 10; i++) {
			values[i] = ssn.charAt(i) - '0';
			
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
	private String trimSSN(String ssn) {
		StringBuffer localSSN = new StringBuffer(ssn);
		int i = ssn.indexOf('-');
		if (i != -1) {
			localSSN.deleteCharAt(i);
		}
		
		if (localSSN.length() == 12)
			ssn = localSSN.substring(2);
		else
			ssn = localSSN.toString();
		
		return ssn;
	}	
	
	public boolean isFemale(String ssn) {
		ssn = trimSSN(ssn);
		if (ssn.length() != 10) 
			return false;
	
		int sex = ssn.charAt(8) - '0';
			
		if (sex % 2 == 0)
			return true;
		else
			return false;
	}
	
	public boolean isMale(String ssn) {
		return !isFemale(ssn);
	}
	
	public static void main(String blabla[]) {
		SSNChecker checker = SSNChecker.getInstance();
		
		System.out.println("Checking ssn = " + blabla[0]);
		if (checker.isValid(blabla[0]))
			System.out.println(blabla[0] + " is valid");
		else
			System.out.println(blabla[0] + " is not valid");
	}
}