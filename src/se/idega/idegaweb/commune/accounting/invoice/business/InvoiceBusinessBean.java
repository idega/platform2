package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.invoice.data.BatchRun;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunHome;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;

/**
 * Holds most of the logic for the batchjob that creates the information that is base for invoicing 
 * and payment data, that is sent to external finance system. Now moved to InvoiceThread
 * 
 * @author Joakim
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceThread
 */
public class InvoiceBusinessBean extends IBOServiceBean implements InvoiceBusiness{

	/**
	 * Spawns a new thread and starts the execution of the posting calculation and then returns
	 * @param month
	 */
	public void startPostingBatch(Date month, String schoolCategory, IWContext iwc) throws IDOLookupException, FinderException, SchoolCategoryNotFoundException{
		//TODO (JJ) select correct thread to start
		SchoolCategoryHome sch =(SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		if(sch.findChildcareCategory().getCategory().equals(schoolCategory)) {
			new InvoiceChildcareThread(month, iwc).start();
		} else if(sch.findElementarySchoolCategory().getCategory().equals(schoolCategory)){
			new PaymentThreadElementarySchool(month, iwc).start();
		} else if(sch.findHighSchoolCategory().getCategory().equals(schoolCategory)){
			new PaymentThreadHighSchool(month, iwc).start();
		} else {
			System.out.println("Error: couldn't find any Schoolcategory for billing named "+schoolCategory);
			throw new SchoolCategoryNotFoundException("Couldn't find any Schoolcategory for billing named "+schoolCategory);
		}
	}
	
	/**
	 * removes all the invoice records and header and the related information in 
	 * the payment records for the given month where the status was set to preliminary
	 * 
	 * @param month
	 */
	public void removePreliminaryInvoice(Date month, String category) throws RemoveException{
		//Remove invoices
		PaymentRecord paymentRecord;
		Iterator headerIter;
		InvoiceHeader header;
		Iterator recordIter;
		InvoiceRecord invoiceRrecord;
		
		try {
			SchoolCategory schoolCategory =((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByLocalizedKey(category);
			headerIter = getInvoiceHeaderHome().findByMonthAndSchoolCategory(month, schoolCategory).iterator();
			while(headerIter.hasNext()){
				header = (InvoiceHeader)headerIter.next();
				if(header.getStatus() == ConstantStatus.PRELIMINARY);{
					recordIter = getInvoiceRecordHome().findByInvoiceHeader(header).iterator();
					while(recordIter.hasNext()){
						invoiceRrecord = (InvoiceRecord) recordIter.next();
						if(invoiceRrecord.getRuleSpecType().equals(RegSpecConstant.CHECK)){
							try {
								paymentRecord = getPaymentRecordHome().findByPrimaryKey(new Integer(invoiceRrecord.getPaymentRecordId()));
								//Remove this instance from the payment record
								paymentRecord.setPlacements(paymentRecord.getPlacements()-1);
								paymentRecord.setTotalAmount(paymentRecord.getTotalAmount()-invoiceRrecord.getAmount());
								paymentRecord.setTotalAmountVAT(paymentRecord.getTotalAmountVAT()-invoiceRrecord.getAmountVAT());
							} catch (FinderException e1) {}
						}
						invoiceRrecord.remove();
					}
					header.remove();
				}
			}
/*
			//Remove payments... Probably shouldn't remove them since they can hold info that has 
			// already been attested.
			recordIter = getPaymentRecordHome().findByMonth(month).iterator();
			while(recordIter.hasNext()){
				paymentRecord = (PaymentRecord) recordIter.next();
				paymentRecord.remove();
			}
*/
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException("Could not remove the records.");
		}
	}
	
	public boolean isHighShool(String category) throws IDOLookupException, FinderException{
		SchoolCategory highSchool = ((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findHighSchoolCategory();
		if(((String)highSchool.getPrimaryKey()).equals(category)){
			return true;
		}
		return false;
	}
	
	public BatchRun getBatchRunByCategory(String category) throws IDOLookupException, FinderException{
		SchoolCategory schoolCategory =((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
		BatchRun batchRun =((BatchRunHome) IDOLookup.getHome(BatchRun.class)).findBySchoolCategory(schoolCategory);
		return batchRun;
	}
	
	public int getNoProviders(BatchRun batchRun) throws RemoteException, FinderException, IDOException{
		Date period = batchRun.getPeriod();
		int schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentHeaderHome().getProviderCountForSchoolCategoryAndPeriod(schoolCategoryID,period);
	}

	public int getNoPlacements(BatchRun batchRun) throws RemoteException, FinderException, IDOException{
		Date period = batchRun.getPeriod();
		int schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentRecordHome().getPlacementCountForSchoolCategoryAndPeriod(schoolCategoryID,period);
	}

	public int getTotAmountWithoutVAT(BatchRun batchRun) throws RemoteException, FinderException, IDOException{
		Date period = batchRun.getPeriod();
		int schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentRecordHome().getTotAmountForSchoolCategoryAndPeriod(schoolCategoryID,period);
	}

	protected PaymentHeaderHome getPaymentHeaderHome() throws RemoteException
	{
		return (PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class);
	}
	
	protected PaymentRecordHome getPaymentRecordHome() throws RemoteException
	{
		return (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
	}
	
	protected InvoiceHeaderHome getInvoiceHeaderHome() throws RemoteException
	{
		return (InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class);
	}
	
	protected InvoiceRecordHome getInvoiceRecordHome() throws RemoteException
	{
		return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
	}
	
}
