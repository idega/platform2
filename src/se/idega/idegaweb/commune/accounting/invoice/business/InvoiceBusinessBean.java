package se.idega.idegaweb.commune.accounting.invoice.business;

import is.idega.block.family.business.FamilyLogic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import se.idega.idegaweb.commune.accounting.business.AccountingUtil;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRun;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunError;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunErrorHome;
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
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationHome;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.care.business.CareInvoiceBusiness;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.user.data.User;
import com.idega.util.CalendarMonth;
import com.idega.util.IWTimestamp;

/**
 * Holds most of the logic for the batchjob that creates the information that is
 * base for invoicing and payment data, that is sent to external finance system.
 * Now moved to InvoiceThread
 * <p>
 * Last modified: $Date: 2004/10/20 17:05:09 $ by $Author: thomas $
 *
 * @author <a href="mailto:joakim@idega.is">Joakim Johnson</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.134 $
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceThread
 */
public class InvoiceBusinessBean extends IBOServiceBean implements InvoiceBusiness, CareInvoiceBusiness {
	
	public int generatePdf (final String title, final MemoryFileBuffer buffer)
		throws RemoteException {
		try {
			final InputStream inStream = new MemoryInputStream (buffer);
			final ICFileHome icFileHome
					= (ICFileHome) getIDOHome (ICFile.class);
			final ICFile file = icFileHome.create ();
			file.setFileValue (inStream);
			file.setMimeType ("application/x-pdf");
			file.setName (title + ".pdf");
			file.setFileSize (buffer.length ());
			file.store ();
			return ((Integer) file.getPrimaryKey()).intValue();
		} catch (Exception e) {
			e.printStackTrace ();
			throw new RemoteException
					("Couldn't generate invoice compilation pdf", e);
		}
	}
	
	/**
	 * removes all the invoice records and header and the related information in 
	 * the payment records for the given month where the status was set to preliminary
	 * 
	 * @param month
	 */
	public void removePreliminaryInvoice(CalendarMonth month, String category) throws FinderException, RemoveException, BatchAlreadyRunningException, SchoolCategoryNotFoundException, IDOLookupException {
		SchoolCategoryHome sch = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		if (sch.findChildcareCategory().getCategory().equals(category)) {
			if (BatchRunSemaphore.getChildcareRunSemaphore()) {
				try {
					removePreliminaryInvoiceSub(month, category);
				} finally {
					BatchRunSemaphore.releaseChildcareRunSemaphore();
				}
			} else {
				throw new BatchAlreadyRunningException("Childcare");
			}
		} else if (sch.findElementarySchoolCategory().getCategory().equals(category)) {
			if (BatchRunSemaphore.getElementaryRunSemaphore()) {
				try {
					removePreliminaryInvoiceSub(month, category);
				} finally {
					BatchRunSemaphore.releaseElementaryRunSemaphore();
				}
			} else {
				throw new BatchAlreadyRunningException("ElementarySchool");
			}
		} else if (sch.findHighSchoolCategory().getCategory().equals(category)) {
			if (BatchRunSemaphore.getHighRunSemaphore()) {
				try {
					removePreliminaryInvoiceSub(month, category);
				} finally {
					BatchRunSemaphore.releaseHighRunSemaphore();
				}
			} else {
				throw new BatchAlreadyRunningException("HighSchool");
			}
		} else {
			throw new SchoolCategoryNotFoundException("Couldn't find any Schoolcategory for billing named " + category);
		}
	}
	
