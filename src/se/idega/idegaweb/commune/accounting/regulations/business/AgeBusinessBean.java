/*
 * $Id: AgeBusinessBean.java,v 1.4 2003/09/02 13:57:32 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

import java.util.Collection;
import java.util.Iterator;
import java.sql.Date;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation;

/** 
 * Business logic for age values and regulations for children in childcare.
 * <p>
 * Last modified: $Date: 2003/09/02 13:57:32 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.4 $
 */
public class AgeBusinessBean extends com.idega.business.IBOServiceBean implements AgeBusiness  {

	private final static String KP = "age_error."; // key prefix 

	public final static String KEY_DATE_FORMAT = KP + "date_format";
	public final static String KEY_SEARCH_PERIOD_VALUES = KP + "search_period_value";
	public final static String KEY_PERIOD_VALUES = KP + "period_values";
	public final static String KEY_FROM_DATE_MISSING = KP + "from_date_missing";
	public final static String KEY_TO_DATE_MISSING = KP + "to_date_missing";
	public final static String KEY_AGE_FROM_MISSING = KP + "age_from_missing";
	public final static String KEY_AGE_FROM_VALUE = KP + "age_from_value";
	public final static String KEY_AGE_TO_MISSING = KP + "age_to_missing";
	public final static String KEY_AGE_TO_VALUE = KP + "age_to_value";
	public final static String KEY_AGE_VALUES = KP + "age_values";
	public final static String KEY_AGE_OVERLAP = KP + "age_overlap";
	public final static String KEY_DESCRIPTION_MISSING = KP + "description_missing";
//	public final static String KEY_CUT_DATE_MISSING = KP + "cut_date_missing";
	public final static String KEY_CANNOT_SAVE_AGE_REGULATION = KP + "cannot_save_age_regulation";
	public final static String KEY_CANNOT_DELETE_AGE_REGULATION = KP + "cannot_delete_age_regulation";
	public final static String KEY_CANNOT_FIND_AGE_REGULATION = KP + "cannot_find_age_regulation";

	public final static String DEFAULT_DATE_FORMAT = "Datum mŒste anges pŒ formen MM, MMDD, eller MMDD.";
	public final static String DEFAULT_PERIOD_VALUES = "Periodens startdatum mŒste vara mindre eller lika med slutdatum.";
	public final static String DEFAULT_SEARCH_PERIOD_VALUES = "Sškperiodens startdatum mŒste vara mindre eller lika med slutdatum.";
	public final static String DEFAULT_FROM_DATE_MISSING = "Periodens startdatum mŒste fyllas i.";
	public final static String DEFAULT_TO_DATE_MISSING = "Periodens slutdatum mŒste fyllas i.";
	public final static String DEFAULT_AGE_FROM_MISSING = "lder frŒn mŒste fyllas i.";
	public final static String DEFAULT_AGE_FROM_VALUE = "lder frŒn mŒste vara mellan 0 och 18.";
	public final static String DEFAULT_AGE_TO_MISSING = "lder till mŒste fyllas i.";
	public final static String DEFAULT_AGE_TO_VALUE = "lder till mŒste vara mellan 0 och 18.";
	public final static String DEFAULT_AGE_VALUES = "lder till mŒste vara mindre Šn Œlder frŒn.";
	public final static String DEFAULT_AGE_OVERLAP = "ldersintervall fŒr ej šverlappa. Det finns redan en regel inom detta intervall.";
	public final static String DEFAULT_DESCRIPTION_MISSING = "BenŠmning av Œldersregeln mŒste fyllas i.";
//	public final static String DEFAULT_CUT_DATE_MISSING = "Brytdatum mŒste fyllas i.";
	public final static String DEFAULT_CANNOT_SAVE_AGE_REGULATION = "ldersregeln kunde inte sparas pŒ grund av tekniskt fel.";
	public final static String DEFAULT_CANNOT_DELETE_AGE_REGULATION = "ldersregeln kunde inte tas bort pŒ grund av tekniskt fel.";
	public final static String DEFAULT_CANNOT_FIND_AGE_REGULATION = "Kan ej hitta Œldersregeln.";

	/**
	 * Return age regulation home. 
	 */	
	protected AgeRegulationHome getAgeRegulationHome() throws RemoteException {
		return (AgeRegulationHome) com.idega.data.IDOLookup.getHome(AgeRegulation.class);
	}	
	
