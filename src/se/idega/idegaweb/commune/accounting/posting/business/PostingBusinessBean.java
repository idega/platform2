/*
 * $Id: PostingBusinessBean.java,v 1.12 2003/08/28 12:53:21 kjell Exp $
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

import se.idega.idegaweb.commune.accounting.regulations.data.ActivityTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;

import se.idega.idegaweb.commune.accounting.posting.data.PostingField;
import se.idega.idegaweb.commune.accounting.posting.data.PostingFieldHome;
import se.idega.idegaweb.commune.accounting.posting.data.PostingString;
import se.idega.idegaweb.commune.accounting.posting.data.PostingStringHome;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParametersHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;

/**
 * @author Joakim
 * @author Kjell Lindman
 * 
 * 
 */
public class PostingBusinessBean extends com.idega.business.IBOServiceBean implements PostingBusiness  {
	
	public static final int JUSTIFY_LEFT = 0;
	public static final int JUSTIFY_RIGHT = 1;

	private final static String KEY_ERROR_POST_PARAM_CREATE = "posting_param_err.create";
	private final static String KEY_ERROR_POST_NOT_FOUND = "posting_parm_edit.post_not_found";
	private final static String KEY_ERROR_POST_PARAM_DATE_ORDER = "posting_parm_edit.post_dateorder";
	private final static String KEY_ERROR_POST_PARAM_SAME_ENTRY = "posting_parm_edit.post_sameentry";
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
	 * Hämta konteringsinformation
	 * Retrieves accounting default information. Matching on the main rules like 
	 * Verksamhet, Regspectyp, Bolagstyp, kommuntill.hörighet is done via keys to avoid 
	 * localisation problems in the selectors. 
	 * 
	 * The example keys I added here are just for "Check och Peng" education/childcare but could easily be 
	 * extened to use other areas such as elderly care etc etc. And of course localized.
	 * We store the keys in the data bean and these are retrived from the regulation framework
	 * @see se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness#
	 *    
	 * @param date Date when the transaction took place
	 * 
	 * @param activity String containing an identifyer for the activity examples are:
	 * Skola, Förskola or "". Matching is done on keys: school, pre_school or ""
	 *  
	 * @param regSpecType String containing an identifyer for the regulations specifications type 
	 * examples are:
	 * Check, Modersmål or "". Matching is done on keys: check, modersmal, ""
	 *
	 * @param companyType String containing an identifyer for the company type 
	 * examples are:
	 * Kommun, Stiftelse, AB, Övriga företag or "". Matching is done on keys:   
	 * "kommun", "stiftelse", "ab", "ovr_foretag" or ""
	 * 
	 * @param communeBelonging String containing an identifyer for the commune belonging 
	 * examples are:
	 * Nacka, EJ Nacka or "". Matching is done on keys:  "nacka", "ej_nacka" or ""
	 * 
	 * @return PostingParameters @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters#
	 * @throws PostingParametersException
	 * 
	 * @author Kjell
	 */
	public PostingParameters getPostingParameter(Date date, String act_key, String reg_key, String com_key, String com_bel_key) throws PostingParametersException {
		
		try {
			int match;
			ActivityTypeHome ath = getActivityTypeHome();
			PostingParametersHome home = getPostingParametersHome();
			
			Collection ppCol = (Collection) home.findPostingParametersByDate(date);
			Iterator iter = ppCol.iterator();

			while(iter.hasNext())  {
				PostingParameters pp = (PostingParameters) iter.next();
				String the_act_key = pp.getActivity() != null ? 
						pp.getActivity().getActivityType() : "";
				String the_reg_key = pp.getRegSpecType() != null ? 
						pp.getRegSpecType().getRegSpecType() : "";
				String the_com_key = pp.getCompanyType() != null ? 
						pp.getCompanyType().getCompanyType() : "";
				String the_com_bel_key = pp.getCommuneBelonging() != null ? 
						pp.getCommuneBelonging().getCommuneBelongingType() : "";
				
				match = 0;
				
				if (the_act_key.indexOf("blank") != -1) 
					match++;
				if (the_reg_key.indexOf("blank") != -1)
					match++;	  					
				if (the_com_key.indexOf("blank") != -1)
					match++;	  					
				if (the_com_bel_key.indexOf("blank") != -1)
					match++;	  		
								
				if(act_key.compareToIgnoreCase(the_act_key) != -1 && act_key.length() != 0) 
					match++;
				if(reg_key.compareToIgnoreCase(the_reg_key) != -1 && reg_key.length() != 0) 
					match++;
				if(com_key.compareToIgnoreCase(the_com_key) != -1 && com_key.length() != 0) 
					match++;
				if(com_bel_key.compareToIgnoreCase(the_com_bel_key) != -1 && com_bel_key.length() != 0) 
					match++;
				
				if (match == 4) {
					return pp;
				}
			}
			throw new PostingParametersException(KEY_ERROR_POST_NOT_FOUND, "Sökt parameter hittades ej");
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	


	public void savePostingParameter(String sppID,
				Date periodeFrom, 
				Date periodeTo,
				String changedSign,
				String activityID,
				String regSpecTypeID,
				String companyTypeID,
				String communeBelongingID,
				String ownPostingString,
				String doublePostingString
			) throws PostingParametersException, RemoteException {

			PostingParametersHome home = null;
			PostingParameters pp = null;
			int parm1 = 0;
			int parm2 = 0;
			int parm3 = 0;
			int parm4 = 0;
			

			if (periodeFrom.after(periodeTo)) {
				throw new PostingParametersException(KEY_ERROR_POST_PARAM_DATE_ORDER, "Från datum kan ej vara senare än tom datum!");			
			}
		
			try {
				home = (PostingParametersHome) IDOLookup.getHome(PostingParameters.class);
	
				if(activityID == null) activityID = "1"; 
				if(regSpecTypeID == null) regSpecTypeID = "1"; 
				if(companyTypeID == null) companyTypeID = "1"; 
				if(communeBelongingID == null) communeBelongingID = "1"; 
				
				if(activityID.indexOf("0") != -1) activityID = "1"; 
				if(regSpecTypeID.indexOf("0") != -1) regSpecTypeID = "1"; 
				if(companyTypeID.indexOf("0") != -1) companyTypeID = "1"; 
				if(communeBelongingID.indexOf("0") != -1) communeBelongingID = "1"; 

				parm1 = Integer.parseInt(activityID);
				parm2 = Integer.parseInt(regSpecTypeID);
				parm3 = Integer.parseInt(companyTypeID);
				parm4 = Integer.parseInt(communeBelongingID);

				if(searchPP(
						periodeFrom, 
						periodeTo, 
						ownPostingString, 
						doublePostingString,
						parm1,
						parm2,
						parm3,
						parm4
					)) {
					throw new PostingParametersException(KEY_ERROR_POST_PARAM_SAME_ENTRY, "Denna post finns redan sparad!");			
				}
				

				int ppID = 0;
				if(sppID != null) {
					ppID = Integer.parseInt(sppID);
				}
				pp = null;
				if(ppID != 0) {				
					int eq = 0;
					pp = home.findPostingParameter(ppID);
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

				pp.setActivity(parm1);
				pp.setRegSpecType(parm2);
				pp.setCompanyType(parm3);
				pp.setCommuneBelonging(parm4);
				pp.store();
			} catch (CreateException ce) {
				throw new PostingParametersException(KEY_ERROR_POST_PARAM_CREATE, "Kan ej skapa parameter");			
			}
		}

	/*
	 * Compares a Posting Parameter with stored parameters
	 * @author Kjell
	 */
	private boolean searchPP(Date from, Date to, String ownPosting, String doublePosting, 
								int code1, int code2, int code3, int code4) {
	
		try {
			int match;
			ActivityTypeHome ath = getActivityTypeHome();
			PostingParametersHome home = getPostingParametersHome();
			Collection ppCol = (Collection) home.findAllPostingParameters();
			Iterator iter = ppCol.iterator();
	
			while(iter.hasNext())  {
				PostingParameters pp = (PostingParameters) iter.next();
				int eq = 0;
				if (pp.getPostingString().compareTo(ownPosting.trim()) == 0) {
					eq++;
				}
				if (pp.getDoublePostingString().compareTo(doublePosting.trim()) == 0) {
					eq++;
				}
				if (pp.getPeriodeFrom() != null) {
					if(pp.getPeriodeFrom().compareTo(from) == 0) {
						eq++;
					}
				}
				if (pp.getPeriodeTo() != null) {
					if(pp.getPeriodeTo().compareTo(to) == 0) {
						eq++;
					}
				}
				if (Integer.parseInt(pp.getActivity().getPrimaryKey().toString()) == code1) {
					eq++;				
				}	
				if (Integer.parseInt(pp.getRegSpecType().getPrimaryKey().toString()) == code2) {
					eq++;				
				}	
				if (Integer.parseInt(pp.getCompanyType().getPrimaryKey().toString()) == code3) {
					eq++;				
				}	
				if (Integer.parseInt(pp.getCommuneBelonging().getPrimaryKey().toString()) == code4) {
					eq++;				
				}	
				if(eq == 8) {
					return true;
				}
			}
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}
		return false;
	}

	/**
	 * Deletes a posting parameter
	 * @param id PostingParameter ID
	 * @author Kjell
	 * 
	 */
	public void deletePostingParameter(int ppID) throws java.rmi.RemoteException {
		try {
			PostingParameters pp = (PostingParameters) findPostingParameter(ppID);
			pp.remove();
			pp.store();	
		} catch (Exception e) {
			e.printStackTrace(System.err);
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
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters# 
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
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingParameters# 
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
	 * Gets a posting fields for a specific date
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingField# 
	 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingString# 
	 * @return PostingField
	 * @author Kjell
	 */
	public Collection getAllPostingFieldsByDate(Date date) {
		try {
			PostingStringHome psHome = getPostingStringHome();
			PostingFieldHome pfHome = getPostingFieldHome();
			PostingString ps = psHome.findPostingStringByDate(date); 
			int psID = Integer.parseInt(ps.getPrimaryKey().toString());
			return pfHome.findAllFieldsByPostingString(psID);				
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

	protected ActivityTypeHome getActivityTypeHome() throws RemoteException {
		return (ActivityTypeHome) com.idega.data.IDOLookup.getHome(ActivityType.class);
	}

}
