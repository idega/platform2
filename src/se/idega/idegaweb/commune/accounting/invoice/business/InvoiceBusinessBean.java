package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;

import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchiveHome;

/**
 * @author Joakim
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class InvoiceBusinessBean {
	
	public void createInvoicingData(Date startDate, Date endDate){
		
		ChildCareContractArchive contract;
		Collection archive = new ArrayList();
//		int days;

		//Flag all contracts as 'not processed'

		
		try {
			archive = getChildCareContractArchiveHome().findByDateRange(startDate, endDate);
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
			if(contract.getValidFromDate().before(startDate) && (contract.getValidFromDate().after(endDate))){
			}
			
			//If contract is used in the given period
				//Calc days placed in the given period
		}
	}

	public ChildCareContractArchiveHome getChildCareContractArchiveHome() throws RemoteException {
		return (ChildCareContractArchiveHome) IDOLookup.getHome(ChildCareContractArchive.class);
	}

}
