/*
 * $Id: ControlListBusinessBean.java,v 1.21 2004/10/14 10:22:27 thomas Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Iterator;
import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;

import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

/**
 * This business handles the logic to retrieve a control list after a batch run.
 * It basically gets all providers for an OperationField at a time. It then gets all contracts
 * for each provider to calulate the number of children involved. Then it gets the amount
 * from the payment records.
 * It does this for the "compare month" and "with month".
 * <p>
 * Last modified: $Date: 2004/10/14 10:22:27 $ by $Author: thomas $
 *
 * @author <a href="mailto:kjell@lindman.se">Kjell Lindman</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.21 $
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

		final Collection schools = new TreeSet (new Comparator () {
				final String getName (final Object o) {
					return ((School) o).getName ();
				}

				public int compare (final Object o1, final Object o2) {
					return getName (o1).compareToIgnoreCase (getName (o2));
				}

				public boolean equals (final Object o) {
					return o.hashCode () == hashCode ();
				}
			});
		for (Iterator i = getProvidersFromPaymentHeadersByPeriodAndSchoolCategory
						 (withMonth, opField).iterator(); i.hasNext ();) {
			schools.add (((PaymentHeader) i.next ()).getSchool ());
		}
		for (Iterator i = getProvidersFromPaymentHeadersByPeriodAndSchoolCategory
						 (compareMonth, opField).iterator(); i.hasNext ();) {
			schools.add (((PaymentHeader) i.next ()).getSchool ());
		}
		final InvoiceBusiness invoiceBusiness = getInvoiceBusiness ();
		//optimization by Sigtryggur. Caching of MainRule strings 
		Map mainRuleStringValues = new HashMap();
		for (Iterator i = schools.iterator (); i.hasNext ();) {
			final School school = (School) i.next ();
			long currentMonthIndividualsCount = 0;
			long compareMonthIndividualsCount = 0;
			long currentMonthTotalAmount = 0;
			long compareMonthTotalAmount = 0;

			final PaymentSummary currentSummary = getPaymentSummary (invoiceBusiness, opField, school, withMonth, mainRuleStringValues);
			currentMonthIndividualsCount = currentSummary.getIndividualsCount ();
			currentMonthTotalAmount = currentSummary.getTotalAmountVatExcluded () + currentSummary.getTotalAmountVat();
			final PaymentSummary compareSummary = getPaymentSummary (invoiceBusiness, opField, school, compareMonth, mainRuleStringValues);
			compareMonthIndividualsCount = compareSummary.getIndividualsCount ();
			compareMonthTotalAmount = compareSummary.getTotalAmountVatExcluded () + compareSummary.getTotalAmountVat();
			
			arr.add(new Object[] {
				new Integer(cnt++), 
				school.getName(), 
				new Long (currentMonthIndividualsCount),
				new Long (compareMonthIndividualsCount),
				new Long (currentMonthTotalAmount),
				new Long (compareMonthTotalAmount)
			});				
		}
		
		return arr;
	}

	private PaymentSummary getPaymentSummary
		(final InvoiceBusiness invoiceBusiness, final String schoolCategory,
		 final School school, final Date period, Map mainRuleStringValues) throws RemoteException {
		final PaymentRecord [] records
				= invoiceBusiness.getPaymentRecordsBySchoolCategoryAndProviderAndPeriod
				(schoolCategory, (Integer) school.getPrimaryKey (), period, period);
		final PaymentSummary result = new PaymentSummary (records, mainRuleStringValues);
		return result;		
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
