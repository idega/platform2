/*
 * $Id: ControlListBusinessBean.java,v 1.14 2004/02/16 10:09:28 staffan Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import java.sql.Date;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;

import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;

/**
 * This business handles the logic to retrieve a control list after a batch run.
 * It basically gets all providers for an OperationField at a time. It then gets all contracts
 * for each provider to calulate the number of children involved. Then it gets the amount
 * from the payment records.
 * It does this for the "compare month" and "with month".
 * <p>
 * <p>
 * Last modified: $Date: 2004/02/16 10:09:28 $ by $Author: staffan $
 *
 * @author <a href="mailto:kjell@lindman.se">Kjell Lindman</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.14 $
 *
 */
public class ControlListBusinessBean extends IBOServiceBean implements ControlListBusiness {

	private final static String KP = "control_list."; // key prefix 

	public final static String KEY_DATE_MISSING = KP + "date_missing";
	public final static String ERROR_DATE_MISSING = "Date missing";

	/**
	 * Retreives an array of values such as
	 * Provider name, 
	 * Number of individuals preliminary this period
	 * Number of individuals preliminary last period
	 * Total amount paid this period 
	 * Total amount paid last period
	 * An empty list is returned if nothing found.
	 *
	 * @return array of data for the ControlList
	 */
	public Collection getControlListValues(Date compareMonth, Date withMonth, String opField) throws ControlListException, RemoteException {
		Iterator providers = null;
		ArrayList arr = new ArrayList();


		if(compareMonth == null) {		
			throw new ControlListException(KEY_DATE_MISSING, ERROR_DATE_MISSING);
		}
		
		if(withMonth == null) {		
			throw new ControlListException(KEY_DATE_MISSING, ERROR_DATE_MISSING);
		}

		int cnt = 1;
		
		arr.add(new Object[] {
			new Integer(cnt++), 
			"", 
			""+withMonth.toString(),
			""+compareMonth.toString(),
			""+withMonth.toString(),
			""+compareMonth.toString()}
		);

		providers = getProvidersFromPaymentHeadersByPeriodAndSchoolCategory(
				withMonth,
				opField).iterator();
		
		final InvoiceBusiness invoiceBusiness = getInvoiceBusiness ();

		while (providers.hasNext()) {
			PaymentHeader ph = (PaymentHeader) providers.next();
			final School school = ph.getSchool ();
			long currentMonthIndividualsCount = 0;
			long compareMonthIndividualsCount = 0;
			long currentMonthTotalAmount = 0;
			long compareMonthTotalAmount = 0;

			try {				
				final PaymentSummary currentSummary = getPaymentSummary (invoiceBusiness, opField, school, withMonth);
				currentMonthIndividualsCount = currentSummary.getIndividualsCount ();
				currentMonthTotalAmount = currentSummary.getTotalAmountVatExcluded ();
				final PaymentSummary compareSummary = getPaymentSummary (invoiceBusiness, opField, school, compareMonth);
				compareMonthIndividualsCount = compareSummary.getIndividualsCount ();
				compareMonthTotalAmount = compareSummary.getTotalAmountVatExcluded ();
			} catch (FinderException e) {
				// continue with initial values, but logg error
				e.printStackTrace ();
			}
			
			arr.add(new Object[] {
				new Integer(cnt++), 
				school.getName(), 
				""+currentMonthIndividualsCount,
				""+compareMonthIndividualsCount,
				""+currentMonthTotalAmount,
				""+compareMonthTotalAmount
			});				
		}
		
		return arr;
	}

	private PaymentSummary getPaymentSummary
		(final InvoiceBusiness invoiceBusiness, final String schoolCategory,
		 final School school, final Date period) throws RemoteException,
																										FinderException {
		return new PaymentSummary
				(invoiceBusiness.getPaymentRecordsBySchoolCategoryAndProviderAndPeriod
				 (schoolCategory, (Integer) school.getPrimaryKey (), period, period));
	}

	private Collection getProvidersFromPaymentHeadersByPeriodAndSchoolCategory(Date from, String sc) {
		Collection providers = new ArrayList();		
		try {
			try {
				PaymentHeaderHome home = getPaymentHeaderHome();				
				providers = home.findBySchoolCategoryAndPeriod(sc, from);
			} catch (FinderException ex) {
				ex.printStackTrace();
			}			
		} catch(RemoteException ex) {
			ex.printStackTrace();
		}
		return providers;
	}
		 
	protected ChildCareContractHome getChildCareContractHome() throws RemoteException {
			return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
	}

	protected PaymentHeaderHome getPaymentHeaderHome() throws RemoteException {
			return (PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class);
	}

	protected PaymentRecordHome getPaymentRecordHome() throws RemoteException {
			return (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
	}

	protected SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
	}


	protected InvoiceBusiness getInvoiceBusiness() throws RemoteException {
		return (InvoiceBusiness) this.getServiceInstance(InvoiceBusiness.class);
	}
}
