package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

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

import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
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
	public void startPostingBatch(Date month, IWContext iwc){
		new InvoiceChildcareThread(month, iwc).start();
	}
	
	/**
	 * removes all the invoice records and header and the related information in 
	 * the payment records for the given month where the status was set to preliminary
	 * 
	 * @param month
	 */
	public void removePreliminaryInvoice(Date month) throws RemoveException{
		//Remove invoices
		PaymentRecord paymentRecord;
		Iterator headerIter;
		InvoiceHeader header;
		Iterator recordIter;
		InvoiceRecord invoiceRrecord;
		
		try {
			headerIter = getInvoiceHeaderHome().findByMonth(month).iterator();
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
