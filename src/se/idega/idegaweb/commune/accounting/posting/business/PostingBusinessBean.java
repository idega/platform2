/*
 * $Id: PostingBusinessBean.java,v 1.6 2003/08/20 13:03:41 kjell Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.posting.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.posting.data.PostingFieldHome;
import se.idega.idegaweb.commune.accounting.posting.data.PostingString;
import se.idega.idegaweb.commune.accounting.posting.data.PostingStringHome;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParametersHome;

/**
 * @author Joakim
 * @author Kjell Lindman
 * 
 * 
 */
public class PostingBusinessBean extends com.idega.business.IBOServiceBean implements PostingBusiness  {
	
	public static final int JUSTIFY_LEFT = 0;
	public static final int JUSTIFY_RIGHT = 1;

	/**
	 * Merges two posting strings according to 15.2 and 15.3 in the Kravspecification Check & Peng
	 * @param first posting string
	 * @param second posting string
	 * @param date for valid time of posting rules
	 * @return the merged posting strings
	 * @throws RemoteException
	 */
	public String generateString(String first, String second, Date date) throws RemoteException {
		StringBuffer ret = new StringBuffer();		//used to build together returnstring
		String temp;
		int readPointer = 0;						//Pointer to place where next field is
		int fieldLength=0;							//Length of next field. Fetched from the definition 

		PostingStringHome ksHome = getPostingStringHome();
		PostingFieldHome kfHome = getPostingFieldHome();
		try {
			PostingString posting = ksHome.findPostingStringByDate(date);
			Collection list = kfHome.findAllFieldsByPostingString(Integer.parseInt(posting.getPrimaryKey().toString()));
			Iterator iter = list.iterator();
			while (iter.hasNext())
			{
				PostingField field = (PostingField)iter.next();
				fieldLength = field.getLen();
				temp = trim(first.substring(readPointer,readPointer+fieldLength),field);
				if(temp.length()==0)
				{
					temp = trim(second.substring(readPointer,readPointer+fieldLength),field);
				}
				temp = pad(temp,field);
				ret.append(temp);
				readPointer += fieldLength;
			}
			if(readPointer != first.length()){
				System.out.println("Error: Wrong length of the string used for posting. Expected: "+readPointer+
				"  Actual: "+first.length());
			}
		} catch (Exception e) {
			// Todo JJ Throw own exception
			System.out.println("Error: The postingt definition and the posting strings did not match.");
			System.out.println("First posting string: '"+first+"'");
			System.out.println("Second posting string:'"+second+"'");
			System.out.println("Date for posting rule: "+date.toString());
			e.printStackTrace();
		}
		return ret.toString();
	}

	/**
	 * Gets posting parameters for a certain periode
	 * @param from periode (4 digits)
	 * @param to periode (4 digits)
	 * @return collection of posting parameters
	 * @author Kjell
	 * 
	 */
	public Collection findPostingParametersByPeriode(String from, String to) {
		try {
			PostingParametersHome home = getPostingParametersHome();
			return home.findPostingParametersByPeriode(from, to);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	

	/**
	 * Gets all posting parameters
	 * @return collection of posting parameters
	 * @author Kjell
	 */
	public Collection findAllPostingParameters() {
		try {
			PostingParametersHome home = getPostingParametersHome();
			return home.findAllPostingParameters();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	
	
	/**
	 * Gets a posting parameter by the ID
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters; 
	 * @return PostingParameters
	 * @author Kjell
	 */
	public Object findPostingParameter(int id) throws FinderException {
		// Move this
		try {
			PostingParametersHome home = getPostingParametersHome();
			return home.findPostingParameter(id);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}

	/**
	 * Pads the string according to the rules in Posting field
	 * @param in the string to pad
	 * @param postingField holds the rules for padding
	 * @return the padded string
	 */
	private String pad(String in, PostingField postingField){
		StringBuffer ret = new StringBuffer(in);
		//Add the padding character on right side of the string until it is of right size
		if(postingField.getJustification() == JUSTIFY_LEFT) {
			while(ret.length()<postingField.getLen()){
				ret.append(postingField.getPadChar());
			}
		} else {
			while(ret.length()<postingField.getLen()){
				ret.insert(0,postingField.getPadChar());
			}
		}
		return ret.toString();
	}
	
	/**
	 * Trims the string according to the rules in Posting field
	 * @param in the string to trim
	 * @param postingField holds the rules for trimming 
	 * @return the trimmed string
	 */
	private String trim(String in, PostingField postingField) {
		String ret = "";
		int i;
		//Remove all padding characters until a non padding character is encountered
		if(postingField.getJustification() == JUSTIFY_LEFT){
			for(i=postingField.getLen();i>0 && in.charAt(i) == postingField.getPadChar();i++){}
			ret = in.substring(0,i);
		}else{
			for(i=0;i<postingField.getLen() && in.charAt(i) == postingField.getPadChar();i++){}
			ret = in.substring(i,postingField.getLen());
		}
		return ret;
	}

	protected PostingParametersHome getPostingParametersHome() throws RemoteException {
		return (PostingParametersHome) com.idega.data.IDOLookup.getHome(PostingParameters.class);
	}
	
	protected PostingStringHome getPostingStringHome() throws RemoteException
	{
		return (PostingStringHome) com.idega.data.IDOLookup.getHome(PostingString.class);
	}
	
	protected PostingFieldHome getPostingFieldHome() throws RemoteException
	{
		return (PostingFieldHome) com.idega.data.IDOLookup.getHome(PostingField.class);
	}
}
