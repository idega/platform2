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
import com.idega.data.IDOLookup;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;

/**
 * @author Joakim
 *
 */

public class InvoiceBusinessBean {

	private static final String BATCH_TEXT = "Härledd uppgift";		//Localize this text in the user interface
	private static final String HOURS_PER_WEEK = "tim/v";		//Localize this text in the user interface
	
	public void createInvoicingData(Date startPeriod, Date endPeriod, String createdBy){
		
		ChildCareContract contract;
		Collection contractArray = new ArrayList();
		Collection regulationArray = new ArrayList();
		User custodian;
		School provider;
		Date currentDate = new Date( new java.util.Date().getTime());
		float months;
		int days;
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

			try {
				contractArray = getChildCareContractHome().findByDateRange(startPeriod, endPeriod);
			} catch (RemoteException e) {
				// TODO (JJ) 
				e.printStackTrace();
			} catch (FinderException e) {
				// TODO (JJ) create feedback that no contracts were found
				e.printStackTrace();
			}
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
				InvoiceHeader invoiceHeader = getInvoiceHeaderHome().create();
				//Fill in all the field available at this time
				//TODO (JJ) invoiceHeader.setMainActivity()
				invoiceHeader.setPeriod(startPeriod);
				invoiceHeader.setCustodianId(custodian);
				invoiceHeader.setReference(provider);//TODO (JJ) Check if this is right. Supposed to be "Responcible person cenrally = BUN"...
				invoiceHeader.setDateCreated(currentDate);
				invoiceHeader.setCreatedBy(createdBy);
				invoiceHeader.setCreatedBy(BATCH_TEXT);
				//TODO (JJ) invoiceHeader.setOwnPosting();
				//TODO (JJ) invoiceHeader.setDoublePosting();
				invoiceHeader.setTotalAmountWithoutVAT(0);
				invoiceHeader.setTotalVATAmount(0);
				
				// **Calculate how big part of time period this contract is valid for
//				if(contract.getValidFromDate().before(startPeriod) && (contract.getValidFromDate().after(endPeriod))){
//					months = 1;
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


				//
				//Get the check for the contract
				//
				RegulationsBusiness regBus = getRegulationsBusinessHome().create();
				
				//Get all the parameters needed to select the correct contract
				//TODO (JJ) get the childcareID
				String operationTypeChildcare = "";
				String childcareType = "";
				//TODO (JJ) Tryggvi, Goran and Anders trying to figure out how this should work!
				//childcareType = cont.getApplication().getProvider().;
//					childcareID = Operation.getChildcareID();
				//int age = cont.getChild().get???
				int hours = contract.getCareTime();
				Age age = new Age(contract.getChild().getDateOfBirth());
				ArrayList conditions = new ArrayList();
				conditions.add(new ConditionParameter(IntervalConstant.ACTIVITY,childcareType));
				conditions.add(new ConditionParameter(IntervalConstant.HOURS,new Integer(hours)));
				conditions.add(new ConditionParameter(IntervalConstant.AGE,new Integer(age.getYears())));

				totalSum = 0;
				//Select a specific row from the regulation, given the following restrictions
				PostingDetail postingDetail = regBus.
				getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(
					operationTypeChildcare,										//The ID that selects barnomsorg in the regulation
					PaymentFlowConstant.OUT, 							//The payment flow is out
					new java.sql.Date(new java.util.Date().getTime()),	//The Should be for the currently valid daterange
					RegSpecConstant.CHECK,								//The ruleSpecType shall be Check
					RuleTypeConstant.DERIVED,							//The conditiontype
					conditions,											//The conditions that need to fulfilled
					totalSum,
					contract);
			
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
				invoiceRecord.setCreatedBy(createdBy);
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
					operationTypeChildcare,										//The ID that selects barnomsorg in the regulation
					PaymentFlowConstant.IN, 							//The payment flow is out
					new java.sql.Date(new java.util.Date().getTime()),	//The Should be for the currently valid daterange
					RuleTypeConstant.DERIVED,							//The conditiontype
					conditions											//The conditions that need to fulfilled
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
					invoiceRecord.setCreatedBy(createdBy);
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
			// TODO (JJ) Auto-generated catch block
			e1.printStackTrace();
		} catch (CreateException e) {
			// TODO (JJ) Auto-generated catch block
			e.printStackTrace();
		} catch (PostingParametersException e) {
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
