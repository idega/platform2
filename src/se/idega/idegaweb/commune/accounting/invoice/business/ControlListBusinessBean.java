/*
 * $Id: ControlListBusinessBean.java,v 1.7 2003/11/01 10:12:00 kjell Exp $
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
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.School;

import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;

/**
 * This business handles the logic to retrieve a control list after a batch run.
 * It basically gets all providers for an OperationField at a time. It then gets all contracts
 * for each provider to calulate the number of children involved. Then it gets the amount
 * from the payment records.
 * It does this for the "compare month" and "with month".
 * <p>
 * $Id: ControlListBusinessBean.java,v 1.7 2003/11/01 10:12:00 kjell Exp $
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
	public Collection getControlListValues(Date compareMonth, Date withMonth) throws ControlListException {
		
		int contractCountCurrent;
		int contractCountLast;
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

	
//		Date currentDate = new Date( System.currentTimeMillis());
		Date currentDate = withMonth;

/*
		startLastPeriod = new IWTimestamp(currentDate);
		startLastPeriod.setAsDate();
		startLastPeriod.addMonths(-1);
		startLastPeriod.setDay(1);
*/
		startLastPeriod = new IWTimestamp(compareMonth);
		startLastPeriod.setAsDate();
		startLastPeriod.setDay(1);
		
		endLastPeriod = new IWTimestamp(startLastPeriod);
		endLastPeriod.setAsDate();
		endLastPeriod.addMonths(1);

		startCurrentPeriod = new IWTimestamp(currentDate);
		startCurrentPeriod.setAsDate();
		startCurrentPeriod.setDay(1);

/*
		endCurrentPeriod = new IWTimestamp(currentDate);
		endCurrentPeriod.setAsDate();
*/
		endCurrentPeriod = new IWTimestamp(startCurrentPeriod);
		endCurrentPeriod.setAsDate();
		endCurrentPeriod.addMonths(1);


		Iterator operationFields = getAllOperationFields().iterator();
		int cnt = 1;
		
		arr.add(new Object[] {
			new Integer(cnt++), 
			"", 
			""+withMonth.toString(),
			""+compareMonth.toString(),
			""+withMonth.toString(),
			""+compareMonth.toString()}
		);
		while (operationFields.hasNext()) {
			SchoolCategory opField = (SchoolCategory) operationFields.next();
			providers = getProvidersByOperationField(opField.getPrimaryKey().toString()).iterator();
			while (providers.hasNext()) {
				School school = (School) providers.next();
				System.out.println("Processing: "+school.getName()+ " : id : "+school.getPrimaryKey().toString());
				try {				
					contractCountLast = 
						getChildCareContractHome().getContractsCountByDateRangeAndProvider(
						startLastPeriod.getDate(), 
						endLastPeriod.getDate(), 
						Integer.parseInt(school.getPrimaryKey().toString())
					);
	
					amountLast = 
						getPaymentRecordHome().getTotAmountForProviderAndPeriod(
						Integer.parseInt(school.getPrimaryKey().toString()),
						startLastPeriod.getDate()
					);
					
	
					contractCountCurrent = 
						getChildCareContractHome().getContractsCountByDateRangeAndProvider(
						startCurrentPeriod.getDate(), 
						endCurrentPeriod.getDate(), 
						Integer.parseInt(school.getPrimaryKey().toString())
					);
	
					amountCurrent = 
						getPaymentRecordHome().getTotAmountForProviderAndPeriod(
						Integer.parseInt(school.getPrimaryKey().toString()),
						startCurrentPeriod.getDate()
					);
	
					if ((contractCountCurrent > 0 || contractCountLast > 0) || 
						(amountCurrent > 0 || amountLast > 0)) { 
						arr.add(new Object[] {
							new Integer(cnt++), 
							school.getName(), 
							""+contractCountCurrent,
							""+contractCountLast,
							""+amountCurrent,
							""+amountLast }
						);
					}
				} catch (FinderException e) {
					e.printStackTrace();
				} catch (IDOException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		return arr;
	}
		
	private Collection getAllOperationFields() {
		Collection operationFields = new ArrayList();		
		try {
			SchoolBusiness schoolBusiness = getSchoolBusiness();
			operationFields = schoolBusiness.getSchoolCategoryHome().findAllCategories();
		} catch (FinderException ex) {
			ex.printStackTrace();
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}
		return operationFields;			
	}
	
	private Collection getProvidersByOperationField(String opField) {
		Collection providers = new ArrayList();		
		try {
			SchoolBusiness schoolBusiness = getSchoolBusiness();
			try {
				SchoolCategory sc = schoolBusiness.getSchoolCategoryHome().findByPrimaryKey(opField);
				SchoolHome home = (SchoolHome) IDOLookup.getHome(School.class);				
				providers = home.findAllByCategory(sc);
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

	protected PaymentRecordHome getPaymentRecordHome() throws RemoteException {
			return (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
	}

	protected SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
	}


}