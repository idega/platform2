package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.util.IWTimestamp;

import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchiveHome;

/**
 * @author Joakim
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InvoiceBusinessBean {
	
	public void createInvoicingData(Date startPeriod, Date endPeriod){
		
		ChildCareContractArchive contract;
		Collection archive = new ArrayList();
		float months;
		IWTimestamp time, startTime, endTime;

		//Flag all contracts as 'not processed'

		
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

		//Fetch the reference at the provider


		//Create the invoice header

		
		//Loop through all contracts
		while(iter.hasNext())
		{
			contract = (ChildCareContractArchive)iter.next();
			
			//Calculate how big part of time period this contract is valid for
			if(contract.getValidFromDate().before(startPeriod) && (contract.getValidFromDate().after(endPeriod))){
				months = 1;
			} else {
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
			}
			
			//If contract is used in the given period
				//Calc days placed in the given period
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

}
