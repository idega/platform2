package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
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
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATException;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.util.CalendarMonth;
import com.idega.util.IWTimestamp;

/**
 * Abstract class that all billing and invoicing threads will extend.
 * A lot of logic is the same for all of the billing and invoicing logic,
 * and that is located in this class
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.InvoiceChildcareThread
 */
public abstract class BillingThread extends Thread{
	protected Logger log = Logger.getLogger(this.getClass().getName());
	protected static final String BATCH_TEXT = "invoice.batchrun";		//Localize this text in the user interface
	protected int errorOrder;
	protected CalendarMonth month;	//Holds working month
	protected IWTimestamp startPeriod;	//Holds first day of period
	protected IWTimestamp endPeriod;		//Holds last day of period
	protected IWContext iwc;
	protected Date currentDate = new Date( System.currentTimeMillis());
	protected SchoolCategory category = null;
	protected ExportDataMapping categoryPosting = null;
	protected BatchRun batchRunLogger=null;
	protected ErrorLogger errorRelated = null;
	
	public BillingThread(Date dateInMonth, IWContext iwc){
		month = new CalendarMonth(dateInMonth);
		startPeriod = month.getFirstTimestamp();
		endPeriod = month.getLastTimestamp();
		this.iwc = iwc;
	}
	
	public static String getBathRunSignatureKey () {
		return BATCH_TEXT;
	}

