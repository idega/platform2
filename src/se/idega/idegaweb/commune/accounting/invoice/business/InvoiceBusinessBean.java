package se.idega.idegaweb.commune.accounting.invoice.business;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.CalendarMonth;
import is.idega.idegaweb.member.business.MemberFamilyLogic;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
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
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

/**
 * Holds most of the logic for the batchjob that creates the information that is
 * base for invoicing and payment data, that is sent to external finance system.
 * Now moved to InvoiceThread
 * <p>
 * Last modified: $Date: 2004/01/06 14:03:15 $ by $Author: tryggvil $
 *
 * @author <a href="mailto:joakim@idega.is">Joakim Johnson</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.76 $
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceThread
 */
public class InvoiceBusinessBean extends IBOServiceBean implements InvoiceBusiness {
	
	/**
	 * Spawns a new thread and starts the execution of the posting calculation and then returns
	 * @param month
	 */
	public void startPostingBatch(Date month, Date readDate, String schoolCategory, IWContext iwc)
		throws IDOLookupException, FinderException, SchoolCategoryNotFoundException {
		//Select correct thread to start
		SchoolCategoryHome sch = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
		if (sch.findChildcareCategory().getCategory().equals(schoolCategory)) {
			new InvoiceChildcareThread(month, iwc).start();
		} else if (sch.findElementarySchoolCategory().getCategory().equals(schoolCategory)) {
			new PaymentThreadElementarySchool(month, iwc).start();
		} else if (sch.findHighSchoolCategory().getCategory().equals(schoolCategory)) {
			new PaymentThreadHighSchool(readDate, iwc).start();
		} else {
			logWarning ("Error: couldn't find any Schoolcategory for billing named " + schoolCategory);
			throw new SchoolCategoryNotFoundException(
																								"Couldn't find any Schoolcategory for billing named " + schoolCategory);
		}
	}
	
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
	public void removePreliminaryInvoice(Date dateInMonth, String category) throws RemoveException {
		PaymentRecord paymentRecord;
		Iterator headerIter;
		InvoiceHeader header;
		CalendarMonth month = new CalendarMonth(dateInMonth);
		
		try {
			SchoolCategory schoolCategory =
					((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
			if(getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month,category) == 0){
				headerIter = getInvoiceHeaderHome().findByMonthAndSchoolCategory(month, schoolCategory).iterator();
				while (headerIter.hasNext()) {
					header = (InvoiceHeader) headerIter.next();
					removePreliminaryInvoice(header);
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
			} catch (FinderException e1) {
				e1.printStackTrace();
			}
		}
		invoiceRecord.remove();
	}
	
	public boolean isHighShool(String category) throws IDOLookupException, FinderException {
		SchoolCategory highSchool =
				((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findHighSchoolCategory();
		if (((String) highSchool.getPrimaryKey()).equals(category)) {
			return true;
		}
		return false;
	}
	
	public BatchRun getBatchRunByCategory(String category) throws IDOLookupException, FinderException {
		SchoolCategory schoolCategory =
				((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
		BatchRun batchRun = ((BatchRunHome) IDOLookup.getHome(BatchRun.class)).findBySchoolCategory(schoolCategory);
		return batchRun;
	}
	
	public int getNoProviders(BatchRun batchRun) throws RemoteException, FinderException, IDOException {
		Date period = batchRun.getPeriod();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentHeaderHome().getProviderCountForSchoolCategoryAndPeriod(schoolCategoryID, period);
	}
	
	public int getNoPlacements(BatchRun batchRun) throws RemoteException, FinderException, IDOException {
		CalendarMonth month = batchRun.getMonth();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentRecordHome().getPlacementCountForSchoolCategoryAndMonth(schoolCategoryID, month);
	}
	
	public int getTotAmountWithoutVAT(BatchRun batchRun) throws RemoteException, FinderException, IDOException {
		Date period = batchRun.getPeriod();
		String schoolCategoryID = batchRun.getSchoolCategoryID();
		return getPaymentRecordHome().getTotAmountForSchoolCategoryAndPeriod(schoolCategoryID, period);
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
		 final java.util.Date fromPeriod, java.util.Date toPeriod) {
		final Collection headers = new ArrayList ();
		final Collection custodians = new ArrayList ();
		
		// find custodians for user
		try {
			final Collection temp
					= getMemberFamilyLogic ().getCustodiansFor (user);
			if (null != temp) {
				custodians.addAll (temp);
			}
		} catch (RemoteException exception) {
			exception.printStackTrace();
		} catch (Exception e) {
			// no problem, no custodians found
		}
		
		// find invoice headers related to custodian or user
		try {
			final Collection temp
					= getInvoiceHeaderHome ().findByCustodianOrChild
					(schoolCategory, user, custodians, fromPeriod, toPeriod);
			if (null != temp) {
				headers.addAll (temp);
			}
		} catch (RemoteException exception) {
			exception.printStackTrace();
		} catch (FinderException exception) {
			// no problem, return empty array
		}
		
		return (InvoiceHeader []) headers.toArray (new InvoiceHeader [0]);
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
		} catch (RemoteException exception) {
			exception.printStackTrace();
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
	
	public Collection getPaymentRecordsByCategoryProviderAndPeriod(String category, String provider, Date period)
		throws RemoteException, FinderException {
		SchoolCategory schoolCategory =
				((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findByPrimaryKey(category);
		School school = ((SchoolHome) IDOLookup.getHome(School.class)).findByPrimaryKey(provider);
		return getPaymentRecordsByCategoryProviderAndPeriod(schoolCategory, school, period);
	}
	
	public Collection getPaymentRecordsByCategoryProviderAndPeriod(
																																 SchoolCategory category,
																																 School provider,
																																 Date period)
		throws RemoteException, FinderException {
		PaymentHeader paymentHeader =
				getPaymentHeaderHome().findBySchoolCategorySchoolPeriod(provider, category, period);
		return getPaymentRecordHome().findByPaymentHeader(paymentHeader);
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
	public InvoiceHeader createInvoiceHeader(
																					 final String schoolCategoryKey,
																					 final User createdBy,
																					 final int custodianId,
																					 final Date period)
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
	
	protected SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}
	
	private String getSignature (final User user) {
		if (null == user) return "not logged in user";
		final String firstName = user.getFirstName ();
		final String lastName = user.getLastName ();
		return (firstName != null ? firstName + " " : "")
				+ (lastName != null ? lastName : "");
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
			if (regSpecTypeName.equals ("cacc_reg_spec_type.check")) {
				final School school = record.getProvider ();
				final SchoolCategory schoolCategory = header.getSchoolCategory ();
				final Date period = header.getPeriod ();
					final PaymentRecord paymentRecord
							= createPaymentRecord
							(school, schoolCategory, period, createdBySignature, now,
							 ruleText, amount, pieceAmount, vatRule, ownPaymentPosting,
							 doublePaymentPosting, regSpecTypeName);
					record.setPaymentRecord (paymentRecord);
					record.store ();
			}
		} catch (Exception e) {
			e.printStackTrace ();
		}
		return record;
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
		 final String regSpecTypeName) throws RemoteException, CreateException {
		final char status = 'P';
		PaymentHeader paymentHeader;
		final PaymentHeaderHome paymentHeaderHome = getPaymentHeaderHome ();
		try {
			paymentHeader	= paymentHeaderHome
					.findBySchoolCategoryAndSchoolAndPeriodAndStatus
					(school, schoolCategory, period, status + "");
		} catch (FinderException e) {
			paymentHeader = paymentHeaderHome.create ();
			paymentHeader.setSchool (school);
			paymentHeader.setSchoolCategory (schoolCategory);
			paymentHeader.setPeriod (period);
			paymentHeader.setStatus (status);
			paymentHeader.store ();
		}
		final PaymentRecord paymentRecord = getPaymentRecordHome ().create ();
		paymentRecord.setCreatedBy (null != createdBy ? createdBy : "");
		paymentRecord.setDateCreated (dateCreated);
		paymentRecord.setPaymentHeader (paymentHeader);
		paymentRecord.setPaymentText (ruleText);
		paymentRecord.setStatus (status);
		paymentRecord.setPeriod (period);
		paymentRecord.setTotalAmount (null != totalAmount ? totalAmount.intValue ()
																	: 0);
		// VAT should be evaluated by a method that TL is developing
		// until that method is developed, set VAT to zero /SN
		paymentRecord.setTotalAmountVAT (0);
		paymentRecord.setPieceAmount (null != pieceAmount ? pieceAmount.intValue ()
																	: 0);
		if (null != vatType) {
			paymentRecord.setVATRuleRegulation(vatType.intValue ());
		}
		paymentRecord.setOwnPosting (ownPosting);
		paymentRecord.setDoublePosting (doublePosting);
		paymentRecord.setRuleSpecType (regSpecTypeName);
		paymentRecord.setPlacements (1);
		paymentRecord.store ();
		return paymentRecord;
	}	

	public SchoolClassMember [] getSchoolClassMembers
		(final InvoiceHeader header) {
		final Collection allPlacements = new ArrayList ();
		try {
			final MemberFamilyLogic familyBusiness = (MemberFamilyLogic)
					IBOLookup.getServiceInstance (getIWApplicationContext(),
																				MemberFamilyLogic.class);
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
	/*
	public VATRule [] getAllVatRules () throws RemoteException {
		try {
			final Collection collection = getVatRuleHome ().findAllVATRules ();
			return null == collection ? new VATRule [0]
					: (VATRule []) collection.toArray (new VATRule [0]);
		} catch (FinderException e) {
			return new VATRule [0];
		}
	}
	
	public VATRule getVatRule (int primaryKey) throws RemoteException {
		try {
			final VATRule rule = getVatRuleHome ().findByPrimaryKey
					(new Integer (primaryKey));
			return rule;
		} catch (FinderException e) {
			return null;
		}
	}*/
	
	public Collection getAllVATRuleRegulations() throws RemoteException{
		return this.getRegulationsBusiness().findAllVATRuleRegulations();
	}
	
	public Regulation getVATRuleRegulation (int primaryKey) throws RemoteException {
		//try {
			Regulation regulation = this.getRegulationsBusiness().findRegulation(primaryKey);
			return regulation;
		///} catch (FinderException e) {
		//	return null;
		//}
	}
	
	protected RegulationSpecTypeHome getRegulationSpecTypeHome ()
		throws RemoteException {
		return (RegulationSpecTypeHome)
				IDOLookup.getHome(RegulationSpecType.class);
	}
	
	public MemberFamilyLogic getMemberFamilyLogic () throws RemoteException {
		return (MemberFamilyLogic) IBOLookup.getServiceInstance(getIWApplicationContext(), MemberFamilyLogic.class);	
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
	
	public InvoiceRecordHome getInvoiceRecordHome() throws RemoteException {
		return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
	}

	protected RegulationsBusiness getRegulationsBusiness(){
		try {
			return (RegulationsBusiness)getServiceInstance(RegulationsBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e);
		}
	}
}
