package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunError;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunErrorHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.MissingMandatoryFieldException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.regulations.business.IntervalConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusinessHome;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionParameter;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;

/**
 * @author Joakim
 *
 */

public class InvoiceBusinessBean implements Runnable{

	private static final String BATCH_TEXT = "invoice.batchrun";		//Localize this text in the user interface
	private static final String HOURS_PER_WEEK = "tim/v";		//Localize this text in the user interface
	private IWTimestamp startPeriod;
	private IWTimestamp endPeriod;
	private ChildCareContract contract;
	private PostingDetail postingDetail;
//	private PaymentRecord paymentRecord;
	private int days;
	private Date currentDate = new Date( new java.util.Date().getTime());
	private IWTimestamp time, startTime, endTime;
	private ExportDataMapping categoryPosting;
	private int childcare = 0;
	private int check = 0;
	private int order;
	private School school;
	private float months;
	
	/**
	 * spawns a new thread and starts the execution of the posting calculation and then returns
	 * @param startPeriod
	 * @param endPeriod
	 */
	public void startPostingBatch(Date month){
		startPeriod = new IWTimestamp(month);
		startPeriod.setDay(1);
		endPeriod = new IWTimestamp(startPeriod);
		endPeriod.addMonths(1);
		
		//Spawn batch thread and return
		new Thread(this).start();
	}
	