	/**
	 * removes all the invoice records and header and the related information in 
	 * the payment records for the given month where the status was set to preliminary
	 * 
	 * @param month
	 * @param category
	 */
	public void removePreliminaryPayment(CalendarMonth month, String category) throws RemoveException {
		PaymentRecord paymentRecord;
		Iterator headerIter;
		PaymentHeader paymentHeader;		
		try {
			SchoolCategory schoolCategory =
				((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
			if(getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month,category) == 0){
				headerIter = getPaymentHeaderHome().findByMonthAndSchoolCategory(month, schoolCategory).iterator();
				while (headerIter.hasNext()) {
					paymentHeader = (PaymentHeader) headerIter.next();
					removePaymentHeader(paymentHeader);
				}
				Iterator recordIter = getPaymentRecordHome().findByMonthAndCategory(month,category).iterator();
				while(recordIter.hasNext()){
					paymentRecord = (PaymentRecord) recordIter.next();
					paymentRecord.remove();
				}
				
			}else{
				throw new RemoveException("invoice.not_allowed_remove_locked_or_history_records");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException("invoice.Could not remove the records.");
		}
	}
	
	/**
	 * 
	 * @param month
	 * @param category
	 */
	public void removeTestRecordsForProvider(CalendarMonth month, String category, School school) throws RemoveException {
		PaymentRecord paymentRecord;
		Iterator headerIter;
		PaymentHeader paymentHeader;	
		
		try {
			SchoolCategory schoolCategory =
				((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
			if(getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month,category) == 0){ //should not be nessassary, but for the security reasons...
				headerIter = getPaymentHeaderHome().findAllBySchoolCategoryAndSchoolAndPeriodAndStatus(school, schoolCategory, month, "" +  ConstantStatus.TEST).iterator();
				while (headerIter.hasNext()) {
					paymentHeader = (PaymentHeader) headerIter.next();
					Iterator recordIter = getPaymentRecordHome().findByPaymentHeader(paymentHeader).iterator();
					while(recordIter.hasNext()){
						paymentRecord = (PaymentRecord) recordIter.next();
						Iterator detailRecordIter = getInvoiceRecordHome().findByPaymentRecord(paymentRecord).iterator();
						while (detailRecordIter.hasNext()){
							((InvoiceRecord) detailRecordIter.next()).remove();
						}
						paymentRecord.remove();
					}					
					paymentHeader.remove();
				}
			}else{
				throw new RemoveException("invoice.not_allowed_remove_locked_or_history_records");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException("invoice.Could not remove the records.");
		}
	}
		
	/**
	 * removes all the invoice records and header and the related information in 
	 * the payment records for the given month where the status was set to preliminary
	 * 
	 * @param month
	 */
	private void removePreliminaryInvoiceSub(CalendarMonth month, String category) throws RemoveException {
		InvoiceRecord invoiceRecord;
		Iterator headerIter;
		InvoiceHeader header;		
		try {
			SchoolCategory schoolCategory =
				((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
			if(getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month,category) == 0){
				headerIter = getInvoiceHeaderHome().findByMonthAndSchoolCategory(month, schoolCategory).iterator();
				while (headerIter.hasNext()) {
					header = (InvoiceHeader) headerIter.next();
					removePreliminaryInvoice(header);
				}
				Iterator recordIter = getInvoiceRecordHome().findByMonthAndCategory(month,category).iterator();
				while(recordIter.hasNext()){
					invoiceRecord = (InvoiceRecord) recordIter.next();
					invoiceRecord.remove();
				}
				
			}else{
				throw new RemoveException("invoice.not_allowed_remove_locked_or_history_records");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoveException("invoice.Could not remove the records.");
		}
	}
	
	/**
	 * Removes the invoice records and header and the related information in 
	 * the payment records if the given header status was set to preliminary
	 * 
	 * @param header invoice to remove
	 */
	public void removePreliminaryInvoice(InvoiceHeader header) throws RemoveException {
		Iterator recordIter;
		InvoiceRecord invoiceRecord;
		UserTransaction transaction = null;
		try {
			transaction = getSessionContext().getUserTransaction();
			transaction.begin();
			if (header.getStatus() == ConstantStatus.PRELIMINARY);
			{
				recordIter = getInvoiceRecordHome().findByInvoiceHeader(header).iterator();
				while (recordIter.hasNext()) {
					invoiceRecord = (InvoiceRecord) recordIter.next();
					removeInvoiceRecord(invoiceRecord);
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
			e.printStackTrace();
			throw new RemoveException("Could not remove the records.");
		}
	}
	
	/**
	 * Removes an invoice record and it's associated payments record if the
	 * rule spec type is check.
	 *
	 * @param invoiceRecord the record to remove
	 * @excpetion RemoteException if data layer fails
	 * @exception RemoveException if it wasn't possible to remove this record
	 */
	public void removeInvoiceRecord(InvoiceRecord invoiceRecord) throws RemoteException, RemoveException {
		PaymentRecord paymentRecord;
		RegulationSpecType regSpecType = invoiceRecord.getRegSpecType ();
		String typeName = regSpecType != null ? regSpecType.getRegSpecType() : null;
		if (null != typeName && RegSpecConstant.CHECK.equals(typeName)) {
			try {
				int paymentRecordId=invoiceRecord.getPaymentRecordId();
				paymentRecord =
						getPaymentRecordHome().findByPrimaryKey(new Integer(paymentRecordId));
				//Remove this instance from the payment record
				paymentRecord.setPlacements(paymentRecord.getPlacements() - 1);
				paymentRecord.setTotalAmount(paymentRecord.getTotalAmount() - invoiceRecord.getAmount());
				paymentRecord.setTotalAmountVAT(paymentRecord.getTotalAmountVAT() - invoiceRecord.getAmountVAT());
				paymentRecord.store ();
				if (0 >= paymentRecord.getPlacements()) {
					paymentRecord.remove ();
				}
			} catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		invoiceRecord.remove();
	}
	
	public void removePaymentRecord (final PaymentRecord paymentRecord)
		throws RemoteException, RemoveException {

		// remove detailed payment records if exists
		try {
			final Collection invoiceRecords
					= getInvoiceRecordHome ().findByPaymentRecord (paymentRecord);
			for (Iterator i = invoiceRecords.iterator (); i.hasNext ();) {
				((InvoiceRecord) i.next ()).remove ();
			}
		} catch (FinderException e) {
			// no invoice records connected to this payment record, it's ok
		}

		// remove payment record
		final PaymentHeader paymentHeader = paymentRecord.getPaymentHeader ();
		paymentRecord.remove ();

		// see if payment header should be removed
		final Collection paymentRecords = new ArrayList ();
		try {
			paymentRecords.addAll (getPaymentRecordHome ().findByPaymentHeader
														 (paymentHeader));
		} catch (FinderException e) {
			// no problem handle after this scope
		}
		if (paymentRecords.isEmpty ()) {
			paymentHeader.remove ();
		}
	}

	private void removePaymentHeader (final PaymentHeader paymentHeader)
		throws RemoteException, RemoveException {
		try {
			final Collection paymentRecords
					= getPaymentRecordHome ().findByPaymentHeader (paymentHeader);
			for (Iterator i = paymentRecords.iterator (); i.hasNext ();) {
				removePaymentRecord ((PaymentRecord) i.next ());
			}
		} catch (FinderException e) {
			// no payment records connected to this payment header, it's ok
		}
		paymentHeader.remove ();
	}

	public boolean isChildCare(String category) throws IDOLookupException, FinderException {
		SchoolCategoryHome sch = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		return (sch.findChildcareCategory().getCategory().equals(category));
	}

	
	public BatchRun getBatchRunByCategory(String category, boolean test) throws IDOLookupException, FinderException {
		SchoolCategory schoolCategory =
				((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
		BatchRun batchRun = ((BatchRunHome) IDOLookup.getHome(BatchRun.class)).findBySchoolCategory(schoolCategory, test);
		return batchRun;
	}
	
	public int getNoProviders(BatchRun batchRun) throws RemoteException, IDOException {
		Date period = batchRun.getPeriod();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentHeaderHome().getProviderCountForSchoolCategoryAndPeriod(schoolCategoryID, period);
	}
	
	public int getNoPlacements(BatchRun batchRun) throws RemoteException, IDOException {
		CalendarMonth month = batchRun.getMonth();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentRecordHome().getPlacementCountForSchoolCategoryAndMonth(schoolCategoryID, month);
	}
	
	public int getTotAmountWithoutVAT(BatchRun batchRun) throws RemoteException, IDOException {
		Date period = batchRun.getPeriod();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentRecordHome().getTotAmountForSchoolCategoryAndPeriod(schoolCategoryID, period);
	}
	
	public double getTotalAmountOfInvoices(BatchRun batchRun) throws RemoteException, IDOException {
		// get the month
		CalendarMonth month = batchRun.getMonth();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		SchoolBusiness schoolBusiness = getSchoolBusiness();
		Collection schoolTypes = schoolBusiness.findAllSchoolTypesInCategory(schoolCategoryID);
		InvoiceRecordHome invoiceRecordHome = getInvoiceRecordHome();
		return invoiceRecordHome.getTotalAmountForSchoolTypesAndMonth(schoolTypes, month);
	}
	
	public int getNumberOfHandledChildren(BatchRun batchRun) throws RemoteException, IDOException {
		// get the month
		CalendarMonth month = batchRun.getMonth();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		SchoolBusiness schoolBusiness = getSchoolBusiness();
		Collection schoolTypes = schoolBusiness.findAllSchoolTypesInCategory(schoolCategoryID);
		InvoiceRecordHome invoiceRecordHome = getInvoiceRecordHome();
		return invoiceRecordHome.getNumberOfHandledChildrenForSchoolTypesAndMonth(schoolTypes, month);
	}
	
	public int getNumberOfInvoices(BatchRun batchRun) throws RemoteException, IDOException {
		// get the month
		CalendarMonth month = batchRun.getMonth();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		InvoiceHeaderHome invoiceHeaderHome = getInvoiceHeaderHome();
		return invoiceHeaderHome.getNumberOfInvoicesForSchoolCategoryAndMonth(schoolCategoryID, month);
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
	public InvoiceHeader[] getInvoiceHeadersByCustodianOrChild
		(final String schoolCategory, final User user,
		 final CalendarMonth fromPeriod, final CalendarMonth toPeriod) {
		final Collection custodians = new HashSet ();
		custodians.add (user);		
		custodians.addAll (getInvoiceReceivers (user, fromPeriod, toPeriod));
		custodians.addAll (getCustodians (user));
		final Collection headers = getInvoiceHeaders
				(custodians, schoolCategory, fromPeriod, toPeriod);
		return (InvoiceHeader []) headers.toArray (new InvoiceHeader [0]);
	}
	
	private Collection getInvoiceHeaders
		(final Collection custodians, final String schoolCategory,
		 final CalendarMonth fromPeriod, final CalendarMonth toPeriod) {
		final Collection headers = new HashSet ();
		try {
			headers.addAll (getInvoiceHeaderHome ()
											.findByCategoryAndCustodiansAndPeriods
											(schoolCategory, custodians, fromPeriod, toPeriod));
		} catch (FinderException exception) {
			// no problem, return empty array
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return headers;
	}

	private Collection getCustodians (final User child) {
		final Collection custodians = new ArrayList ();
		try {
			custodians.addAll	(getMemberFamilyLogic ().getCustodiansFor (child));
		} catch (FinderException e) {
			// no problem, no custodians found
		} catch (Exception exception) {
			exception.printStackTrace ();
		}
		return custodians;
	}

	private Collection getInvoiceReceivers
		(final User child, final CalendarMonth fromPeriod,
		 final CalendarMonth toPeriod) {
		final Collection custodians = new ArrayList ();
		try {
			Collection contracts = getChildCareContractHome ().findByChildAndDateRange
					(child, fromPeriod.getFirstDateOfMonth (),
					 toPeriod.getLastDateOfMonth ());
			for (Iterator i = contracts.iterator (); i.hasNext ();) {
				final ChildCareContract contract = (ChildCareContract) i.next ();
				final User custodian = contract.getInvoiceReceiver ();
				if (null != custodian) { custodians.add (custodian); }
			}
		} catch (FinderException e) {
			// no problem, no custodians could be found from child care contracts
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return custodians;
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
		Collection collection = new ArrayList ();
		try {
			collection.addAll (getInvoiceRecordHome ().findByInvoiceHeader
												 (header));
		} catch (FinderException exception) {
			// no problem, return empty array
		}
		return (InvoiceRecord[]) collection.toArray (new InvoiceRecord[0]);
	}
	
	public User getChildByInvoiceRecord(final InvoiceRecord record)
		throws RemoteException {
		final SchoolClassMemberHome home = (SchoolClassMemberHome)
				IDOLookup.getHome (SchoolClassMember.class);
		try {
			final SchoolClassMember student = home.findByPrimaryKey
					(new Integer (record.getSchoolClassMemberId()));
			return null != student ? student.getStudent () : null;
		} catch (final FinderException e) {
			return null;
		}
	}
	
	public PaymentRecord []
		getPaymentRecordsBySchoolCategoryAndProviderAndPeriod
		(final String schoolCategory, final Integer providerId,
		 final Date startPeriod, final Date endPeriod) throws RemoteException {
		final PaymentRecord [] recordArray = new PaymentRecord [0];
		try {
			final Collection paymentHeaders = getPaymentHeaderHome ()
					.findBySchoolCategoryAndSchoolAndPeriod
					(schoolCategory, providerId, startPeriod, endPeriod);
			if (null == paymentHeaders || paymentHeaders.isEmpty ()) {
				return recordArray;
			}
			final Collection paymentRecords = getPaymentRecordHome ()
					.findByPaymentHeaders (paymentHeaders);
			return null == paymentRecords ? recordArray
					: (PaymentRecord []) paymentRecords.toArray (recordArray);
		} catch (FinderException e) {
			return recordArray;
		}            
	}
	
	/**
	 * Creates and stores a new Invoice Header. Status will be set to
	 * preliminary.
	 *
	 * @param schoolCategoryKey string constant from SchoolCategoryBMPBean
	 * @param createdBy the user who was logged on and initiated this
	 * @param custodianId the invoice receiver's user id
	 * @param period the month this occurs
	 * @return the new Invoice Header
	 * @exception CreateException if lower level fails
	 */
	public InvoiceHeader createInvoiceHeader(final String schoolCategoryKey,final User createdBy,final int custodianId,final Date period)
		throws CreateException {
		try {
			final InvoiceHeader header = getInvoiceHeaderHome ().create ();
			if (null != schoolCategoryKey) header.setSchoolCategoryID
																				 (schoolCategoryKey);
			if (null != createdBy) {
				final String createdBySignature = getSignature (createdBy);
				header.setCreatedBy (createdBySignature);
			}
			header.setCustodianId (custodianId);
			header.setDateCreated (new Date (new java.util.Date ().getTime ()));
			if (null != period) header.setPeriod (period);
			header.setStatus(ConstantStatus.PRELIMINARY);
			header.store();
			return header;
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new CreateException(e.getMessage());
		}
	}
	
	private String getSignature (final User user) {
		if (null == user) return "not logged in user";
		final String firstName = user.getFirstName ();
		final String lastName = user.getLastName ();
		return (firstName != null ? firstName + " " : "")
				+ (lastName != null ? lastName : "");
	}

	public InvoiceRecord createInvoiceRecord
		(final PaymentRecord paymentRecord, final SchoolClassMember placement,
		 final PostingDetail postingDetail, PlacementTimes checkPeriod,
		 final Date startPlacementDate, final Date endPlacementDate,
		 final String createdBySignature) throws CreateException {
		final InvoiceRecord result = createInvoiceRecord
				(paymentRecord, placement, checkPeriod, startPlacementDate,
				 endPlacementDate, createdBySignature);
		if (null != postingDetail) {
			result.setAmount (AccountingUtil.roundAmount
												(checkPeriod.getMonths ()
												 * postingDetail.getAmount ()));
			result.setInvoiceText(postingDetail.getTerm());
			result.setRuleText(postingDetail.getTerm());
			result.setOrderId(postingDetail.getOrderID());
			try {
				final RegulationSpecType regSpecType
						= getRegulationSpecTypeHome ().findByRegulationSpecType
						(postingDetail.getRuleSpecType ());
				result.setRegSpecType (regSpecType);
			} catch (Exception e) {
				e.printStackTrace ();
			}
		}
		result.store ();
		return result;
	}

	private InvoiceRecord createInvoiceRecord
		(final PaymentRecord paymentRecord, final SchoolClassMember placement,
		 final PlacementTimes checkPeriod, final Date startPlacementDate,
		 final Date endPlacementDate, final String createdBySignature)
		throws CreateException {
		final InvoiceRecord result = getInvoiceRecordHome ().create ();
		result.setCreatedBy (createdBySignature);
		result.setDateCreated (new Date (System.currentTimeMillis ()));
		result.setDays (checkPeriod.getDays ());
		if (null != paymentRecord) {
			result.setPaymentRecord (paymentRecord);
		}
		result.setPeriodStartCheck (checkPeriod.getFirstCheckDay ().getDate ());
		result.setPeriodEndCheck (checkPeriod.getLastCheckDay ().getDate ());
		if (null != placement) {
			result.setSchoolClassMember (placement);
			final SchoolType schoolType = placement.getSchoolType ();
			if (null != schoolType) result.setSchoolType (schoolType);
		}
		if (null != startPlacementDate) {
			result.setPeriodStartPlacement (startPlacementDate);
		}
		if (null != endPlacementDate) {
			result.setPeriodEndPlacement (endPlacementDate);
		}
		result.store ();
		return result;
	}
	
	public InvoiceRecord createInvoiceRecord
		(final User createdBy,
		 final InvoiceHeader header,
		 final Integer placementId,
		 final Integer providerId,
		 final String ruleText,
		 final String invoiceText,
		 final String invoiceText2,
		 final String note,
		 final Date placementStartPeriod,
		 final Date placementEndPeriod,	 
		 final Date checkStartPeriod,
		 final Date checkEndPeriod,
		 final Integer amount,
		 final Integer vatAmount,
		 final Integer numberOfDays,
		 final Integer regSpecTypeId,
		 final Integer vatRule,
		 final String ownPosting,
		 final String doublePosting,
		 final Integer pieceAmount,
		 final String ownPaymentPosting,
		 final String doublePaymentPosting,
		 final Integer orderId)
		throws CreateException, RemoteException {
		final InvoiceRecord record = getInvoiceRecordHome ().create ();
		final String createdBySignature = getSignature (createdBy);
		record.setCreatedBy (createdBySignature);
		final Date now = new Date (new java.util.Date ().getTime());
		record.setDateCreated (now);
		record.setAmount (null != amount ? amount.floatValue () : 0.0f);

		record.setAmountVAT (null != vatAmount ? vatAmount.floatValue () : 0.0f);
		record.setDays (null != numberOfDays ? numberOfDays.intValue () : 0);
		record.setDoublePosting (doublePosting != null ? doublePosting : "");
		if (null != header) {
			record.setInvoiceHeader (header);
			header.setChangedBy (createdBySignature);
			header.setDateAdjusted (now);
			header.store ();
		}
		record.setInvoiceText (null != invoiceText && 0 < invoiceText.length ()
				 ? invoiceText : ruleText);
		record.setInvoiceText2(null != invoiceText2 && 0 < invoiceText2.length ()
				 ? invoiceText2 : "");
		record.setRuleText (ruleText);
		record.setNotes (note);
		record.setOwnPosting (ownPosting);
		if (null != checkEndPeriod) record.setPeriodEndCheck(checkEndPeriod);
		if (null != placementEndPeriod) record.setPeriodEndPlacement(placementEndPeriod);
		if (null != checkStartPeriod) record.setPeriodStartCheck(checkStartPeriod);
		if (null != placementStartPeriod) record.setPeriodStartPlacement(placementStartPeriod);
		if (null != regSpecTypeId) {
			record.setRegSpecTypeId (regSpecTypeId.intValue ());
		}
		if (null != vatRule)  record.setVATRuleRegulation (vatRule.intValue ());
		if (null != providerId) record.setProviderId(providerId.intValue ());
		if (null != orderId) record.setOrderId(orderId.intValue ());
		try {
			record.setSchoolClassMemberId (placementId.intValue ());
			final SchoolClassMember placement = record.getSchoolClassMember ();
			record.setSchoolType (placement.getSchoolType ());
			final ChildCareContract contract
					= getChildCareContractHome ().findBySchoolClassMember (placement);
			record.setChildCareContract (contract);
		} catch (FinderException e) {
			// no problem, only child care have contracts
		} catch (NullPointerException e) {
			// no problem, placementId was null or couldn't find placement
		}
		record.store ();
		try {
			final RegulationSpecType regSpecType
					= getRegulationSpecTypeHome ().findByPrimaryKey
					(regSpecTypeId);
			final String regSpecTypeName = regSpecType.getRegSpecType ();
			if (regSpecTypeName.equals (RegSpecConstant.CHECK)) {
				// since invoice is check, then create payment
				final School school = record.getProvider ();
				final SchoolCategory schoolCategory = header.getSchoolCategory ();
				final Date period = header.getPeriod ();
				final PaymentRecord paymentRecord
						= createPaymentRecord
						(school, schoolCategory, period, createdBySignature, now,
						 ruleText, amount, pieceAmount, vatRule, ownPaymentPosting,
						 doublePaymentPosting, regSpecTypeName, orderId);
				record.setPaymentRecord (paymentRecord);
				record.store ();
				final SchoolClassMember placement = record.getSchoolClassMember ();
				// create vat record for this payment
				createOrUpdateVatPaymentRecord
						(paymentRecord, placement.getSchoolType (),
						 placement.getSchoolYear (), createdBySignature);
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return record;
	}

	public PaymentRecord createOrUpdateVatPaymentRecord
		(final PaymentRecord paymentRecord, final SchoolType schoolType,
		 final SchoolYear schoolYear, final String createdBySignature)
	throws RemoteException, CreateException {

		// is school vat eligible?
		final PaymentHeader paymentHeader = paymentRecord.getPaymentHeader ();
		final School school = paymentHeader.getSchool ();
		if (!getVATBusiness ().isSchoolVATEligible (school)) return null;

		// get vat regulation, if exists
		final Regulation vatRuleRegulation = paymentRecord.getVATRuleRegulation();
		if (null == vatRuleRegulation) return null;

		// init some multiply used values
		final RegulationSpecType regSpecType = vatRuleRegulation.getRegSpecType ();
		final Date period = paymentRecord.getPeriod ();
		final char status = paymentHeader.getStatus ();

		// get own and double postings
		final int regSpecTypeId
				= ((Number) regSpecType.getPrimaryKey ()).intValue ();
		final Provider provider = new Provider (school);
		final int schoolYearId = null == schoolYear ? -1
				: ((Number) schoolYear.getPrimaryKey ()).intValue ();
		String ownPosting = "";
		String doublePosting = "";
		try {
			final String [] postings = getPostingBusiness ().getPostingStrings
					(paymentHeader.getSchoolCategory (), schoolType, regSpecTypeId,
					 provider, period, schoolYearId);
			ownPosting = postings [0];
			doublePosting = postings [1];
		} catch (PostingException e) {
			// no postings found
			e.printStackTrace ();
		}

		// find or create vat payment record
		PaymentRecord vatPaymentRecord;
		try {
			// update old vat payment record
			vatPaymentRecord = getPaymentRecordHome ()
					.findByPostingStringsAndVATRuleRegulationAndPaymentTextAndMonthAndStatus
					(ownPosting, doublePosting, null, vatRuleRegulation.getName (),
					 new CalendarMonth (period), status);
			final float newAmount = vatPaymentRecord.getTotalAmount ()
					+ paymentRecord.getTotalAmountVAT ();
			vatPaymentRecord.setTotalAmount	(AccountingUtil.roundAmount (newAmount));
			vatPaymentRecord.setChangedBy (createdBySignature);
			vatPaymentRecord.setDateChanged (now ());
			vatPaymentRecord.store ();
		} catch (FinderException e1) {
			//It didn't exist, so we create it
			vatPaymentRecord = (PaymentRecord) IDOLookup.create(PaymentRecord.class);
			vatPaymentRecord.setPaymentHeader (paymentHeader);
			vatPaymentRecord.setStatus (status);
			vatPaymentRecord.setPeriod (period);
			vatPaymentRecord.setPaymentText (vatRuleRegulation.getName ());
			vatPaymentRecord.setDateCreated (now ());
			vatPaymentRecord.setCreatedBy (createdBySignature);
			vatPaymentRecord.setPlacements (0);
			vatPaymentRecord.setPieceAmount (0);
			vatPaymentRecord.setTotalAmount (paymentRecord.getTotalAmountVAT ());
			vatPaymentRecord.setTotalAmountVAT (0);
			vatPaymentRecord.setRuleSpecType (regSpecType.getRegSpecType ());
			vatPaymentRecord.setOwnPosting (ownPosting);
			vatPaymentRecord.setDoublePosting (doublePosting);
			vatPaymentRecord.store();
		}
		return vatPaymentRecord;
	}

	private PaymentRecord createPaymentRecord
		(final School school,
		 final SchoolCategory schoolCategory,
		 final Date period,
		 final String createdBy,
		 final Date dateCreated,
		 final String ruleText,
		 final Integer totalAmount,
		 final Integer pieceAmount,
		 final Integer vatType,
		 final String ownPosting,
		 final String doublePosting,
		 final String regSpecTypeName,
		 final Integer orderId) throws RemoteException, CreateException {
		final char status = ConstantStatus.PRELIMINARY;
		final PaymentHeader paymentHeader = findOrElseCreatePaymentHeader
				(school, schoolCategory, period, status);
		final PaymentRecord paymentRecord = getPaymentRecordHome ().create ();
		paymentRecord.setCreatedBy (null != createdBy ? createdBy : "");
		paymentRecord.setDateCreated (dateCreated);
		paymentRecord.setPaymentHeader (paymentHeader);
		paymentRecord.setPaymentText (ruleText);
		paymentRecord.setStatus (status);
		paymentRecord.setPeriod (period);
		paymentRecord.setTotalAmount (null != totalAmount ? totalAmount.intValue ()
																	: 0);
		paymentRecord.setPieceAmount (null != pieceAmount ? pieceAmount.intValue ()
																	: 0);
		if (null != orderId) paymentRecord.setOrderId (orderId.intValue ());
		if (null != vatType) {
			paymentRecord.setVATRuleRegulationId(vatType.intValue ());
		}
		try {
			final Regulation vatRegulation = paymentRecord.getVATRuleRegulation ();
			final float vatPercent = getVATBusiness ()
					.getVATPercentForVATRuleRegulation (vatRegulation) / 100.0f;
			paymentRecord.setTotalAmountVAT
					(vatPercent * paymentRecord.getTotalAmount ());
		} catch (Exception e) {
			e.printStackTrace ();
			paymentRecord.setTotalAmountVAT (0);
		}

		paymentRecord.setOwnPosting (ownPosting);
		paymentRecord.setDoublePosting (doublePosting);
		paymentRecord.setRuleSpecType (regSpecTypeName);
		paymentRecord.setPlacements (1);
		paymentRecord.store ();

		return paymentRecord;
	}	

	public void saveInvoiceRecord
		(final Integer recordId, final User currentUser, final Integer placementId,
		 Integer providerId, final String invoiceText, final String invoiceText2,
		 final String ruleText, final String note, final Date checkEndPeriod,
		 final Date checkStartPeriod, final Date placementStartPeriod,
		 final Date placementEndPeriod, final String ownPosting,
		 final String doublePosting, final Integer amount, final Integer vatAmount,
		 final Integer vatRule, final Integer regSpecTypeId)
		throws RemoteException, FinderException {

		// find record to update
		final InvoiceRecord record
				= getInvoiceRecordHome ().findByPrimaryKey (recordId);

		// count some values to store in record
		final int dayDiff = null != checkStartPeriod && null != checkEndPeriod
				? 1 + AccountingUtil.getDayDiff (checkStartPeriod, checkEndPeriod) : 0;
		final Integer numberOfDays = new Integer (0 > dayDiff ? 0 : dayDiff);
		if (null == providerId && null != placementId) {
			// unknown provider - find it from the placement
			final SchoolBusiness schoolBusiness = getSchoolBusiness ();
			final SchoolClassMemberHome placementHome
					= schoolBusiness.getSchoolClassMemberHome ();
			final SchoolClassMember placement
					= placementHome.findByPrimaryKey (placementId);
			providerId
					= (Integer) placement.getSchoolClass ().getSchool ().getPrimaryKey ();
		}
		final Date dateChanged = now ();

		// set updated values in record
		final float oldAmount = record.getAmount ();
		if (null != amount) record.setAmount (amount.floatValue ());
		final float oldAmountVat = record.getAmountVAT ();
		if (null != vatAmount) record.setAmountVAT (vatAmount.floatValue ());
		final String changedBy = getSignature (currentUser);
		record.setChangedBy (changedBy);
		record.setDateChanged (dateChanged);
		if (null != numberOfDays) record.setDays (numberOfDays.intValue ());
		record.setInvoiceText (null != invoiceText && 0 < invoiceText.length ()
													 ? invoiceText : ruleText);
		record.setInvoiceText2
				(null != invoiceText2 && 0 < invoiceText2.length ()
				 ? invoiceText2 : "");
		if (null != note) record.setNotes (note);
		if (null != ownPosting) record.setOwnPosting (ownPosting);
		if (null != doublePosting) record.setDoublePosting (doublePosting);
		record.setPeriodStartCheck (checkStartPeriod);
		record.setPeriodEndCheck (checkEndPeriod);
		record.setPeriodStartPlacement (placementStartPeriod);
		record.setPeriodEndPlacement (placementEndPeriod);
		if (null != providerId) record.setProviderId (providerId.intValue ());
		if (null != placementId) record.setSchoolClassMemberId
																 (placementId.intValue ());
		if (null != regSpecTypeId) record.setRegSpecTypeId
																	 (regSpecTypeId.intValue ());
		if (null != vatRule) record.setVATRuleRegulation(vatRule.intValue ());
		record.setRuleText (ruleText);
		
		// store updated record
		record.store ();

		// update invoice header
		try {
			final InvoiceHeader header = record.getInvoiceHeader ();
			header.setChangedBy (changedBy);
			header.setDateAdjusted (dateChanged);
			header.store ();
		} catch (Exception e) {
			e.printStackTrace ();
		}

		// update payment record, if this is an detailed payment record
		try {
			final PaymentRecord paymentRecord = record.getPaymentRecord ();
			paymentRecord.setTotalAmount (paymentRecord.getTotalAmount ()
																		+ record.getAmount () - oldAmount);
			paymentRecord.setTotalAmountVAT (paymentRecord.getTotalAmountVAT ()
																			 + record.getAmountVAT () - oldAmountVat);
			paymentRecord.store ();
		} catch (NullPointerException e) {
			// no problem, this is not an detailed payement record
		} catch (Exception e) {
			e.printStackTrace ();
		}			
	}

	private PaymentHeader findOrElseCreatePaymentHeader
		(final School school, final SchoolCategory schoolCategory,
		 final Date period, final char status)
		throws RemoteException, CreateException {
		PaymentHeader paymentHeader;
		final PaymentHeaderHome paymentHeaderHome = getPaymentHeaderHome ();
		try {
			paymentHeader	= paymentHeaderHome
					.findBySchoolCategoryAndSchoolAndPeriodAndStatus
					(school, schoolCategory, new CalendarMonth (period), status + "");
		} catch (FinderException e) {
			paymentHeader = paymentHeaderHome.create ();
			paymentHeader.setSchool (school);
			paymentHeader.setSchoolCategory (schoolCategory);
			paymentHeader.setPeriod (period);
			paymentHeader.setStatus (status);
			paymentHeader.store ();
		}
		return paymentHeader;
	}
	
	public Collection findInvoiceRecordsByContract(ChildCareContract contract) throws FinderException {
		return getInvoiceRecordHome().findByContract(contract);
	}

	public SchoolClassMember [] getSchoolClassMembers
		(final InvoiceHeader header) {
		final Collection allPlacements = new ArrayList ();
		try {
			final FamilyLogic familyBusiness = (FamilyLogic)
					IBOLookup.getServiceInstance (getIWApplicationContext(),
																				FamilyLogic.class);
			final Collection children = familyBusiness.getChildrenInCustodyOf
					(header.getCustodian ());
			final SchoolClassMemberHome placementHome = (SchoolClassMemberHome)
					IDOLookup.getHome (SchoolClassMember.class);
			final SchoolCategory category =  header.getSchoolCategory ();
			final Date period = header.getPeriod ();
			for (Iterator i = children.iterator (); i.hasNext ();) {
				final User child = (User) i.next ();
				try {
					final Collection childsPlacements = placementHome
							.findAllByUserAndPeriodAndSchoolCategory
							(child, period, category);
					if (null != childsPlacements) {
						allPlacements.addAll (childsPlacements);
					}
				} catch (FinderException e) {
					// no problem, try next child instead
				}
			}
		} catch (FinderException e) {
			// no problem, return an empty list
		} catch (Exception e) {
			e.printStackTrace ();
		}
		
		return (SchoolClassMember []) allPlacements.toArray
				(new SchoolClassMember [0]);
	}

	public InvoiceRecord createDetailedPaymentRecord
		(final User child, final PaymentRecord paymentRecord,
		 final User registrator)
		throws IDOLookupException, FinderException, RemoteException,
					 CreateException {
		final PaymentHeader paymentHeader = paymentRecord.getPaymentHeader ();
		final SchoolCategory schoolCategory = paymentHeader.getSchoolCategory ();
		final SchoolClassMemberHome memberHome
				= (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
		final SchoolClassMember member
				= memberHome.findLatestByUserAndSchCategory (child, schoolCategory);
		final Date period = paymentRecord.getPeriod ();
		final PlacementTimes placementTimes
				= getPlacementTimes (member, new CalendarMonth (period));
		final Date startDate = null != member	&& null != member.getRegisterDate ()
				? new Date (member.getRegisterDate ().getTime ()) : null;
		final Date endDate = null != member && null != member.getRemovedDate ()
				? new Date (member.getRemovedDate ().getTime ()) : null;
		final InvoiceRecord result = createInvoiceRecord
				(paymentRecord, member, placementTimes, startDate, endDate,
				 getSignature (registrator));
		final School school = paymentHeader.getSchool ();
		result.setProvider (school);

		// find regulation and connected values
		final Regulation regulation
				= findRegulation (paymentRecord, schoolCategory, period);
		if (null != regulation) {
			final RegulationSpecType regSpecType = regulation.getRegSpecType ();
			result.setRegSpecType (regSpecType);
			final String regulationName = regulation.getName ();
			result.setRuleText (regulationName);
			result.setInvoiceText (regulationName);
			result.setInvoiceText2 (" ");
			final int pieceAmount = null == regulation.getAmount ()
					? 0 : regulation.getAmount ().intValue ();
			result.setAmount (pieceAmount * placementTimes.getMonths ());
			if (null != regulation.getConditionOrder ()) {
				result.setOrderId (regulation.getConditionOrder ().intValue ());
			}

			// set postings
			try {
				final Integer regSpecTypeId	= (Integer) regSpecType.getPrimaryKey ();
				final SchoolType schoolType
						= getRegulationsBusiness ().getSchoolType (regulation);
				final String [] paymentPostings
						= getPostingBusiness ().getPostingStrings
						(schoolCategory, schoolType, regSpecTypeId.intValue (),
						 new Provider (school), period);
				result.setOwnPosting (paymentPostings [0]);
				result.setDoublePosting (paymentPostings [1]);
			} catch (PostingException e) {
				e.printStackTrace ();
			}
		}
		result.store ();
		return result;
	}

	public void removeInvoiceRecords(ChildCareContract contract) throws RemoveException {
		try {
			InvoiceBusiness business = (InvoiceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), InvoiceBusiness.class);
			Collection records = business.findInvoiceRecordsByContract(contract);
			Iterator iter = records.iterator();
			while (iter.hasNext()) {
				InvoiceRecord element = (InvoiceRecord) iter.next();
				element.remove();
			}
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
		catch (FinderException fe) {
			//Nothing found, which is OK...
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}	
	
	private PlacementTimes getPlacementTimes(SchoolClassMember schoolClassMember, CalendarMonth month) {
		Date sDate = null;
		Date eDate = null;
		if (schoolClassMember.getRegisterDate() != null) {
			sDate = new Date(schoolClassMember.getRegisterDate().getTime());
		}
		if (schoolClassMember.getRemovedDate() != null) {
			eDate = new Date(schoolClassMember.getRemovedDate().getTime());
		}
		PlacementTimes placementTimes = calculateTime(sDate, eDate, month);
		return placementTimes;
	}

	private PlacementTimes calculateTime(Date start, Date end, CalendarMonth month){
		IWTimestamp firstCheckDay = new IWTimestamp(start);
		firstCheckDay.setAsDate();
		IWTimestamp time = new IWTimestamp(month.getFirstDateOfMonth());
		time.setAsDate();
		if(!firstCheckDay.isLaterThan(time)){
			firstCheckDay = time;
		}
		IWTimestamp lastCheckDay = new IWTimestamp(month.getLastDateOfMonth());
		lastCheckDay.setAsDate();
		if(end!=null){
			time = new IWTimestamp(end.getTime ());
			if(!lastCheckDay.isEarlierThan(time)){
				lastCheckDay = time;
			}
		}
		PlacementTimes placementTimes = new PlacementTimes (firstCheckDay, lastCheckDay);
		return placementTimes;
	}
	
	private Regulation findRegulation
		(final PaymentRecord paymentRecord, final SchoolCategory schoolCategory,
		 final Date period) {
		Regulation matchedRegulation  = null;
		final RegulationHome regulationHome = getRegulationHome ();
		try {
			final Collection regulations =
					regulationHome.findRegulationsByNameNoCaseDateAndCategory
					(paymentRecord.getPaymentText (), period,
					 schoolCategory.getCategory ());
			if (1 == regulations.size ()) {
				matchedRegulation = (Regulation) regulations.iterator ().next ();
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return matchedRegulation;
	}
	
	public RegulationSpecType [] getAllRegulationSpecTypes ()
		throws RemoteException {
		try {
			final Collection collection = getRegulationSpecTypeHome ()
					.findAllRegulationSpecTypes ();
			return null == collection ? new RegulationSpecType [0]
					: (RegulationSpecType []) collection.toArray
					(new RegulationSpecType [0]);
		} catch (FinderException e) {
			return new RegulationSpecType [0];
		}
	}
	
	public Collection getAllVATRuleRegulations() throws RemoteException{
		return getRegulationsBusiness().findAllVATRuleRegulations();
	}
	
	public Regulation getVATRuleRegulation (int primaryKey) throws RemoteException {
		//try {
			Regulation regulation = getRegulationsBusiness().findRegulation(primaryKey);
			return regulation;
		///} catch (FinderException e) {
		//	return null;
		//}
	}
	
	/**
	 * A method to export the work reports to excel for those who are not using the member system.
	 * 
	 * @param regionalUnionId The id of the regional union who is to receive the file
	 * @param year The year we are creating excel files for
	 * @param templateId The id for the template for the excel files in the IW file system
	 *  
	 * @return A Collection of WorkReportExportFile data beans, one for each club in the union.
	 */
	public ICFile exportToExcel(IWResourceBundle iwrb, String fileName, BatchRun batchRun, boolean isTestRun) throws FinderException, IOException, CreateException{
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("new sheet");

		
		
		BatchRunErrorHome batchRunErrorHome = (BatchRunErrorHome)IDOLookup.getHome(BatchRunError.class);
		Collection errorColl = batchRunErrorHome.findByBatchRun(batchRun, isTestRun);

		HSSFRow row = sheet.createRow(0);

		row.createCell((short)0).setCellValue(iwrb.getLocalizedString("invbr.related_object","Related object"));
		row.createCell((short)1).setCellValue(iwrb.getLocalizedString("invbr.suspected_error","Suspected error"));
		
		System.out.println("Size of table BatchRunError: "+errorColl.size());
		Iterator errorIter = errorColl.iterator();
		if(errorIter.hasNext()){
			System.out.println("Found error description");
			
			short rowNr = 1;
			while(errorIter.hasNext()){
				BatchRunError batchRunError = (BatchRunError)errorIter.next();
				
				// Create a row and put some cells in it. Rows are 0 based.
				row = sheet.createRow(rowNr);

				row.createCell((short)0).setCellValue(batchRunError.getRelated());
				row.createCell((short)1).setCellValue(batchRunError.getDescription());
				rowNr++;
			}
		}
		
		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		fileOut.close();
		
		ICFile icfile = ((ICFileHome) IDOLookup.getHome(ICFile.class)).create();
		icfile.setFileValue(new FileInputStream(fileName));
		icfile.setMimeType("application/vnd.ms-excel");
		icfile.setName(fileName);
		icfile.store();

		File file = new File(fileName);
		file.delete();
		
		return icfile;
	}
	
	private Date now () {
		return new Date (System.currentTimeMillis ());
	}

	private RegulationSpecTypeHome getRegulationSpecTypeHome ()
		throws RemoteException {
		return (RegulationSpecTypeHome)
				IDOLookup.getHome(RegulationSpecType.class);
	}
	
	private FamilyLogic getMemberFamilyLogic () throws RemoteException {
		return (FamilyLogic) IBOLookup.getServiceInstance(getIWApplicationContext(), FamilyLogic.class);	
	}
	
	public PaymentHeaderHome getPaymentHeaderHome() throws RemoteException {
		return (PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class);
	}
	
	public ChildCareContractHome getChildCareContractHome()
		throws RemoteException {
		return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
	}
	
	public PaymentRecordHome getPaymentRecordHome() throws RemoteException {
		return (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
	}
	
	public InvoiceHeaderHome getInvoiceHeaderHome() throws RemoteException {
		return (InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class);
	}
	
	public InvoiceRecordHome getInvoiceRecordHome() {
		try {
			return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private RegulationHome getRegulationHome() {
		try {
			return (RegulationHome) IDOLookup.getHome(Regulation.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private RegulationsBusiness getRegulationsBusiness(){
		try {
			return (RegulationsBusiness)getServiceInstance(RegulationsBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	private PostingBusiness getPostingBusiness(){
		try {
			return (PostingBusiness)getServiceInstance(PostingBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	private SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
		
	private VATBusiness getVATBusiness() {
		try {
			return (VATBusiness) getServiceInstance(VATBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}	
}
