/*
 * $Id: PostingBusinessBean.java,v 1.8 2003/08/25 21:40:54 kjell Exp $
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
import javax.ejb.CreateException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import com.idega.data.IDOLookup;
import com.idega.util.IWTimestamp;

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

	private final static String KEY_ERROR_POST_PARAM_ERROR1 = "posting_param_err.activity_id_missing";
	private final static String KEY_ERROR_POST_PARAM_ERROR2 = "posting_param_err.reg_spec_id_missing";
	private final static String KEY_ERROR_POST_PARAM_ERROR3 = "posting_param_err.company_id_missing";
	private final static String KEY_ERROR_POST_PARAM_ERROR4 = "posting_param_err.com_bel_id_missing";
	private final static String KEY_ERROR_POST_PARAM_ERROR5 = "posting_param_err.cant_create_post";

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

	public void savePostingParameter(String sppID,
				Date periodeFrom, 
				Date periodeTo,
				String changedSign,
				String activityID,
				String regSpecTypeID,
				String companyTypeID,
				String communeBelongingID,
				String ownAccount,
				String ownLiability,
				String ownResource,
				String ownActivityCode,
				String ownDoubleEntry,
				String ownActivity,
				String ownProject,
				String ownObject,
				String doubleAccount,
				String doubleLiability,
				String doubleResource,
				String doubleActivityCode,
				String doubleDoubleEntry,
				String doubleActivity,
				String doubleProject,
				String doubleObject
			) throws PostingParamException, RemoteException {

			PostingParametersHome home = null;
			PostingParameters pp = null;
										
			try {
				home = (PostingParametersHome) IDOLookup.getHome(PostingParameters.class);
	
				if (activityID == null) throw new PostingParamException(KEY_ERROR_POST_PARAM_ERROR1);
				if (regSpecTypeID == null) throw new PostingParamException(KEY_ERROR_POST_PARAM_ERROR2);
				if (companyTypeID == null) throw new PostingParamException(KEY_ERROR_POST_PARAM_ERROR3);
				if (communeBelongingID == null) throw new PostingParamException(KEY_ERROR_POST_PARAM_ERROR4);
//				if (sppID == null) throw new PostingParamException(KEY_ERROR_POST_PARAM_ERROR5);

				
				int ppID = 0;
				if(sppID != null) {
					ppID = Integer.parseInt(sppID);
				}
				if(ppID != 0) {				
					pp = home.findPostingParameter(ppID);
				} else {
					pp = null;
				}

			} catch (FinderException e) {
				pp = null;
			}
			
			try {
				if (pp == null) {
					pp = home.create();
				}
				pp.setPeriodeFrom(periodeFrom);
				pp.setPeriodeTo(periodeTo);
				pp.setChangedSign(changedSign);
				pp.setChangedDate(IWTimestamp.getTimestampRightNow());

				pp.setActivity(activityID != null ? Integer.parseInt(activityID) : 0);
				pp.setRegSpecType(regSpecTypeID != null ? Integer.parseInt(regSpecTypeID) : 0);
				pp.setCompanyType(companyTypeID != null ? Integer.parseInt(companyTypeID) : 0);
				pp.setCommuneBelonging(communeBelongingID != null ? Integer.parseInt(communeBelongingID) : 0);
				
				pp.setPostingAccount(ownAccount);
				pp.setPostingLiability(ownLiability);
				pp.setPostingResource(ownResource);
				pp.setPostingActivityCode(ownActivityCode);
				pp.setPostingDoubleEntry(ownDoubleEntry);
				pp.setPostingActivity(ownActivity);
				pp.setPostingProject(ownProject);
				pp.setPostingObject(ownObject);
	
				pp.setDoublePostingAccount(doubleAccount);
				pp.setDoublePostingLiability(doubleLiability);
				pp.setDoublePostingResource(doubleResource);
				pp.setDoublePostingActivityCode(doubleActivityCode);
				pp.setDoublePostingDoubleEntry(doubleDoubleEntry);
				pp.setDoublePostingActivity(doubleActivity);
				pp.setDoublePostingProject(doubleProject);
				pp.setDoublePostingObject(doubleObject);
									
				pp.store();
			} catch (CreateException ce) {
				throw new PostingParamException(KEY_ERROR_POST_PARAM_ERROR4);			
			}
		}


	/**
	 * Gets posting parameters for a certain periode
	 * @param from periode (4 digits)
	 * @param to periode (4 digits)
	 * @return collection of posting parameters
	 * @author Kjell
	 * 
	 */
	public Collection findPostingParametersByPeriode(Date from, Date to) {
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
	 * Gets a posting parameter by a periode
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters; 
	 * @return PostingParameters
	 * @author Kjell
	 */
	public Object findPostingParameterByPeriode(Date from, Date to) throws FinderException {
		// Move this
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