	/**
	 * Finds all age regulations.
	 * @return collection of age regulation objects
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation 
	 */
	public Collection findAllAgeRegulations() {
		try {
			AgeRegulationHome home = getAgeRegulationHome();
			return home.findAll();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	
	
	/**
	 * Finds all age regulations for the specified period.
	 * The string values are used for exception handling only.
	 * @param periodFrom the start of the period
	 * @param periodTo the end of the period
	 * @param periodFromString the unparsed from date
	 * @param periodToString the unparsed to date
	 * @return collection of age regulation objects
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation
	 * @throws AgeException if invalid period parameters
	 */
	public Collection findAgeRegulations(
			Date periodFrom,
			Date periodTo,
			String periodFromString,
			String periodToString) throws AgeException {
		try {
			AgeRegulationHome home = getAgeRegulationHome();

			String s = periodFromString.trim();
			if (s != null) {
				if (!s.equals("")) {
					if (periodFrom == null) {
						throw new AgeException(KEY_DATE_FORMAT, DEFAULT_DATE_FORMAT);
					}
				}
			}

			s = periodToString.trim();
			if (s != null) {
				if (!s.equals("")) {
					if (periodTo == null) {
						throw new AgeException(KEY_DATE_FORMAT, DEFAULT_DATE_FORMAT);
					}
				}
			}
			
			if ((periodFrom != null) && (periodTo != null)) {
				if (periodFrom.getTime() > periodTo.getTime()) {
					throw new AgeException(KEY_SEARCH_PERIOD_VALUES, DEFAULT_SEARCH_PERIOD_VALUES);
				}
			}

			return home.findByPeriod(periodFrom, periodTo);
			
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	
	
	/**
	 * Saves an age regulation object.
	 * Creates a new persistent object if nescessary.
	 * @parame id the age regulation id
	 * @param periodFrom the start of the period
	 * @param periodTo the end of the period
	 * @param periodFromString the unparsed from date
	 * @param periodToString the unparsed to date
	 * @param ageFromString the age from value
	 * @param ageToString the age to value
	 * @param description the description of the age regulation
	 * @param cutDate the cut date
	 * @param cutDateString the unparsed cut date
	 * @throws AgeException if invalid parameters
	 */
	public void saveAgeRegulation(
			int id,
			Date periodFrom,
			Date periodTo,
			String periodFromString,
			String periodToString,
			String ageFromString,
			String ageToString,
			String description,
			Date cutDate,
			String cutDateString) throws AgeException {
				
		// Period from
		String s = periodFromString.trim();
		if (s.equals("")) {
			throw new AgeException(KEY_FROM_DATE_MISSING, DEFAULT_FROM_DATE_MISSING);
		} else {
			if (periodFrom == null) {
				throw new AgeException(KEY_DATE_FORMAT, DEFAULT_DATE_FORMAT);
			}
		}
		
		// Period to
		s = periodToString.trim();
		if (s.equals("")) {
			throw new AgeException(KEY_TO_DATE_MISSING, DEFAULT_TO_DATE_MISSING);
		} else {
			if (periodTo == null) {
				throw new AgeException(KEY_DATE_FORMAT, DEFAULT_DATE_FORMAT);
			}
		}
		
		if (periodFrom.getTime() > periodTo.getTime()) {
			throw new AgeException(KEY_PERIOD_VALUES, DEFAULT_PERIOD_VALUES);
		}
		
		// Age from
		s = ageFromString.trim();
		int ageFrom = 0;
		if (s.equals("")) { 
			throw new AgeException(KEY_AGE_FROM_MISSING, DEFAULT_AGE_FROM_MISSING);
		}
		try {
			ageFrom = Integer.parseInt(s); 
		} catch (NumberFormatException e) {
			throw new AgeException(KEY_AGE_FROM_VALUE, DEFAULT_AGE_FROM_VALUE);
		}
		if ((ageFrom < 0) || (ageFrom > 18)) {
			throw new AgeException(KEY_AGE_FROM_VALUE, DEFAULT_AGE_FROM_VALUE);
		}
		
		// Age to
		s = ageToString.trim();
		int ageTo = 0;
		if (s.equals("")) { 
			throw new AgeException(KEY_AGE_TO_MISSING, DEFAULT_AGE_TO_MISSING);
		}
		try {
			ageTo = Integer.parseInt(s); 
		} catch (NumberFormatException e) {
			throw new AgeException(KEY_AGE_TO_VALUE, DEFAULT_AGE_TO_VALUE);
		}
		if ((ageTo < 0) || (ageTo > 18)) {
			throw new AgeException(KEY_AGE_TO_VALUE, DEFAULT_AGE_TO_VALUE);
		}

		if (ageFrom >= ageTo) {
			throw new AgeException(KEY_AGE_VALUES, DEFAULT_AGE_VALUES);
		}

		Collection c = findAllAgeRegulations();
		Iterator iter = c.iterator();
		while (iter.hasNext()) {
			AgeRegulation ar = (AgeRegulation) iter.next();
			if (((ageFrom > ar.getAgeFrom()) && (ageFrom < ar.getAgeTo())) ||
					((ageTo > ar.getAgeFrom()) && (ageTo < ar.getAgeTo())) ||
					((ageFrom <= ar.getAgeFrom()) && (ageTo >= ar.getAgeTo()))) {
				throw new AgeException(KEY_AGE_OVERLAP, DEFAULT_AGE_OVERLAP);
			}
		}
		

		// Description
		s = description.trim();
		if (s.equals("")) {
			throw new AgeException(KEY_DESCRIPTION_MISSING, DEFAULT_DESCRIPTION_MISSING);
		} else {
			description = s;
		}
		
		// Cut date
		s = cutDateString.trim();
		if (!s.equals("") && (cutDate == null)) {
			throw new AgeException(KEY_DATE_FORMAT, DEFAULT_DATE_FORMAT);
		}
		
		try {
			AgeRegulationHome home = getAgeRegulationHome();
			AgeRegulation ar = null;
			try {
				ar = home.findByPrimaryKey(new Integer(id));
			} catch (FinderException e) {
				ar = home.create();
			}
			ar.setPeriodFrom(periodFrom);
			ar.setPeriodTo(periodTo);
			ar.setAgeFrom(ageFrom);
			ar.setAgeTo(ageTo);
			ar.setDescription(description);
			ar.setCutDate(cutDate);
			ar.store();
		} catch (RemoteException e) { 
			throw new AgeException(KEY_CANNOT_SAVE_AGE_REGULATION, DEFAULT_CANNOT_SAVE_AGE_REGULATION);
		} catch (CreateException e) { 
			throw new AgeException(KEY_CANNOT_SAVE_AGE_REGULATION, DEFAULT_CANNOT_SAVE_AGE_REGULATION);
		}		
	}
	
	/**
	 * Deletes the age regulation object with the specified id.
	 * @param id the age regulation id
	 * @throws AgeException if the age regulation could not be deleted
	 */ 
	public void deleteAgeRegulation(int id) throws AgeException {
		try {
			AgeRegulationHome home = getAgeRegulationHome();
			AgeRegulation ar = home.findByPrimaryKey(new Integer(id));
			ar.remove();
		} catch (RemoteException e) { 
			throw new AgeException(KEY_CANNOT_DELETE_AGE_REGULATION, DEFAULT_CANNOT_DELETE_AGE_REGULATION);
		} catch (FinderException e) { 
			throw new AgeException(KEY_CANNOT_DELETE_AGE_REGULATION, DEFAULT_CANNOT_DELETE_AGE_REGULATION);
		} catch (RemoveException e) { 
			throw new AgeException(KEY_CANNOT_DELETE_AGE_REGULATION, DEFAULT_CANNOT_DELETE_AGE_REGULATION);
		}		
	}
	
	/**
	 * Returns the age regulation with the specified id.
	 * @param id the age regulation id
	 * @throws AgeException if age regulation not found
	 */
	public AgeRegulation getAgeRegulation(int id) throws AgeException {
		AgeRegulation ar = null;
		try {
			AgeRegulationHome home = getAgeRegulationHome();
			ar = home.findByPrimaryKey(new Integer(id));
		} catch (RemoteException e) { 
			throw new AgeException(KEY_CANNOT_FIND_AGE_REGULATION, DEFAULT_CANNOT_FIND_AGE_REGULATION);
		} catch (FinderException e) { 
			throw new AgeException(KEY_CANNOT_FIND_AGE_REGULATION, DEFAULT_CANNOT_FIND_AGE_REGULATION);
		}
		
		return ar;		
	}
}
