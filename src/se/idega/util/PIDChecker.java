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

import java.sql.Date;

import com.idega.util.IWTimestamp;

/**
 * A class containing some common functions for working with
 * PersonalIds - Swedish social security numbers (Personnummer)
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
		return isValid(personalID, false);
	}

	/**
	 * A method to check if a Swedish social security number is valid.
	 * 
	 * @param ssn A string representation of the ssn. Can be of the form
	 *            [XX]XXXXXX[-]XXXX
	 * @return true if the ssn is valid, false otherwise.
	 */
	public boolean isValid(String personalID, boolean allowTemporary) {
		personalID = trimSSN(personalID);

		if (personalID.length() != 10)
			return false;

		if (allowTemporary && personalID.indexOf("TF") != -1){
				return true;
		}
		else {
			int values[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
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
	
			return (sum % 10 == 0);
		}
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

		return (sex % 2 == 0);
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

	
	/**
	 * The main method to run methods of this class.
	 * If no parameters are given it returns a random generated PID.
	 * If a parameter is given it tries to validate it as a personalId if the length is larger than 4.
	 * If the length of the parameter is 2 or 4 it accepts it as a year and generates a random PID for that year.
	 */	
	public static void main(String blabla[]) {
		PIDChecker checker = PIDChecker.getInstance();
		if (blabla.length > 0) {
			String value = blabla[0];
			if (value.length() == 4 || value.length() == 2) {
				int year = Integer.parseInt(value);
				System.out.println("Generating a random PersonalID born in year: " + year);
				String pid = checker.getRandomValidPID(Integer.toString(year));
				System.out.println("Random valid PID : " + pid);
			} else {
				System.out.println("Checking ssn = " + value);
				if (checker.isValid(blabla[0])) {
					System.out.println(blabla[0] + " is valid");
					if (checker.isFemale(blabla[0]))
						System.out.println(blabla[0] + " belongs to a female");
					else
						System.out.println(blabla[0] + " belongs to a male");
				} else
					System.out.println(blabla[0] + " is not valid");
			}
		} else {
			System.out.println("Generating a random PersonalID:");
			String pid = checker.getRandomValidPID();
			System.out.println("Random valid PID : " + pid);

		}

	}



	/**
	 * A method to convert a personal ID string to <code>Date</code>.
	 * @param personalID	The personal ID to convert to date.
	 * @return Date	Returns null if personal ID is not valid.
	 */
	public Date getDateFromPersonalID(String personalID) {
		if (isValid(personalID, true)) {
			int year = 0;
			int month = 0;
			int day = 0;

			if (personalID.length() == 10) {
				year = Integer.parseInt(personalID.substring(0, 2)) + 1900;
				month = Integer.parseInt(personalID.substring(2, 4));
				day = Integer.parseInt(personalID.substring(4, 6));
			} else if (personalID.length() == 12) {
				year = Integer.parseInt(personalID.substring(0, 4));
				month = Integer.parseInt(personalID.substring(4, 6));
				day = Integer.parseInt(personalID.substring(6, 8));
			}

			IWTimestamp stamp = new IWTimestamp(day, month, year);
			return stamp.getDate();
		}
		return null;
	}
	/** 
	 * Get a random valid PersonalID as string representation of in the form XXXXXX-XXXX
	 */
	private String getRandomValidPID() {
		String randomYearString = getRandomIntDecimalString() + getRandomIntDecimalString();
		return getRandomValidPID(randomYearString);
	}
	
	/** 
	 * Get a random valid PersonalID as string representation of in the form XXXXXX-XXXX, with the year provided by yearString (either 2 or 4 in length)
	 */
	public String getRandomValidPID(String yearString) {
		String tryString = "";
		while (true) {
			String shortYearString = yearString;
			if(yearString.length()== 4){
				shortYearString = yearString.substring(2);
			}
			tryString = shortYearString + getRandomMonthString() + getRandomMonthDayString() + "-" + getRandomIntDecimalString() + getRandomIntDecimalString() + getRandomIntDecimalString() + getRandomIntDecimalString();
			//System.out.println("Trying: "+tryString);
			if (isValid(tryString)) {
				return tryString;
			}
		}
		//throw new RuntimeException("No valid PID found");	
	}

	/**
	 * Gets a random int on the range from 0-9 as a String
	 * @return
	 */
	public String getRandomIntDecimalString() {
		return Integer.toString(getRandomIntDecimal());
	}

	/**
	 * Gets a random int on the range from 0-9
	 * @return
	 */
	public int getRandomIntDecimal() {
		int theReturn = (int)Math.round(10 * Math.random() - 1);
		while (theReturn > 9 || theReturn < 0) {
			theReturn = theReturn = (int)Math.round(10 * Math.random() - 1);
		}
		return theReturn;
	}

	/**
	 * Gets a random int on the range from 1-12 as a double digit String (0 in front i number is lower than 10)
	 * @return
	 */
	public String getRandomMonthString() {
		int month = getRandomMonth();
		if (month < 10) {
			return "0" + month;
		} else {
			return Integer.toString(month);
		}
	}

	/**
	 * Gets a random int on the range from 1-12
	 * @return
	 */
	public int getRandomMonth() {
		int theReturn = (int)Math.round(12 * Math.random()) + 1;
		while (theReturn > 12) {
			theReturn = (int)Math.round(12 * Math.random()) + 1;
		}
		return theReturn;
	}

	/**
	 * Gets a random int on the range from 1-28 as a double digit String (0 in front i number is lower than 10)
	 * @return
	 */
	public String getRandomMonthDayString() {
		int monthday = getRandomMonthDay();
		if (monthday < 10) {
			return "0" + monthday;
		} else {
			return Integer.toString(monthday);
		}
	}

	/**
	 * Gets a random int on the range from 1-28 
	 * @return
	 */
	public int getRandomMonthDay() {
		int theReturn = (int)Math.round(28 * Math.random()) + 1;
		while (theReturn > 28) {
			theReturn = theReturn = (int)Math.round(28 * Math.random()) + 1;
		}
		return theReturn;
	}

}
