package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
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
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

/**
 * Holds most of the logic for the batchjob that creates the information that is
 * base for invoicing and payment data, that is sent to external finance system.
 * Now moved to InvoiceThread
 * <p>
 * Last modified: $Date: 2003/11/04 14:58:36 $ by $Author: staffan $
 *
 * @author Joakim
 * @version $Revision: 1.29 $
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceThread
 */
public class InvoiceBusinessBean extends IBOServiceBean implements InvoiceBusiness{

	/**
	 * Spawns a new thread and starts the execution of the posting calculation and then returns
	 * @param month
	 */
	public void startPostingBatch(Date month, String schoolCategory, IWContext iwc) throws IDOLookupException, FinderException, SchoolCategoryNotFoundException{
		//Select correct thread to start
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
				removePreliminaryInvoice(header);
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
	
	/**
	 * Removes the invoice records and header and the related information in 
	 * the payment records if the given header status was set to preliminary
	 * 
	 * @param header invoice to remove
	 */
	public void removePreliminaryInvoice (InvoiceHeader header) throws RemoveException {
		Iterator recordIter;
		InvoiceRecord invoiceRrecord;
		UserTransaction transaction = null;
		try {
			transaction = getSessionContext().getUserTransaction();
			transaction.begin();
            if(header.getStatus() == ConstantStatus.PRELIMINARY);{
                recordIter = getInvoiceRecordHome().findByInvoiceHeader(header).iterator();
                while(recordIter.hasNext()){
                    invoiceRrecord = (InvoiceRecord) recordIter.next();
                    removeInvoiceRecord (invoiceRrecord);
                }
                header.remove();
            }
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				try {
					transaction.rollback();
				} catch (SystemException se) {
					se.printStackTrace();
				}
            }
            e.printStackTrace ();
            throw new RemoveException("Could not remove the records.");
        }
	}

    public void removeInvoiceRecord (InvoiceRecord invoiceRrecord) throws RemoteException, RemoveException {
		PaymentRecord paymentRecord;
        String ruleSpecType = invoiceRrecord.getRuleSpecType();
        if(null != ruleSpecType && RegSpecConstant.CHECK.equals (ruleSpecType)) {
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

    /**
     * Retreives an array of all InvoiceHeaders where the user given is either
     * custodian or the child and in the period. If any of the dates
     * are null, that constraint will be ignored. An empty list is returned if
     * no invoice header was found.
     *
     * @param user the user to search for
     * @param fromDate first month in search span
     * @param toDate last month in search span
     * @return array of invoice headers
     */
    public InvoiceHeader [] getInvoiceHeadersByCustodianOrChild
        (final User user, final java.util.Date fromPeriod,
         java.util.Date toPeriod) {
        Collection collection = null;
        try {
            collection = getInvoiceHeaderHome ().findByCustodianOrChild
                    (user, fromPeriod, toPeriod);
        } catch (RemoteException exception) {
            exception.printStackTrace ();
        } catch (FinderException exception) {
            // no problem, return empty array
        }
        return null == collection ? new InvoiceHeader [0]
                : (InvoiceHeader []) collection.toArray (new InvoiceHeader [0]);
    }

    /**
     * Retreives an array of all InvoiceRecords connected to this InvoiceHeader
     * An empty list is returned if no invoice header was found.
     *
     * @param header the header to find records connected to
     * @return array of invoice records
     */
    public InvoiceRecord [] getInvoiceRecordsByInvoiceHeader
        (final InvoiceHeader header) {
        Collection collection = null;
        try {
            collection = getInvoiceRecordHome ().findByInvoiceHeader (header);
        } catch (RemoteException exception) {
            exception.printStackTrace ();
        } catch (FinderException exception) {
            // no problem, return empty array
        }
        return null == collection ? new InvoiceRecord [0]
                : (InvoiceRecord []) collection.toArray (new InvoiceRecord [0]);
    }

    public User getChildByInvoiceRecord (final InvoiceRecord record)
        throws RemoteException {
        final ChildCareContractHome contractHome = (ChildCareContractHome)
                IDOLookup.getHome (ChildCareContract.class);
        try {
            final ChildCareContract contract = contractHome
                    .findApplicationByContract (record.getContractId ());
            return null != contract ? contract.getChild () : null;
        } catch (final FinderException e) {
            return null;
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
	
	public InvoiceHeaderHome getInvoiceHeaderHome() throws RemoteException
	{
		return (InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class);
	}
	
	public InvoiceRecordHome getInvoiceRecordHome() throws RemoteException
	{
		return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
	}	
}