	/**
	 * Does the acctual work on the batch process
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Collection contractArray = new ArrayList();
		Collection regulationArray = new ArrayList();
		User custodian;
		Age age;
		int hours;
		float totalSum;
		InvoiceRecord invoiceRecord, subvention;


		try {
			// **Flag all contracts as 'not processed'
			
			setSiblingOrder();

			SchoolCategory childcareCategory = ((SchoolCategoryHome) IDOLookup.getHome(SchoolCategoryHome.class)).findChildcareCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).findByPrimaryKeyIDO(childcareCategory.getPrimaryKey());
			
			
			contractArray = getChildCareContractHome().findByDateRange(startPeriod.getDate(), endPeriod.getDate());
			
			Iterator contractIter = contractArray.iterator();
			order = 0;
	
			//Loop through all contracts
			while(contractIter.hasNext())
			{
				contract = (ChildCareContract)contractIter.next();
			
				// **Fetch invoice receiver
				custodian = contract.getApplication().getOwner();
				//**Fetch the reference at the provider
				school = contract.getApplication().getProvider();
				// **Get or create the invoice header
				InvoiceHeader invoiceHeader;
				try{
					invoiceHeader = getInvoiceHeaderHome().findByCustodian(custodian);
				} catch (FinderException e) {
					//No header was found so we have to create it
					invoiceHeader = getInvoiceHeaderHome().create();
					//Fill in all the field available at this times
					invoiceHeader.setSchoolCagtegoryID(childcareCategory);
					invoiceHeader.setPeriod(startPeriod.getDate());
					invoiceHeader.setCustodianId(custodian);
					invoiceHeader.setDateCreated(currentDate);
					invoiceHeader.setCreatedBy(BATCH_TEXT);
					invoiceHeader.setOwnPosting(categoryPosting.getAccount());
					invoiceHeader.setDoublePosting(categoryPosting.getCounterAccount());
					if(categoryPosting.getProviderAuthorization()){
						invoiceHeader.setStatus(invoiceHeader.getStatusBase());
					} else {
						invoiceHeader.setStatus(invoiceHeader.getStatusPreliminary());
					}
				}
				
				// **Calculate how big part of time period this contract is valid for
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

				totalSum = 0;
				subvention = null;
				//
				//Get the check for the contract
				//
				RegulationsBusiness regBus = getRegulationsBusinessHome().create();
				
				//Get all the parameters needed to select the correct contract
				//TODO (JJ) Tryggvi, Goran and Anders trying to figure out how this should work!
				String childcareType = "";
				hours = contract.getCareTime();
				age = new Age(contract.getChild().getDateOfBirth());
				ArrayList conditions = new ArrayList();
				conditions.add(new ConditionParameter(IntervalConstant.ACTIVITY,childcareType));
				conditions.add(new ConditionParameter(IntervalConstant.HOURS,new Integer(hours)));
				conditions.add(new ConditionParameter(IntervalConstant.AGE,new Integer(age.getYears())));

				//Select a specific row from the regulation, given the following restrictions
				postingDetail = regBus.
				getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(
					childcareCategory.getLocalizedKey(),//The ID that selects barnomsorg in the regulation
					PaymentFlowConstant.OUT, 		//The payment flow is out
					currentDate,					//Current date to select the correct date range
					RegSpecConstant.CHECK,			//The ruleSpecType shall be Check
					RuleTypeConstant.DERIVED,		//The conditiontype
					conditions,						//The conditions that need to fulfilled
					totalSum,						//Sent in to be used for "Specialutrakning
					contract);						//Sent in to be used for "Specialutrakning
			
				try{
					PaymentRecord paymentRecord = createPaymentRecord();			//MUST create payment record first, since it is used in invoice record
					// **Create the invoice record
					invoiceRecord = createInvoiceRecordForCheck(invoiceHeader, 
							school.getName()+", "+contract.getCareTime()+" "+HOURS_PER_WEEK, paymentRecord);
				
					totalSum = postingDetail.getAmount();

					//Get all the rules for this contract
					//TODO (JJ) This is a func that Thomas will provide.
					regulationArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
					childcareCategory.getLocalizedKey(),//The ID that selects barnomsorg in the regulation
						PaymentFlowConstant.IN, 		//The payment flow is out
						currentDate,					//Current date to select the correct date range
						RuleTypeConstant.DERIVED,		//The conditiontype
						conditions						//The conditions that need to fulfilled
						);


					Iterator regulationIter = regulationArray.iterator();
					while(regulationIter.hasNext())
					{
						postingDetail = regBus.getPostingDetailForContract(
							totalSum,
							contract);

						// **Create the invoice record
						invoiceRecord = createInvoiceRecord(invoiceHeader);
					
						if(postingDetail.getRuleSpecType()== RegSpecConstant.SUBVENTION){
							subvention = invoiceRecord;
						}

						totalSum += postingDetail.getAmount();

					}
					//Make sure that the sum is not less than 0
					if(totalSum<0){
						if(subvention!=null){
							subvention.setAmount(subvention.getAmount()+totalSum);
							subvention.store();
						} else {
							createNewErrorMessage(postingDetail.getTerm(),"invoice.noSubventionFound");
						}
					}
				} catch(MissingMandatoryFieldException e){
					createNewErrorMessage(postingDetail.getTerm(),"invoice.DBSetupProblem");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			createNewErrorMessage(postingDetail.getTerm(),"invoice.DBSetupProblem");
			e.printStackTrace();
		}
	}
	
	/**
	 * @param invoiceRecord
	 */
	private PaymentRecord createPaymentRecord() throws IDOLookupException, CreateException {
		PaymentRecord paymentRecord = ((PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class)).create();
		paymentRecord.setAmount(postingDetail.getAmount()*months);
		//TODO (JJ) set the rest of the parameters needed. Check first with Lotta what really needs to go in here.
		paymentRecord.store();
		return paymentRecord;
	}

	private void setSiblingOrder(){
		//TODO (JJ) Insert code here
	}
	
