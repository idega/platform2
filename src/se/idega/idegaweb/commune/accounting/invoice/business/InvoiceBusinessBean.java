package se.idega.idegaweb.commune.accounting.invoice.business;
/*
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessHome;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.regulations.data.KeyMapping;
import se.idega.idegaweb.commune.accounting.regulations.data.KeyMappingBMPBean;
import se.idega.idegaweb.commune.accounting.regulations.data.KeyMappingHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchiveHome;

import com.idega.block.school.data.School;
import com.idega.data.IDOLookup;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
*/
/**
 * @author Joakim
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class InvoiceBusinessBean {
/*
	private static final String BATCH_TEXT = "Härledd uppgift";		//Localize this text in the user interface
	private static final String HOURS_PER_WEEK = "tim/v";		//Localize this text in the user interface
	
	public void createInvoicingData(Date startPeriod, Date endPeriod, String createdBy){
		
		ChildCareContractArchive contract;
		Collection archive = new ArrayList();
		User custodian;
		School provider;
		Date currentDate = new Date( new java.util.Date().getTime());
		float months;
		int days;
		float totalSum;
		IWTimestamp time, startTime, endTime;
		try {
			//TODO (JJ) Probably need to remove the ref to KeyMappingBMPBean. But to what???
			int childcare = getKeyMappingHome().findValueByCategoryAndKey(
				KeyMappingBMPBean.CAT_ACTIVITY,KeyMappingBMPBean.KEY_CHILDCARE).getValue();
			int check = getKeyMappingHome().findValueByCategoryAndKey(
				KeyMappingBMPBean.CAT_REG_SPEC,KeyMappingBMPBean.KEY_CHECK).getValue();
	
			// **Flag all contracts as 'not processed'
	
			try {
				archive = getChildCareContractArchiveHome().findByDateRange(startPeriod, endPeriod);
			} catch (RemoteException e) {
				// TODO (JJ) 
				e.printStackTrace();
			} catch (FinderException e) {
				// TODO (JJ) create feedback that no contracts were found
				e.printStackTrace();
			}
			Iterator archiveIter = archive.iterator();
	
			//Loop through all contracts
			while(archiveIter.hasNext())
			{
				contract = (ChildCareContractArchive)archiveIter.next();
			
				// **Fetch invoice receiver
				custodian = contract.getApplication().getOwner();
				//**Fetch the reference at the provider
				provider = contract.getApplication().getProvider();
				// **Create the invoice header
				//TODO (JJ) This should not always be done! Sometimes the header might already be created...
				InvoiceHeader invoiceHeader = getInvoiceHeaderHome().create();
				//Fill in all the field available at this time
				//TODO (JJ) invoiceHeader.setMainActivity()
				invoiceHeader.setPeriod(startPeriod);
				invoiceHeader.setCustodianId(custodian);
				invoiceHeader.setReference(provider);//TODO (JJ) Check if this is right. Supposed to be "Responcible person cenrally = BUN"...
				invoiceHeader.setDateCreated(currentDate);
				invoiceHeader.setCreatedBy(createdBy);
				invoiceHeader.setCreatedBy(BATCH_TEXT);
				//TODO (JJ) invoiceHeader.setOwnPosting();
				//TODO (JJ) invoiceHeader.setDoublePosting();
				invoiceHeader.setTotalAmountWithoutVAT(0);
				invoiceHeader.setTotalVATAmount(0);

				// **Calculate how big part of time period this contract is valid for
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


				totalSum = 0;
				//Get all the rules for this contract
				//TODO (JJ) This is a func that Thomas will provide.
				Rule rule = (Rule)ruleIter.next();
				// **Create the invoice record
				InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
				totalSum = rule.getAmount();
				invoiceRecord.setAmount(totalSum);
				invoiceRecord.setInvoiceText(provider.getName()+", "+contract.getCareTime()+" "+HOURS_PER_WEEK);
				invoiceRecord.setRuleText(rule.getText());
				//TODO (JJ) get the "huvudverksamhet" object that laddi will create.
				invoiceRecord.setDateCreated(currentDate);
				invoiceRecord.setCreatedBy(createdBy);
				invoiceRecord.setCreatedBy(BATCH_TEXT);
				//TODO (JJ) Create a "utbetalningspost" waiting for description from Lotta
				//TODO (JJ) set the reference to utbetalningsposten
				invoiceRecord.setInvoiceHeader(invoiceHeader);
				//TODO (JJ) Some VAT stuff needed here...

				PostingBusiness postingBusiness = getPostingBusinessHome().create();
				PostingParameters parameters = postingBusiness.getPostingParameter(
					new Date(new java.util.Date().getTime()), childcare, check, 0, 0);
				invoiceRecord.setOwnPosting(parameters.getPostingString());

				//TODO (JJ) This is a func that Thomas will provide.
				Iterator ruleIter = rule.iterator();
				while(ruleIter.hasNext()){
//					Rule rule = (Rule)ruleIter.next();
					totalSum -= rule.getAmount();
				}
				//Make sure that the sum is not less than 0
				if(totalSum<0){
//					subvention -= totalSum;
					//TODO (JJ) have to create some sort of record reference to the subvention row.
				}

		
			}
				
		} catch (RemoteException e1) {
			// TODO (JJ) Auto-generated catch block
			e1.printStackTrace();
		} catch (CreateException e) {
			// TODO (JJ) Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public InvoiceRecordHome getInvoiceRecordHome() throws RemoteException {
		return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
	}

	public KeyMappingHome getKeyMappingHome() throws RemoteException {
		return (KeyMappingHome) IDOLookup.getHome(KeyMapping.class);
	}

	public PostingBusinessHome getPostingBusinessHome() throws RemoteException {
		return (PostingBusinessHome) IDOLookup.getHome(PostingBusiness.class);
	}
*/
}
