/*
 * $Id: AgeBusinessBean.java,v 1.11 2003/10/10 09:48:34 anders Exp $
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
import java.util.ArrayList;
import java.sql.Date;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.util.IWTimestamp;

import se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation;

/** 
 * Business logic for age values and regulations for children in childcare.
 * <p>
 * Last modified: $Date: 2003/10/10 09:48:34 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.11 $
 */
public class AgeBusinessBean extends com.idega.business.IBOServiceBean implements AgeBusiness  {

	private final static String KP = "age_error."; // key prefix 

	public final static String KEY_DATE_FORMAT = KP + "date_format";
	public final static String KEY_SEARCH_PERIOD_VALUES = KP + "search_period_value";
	public final static String KEY_PERIOD_VALUES = KP + "period_values";
	public final static String KEY_OPERATIONAL_FIELD_MISSING = KP + "operational_field_missing";
	public final static String KEY_FROM_DATE_MISSING = KP + "from_date_missing";
	public final static String KEY_TO_DATE_MISSING = KP + "to_date_missing";
	public final static String KEY_AGE_FROM_MISSING = KP + "age_from_missing";
	public final static String KEY_AGE_FROM_VALUE = KP + "age_from_value";
	public final static String KEY_AGE_TO_MISSING = KP + "age_to_missing";
	public final static String KEY_AGE_TO_VALUE = KP + "age_to_value";
	public final static String KEY_AGE_VALUES = KP + "age_values";
	public final static String KEY_AGE_OVERLAP = KP + "age_overlap";
	public final static String KEY_NOT_ONE_YEAR_AGE_INTERVAL = KP + "not_one_year_age_interval";
	public final static String KEY_DESCRIPTION_MISSING = KP + "description_missing";
	public final static String KEY_CUT_DATE_FORMAT = KP + "cut_date_format";
	public final static String KEY_NO_CUT_DATE_TYPE = KP + "no_cut_date_type";
	public final static String KEY_CUT_DATE_MISSING = KP + "cut_date_missing";
	public final static String KEY_CANNOT_SAVE_AGE_REGULATION = KP + "cannot_save_age_regulation";
	public final static String KEY_CANNOT_DELETE_AGE_REGULATION = KP + "cannot_delete_age_regulation";
	public final static String KEY_CANNOT_FIND_AGE_REGULATION = KP + "cannot_find_age_regulation";

	public final static String DEFAULT_DATE_FORMAT = "Datum mŒste anges pŒ formen MM, MMDD, eller MMDD.";
	public final static String DEFAULT_PERIOD_VALUES = "Periodens startdatum mŒste vara mindre eller lika med slutdatum.";
	public final static String DEFAULT_SEARCH_PERIOD_VALUES = "Sškperiodens startdatum mŒste vara mindre eller lika med slutdatum.";
	public final static String DEFAULT_OPERATIONAL_FIELD_MISSING = "Huvudverksamhet mŒste v?ljas.";
	public final static String DEFAULT_FROM_DATE_MISSING = "Periodens startdatum mŒste fyllas i.";
	public final static String DEFAULT_TO_DATE_MISSING = "Periodens slutdatum mŒste fyllas i.";
	public final static String DEFAULT_AGE_FROM_MISSING = "lder frŒn mŒste fyllas i.";
	public final static String DEFAULT_AGE_FROM_VALUE = "lder frŒn mŒste vara mellan 0 och 18.";
	public final static String DEFAULT_AGE_TO_MISSING = "lder till mŒste fyllas i.";
	public final static String DEFAULT_AGE_TO_VALUE = "lder till mŒste vara mellan 0 och 18.";
	public final static String DEFAULT_AGE_VALUES = "lder till mŒste vara mindre Šn Œlder frŒn.";
	public final static String DEFAULT_AGE_OVERLAP = "ldersintervall fŒr ej šverlappa. Det finns redan en regel inom detta intervall.";
	public final static String DEFAULT_NOT_ONE_YEAR_AGE_INTERVAL = "ldersintervallet m?ste vara ett ?r mellan ?lder fr?n och ?lder till.";
	public final static String DEFAULT_DESCRIPTION_MISSING = "Regeltyp mŒste vŠljas.";
	public final static String DEFAULT_CUT_DATE_FORMAT = "Brytdatum mŒste anges p? formen MMDD.";
	public final static String DEFAULT_NO_CUT_DATE_TYPE = "Brytdatum kan endast anges n?r regeltyp brytdatum, aktuellt ?r har valts.";
	public final static String DEFAULT_CUT_DATE_MISSING = "Brytdatum mŒste fyllas i n?r regeltyp brytdatum, aktuellt ?r har valts.";
	public final static String DEFAULT_CANNOT_SAVE_AGE_REGULATION = "ldersregeln kunde inte sparas pŒ grund av tekniskt fel.";
	public final static String DEFAULT_CANNOT_DELETE_AGE_REGULATION = "ldersregeln kunde inte tas bort pŒ grund av tekniskt fel.";
	public final static String DEFAULT_CANNOT_FIND_AGE_REGULATION = "Kan ej hitta Œldersregeln.";