	private void createNewErrorMessage(String related, String desc){
		try {
			BatchRunError error = ((BatchRunErrorHome) IDOLookup.getHome(BatchRunError.class)).create();
			error.setRelated(related);
			error.setDescription(desc);
			error.setOrder(order);
			order++;
		} catch (IDOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private InvoiceRecord createInvoiceRecordForCheck(InvoiceHeader invoiceHeader, String header, PaymentRecord paymentRecord) throws PostingParametersException, PostingException, RemoteException, CreateException, MissingMandatoryFieldException{
		InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
		invoiceRecord.setInvoiceHeader(invoiceHeader);
		invoiceRecord.setInvoiceText(header);
		//TODO (JJ) set the reference to utbetalningsposten
		invoiceRecord.setPaymentRecordId(paymentRecord);
		return createInvoiceRecordSub(invoiceRecord);
	}
	
	private InvoiceRecord createInvoiceRecord(InvoiceHeader invoiceHeader) throws PostingParametersException, PostingException, RemoteException, CreateException, MissingMandatoryFieldException{
		InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
		invoiceRecord.setInvoiceHeader(invoiceHeader);
		invoiceRecord.setInvoiceText(postingDetail.getTerm());
		return createInvoiceRecordSub(invoiceRecord);
	}
	
	private InvoiceRecord createInvoiceRecordSub(InvoiceRecord invoiceRecord) throws CreateException, PostingParametersException, PostingException, RemoteException, MissingMandatoryFieldException{
		String ownPosting, doublePosting;

		invoiceRecord.setProviderId(school);
		invoiceRecord.setContractId(contract.getContractID());
		invoiceRecord.setRuleText(postingDetail.getTerm());
		invoiceRecord.setDays(days);
		invoiceRecord.setPeriodStartCheck(startPeriod.getDate());
		invoiceRecord.setPeriodEndCheck(endPeriod.getDate());
		invoiceRecord.setPeriodStartPlacement(startTime.getDate());
		invoiceRecord.setPeriodEndPlacement(endTime.getDate());
		invoiceRecord.setDateCreated(currentDate);
		invoiceRecord.setCreatedBy(BATCH_TEXT);
		invoiceRecord.setAmount(postingDetail.getAmount()*months);
		invoiceRecord.setAmountVAT(postingDetail.getVat()*months);
		invoiceRecord.setVATType(postingDetail.getVatRegulationID());
		invoiceRecord.setRuleSpecType(postingDetail.getRuleSpecType());
		//TODO (JJ) get the posting strings
		//Set the posting strings
		PostingBusiness postingBusiness = getPostingBusinessHome().create();
		PostingParameters parameters = postingBusiness.getPostingParameter(
			new Date(new java.util.Date().getTime()), childcare, check, 0, 0);

		ownPosting = parameters.getPostingString();
		Provider provider = new Provider(((Integer)contract.getApplication().getProvider().getPrimaryKey()).intValue());
		ownPosting = postingBusiness.generateString(ownPosting,provider.getOwnPosting(),currentDate);
		ownPosting = postingBusiness.generateString(ownPosting,categoryPosting.getAccount(),currentDate);
		postingBusiness.validateString(ownPosting,currentDate);
		invoiceRecord.setOwnPosting(ownPosting);
	
		doublePosting = parameters.getDoublePostingString();
		doublePosting = postingBusiness.generateString(doublePosting,provider.getDoublePosting(),currentDate);
		doublePosting = postingBusiness.generateString(doublePosting,categoryPosting.getCounterAccount(),currentDate);
		postingBusiness.validateString(doublePosting,currentDate);
		invoiceRecord.setDoublePosting(doublePosting);
		invoiceRecord.store();
		
		return invoiceRecord;
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

	public ChildCareContractHome getChildCareContractHome() throws RemoteException {
		return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
	}

	public InvoiceHeaderHome getInvoiceHeaderHome() throws RemoteException {
		return (InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class);
	}

	public InvoiceRecordHome getInvoiceRecordHome() throws RemoteException {
		return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
	}

	public PostingBusinessHome getPostingBusinessHome() throws RemoteException {
		return (PostingBusinessHome) IDOLookup.getHome(PostingBusiness.class);
	}

	public RegulationsBusinessHome getRegulationsBusinessHome() throws RemoteException {
		return (RegulationsBusinessHome) IDOLookup.getHome(RegulationsBusiness.class);
	}

}
