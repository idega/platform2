package se.idega.idegaweb.commune.accounting.invoice.business;

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
	
	/**
	 * Does the acctual work on the batch process
	 * @see java.lang.Runnable#run()
	 */
	public void run() {		
		
		ChildCareContract contract;
		Collection contractArray = new ArrayList();
		Collection regulationArray = new ArrayList();
		User custodian;
		School provider;
		Date currentDate = new Date( new java.util.Date().getTime());
		Age age;
		float months;
		int days;
		int hours;
		float totalSum;
		int childcare = 0;
		int check = 0;

		IWTimestamp time, startTime, endTime;

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
					invoiceHeader.setReference(provider);//TODO (JJ) Check if this is right. Supposed to be "Responcible person cenrally = BUN"...
					invoiceHeader.setDateCreated(currentDate);
					invoiceHeader.setCreatedBy(BATCH_TEXT);
					//TODO (JJ) invoiceHeader.setOwnPosting();
					//TODO (JJ) invoiceHeader.setDoublePosting();
				}
				
				// **Calculate how big part of time period this contract is valid for
//				if(contract.getValidFromDate().before(startPeriod) && 
//						(contract.getTerminatedDate()==null || contract.getTerminatedDate().after(endPeriod))){
//					months = 1;
//					days = IWTimestamp.getDaysBetween(startPeriod, endPeriod);
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
				PostingDetail postingDetail = regBus.
				getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(
					childcareCategory.getLocalizedKey(),//The ID that selects barnomsorg in the regulation
					PaymentFlowConstant.OUT, 		//The payment flow is out
					currentDate,					//Current date to select the correct date range
					RegSpecConstant.CHECK,			//The ruleSpecType shall be Check
					RuleTypeConstant.DERIVED,		//The conditiontype
					conditions,						//The conditions that need to fulfilled
					totalSum,						//Sent in to be used for "Specialutrakning
					contract);						//Sent in to be used for "Specialutrakning
			
				float checkAmount = postingDetail.getAmount();
				totalSum += checkAmount;

				// **Create the invoice record
				InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
				totalSum = postingDetail.getAmount();
				invoiceRecord.setInvoiceHeader(invoiceHeader);
				//TODO (JJ) Create a "utbetalningspost" waiting for description from Lotta
				invoiceRecord.setAmount(totalSum);
				invoiceRecord.setInvoiceText(provider.getName()+", "+contract.getCareTime()+" "+HOURS_PER_WEEK);
				invoiceRecord.setRuleText(postingDetail.getTerm());
				//TODO (JJ) get the "huvudverksamhet" object that laddi will create.
				invoiceRecord.setDateCreated(currentDate);
				invoiceRecord.setCreatedBy(BATCH_TEXT);
				//TODO (JJ) set the reference to utbetalningsposten
				//TODO (JJ) Some VAT stuff needed here...

				PostingBusiness postingBusiness = getPostingBusinessHome().create();
				PostingParameters parameters = postingBusiness.getPostingParameter(
					new Date(new java.util.Date().getTime()), childcare, check, 0, 0);
				invoiceRecord.setOwnPosting(parameters.getPostingString());
				invoiceRecord.store();

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

					totalSum += postingDetail.getAmount();

					// **Create the invoice record
					invoiceRecord = getInvoiceRecordHome().create();
					invoiceRecord.setInvoiceHeader(invoiceHeader);
					//TODO (JJ) set the reference to utbetalningsposten
					invoiceRecord.setContractId(contract.getContractID());
					invoiceRecord.setInvoiceText(postingDetail.getTerm());
					invoiceRecord.setRuleText(postingDetail.getTerm());
					invoiceRecord.setDays(days);
					//TODO (JJ) get the "huvudverksamhet" object that laddi will create.
					invoiceRecord.setDateCreated(currentDate);
					invoiceRecord.setCreatedBy(BATCH_TEXT);
					invoiceRecord.setAmount(postingDetail.getAmount());
					//TODO (JJ) Create a "utbetalningspost" waiting for description from Lotta
					//TODO (JJ) Some VAT stuff needed here...

					invoiceRecord.store();
				}
				//Make sure that the sum is not less than 0
				if(totalSum<0){
//					subvention -= totalSum;
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
