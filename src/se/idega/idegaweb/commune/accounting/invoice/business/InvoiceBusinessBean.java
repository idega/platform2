package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.data.IDOLookup;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchiveHome;

/**
 * @author Joakim
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InvoiceBusinessBean {
	private static final String BATCH = "Härledd uppgift";

	public void createInvoicingData(Date startPeriod, Date endPeriod){
		
		ChildCareContractArchive contract;
		Collection archive = new ArrayList();
		User custodian;
		School provider;
		Date currentDate = new Date( new java.util.Date().getTime());
		float months;
		int days;
		IWTimestamp time, startTime, endTime;

		//**Flag all contracts as 'not processed'

		
		try {
			archive = getChildCareContractArchiveHome().findByDateRange(startPeriod, endPeriod);
		} catch (RemoteException e) {
			// TODO (JJ) 
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO (JJ) create feedback that no contracts were found
			e.printStackTrace();
		}
		Iterator iter = archive.iterator();

		//Loop through all contracts
		while(iter.hasNext())
		{
			contract = (ChildCareContractArchive)iter.next();
			
			try {
				//**Fetch invoice receiver
				custodian = contract.getApplication().getOwner();
				//**Fetch the reference at the provider
				provider = contract.getApplication().getProvider();
				//**Create the invoice header
				InvoiceHeader invoiceHeader = getInvoiceHeaderHome().create();
				//Fill in all the field available at this time
				//TODO (JJ) invoiceHeader.setMainActivity()
				invoiceHeader.setPeriod(startPeriod);
				invoiceHeader.setCustodianId(custodian);
				invoiceHeader.setReference(provider);//TODO (JJ) Check if this is right. Supposed to be "Responcible person cenrally = BUN"...
				invoiceHeader.setDateCreated(currentDate);
				invoiceHeader.setCreatedBy(BATCH);	//TODO (JJ) Find out how the localization should be done
				//TODO (JJ) invoiceHeader.setOwnPosting();
				//TODO (JJ) invoiceHeader.setDoublePosting();
				invoiceHeader.setTotalAmountWithoutVAT(0);
				invoiceHeader.setTotalVATAmount(0);

				//**Calculate how big part of time period this contract is valid for
//				if(contract.getValidFromDate().before(startPeriod) && (contract.getValidFromDate().after(endPeriod))){
//					months = 1;
//				} else {
					//first get the start date
					startTime = new IWTimestamp(contract.getValidFromDate());
					time = new IWTimestamp(startPeriod);
					startTime = startTime.isLater(startTime,time);
					//Then get end date
					endTime = new IWTimestamp(endPeriod);
					if(contract.getTerminatedDate()!=null){
						time = new IWTimestamp(contract.getTerminatedDate());
						endTime = endTime.isEarlier(endTime, time);
					}
					//calc the how many months are in the given time.
					months = endTime.getMonth() - startTime.getMonth() + (endTime.getYear()-startTime.getYear())*12;
					months += 1.0;
					months -= percentOfMonthDone(startTime);
					months -= 1.0 - percentOfMonthDone(endTime);
					days = IWTimestamp.getDaysBetween(startTime, endTime);
//				}
				
			


			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CreateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		}
	}
	
	private float percentOfMonthDone(IWTimestamp date){
		int daysInMonth;
		IWTimestamp firstDay, lastDay;

		firstDay = new IWTimestamp(date);
		firstDay.setDay(1);
		lastDay = new IWTimestamp(firstDay);
		lastDay.addMonths(1);
		daysInMonth = IWTimestamp.getDaysBetween(firstDay, lastDay);
		return (float)(date.getDay()-1)/(float)daysInMonth;
		
	}

	public ChildCareContractArchiveHome getChildCareContractArchiveHome() throws RemoteException {
		return (ChildCareContractArchiveHome) IDOLookup.getHome(ChildCareContractArchive.class);
	}

	public InvoiceHeaderHome getInvoiceHeaderHome() throws RemoteException {
		return (InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class);
	}

}
