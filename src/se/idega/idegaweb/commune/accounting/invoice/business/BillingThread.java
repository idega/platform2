package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.business.AccountingUtil;
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
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.VATBusiness;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;
import se.idega.util.ErrorLogger;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
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
	protected IWApplicationContext iwc;
	protected Date currentDate = new Date( System.currentTimeMillis());
	protected Date calculationDate; //Date used mainly for HighSchool
	protected SchoolCategory category = null;
	protected ExportDataMapping categoryPosting = null;
	protected BatchRun batchRunLogger=null;
	protected ErrorLogger errorRelated = null;
	protected boolean running;
	private boolean testRun;
	private School school;
	private static String IW_BUNDLE_IDENTIFIER="se.idega.idegaweb.commune.accounting";
	
	public BillingThread(Date dateInMonth, IWApplicationContext iwc, School school, boolean testRun){
		month = new CalendarMonth(dateInMonth);
		startPeriod = month.getFirstTimestamp();
		endPeriod = month.getLastTimestamp();
		calculationDate=dateInMonth;
		this.iwc = iwc;
		running = true;
		this.testRun = testRun;
		this.school = school;
	}

	public BillingThread(Date dateInMonth, IWApplicationContext iwc){
		this(dateInMonth, iwc, null, false);
	}	
	
	public static String getBatchRunSignatureKey () {
		return BATCH_TEXT;
	}
	
	public void terminate(){
		running = false;
	}
	
	public IWBundle getBundle(){
		return iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
	}
	
	public IWResourceBundle getResourceBundle(){
		Locale locale = iwc.getApplicationSettings().getDefaultLocale();
		return getBundle().getResourceBundle(locale);
	}
	
	public String getLocalizedString(String key,String defaultValue){
		return getResourceBundle().getLocalizedString(key,defaultValue);
	}

	protected PaymentHeader getPaymentHeader(School school)throws CreateException, IDOLookupException{
		PaymentHeader paymentHeader=null;
		try {
			paymentHeader = ((PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class)).
			findBySchoolCategorySchoolPeriod(school,category,calculationDate);
		} catch (FinderException e) {
			//If No header found, create it	
			paymentHeader = (PaymentHeader) IDOLookup.create(PaymentHeader.class);
			paymentHeader.setSchool (school);
			paymentHeader.setSchoolCategory(category);
			paymentHeader.setStatus(getStatus());
			IWTimestamp period = new IWTimestamp(startPeriod);
			period.setAsDate();
			paymentHeader.setPeriod(period.getDate());
			paymentHeader.store();
		}
		return paymentHeader;
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
		return createPaymentRecord(postingDetail, ownPosting, doublePosting, months, school, "");
	}
	
	/**
	 * Finds existing payment reacord or creates a new payment record if needed, 
	 * and populates the values as needed. It also creates the payment header if needed
	 *  
	 * @return the created payment record
	 * @throws CreateException
	 * @throws IDOLookupException
	 */
	protected PaymentRecord createPaymentRecord(PostingDetail postingDetail, String ownPosting, String doublePosting, float months, School school, String notes) 
			throws CreateException, IDOLookupException {

		//Get the payment header
		PaymentHeader paymentHeader = getPaymentHeader(school);		
		PaymentRecord paymentRecord;

		//Update or create the payment record
		String paymentText = postingDetail.getTerm();
		String ruleSpecType = postingDetail.getRuleSpecType();
		float amount = postingDetail.getAmount();
		//float vatPercent = postingDetail.getVATPercent();
		//float vatPercentage = vatPercent/100;
		float newTotalAmount = AccountingUtil.roundAmount(amount*months);
		float newTotalVATAmount = AccountingUtil.roundAmount(postingDetail.getVATAmount()*months);
		
		try {
			PaymentRecordHome prechome = (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
			paymentRecord = prechome.findByPaymentHeaderAndPostingStringsAndRuleSpecTypeAndPaymentTextAndMonth(paymentHeader,ownPosting,doublePosting,ruleSpecType,paymentText,month);
			
			//If it already exists, just update the changes needed.
			paymentRecord.setPlacements(paymentRecord.getPlacements()+1);

			paymentRecord.setTotalAmount(paymentRecord.getTotalAmount()+newTotalAmount);
			paymentRecord.setTotalAmountVAT(paymentRecord.getTotalAmountVAT()+newTotalVATAmount);
			paymentRecord.store();
		} catch (FinderException e1) {
			//It didn't exist, so we create it
			paymentRecord = (PaymentRecord) IDOLookup.create(PaymentRecord.class);
			//Set all the values for the payment record
			paymentRecord.setPaymentHeaderId(((Integer)paymentHeader.getPrimaryKey()).intValue());
			paymentRecord.setStatus(getStatus());
			paymentRecord.setPeriod(startPeriod.getDate());
			paymentRecord.setPaymentText(paymentText);
			paymentRecord.setDateCreated(currentDate);
			paymentRecord.setCreatedBy(BATCH_TEXT);
			paymentRecord.setPlacements(1);
			paymentRecord.setPieceAmount(postingDetail.getAmount());
			paymentRecord.setTotalAmount(newTotalAmount);
			paymentRecord.setTotalAmountVAT(newTotalVATAmount);
			paymentRecord.setRuleSpecType(ruleSpecType);
			paymentRecord.setOwnPosting(ownPosting);
			paymentRecord.setDoublePosting(doublePosting);
			int vatRuleRegulationId=postingDetail.getVatRuleRegulationId();
			if(vatRuleRegulationId!=-1){
				paymentRecord.setVATRuleRegulationId(vatRuleRegulationId);
			}
			paymentRecord.setOrderId (postingDetail.getOrderID());
			paymentRecord.setNotes(notes);
			paymentRecord.store();
		}
		return paymentRecord;
	}
	
	protected PaymentRecord createVATPaymentRecord(PaymentRecord previousPaymentRecord,PostingDetail postingDetail,float months, School school, SchoolType sType,SchoolYear sYear) 
	throws CreateException, IDOLookupException {
		try{
			Regulation vatRuleRegulation = previousPaymentRecord.getVATRuleRegulation();
			if(vatRuleRegulation!=null){
				//Get the payment header
				PaymentHeader paymentHeader = getPaymentHeader(school);		
				PaymentRecord paymentRecord;
//				System.out.println("About to create VAT payment record");
				Provider provider = new Provider(school);
				RegulationSpecType regSpecType = vatRuleRegulation.getRegSpecType();
				String ruleSpecType = regSpecType.getRegSpecType();
				//String ruleSpecType = RegSpecConstant.MOMS;
				//RegulationSpecType regSpecType;
				String[] postingStrings=null;
				//float amount = postingDetail.getAmount();
				//float vatPercent = postingDetail.getVATPercent();
				//float vatPercentage = vatPercent/100;
				//float newTotalAmount = AccountingUtil.roundAmount(amount*months);
				float newTotalVATAmount = AccountingUtil.roundAmount(postingDetail.getVATAmount()*months);				
				
				try {
					//regSpecType = this.getRegulationSpecTypeHome().findByRegulationSpecType(ruleSpecType);
					int regSpecTypeId= ((Number)regSpecType.getPrimaryKey()).intValue();
					int schoolYearId = -1;
					if(sYear!=null){
						schoolYearId = ((Number)sYear.getPrimaryKey()).intValue();
					}
					postingStrings = this.getPostingBusiness().getPostingStrings(category,sType,regSpecTypeId,provider,startPeriod.getDate(),schoolYearId);
				}
				catch (RemoteException e) {
					createNewErrorMessage(errorRelated,getLocalizedString("invoice.RemoteException","RemoteException"));
					e.printStackTrace();
				}
				catch (PostingException e) {
					errorRelated.append(getLocalizedString("billingthread_category","Category")+": "+category);
					errorRelated.append(getLocalizedString("billingthread_school_type","School Type")+":"+sType);
					errorRelated.append(getLocalizedString("billingthread_provider","Provider")+":"+provider);
					errorRelated.append(getLocalizedString("billingthread_period","Period")+":"+startPeriod.getDate());
					errorRelated.append(getLocalizedString("invoice.schoolyear","Schoolyear")+":"+sYear.getName());
					createNewErrorMessage(errorRelated,getLocalizedString("invoice.PostingException","PostingException"));
					e.printStackTrace();
				}	
				
				String ownPosting = postingStrings[0];
				String doublePosting = postingStrings[1];
				
				//Update or create the payment record
				//String ruleText = postingDetail.getTerm();
				//String ruleSpecType = postingDetail.getRuleSpecType();
		
				String paymentText = vatRuleRegulation.getName();
				//float newamount=previousPaymentRecord.getTotalAmountVAT();
				float vatAmount=0;
				//float vatPercent=0;
				
				try {
					PaymentRecordHome prechome = (PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class);
					paymentRecord = prechome.findByPaymentHeaderAndPostingStringsAndVATRuleRegulationAndPaymentTextAndMonth(paymentHeader,ownPosting,doublePosting,null,paymentText,month);
					
					//paymentRecord.setPlacements(paymentRecord.getPlacements()+1);
		
					paymentRecord.setTotalAmount(AccountingUtil.roundAmount(paymentRecord.getTotalAmount()+newTotalVATAmount));
					paymentRecord.setTotalAmountVAT(vatAmount);
					paymentRecord.store();
				} catch (FinderException e1) {
					//It didn't exist, so we create it
					paymentRecord = (PaymentRecord) IDOLookup.create(PaymentRecord.class);
					//Set all the values for the payment record
					paymentRecord.setPaymentHeaderId(((Integer)paymentHeader.getPrimaryKey()).intValue());
					paymentRecord.setStatus(getStatus());
					paymentRecord.setPeriod(startPeriod.getDate());
					paymentRecord.setPaymentText(paymentText);
					paymentRecord.setDateCreated(currentDate);
					paymentRecord.setCreatedBy(BATCH_TEXT);
					paymentRecord.setPlacements(0);
					paymentRecord.setPieceAmount(0);
					paymentRecord.setTotalAmount(newTotalVATAmount);
					paymentRecord.setTotalAmountVAT(vatAmount);
					paymentRecord.setRuleSpecType(ruleSpecType);
					paymentRecord.setOwnPosting(ownPosting);
					paymentRecord.setDoublePosting(doublePosting);
					//paymentRecord.setVATRuleRegulation(postingDetail.getVatRuleRegulationId());
					//paymentRecord.setOrderId (postingDetail.getOrderID());
					paymentRecord.store();
				}
				return paymentRecord;
			}
		}
		catch(NullPointerException npe){
		}
		return null;
		
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
		return getInvoiceBusiness ().createInvoiceRecord
				(paymentRecord, placement, postingDetail, checkPeriod,
				 startPlacementDate, endPlacementDate, BATCH_TEXT);
	}
    
	/**
	 * Creates the VATpostings for private providers
	 */
	/*protected void calcVAT(){
		try {
			Iterator paymentHeaderIter = getPaymentHeaderHome().findBySchoolCategoryAndPeriodForPrivate(category,calculationDate).iterator();
			Collection vatRuleRegulations = getRegulationsBusiness().findAllVATRuleRegulations();
			while(paymentHeaderIter.hasNext()){
				PaymentHeader paymentHeader = (PaymentHeader)paymentHeaderIter.next();
				School school = paymentHeader.getSchool();
				school.getSchoolTypes();
				Provider provider;
				try {
					Iterator vatRuleRegulationsIter = vatRuleRegulations.iterator();
					while(vatRuleRegulationsIter.hasNext()){
						Regulation vatRuleRegulation = (Regulation)vatRuleRegulationsIter.next();
						int amount = getPaymentRecordHome().getTotalVATAmountForPaymentHeaderAndMonthAndVATRuleRegulation(paymentHeader,this.month,vatRuleRegulation);
						RegulationSpecType regSpecType = vatRuleRegulation.getRegSpecType();
						String[] postingsStrings = getPostingBusiness().getPostingStrings(
								category, schoolClassMember.getSchoolType(), ((Integer)regSpecType.getPrimaryKey()).intValue(), provider,calculationDate);
						
						
					}
				} catch (RemoteException e1) {
					if(errorRelated!=null){
						createNewErrorMessage(errorRelated.toString(),"invoice.DBError");
					}else{
						createNewErrorMessage(paymentHeader.getSchool().getName(),"invoice.DBError");
					}
					e1.printStackTrace();
				} 
				//catch (FinderException e1) {
				//	if(errorRelated!=null){
				//		createNewErrorMessage(errorRelated.toString(),"invoice.DBError_No_VAT_found");
				//	}else{
				//		createNewErrorMessage(paymentHeader.getSchool().getName(),"invoice.DBError_No_VAT_found");
				//	}
				//	e1.printStackTrace();
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.VATCalculationAborted");
		}
	}*/

	protected PlacementTimes calculateTime(Date start, Date end){
		return calculateTime(start, end, true);
	}
	
	protected PlacementTimes calculateTime(Date start, Date end, boolean displayWarning){
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
		PlacementTimes placementTimes = new PlacementTimes (firstCheckDay, lastCheckDay);
		if(placementTimes.getDays()<0f && displayWarning){
			errorRelated.append(getLocalizedString("invoice.PlacementDays","Placement days")+":"+placementTimes.getDays());
			createNewErrorMessage(errorRelated, getLocalizedString("invoice.WarningNegativePlacementTimeFound","Warning. Negative placement time found"));
		}
		return placementTimes;
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
//			System.out.println("About to enter a batch run error to header "+batchRunLogger.getPrimaryKey()+"  "+related+"  "+desc+"  "+errorOrder);
			BatchRunError error = (BatchRunError) IDOLookup.create(BatchRunError.class);
			error.setBatchRunID(((Integer)batchRunLogger.getPrimaryKey()).intValue());
			if (related.length () > 990) related = related.substring (0, 990);
			if (desc.length () > 990) desc = desc.substring (0, 990);
			error.setRelated(related);
			error.setDescription(desc);
			error.setOrder(errorOrder);
			error.setTest(testRun);
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
			String related = errorLogger.toStringForWeb();
			if(related.length()>=1000){
				related = related.substring(0,999);
			}
			error.setRelated(related);
			error.setDescription(desc);
			error.setOrder(errorOrder);
			error.setTest(testRun);			
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
		try {
			batchRunLogger = ((BatchRunHome) IDOLookup.getHome(BatchRun.class)).findBySchoolCategory(category, testRun);
			
			try{
				Iterator errorIter = ((BatchRunErrorHome) IDOLookup.getHome(BatchRunError.class)).findByBatchRun(batchRunLogger, testRun).iterator();
				while (errorIter.hasNext()) {
					BatchRunError error = (BatchRunError) errorIter.next();
					try {
	//					System.out.println("Removing BatchRunLogError");
						error.remove();
					} catch (EJBException e) {
						createNewErrorMessage(getLocalizedString("invoice.batchrun","batchrun"),
								getLocalizedString("invoice.batchrun.cannotRemoveOldBatchrunDataEJBException","Cannot remove old batchrun data, EJBException"));
						//If it cant be removed, it is just left... Not much to do about it.
						e.printStackTrace();
					} catch (RemoveException e) {
						createNewErrorMessage(getLocalizedString("invoice.batchrun","batchrun"),
								getLocalizedString("invoice.batchrun.cannotRemoveOldBatchrunDataRemoveException","Cannot remove old batchrun data, RemoveException"));
						//If it cant be removed, it is just left... Not much to do about it.
						e.printStackTrace();
					}
				}
			}catch (FinderException ex){
				
			}
			
		} catch (FinderException e1) {
			//Excepiton OK We just create it instead
			batchRunLogger = (BatchRun) IDOLookup.create(BatchRun.class);
			batchRunLogger.setSchoolCategoryID(category);
		}
		batchRunLogger.setPeriod(startPeriod.getDate());
		batchRunLogger.setStart(IWTimestamp.getTimestampRightNow());
		batchRunLogger.setTest(testRun);
		batchRunLogger.setEnd(null);
		batchRunLogger.store();
	}
	
	protected boolean hasPlacements() throws IDOException, RemoteException, EJBException {
		if (isTestRun()){
			return false;
		}
		return getPaymentRecordHome().getPlacementCountForSchoolCategoryAndMonth((String) category.getPrimaryKey(), month) > 0;
	}
	
	protected void removePreliminaryInformation(CalendarMonth month, String schoolCategory) throws IDOLookupException, RemoteException, RemoveException {
//		If this is a test run, we should not do anything with other schools data. 
//		This schools test record will be deleted in TestPosts.handleSave().
		
		if (! isTestRun()){ 
			InvoiceBusiness invoiceBusiness = (InvoiceBusiness)IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
	//		invoiceBusiness.removePreliminaryInvoice(month, schoolCategory);
			invoiceBusiness.removePreliminaryPayment(month, schoolCategory);
		}
	}

	protected void batchRunLoggerDone(){
		if(batchRunLogger!=null){
			if(!running){
				createNewErrorMessage(getLocalizedString("invoice.batchrun","batchrun"),
						getLocalizedString("invoice.Run_terminated_by_user","Run terminated by user"));
			}
			batchRunLogger.setEnd(IWTimestamp.getTimestampRightNow());
			batchRunLogger.store();
		}
	}

	/**
	 * Adds to the condition ArrayList according to the schoolClassMemeber
	 * Default is no change. Overridden by PymentThreadHighSchool
	 * 
	 * @param schoolClassMember
	 * @param conditions
	 */
	abstract protected void setStudyPath(SchoolClassMember schoolClassMember, ArrayList conditions);
	
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

	protected InvoiceBusiness getInvoiceBusiness() throws RemoteException {
		return (InvoiceBusiness) IBOLookup.getServiceInstance(iwc, InvoiceBusiness.class);
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
	
	public School getSchool(){
		return school; 
	}
	
	public boolean isTestRun(){
		return testRun;
	}
	
	private char getStatus(){
		if (isTestRun()) {
			return ConstantStatus.TEST;			
		} else {
			if(categoryPosting.getProviderAuthorization()){
				return ConstantStatus.BASE;
			} else {
				return ConstantStatus.PRELIMINARY;
			}		
		}		
	}
}