	/**
	 * Finds existing payment reacord or creates a new payment record if needed, 
	 * and populates the values as needed. It also creates the payment header if needed
	 *  
	 * @return the created payment record
	 * @throws CreateException
	 * @throws IDOLookupException
	 */
	protected PaymentRecord createPaymentRecord(PostingDetail postingDetail, String ownPosting, String doublePosting, float months, School school) 
			throws CreateException, IDOLookupException {

		PaymentHeader paymentHeader;
		PaymentRecord paymentRecord;
		System.out.println("About to create payment record");
		//Get the payment header
		try {
			paymentHeader = ((PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class)).
					findBySchoolCategorySchoolPeriod(school,category,currentDate);
		} catch (FinderException e) {
			//If No header found, create it	
			paymentHeader = (PaymentHeader) IDOLookup.create(PaymentHeader.class);
			paymentHeader.setSchoolID(school);
			paymentHeader.setSchoolCategoryID(category);
			if(categoryPosting.getProviderAuthorization()){
				paymentHeader.setStatus(ConstantStatus.BASE);
			} else {
				paymentHeader.setStatus(ConstantStatus.PRELIMINARY);
			}
			IWTimestamp period = new IWTimestamp(currentDate);
			period.setAsDate();
			period.setDay(1);
			paymentHeader.setPeriod(period.getDate());
			paymentHeader.store();
		}
		//Update or create the payment record
		String ruleText = postingDetail.getTerm();
		String ruleSpecType = postingDetail.getRuleSpecType();
		
		try {
			PaymentRecordHome prechome = (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
			paymentRecord = prechome.findByPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(ownPosting,doublePosting,ruleSpecType,ruleText,month);
			
			//If it already exists, just update the changes needed.
			paymentRecord.setPlacements(paymentRecord.getPlacements()+1);

			paymentRecord.setTotalAmount(paymentRecord.getTotalAmount()+postingDetail.getAmount()*months);
			paymentRecord.setTotalAmountVAT(paymentRecord.getTotalAmountVAT()+postingDetail.getVat()*months);
			paymentRecord.store();
		} catch (FinderException e1) {
			//It didn't exist, so we create it
			paymentRecord = (PaymentRecord) IDOLookup.create(PaymentRecord.class);
			//Set all the values for the payment record
			paymentRecord.setPaymentHeader(((Integer)paymentHeader.getPrimaryKey()).intValue());
			if(categoryPosting.getProviderAuthorization()){
				paymentRecord.setStatus(ConstantStatus.BASE);
			} else {
				paymentRecord.setStatus(ConstantStatus.PRELIMINARY);
			}
			paymentRecord.setPeriod(startPeriod.getDate());
			paymentRecord.setPaymentText(ruleText);
			paymentRecord.setDateCreated(currentDate);
			paymentRecord.setCreatedBy(BATCH_TEXT);
			paymentRecord.setPlacements(1);
			paymentRecord.setPieceAmount(postingDetail.getAmount());
			paymentRecord.setTotalAmount(postingDetail.getAmount()*months);
			paymentRecord.setTotalAmountVAT(postingDetail.getVat()*months);
			paymentRecord.setRuleSpecType(ruleSpecType);
			paymentRecord.setOwnPosting(ownPosting);
			paymentRecord.setDoublePosting(doublePosting);
			paymentRecord.setVATType(postingDetail.getVatRegulationID());
			paymentRecord.store();
		}
		return paymentRecord;
	}
	
	protected InvoiceRecord createInvoiceRecord
		(final PaymentRecord paymentRecord, final SchoolClassMember placement,
		 final PostingDetail postingDetail, PlacementTimes checkPeriod)
		throws RemoteException, CreateException {
		Date startDate = null != placement	&& null != placement.getRegisterDate ()
				? new Date (placement.getRegisterDate ().getTime ()) : null;
		Date endDate = null != placement && null != placement.getRemovedDate ()
				? new Date (placement.getRemovedDate ().getTime ()) : null;
		return createInvoiceRecord (paymentRecord, placement, postingDetail,
																checkPeriod, startDate, endDate);
	}

	protected InvoiceRecord createInvoiceRecord
		(final PaymentRecord paymentRecord, final SchoolClassMember placement,
		 final PostingDetail postingDetail, PlacementTimes checkPeriod,
		 final Date startPlacementDate, final Date endPlacementDate)
		throws RemoteException, CreateException {
		final InvoiceRecord result = getInvoiceRecordHome ().create ();
		result.setAmount (checkPeriod.getMonths () * postingDetail.getAmount ());
		result.setCreatedBy (BATCH_TEXT);
		result.setDateCreated (new Date (System.currentTimeMillis ()));
		result.setDays (checkPeriod.getDays ());
		if (null != paymentRecord) {
			result.setPaymentRecord (paymentRecord);
		}
		if (null != postingDetail.getTerm()) {
			result.setRuleText (postingDetail.getTerm());
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
		try {
			final RegulationSpecType regSpecType
					= getRegulationSpecTypeHome ().findByRegulationSpecType
					(postingDetail.getRuleSpecType ());
			result.setRegSpecType (regSpecType);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		result.store ();
		return result;
	}
    
	/**
	 * Creates the VATpostings for private providers
	 */
	protected void calcVAT(){
		try {
			Iterator paymentHeaderIter = getPaymentHeaderHome().findBySchoolCategoryAndPeriodForPrivate(category,currentDate).iterator();
			while(paymentHeaderIter.hasNext()){
				PaymentHeader paymentHeader = (PaymentHeader)paymentHeaderIter.next();
				Iterator paymentRecordIter;
				try {
					paymentRecordIter = getPaymentRecordHome().findByPaymentHeader(paymentHeader).iterator();
					while(paymentRecordIter.hasNext()){
						PaymentRecord paymentRecord = (PaymentRecord) paymentRecordIter.next();
						VATRegulation vatRegulation;
						try {
							vatRegulation = getVATBusiness().getVATRegulation(paymentRecord.getVATType());
							float vat = paymentRecord.getTotalAmount() * vatRegulation.getVATPercent();
							paymentRecord.setTotalAmountVAT(-vat);
						} catch (VATException e) {
							if(errorRelated!=null){
								createNewErrorMessage(errorRelated.toString(),"invoice.VATError");
							}else{
								createNewErrorMessage(paymentRecord.getPaymentText(),"invoice.VATError");
							}
							e.printStackTrace();
						} catch (RemoteException e) {
							if(errorRelated!=null){
								createNewErrorMessage(errorRelated.toString(),"invoice.DBError");
							}else{
								createNewErrorMessage(paymentRecord.getPaymentText(),"invoice.DBError");
							}
							e.printStackTrace();
						}
					}
				} catch (RemoteException e1) {
					if(errorRelated!=null){
						createNewErrorMessage(errorRelated.toString(),"invoice.DBError");
					}else{
						createNewErrorMessage(paymentHeader.getSchool().getName(),"invoice.DBError");
					}
					e1.printStackTrace();
				} catch (FinderException e1) {
					if(errorRelated!=null){
						createNewErrorMessage(errorRelated.toString(),"invoice.DBError_No_VAT_found");
					}else{
						createNewErrorMessage(paymentHeader.getSchool().getName(),"invoice.DBError_No_VAT_found");
					}
					e1.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.VATCalculationAborted");
		}
	}

	protected PlacementTimes calculateTime(Date start, Date end){
		IWTimestamp firstCheckDay = new IWTimestamp(start);
		firstCheckDay.setAsDate();
		IWTimestamp time = new IWTimestamp(startPeriod);
		time.setAsDate();
		if(!firstCheckDay.isLaterThan(time)){
			firstCheckDay = time;
		}
		IWTimestamp lastCheckDay = new IWTimestamp(endPeriod);
		lastCheckDay.setAsDate();
		if(end!=null){
			time = new IWTimestamp(end.getTime ());
			if(!lastCheckDay.isEarlierThan(time)){
				lastCheckDay = time;
			}
		}
		return new PlacementTimes (firstCheckDay, lastCheckDay);
	}
	
	public static void main(String[] arg){
		IWTimestamp s = new IWTimestamp();
		s.setMonth(10);
	}

	/**
	 * Creates a new error log message (to be used according to fonster 33 in C&P Req.Spec.)
	 *  
	 * @param related Description of related objects
	 * @param desc Description possible errors
	 */
	protected void createNewErrorMessage(String related, String desc){
		try {
			System.out.println("About to enter new error message "+related);
			System.out.println("About to enter a batch run error to header "+batchRunLogger.getPrimaryKey()+"  "+related+"  "+desc+"  "+errorOrder);
			BatchRunError error = (BatchRunError) IDOLookup.create(BatchRunError.class);
			error.setBatchRunID(((Integer)batchRunLogger.getPrimaryKey()).intValue());
			if (related.length () > 990) related = related.substring (1, 990);
			if (desc.length () > 990) desc = desc.substring (1, 990);
			error.setRelated(related);
			error.setDescription(desc);
			error.setOrder(errorOrder);
			error.store();
			errorOrder++;
		} catch (Exception e) {
			System.out.println("Exception so complicated that it wasn't even possible to create an error message in the log!");
			e.printStackTrace();
		}
	}
	
	protected void createNewErrorMessage(ErrorLogger errorLogger, String desc){
		try {
			log.info("About to enter a batch run error to header "+batchRunLogger.getPrimaryKey()+"  "+errorLogger.toString()+"  "+desc+"  "+errorOrder);
			BatchRunError error = (BatchRunError) IDOLookup.create(BatchRunError.class);
			error.setBatchRunID(((Integer)batchRunLogger.getPrimaryKey()).intValue());
			error.setRelated(errorLogger.toStringForWeb());
			error.setDescription(desc);
			error.setOrder(errorOrder);
			error.store();
			errorOrder++;
		} catch (Exception e) {
			System.out.println("Exception so complicated that it wasn't even possible to create an error message in the log!");
			e.printStackTrace();
		}
	}
	
	/**
	 * The user needs to be able to see what went wrong or is possibly incorrect, so that 
	 * is logged in the BatchRun and BatchRunError tables.
	 * 
	 * @param category
	 * @throws IDOLookupException
	 * @throws CreateException
	 */
	protected void createBatchRunLogger(SchoolCategory category) throws IDOLookupException, CreateException{
		//First delete all old logging for this category
		System.out.println("Entering createBatchRunLogger");
		try {
			batchRunLogger = ((BatchRunHome) IDOLookup.getHome(BatchRun.class)).findBySchoolCategory(category);
			Iterator errorIter = ((BatchRunErrorHome) IDOLookup.getHome(BatchRunError.class)).findByBatchRun(batchRunLogger).iterator();
			System.out.println("About to remobe BatchRunErrors");
			while (errorIter.hasNext()) {
				BatchRunError error = (BatchRunError) errorIter.next();
				try {
					System.out.println("Removing BatchRunLogError");
					error.remove();
				} catch (EJBException e) {
					//If it cant be removed, it is just left... Not much to do about it.
					e.printStackTrace();
				} catch (RemoveException e) {
					//If it cant be removed, it is just left... Not much to do about it.
					e.printStackTrace();
				}
			}
		} catch (FinderException e1) {
			//Excepiton OK We just create it instead
			System.out.println("No logger found creating it instead");
			batchRunLogger = (BatchRun) IDOLookup.create(BatchRun.class);
			System.out.println("logger created");
			batchRunLogger.setSchoolCategoryID(category);
			
			System.out.println("logger stored");
		}
		batchRunLogger.setPeriod(startPeriod.getDate());
		batchRunLogger.setStart(IWTimestamp.getTimestampRightNow());
		batchRunLogger.setEnd(null);
		batchRunLogger.store();
	}
	
	protected boolean hasPlacements() throws FinderException, IDOException, RemoteException, EJBException {
		return getPaymentRecordHome().getPlacementCountForSchoolCategoryAndPeriod((String) category.getPrimaryKey(), currentDate) > 0;
	}

	protected void batchRunLoggerDone(){
		if(batchRunLogger!=null){
			batchRunLogger.setEnd(IWTimestamp.getTimestampRightNow());
			batchRunLogger.store();
		}
	}
	
	protected void finalizeBatchRunLogger(){
		batchRunLogger.setEnd(IWTimestamp.getTimestampRightNow());
	}

	protected RegulationsBusiness getRegulationsBusiness() throws RemoteException {
		return (RegulationsBusiness) IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}
	
	protected ChildCareContractHome getChildCareContractHome() throws RemoteException {
		return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
	}

	protected RegularInvoiceBusiness getRegularInvoiceBusiness() throws RemoteException {
		return (RegularInvoiceBusiness) IBOLookup.getServiceInstance(iwc, RegularInvoiceBusiness.class);
	}

	protected RegularPaymentBusiness getRegularPaymentBusiness() throws RemoteException {
		return (RegularPaymentBusiness) IBOLookup.getServiceInstance(iwc, RegularPaymentBusiness.class);
	}

	protected PostingBusiness getPostingBusiness() throws RemoteException {
		return (PostingBusiness) IBOLookup.getServiceInstance(iwc, PostingBusiness.class);
	}

	protected InvoiceHeaderHome getInvoiceHeaderHome() throws RemoteException {
		return (InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class);
	}

	protected PaymentHeaderHome getPaymentHeaderHome() throws RemoteException {
		return (PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class);
	}

	protected InvoiceRecordHome getInvoiceRecordHome() throws RemoteException {
		return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
	}

	protected PaymentRecordHome getPaymentRecordHome() throws RemoteException {
		return (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
	}

	protected VATBusiness getVATBusiness() throws RemoteException {
		return (VATBusiness) IBOLookup.getServiceInstance(iwc, VATBusiness.class);
	}

	protected RegulationSpecTypeHome getRegulationSpecTypeHome() throws RemoteException {
		return (RegulationSpecTypeHome) IDOLookup.getHome(RegulationSpecType.class);
	}
	
}
