/*
 * $Id: VATBusinessBean.java,v 1.2 2003/08/24 22:35:38 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.regulations.business;

import java.util.Collection;
import java.sql.Date;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.regulations.data.VATRegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation;

/** 
 * Business logic for VAT values and regulations.
 * <p>
 * Last modified: $Date: 2003/08/24 22:35:38 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.2 $
 */
public class VATBusinessBean extends com.idega.business.IBOServiceBean implements VATBusiness  {

	/**
	 * Return VAT regulation home. 
	 */	
	protected VATRegulationHome getVATRegulationHome() throws RemoteException {
		return (VATRegulationHome) com.idega.data.IDOLookup.getHome(VATRegulation.class);
	}	
	
	/**
	 * Finds all VAT regulations.
	 * @return collection of VAT regulation objects
	 * @see se.idega.idegaweb.commune.accounting.regulations.data#VATRegulation 
	 * @author anders
	 */
	public Collection findAllVATRegulations() {
		try {
			VATRegulationHome home = getVATRegulationHome();
			return home.findAll();				
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
			return null;
		}
	}	
	
	/**
	 * Finds all VAT regulations for the specified period.
	 * The string values are used for exception handling only.
	 * @param periodFrom the start of the period
	 * @param periodTo the end of the period
	 * @param periodFromString the unparsed from date
	 * @param periodToString the unparsed to date
	 * @return collection of VAT regulation objects
	 * @see se.idega.idegaweb.commune.accounting.regulations.data#VATRegulation
	 * @throws VATException if invalid period parameters
	 * @author anders
	 */
	public Collection findVATRegulations(
			Date periodFrom,
			Date periodTo,
			String periodFromString,
			String periodToString) throws VATException {
		try {
			VATRegulationHome home = getVATRegulationHome();

			String s = periodFromString.trim();
			if (s != null) {
				if (!s.equals("")) {
					if (periodFrom == null) {
						throw new VATException(VATException.KEY_DATE_FORMAT, VATException.DEFAULT_DATE_FORMAT);
					}
				}
			}

			s = periodToString.trim();
			if (s != null) {
				if (!s.equals("")) {
					if (periodTo == null) {
						throw new VATException(VATException.KEY_DATE_FORMAT, VATException.DEFAULT_DATE_FORMAT);
					}
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
	 * Creates a new persistent VAT regulation record.
	 * @param periodFrom the start of the period
	 * @param periodTo the end of the period
	 * @param periodFromString the unparsed from date
	 * @param periodToString the unparsed to date
	 * @param descriptionString the description of the VAT regulation
	 * @param vatPercentString the VAT percent value
	 * @param paymentFlowTypeIdString the payment flow type id
	 * @param providerTypeIdString the provider type id
	 * @throws VATException if invalid parameters
	 */
	public void createVATRegulation(
			Date periodFrom,
			Date periodTo,
			String periodFromString,
			String periodToString,
			String description,
			String vatPercentString,
			String paymentFlowTypeIdString,
			String providerTypeIdString) throws VATException {
				
		// Period from
		String s = periodFromString.trim();
		if (s.equals("")) {
			throw new VATException(VATException.KEY_FROM_DATE_MISSING, VATException.DEFAULT_FROM_DATE_MISSING);
		} else {
			if (periodFrom == null) {
				throw new VATException(VATException.KEY_DATE_FORMAT, VATException.DEFAULT_DATE_FORMAT);
			}
		}
		
		// Period to
		s = periodToString.trim();
		if (s.equals("")) {
			throw new VATException(VATException.KEY_TO_DATE_MISSING, VATException.DEFAULT_TO_DATE_MISSING);
		} else {
			if (periodTo == null) {
				throw new VATException(VATException.KEY_DATE_FORMAT, VATException.DEFAULT_DATE_FORMAT);
			}
		}

		// Description
		s = description.trim();
		if (s.equals("")) {
			throw new VATException(VATException.KEY_DESCRIPTION_MISSING, VATException.DEFAULT_DESCRIPTION_MISSING);
		} else {
			description = s;
		}
		
		// VAT percent
		s = vatPercentString.trim();
		int vatPercent = 0;
		if (s.equals("")) { 
			throw new VATException(VATException.KEY_VAT_PERCENT_MISSING, VATException.DEFAULT_VAT_PERCENT_MISSING);
		}
		try {
			vatPercent = Integer.parseInt(s); 
		} catch (NumberFormatException e) {
			throw new VATException(VATException.KEY_VAT_PERCENT_VALUE, VATException.DEFAULT_VAT_PERCENT_VALUE);
		}
		if ((vatPercent < 0) || (vatPercent > 100)) {
			throw new VATException(VATException.KEY_VAT_PERCENT_VALUE, VATException.DEFAULT_VAT_PERCENT_VALUE);
		}

		// Payment flow type
		s = paymentFlowTypeIdString;
		if (s.equals("0")) {
			throw new VATException(VATException.KEY_PAYMENT_FLOW_TYPE_MISSING, VATException.DEFAULT_PAYMENT_FLOW_TYPE_MISSING);
		}
		int paymentFlowTypeId = Integer.parseInt(s);

		// Provider type
		s = providerTypeIdString;
		if (s.equals("0")) {
			throw new VATException(VATException.KEY_PROVIDER_TYPE_MISSING, VATException.DEFAULT_PROVIDER_TYPE_MISSING);
		}
		int providerTypeId = Integer.parseInt(s);
		
		try {
			VATRegulationHome home = getVATRegulationHome();
			VATRegulation vr = home.create();
			vr.setPeriodFrom(periodFrom);
			vr.setPeriodTo(periodTo);
			vr.setDescription(description);
			vr.setVATPercent(vatPercent);
			vr.setPaymentFlowTypeId(paymentFlowTypeId);
			vr.setProviderTypeId(providerTypeId);
			vr.store();
		} catch (RemoteException e) { 
			throw new VATException(VATException.KEY_CANNOT_SAVE_VAT_REGULATION, VATException.DEFAULT_CANNOT_SAVE_VAT_REGULATION);
		} catch (CreateException e) { 
			throw new VATException(VATException.KEY_CANNOT_SAVE_VAT_REGULATION, VATException.DEFAULT_CANNOT_SAVE_VAT_REGULATION);
		}		
	}
	
	/**
	 * Deletes the VAT regulation objects with the specified ids.
	 * @param vatRegulationIds the array of VAT regulation ids
	 * @throws VATException if a VAT regulation could not be deleted
	 */ 
	public void deleteVATRegulations(String[] vatRegulationIds) throws VATException {
		try {
			VATRegulationHome home = getVATRegulationHome();
			for (int i = 0; i < vatRegulationIds.length; i++) {
				int id = Integer.parseInt(vatRegulationIds[i]);
				VATRegulation vr = home.findByPrimaryKey(new Integer(id));
				vr.remove();
			}
		} catch (RemoteException e) { 
			throw new VATException(VATException.KEY_CANNOT_DELETE_VAT_REGULATION, VATException.DEFAULT_CANNOT_DELETE_VAT_REGULATION);
		} catch (FinderException e) { 
			throw new VATException(VATException.KEY_CANNOT_DELETE_VAT_REGULATION, VATException.DEFAULT_CANNOT_DELETE_VAT_REGULATION);
		} catch (RemoveException e) { 
			throw new VATException(VATException.KEY_CANNOT_DELETE_VAT_REGULATION, VATException.DEFAULT_CANNOT_DELETE_VAT_REGULATION);
		}		
	}
}