	private final static String RP = "age_rule."; // Rule type prefix
	
	private final static String RULE_MONTH_AFTER_BIRTH_DAY = RP + "month_after_birth_day";
	private final static String RULE_CUT_DATE = RP + "cut_date";
	 

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
	 * Finds all age regulations with the specified operational field.
	 * @param operationalField the operational field
	 * @return collection of age regulation objects
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation 
	 */
	public Collection findByOperationalField(String operationalField) {
		try {
			AgeRegulationHome home = getAgeRegulationHome();
			return home.findByCategory(operationalField);				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	
	
	/**
	 * Finds all age regulations for the specified period and operational field.
	 * The string values are used for exception handling only.
	 * @param operationalField the operational field
	 * @param periodFrom the start of the period
	 * @param periodTo the end of the period
	 * @param periodFromString the unparsed from date
	 * @param periodToString the unparsed to date
	 * @return collection of age regulation objects
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.AgeRegulation
	 * @throws AgeException if invalid period parameters
	 */
	public Collection findAgeRegulations(
			String operationalField,
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

			return home.findByPeriodAndCategory(periodFrom, periodTo, operationalField);
			
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	
	
	/**
	 * Saves an age regulation object.
	 * Creates a new persistent object if nescessary.
	 * @param id the age regulation id
	 * @param operationalField the operational field
	 * @param periodFrom the start of the period
	 * @param periodTo the end of the period
	 * @param periodFromString the unparsed from date
	 * @param periodToString the unparsed to date
	 * @param ageFromString the age from value
	 * @param ageToString the age to value
	 * @param ruleType the age rule type
	 * @param cutDate the cut date
	 * @param cutDateString the unparsed cut date
	 * @throws AgeException if invalid parameters
	 */
	public void saveAgeRegulation(
			int id,
			String operationalField,
			Date periodFrom,
			Date periodTo,
			String periodFromString,
			String periodToString,
			String ageFromString,
			String ageToString,
			String ruleType,
			Date cutDate,
			String cutDateString) throws AgeException {
				
		// Operational field
		String s = operationalField.trim();
		if (s.equals("")) {
			throw new AgeException(KEY_OPERATIONAL_FIELD_MISSING, DEFAULT_OPERATIONAL_FIELD_MISSING);
		} else {
			operationalField = s;
		}

		// Period from
		s = periodFromString.trim();
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

		Collection c = findByOperationalField(operationalField);
		Iterator iter = c.iterator();
		while (iter.hasNext()) {
			AgeRegulation ar = (AgeRegulation) iter.next();
			
			int arId = ((Integer) ar.getPrimaryKey()).intValue();
			if (id == arId) {
				continue;
			}
			
			if (((ageFrom > ar.getAgeFrom()) && (ageFrom < ar.getAgeTo())) ||
					((ageTo > ar.getAgeFrom()) && (ageTo < ar.getAgeTo())) ||
					((ageFrom <= ar.getAgeFrom()) && (ageTo >= ar.getAgeTo()))) {
				throw new AgeException(KEY_AGE_OVERLAP, DEFAULT_AGE_OVERLAP);
			}
		}
		if (ageTo - ageFrom != 1) {
			throw new AgeException(KEY_NOT_ONE_YEAR_AGE_INTERVAL, DEFAULT_NOT_ONE_YEAR_AGE_INTERVAL);
		}

		// Rule type
		s = ruleType.trim();
		if (s.equals("")) {
			throw new AgeException(KEY_DESCRIPTION_MISSING, DEFAULT_DESCRIPTION_MISSING);
		} else {
			ruleType = s;
		}
		
		// Cut date
		s = cutDateString.trim();
		if (!s.equals("") && (cutDate == null)) {
			throw new AgeException(KEY_CUT_DATE_FORMAT, DEFAULT_CUT_DATE_FORMAT);
		} else if (!s.equals("") && (cutDate != null) && (s.length() != 8)) {
			throw new AgeException(KEY_CUT_DATE_FORMAT, DEFAULT_CUT_DATE_FORMAT);
		}
		if ((cutDate != null) && (!ruleType.equals(RULE_CUT_DATE))) {
			throw new AgeException(KEY_NO_CUT_DATE_TYPE, DEFAULT_NO_CUT_DATE_TYPE);
		}
		if ((cutDate == null) && (ruleType.equals(RULE_CUT_DATE))) {
			throw new AgeException(KEY_CUT_DATE_MISSING, DEFAULT_CUT_DATE_MISSING);
		}
 		
		try {
			AgeRegulationHome home = getAgeRegulationHome();
			AgeRegulation ar = null;
			try {
				ar = home.findByPrimaryKey(new Integer(id));
			} catch (FinderException e) {
				ar = home.create();
			}
			ar.setCategory(operationalField);
			ar.setPeriodFrom(periodFrom);
			ar.setPeriodTo(periodTo);
			ar.setAgeFrom(ageFrom);
			ar.setAgeTo(ageTo);
			ar.setDescription(ruleType);
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
	
	/**
	 * Returns all age rule types.
	 */
	public Collection getAllAgeRuleTypes() {
		ArrayList l = new ArrayList();
		l.add(RULE_MONTH_AFTER_BIRTH_DAY);
		l.add(RULE_CUT_DATE);
		return l;
	}
	
	/**
	 * Returns the age of the child with the specified personal id according
	 * to the age rules/regulations.
	 * @param operationalField the operational field
	 * @param childPersonalId the child's personal id (format: 'yyyyMMddnnnn')
	 * @param date the calendar date to use for calculating the child's age
	 * @return the age of the child according to the age rules, -1 if no matching rule found
	 */
	public int getChildAge(String childPersonalId, Date date, String operationalField) {
		int childAgeAccordingToRegulations = -1;
		
		try {
			IWTimestamp calendarDate = new IWTimestamp(date);
			calendarDate.setAsDate();
			String s = childPersonalId;
			String sqlDateFormat = s.substring(0, 4) + "-" + s.substring(4, 6) + "-" + s.substring(6, 8);
			IWTimestamp childDate = new IWTimestamp(sqlDateFormat);
			childDate.setAsDate();
			int childMaxAge = calendarDate.getYear() - childDate.getYear();
			
			Collection ageRegulations = findByOperationalField(operationalField);
			Iterator iter = ageRegulations.iterator();
			AgeRegulation regulationToUse = null; 
			while (iter.hasNext()) {
				AgeRegulation ar = (AgeRegulation) iter.next();
				if (ar.getAgeTo() == childMaxAge) {
					regulationToUse = ar;
					break;
				}
			}
			childAgeAccordingToRegulations = childMaxAge;
			String rule = regulationToUse.getDescription(); 
			if (rule.equals(RULE_CUT_DATE)) {
				IWTimestamp cutDate = new IWTimestamp(regulationToUse.getCutDate());
				cutDate.setAsDate();
				cutDate.setYear(calendarDate.getYear());
				if (calendarDate.isEarlierThan(cutDate)) {
					childAgeAccordingToRegulations--;
				}
			} else if (rule.equals(RULE_MONTH_AFTER_BIRTH_DAY)) {
				if (childDate.getMonth() >= calendarDate.getMonth()) {
					childAgeAccordingToRegulations--;
				}
			}
		} catch (Exception e) {}
		
		return childAgeAccordingToRegulations;		
	}
	
	/**
	 * Returns the age of the child with the specified personal id according
	 * to the age rules/regulations. The operational field ChildCare is used.
	 * 
	 * @param childPersonalId the child's personal id (format: 'yyyyMMddnnnn')
	 * @param date the calendar date to use for calculating the child's age
	 * @return the age of the child according to the age rules, -1 if no matching rule found
	 */
	public int getChildAge(String childPersonalId, Date date) {
		String operationalField = "";
		try {
			com.idega.block.school.business.SchoolBusiness sb = (com.idega.block.school.business.SchoolBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), 
					com.idega.block.school.business.SchoolBusiness.class);
			com.idega.block.school.data.SchoolCategory sc = sb.getCategoryChildcare();
			operationalField = sc.getPrimaryKey().toString();
		} catch (RemoteException e) {}
		return getChildAge(childPersonalId, date, operationalField);	
	}
	
	/**
	 * Returns the age of the child with the specified personal id according
	 * to the age rules/regulations. The current date is used for comparing.
	 *  The operational field ChildCare is used.
	 * 
	 * @param childPersonalId the child's personal id (format: 'yyyyMMddnnnn')
	 * @return the age of the child according to the age rules, -1 if no matching rule found
	 */
	public int getChildAge(String childPersonalId) {
		return getChildAge(childPersonalId, new Date(System.currentTimeMillis()));
	}
	
	/**
	 * Returns the age of the child with the specified personal id according
	 * to the age rules/regulations. The current date is used for comparing.
	 * @param operationalField the operationalField
	 * @param childPersonalId the child's personal id (format: 'yyyyMMddnnnn')
	 * @return the age of the child according to the age rules, -1 if no matching rule found
	 */
	public int getChildAge(String childPersonalId, String operationalField) {
		return getChildAge(childPersonalId, new Date(System.currentTimeMillis()), operationalField);
	}
}
