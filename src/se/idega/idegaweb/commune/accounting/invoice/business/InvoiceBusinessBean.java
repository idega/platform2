package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusinessHome;
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
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.data.IDOLookup;
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
	
	/**
	 * spawns a new thread and starts the execution of the posting calculation and then returns
	 * @param startPeriod
	 * @param endPeriod
	 */
	public void startPostingBatch(Date month){
		//TODO (JJ) change this to use only month, not from and to.
		startPeriod = new IWTimestamp(month);
		startPeriod.setDay(1);
		endPeriod = new IWTimestamp(startPeriod);
		endPeriod.addMonths(1);
		new Thread(this).start();
	}
	
	private ChildCareContract contract;
	private PostingDetail postingDetail;
	private int days;
	private Date currentDate = new Date( new java.util.Date().getTime());
	private IWTimestamp time, startTime, endTime;
	private ExportDataMapping categoryPosting;
	private int childcare = 0;
	private int check = 0;
	private School provider;
	/**
	 * Does the acctual work on the batch process
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Collection contractArray = new ArrayList();
		Collection regulationArray = new ArrayList();
		User custodian;
		Age age;
		float months;
		int hours;
		float totalSum;


		try {
			//TODO (JJ) Probably need to remove the ref to KeyMappingBMPBean. But to what???
//			iwc.g
/*
			int childcare = getKeyMappingHome().findValueByCategoryAndKey(
				KeyMappingBMPBean.CAT_ACTIVITY,KeyMappingBMPBean.KEY_CHILDCARE).getValue();
			int check = getKeyMappingHome().findValueByCategoryAndKey(
				KeyMappingBMPBean.CAT_REG_SPEC,KeyMappingBMPBean.KEY_CHECK).getValue();
*/	
			// **Flag all contracts as 'not processed'

			SchoolCategory childcareCategory = ((SchoolCategoryHome) IDOLookup.getHome(SchoolCategoryHome.class)).findChildcareCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).findByPrimaryKeyIDO(childcareCategory.getPrimaryKey());
			
			
			contractArray = getChildCareContractHome().findByDateRange(startPeriod.getDate(), endPeriod.getDate());
			
			Iterator contractIter = contractArray.iterator();
	
			//Loop through all contracts
			while(contractIter.hasNext())
			{
				contract = (ChildCareContract)contractIter.next();
			
				// **Fetch invoice receiver
				custodian = contract.getApplication().getOwner();
				//**Fetch the reference at the provider
				provider = contract.getApplication().getProvider();
				// **Create the invoice header
				//TODO (JJ) This should not always be done! Sometimes the header might already be created...
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
			
				// **Create the invoice record
				createInvoiceRecord(invoiceHeader, HOURS_PER_WEEK);
				
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
					createInvoiceRecord(invoiceHeader, null);
					
					//TODO (JJ) Have to set the status of the invoiceHeader as well

					totalSum += postingDetail.getAmount();

				}
				//Make sure that the sum is not less than 0
				if(totalSum<0){
//					subvention += totalSum;
					//TODO (JJ) have to create some sort of record reference to the subvention row.
				}

		
			}
				
		} catch (RemoteException e1) {
//			TODO (JJ) Auto-generated catch block
			e1.printStackTrace();
		} catch (CreateException e) {
			// TODO (JJ) Auto-generated catch block
			e.printStackTrace();
		} catch (PostingParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private float createInvoiceRecord(InvoiceHeader invoiceHeader, String header) throws CreateException, PostingParametersException, RemoteException{
		String ownPosting, doublePosting, providerOwnPosting, providerDoublePosting;

		InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
		invoiceRecord.setInvoiceHeader(invoiceHeader);
		//TODO (JJ) set the reference to utbetalningsposten
		invoiceRecord.setProviderId(provider);
		invoiceRecord.setContractId(contract.getContractID());
		if(header != null){
			invoiceRecord.setInvoiceText(header);
		} else {
			invoiceRecord.setInvoiceText(postingDetail.getTerm());
		}
		invoiceRecord.setRuleText(postingDetail.getTerm());
		invoiceRecord.setDays(days);
		invoiceRecord.setPeriodStartCheck(startPeriod.getDate());
		invoiceRecord.setPeriodEndCheck(endPeriod.getDate());
		invoiceRecord.setPeriodStartPlacement(startTime.getDate());
		invoiceRecord.setPeriodEndPlacement(endTime.getDate());
		invoiceRecord.setDateCreated(currentDate);
		invoiceRecord.setCreatedBy(BATCH_TEXT);
		invoiceRecord.setAmount(postingDetail.getAmount());
		invoiceRecord.setAmountVAT(postingDetail.getVat());
		invoiceRecord.setVATType(postingDetail.getVatRegulationID());
		invoiceRecord.setRuleSpecType(postingDetail.getRuleSpecType());
		//TODO (JJ) get the posting strings
		//Set the posting strings
		PostingBusiness postingBusiness = getPostingBusinessHome().create();
		PostingParameters parameters = postingBusiness.getPostingParameter(
			new Date(new java.util.Date().getTime()), childcare, check, 0, 0);

		ownPosting = parameters.getPostingString();
		//TODO (JJ) providerOwnPosting = contract.getApplication().getProvider().getPrimaryKey();
		providerOwnPosting = "";
		ownPosting = postingBusiness.generateString(ownPosting,providerOwnPosting,currentDate);
		ownPosting = postingBusiness.generateString(ownPosting,categoryPosting.getAccount(),currentDate);
				
		invoiceRecord.setOwnPosting(ownPosting);
		doublePosting = parameters.getDoublePostingString();
		//TODO (JJ) providerDoublePosting = contract.getApplication().getProvider().getPrimaryKey();
		providerDoublePosting = "";
		doublePosting = postingBusiness.generateString(doublePosting,providerDoublePosting,currentDate);
		doublePosting = postingBusiness.generateString(doublePosting,categoryPosting.getCounterAccount(),currentDate);
		invoiceRecord.setOwnPosting(doublePosting);
		invoiceRecord.store();
		
		return postingDetail.getAmount();
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
