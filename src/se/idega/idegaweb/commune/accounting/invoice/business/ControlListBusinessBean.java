/*
 * $Id: ControlListBusinessBean.java,v 1.11 2004/02/11 21:43:35 kjell Exp $
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

import com.idega.business.IBOServiceBean;
import com.idega.util.IWTimestamp;
import com.idega.data.IDOLookup;
import com.idega.data.IDOException;
import com.idega.block.school.business.SchoolBusiness;

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
 * $Id: ControlListBusinessBean.java,v 1.11 2004/02/11 21:43:35 kjell Exp $
 *
 * @author Kelly
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
	public Collection getControlListValues(Date compareMonth, Date withMonth, String opField) throws ControlListException {
		
		int childrenCountCurrent;
		int childrenCountLast;
		int amountCurrent;
		int amountLast;

		IWTimestamp startLastPeriod;
		IWTimestamp endLastPeriod;
		IWTimestamp startCurrentPeriod;
		IWTimestamp endCurrentPeriod;
		Iterator providers = null;
		ArrayList arr = new ArrayList();


		if(compareMonth == null) {		
			throw new ControlListException(KEY_DATE_MISSING, ERROR_DATE_MISSING);
		}
		
		if(withMonth == null) {		
			throw new ControlListException(KEY_DATE_MISSING, ERROR_DATE_MISSING);
		}

	
		Date currentDate = withMonth;

		startLastPeriod = new IWTimestamp(compareMonth);
		startLastPeriod.setAsDate();
		startLastPeriod.setDay(1);
		
		endLastPeriod = new IWTimestamp(startLastPeriod);
		endLastPeriod.setAsDate();
		endLastPeriod.addMonths(1);

		startCurrentPeriod = new IWTimestamp(currentDate);
		startCurrentPeriod.setAsDate();
		startCurrentPeriod.setDay(1);

		endCurrentPeriod = new IWTimestamp(startCurrentPeriod);
		endCurrentPeriod.setAsDate();
		endCurrentPeriod.addMonths(1);


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
				startCurrentPeriod.getDate(),
				opField).iterator();
		
		while (providers.hasNext()) {
			PaymentHeader ph = (PaymentHeader) providers.next();
			
			try {				
				amountCurrent = getPaymentRecordHome().getTotAmountForProviderAndPeriod(
					ph.getSchoolID(),
					startCurrentPeriod.getDate(), opField
				);
				amountLast = getPaymentRecordHome().getTotAmountForProviderAndPeriod(
					ph.getSchoolID(),
					startLastPeriod.getDate(), opField
				);
				childrenCountCurrent = 
					getPaymentHeaderHome().getPlacementCountForSchoolAndPeriod(
					ph.getSchoolID(),
					startCurrentPeriod.getDate()
				);
				childrenCountLast = 
					getPaymentHeaderHome().getPlacementCountForSchoolAndPeriod(
					ph.getSchoolID(),
					startLastPeriod.getDate()
				);
				arr.add(new Object[] {
					new Integer(cnt++), 
					getSchoolBusiness().getSchoolHome().findByPrimaryKey(new Integer(ph.getSchoolID())).getName(), 
					""+childrenCountCurrent,
					""+childrenCountLast,
					""+amountCurrent,
					""+amountLast
					}
				);
			} catch (FinderException e) {
				e.printStackTrace();
			} catch (IDOException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
				
		}

		return arr;
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


}