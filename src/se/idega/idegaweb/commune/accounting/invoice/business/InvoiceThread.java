package se.idega.idegaweb.commune.accounting.invoice.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoCohabitantFound;
import is.idega.idegaweb.member.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.BatchRunError;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderHome;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecordHome;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.accounting.invoice.data.SortableSibling;
import se.idega.idegaweb.commune.accounting.posting.business.MissingMandatoryFieldException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingBusiness;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;
import se.idega.idegaweb.commune.accounting.posting.data.PostingParameters;
import se.idega.idegaweb.commune.accounting.regulations.business.IntervalConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionParameter;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractHome;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;

/**
 * Holds most of the logic for the batchjob that creates the information that is base for invoicing 
 * and payment data, that is sent to external finance system.
 * 
 * @author Joakim
 */
public class InvoiceThread extends Thread{

	private static final String BATCH_TEXT = "invoice.batchrun";		//Localize this text in the user interface
//	private static final String MANUALLY_TEXT = "invoice.manually";		//Localize this text in the user interface
	private static final String HOURS_PER_WEEK = "tim/v";		//Localize this text in the user interface
	private IWTimestamp startPeriod;
	private IWTimestamp endPeriod;
	private ChildCareContract contract;
	private PostingDetail postingDetail;
	private int days;
	private Date currentDate = new Date( new java.util.Date().getTime());
	private IWTimestamp time, startTime, endTime;
	private ExportDataMapping categoryPosting;
	private SchoolCategory childcareCategory;
	private int childcare = 0;
	private int check = 0;
	private int errorOrder;
	private School school;
	private float months;
	private Map siblingOrders = new HashMap();
	private String ownPosting, doublePosting;
	private IWContext iwc;

	public InvoiceThread(Date month, IWContext iwc){
		startPeriod = new IWTimestamp(month);
		startPeriod.setAsDate();
		startPeriod.setDay(1);
		endPeriod = new IWTimestamp(startPeriod);
		endPeriod.setAsDate();
		endPeriod.addMonths(1);
		this.iwc = iwc;
	}

	/**
	 * The thread that does the acctual work on the batch process
	 * @see java.lang.Runnable#run()
	 */
	public void run(){
		try {
			childcareCategory = ((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findChildcareCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).findByPrimaryKeyIDO(childcareCategory.getPrimaryKey());
		
			contracts();
			regularPayments();

		} catch (Exception e) {
			e.printStackTrace();
			if(postingDetail != null){
				createNewErrorMessage(postingDetail.getTerm(),"invoice.DBSetupProblem");
			}else{
				createNewErrorMessage("invoice.severeError","invoice.DBSetupProblem");
			}
		}
	}

	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the childcare contracts
	 */
	private void contracts(){
		Collection contractArray = new ArrayList();
		Collection regulationArray = new ArrayList();
		User custodian;
		Age age;
		int hours;
		float totalSum;
		InvoiceRecord invoiceRecord, subvention;


		try {
			contractArray = getChildCareContractHome().findByDateRange(startPeriod.getDate(), endPeriod.getDate());
		
			Iterator contractIter = contractArray.iterator();
			errorOrder = 0;

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
					invoiceHeader.setStatus(ConstantStatus.PRELIMINARY);
					invoiceHeader.store();
				}
			
				// **Calculate how big part of time period this contract is valid for
				calculateTime(contract.getValidFromDate(), contract.getTerminatedDate());
/*
				//first get the start date
				startTime = new IWTimestamp(contract.getValidFromDate());
				startTime.setAsDate();
				time = new IWTimestamp(startPeriod);
				time.setAsDate();
				startTime = startTime.isLater(startTime,time);
				//Then get end date
				endTime = new IWTimestamp(endPeriod);
				endTime.setAsDate();
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
*/
				totalSum = 0;
				subvention = null;
				//
				//Get the check for the contract
				//
				RegulationsBusiness regBus = getRegulationsBusiness();
			
				//Get all the parameters needed to select the correct contract
				String childcareType = contract.getSchoolClassMmeber().getSchoolClass().getSchoolType().getName();
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
					totalSum,						//Sent in to be used for "Specialutrakning"
					contract);						//Sent in to be used for "Specialutrakning"
		
				try{
					compilePostingStrings();
					PaymentRecord paymentRecord = createPaymentRecord();			//MUST create payment record first, since it is used in invoice record
					// **Create the invoice record
					invoiceRecord = createInvoiceRecordForCheck(invoiceHeader, 
							school.getName()+", "+contract.getCareTime()+" "+HOURS_PER_WEEK, paymentRecord);
			
					totalSum = postingDetail.getAmount()*months;
					conditions.add(new ConditionParameter(IntervalConstant.SIBLING_NUMBER,
							new Integer(getSiblingOrder(contract))));

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

						//Need to store the subvention row, so that it can be adjusted later if needed					
						if(postingDetail.getRuleSpecType()== RegSpecConstant.SUBVENTION){
							subvention = invoiceRecord;
						}
						totalSum += postingDetail.getAmount()*months;
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
			e.printStackTrace();
			if(postingDetail != null){
				createNewErrorMessage(postingDetail.getTerm(),"invoice.DBSetupProblem");
			}else{
				createNewErrorMessage("invoice.severeError","invoice.DBSetupProblem");
			}
		}
	}
	
	/**
	 * calculatest the number of days andmonths between the start and end date 
	 * and sets the local variables monts and days
	 * 
	 * @param start
	 * @param end
	 */
	private void calculateTime(Date start, Date end){
		startTime = new IWTimestamp(start);
		startTime.setAsDate();
		time = new IWTimestamp(startPeriod);
		time.setAsDate();
		startTime = startTime.isLater(startTime,time);
		//Then get end date
		endTime = new IWTimestamp(endPeriod);
		endTime.setAsDate();
		if(contract.getTerminatedDate()!=null){
			time = new IWTimestamp(end);
			endTime = endTime.isEarlier(endTime, time);
		}
		//calc the how many months are in the given time.
		months = endTime.getMonth() - startTime.getMonth() + (endTime.getYear()-startTime.getYear())*12;
		months += 1.0;
		months -= percentOfMonthDone(startTime);
		months -= 1.0 - percentOfMonthDone(endTime);
		days = IWTimestamp.getDaysBetween(startTime, endTime);
	}

	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the Regular payments
	 */
	private void regularPayments(){
		User custodian;
		try {
			RegularPaymentBusiness regularPaymentBusiness = getRegularPaymentBusiness();
			Iterator regularPaymentIter = regularPaymentBusiness.findRegularPaymentsForPeriode(startPeriod.getDate(), endPeriod.getDate()).iterator();
			//Go through all the regular payments
			while(regularPaymentIter.hasNext()){
				RegularPaymentEntry regularPaymentEntry = (RegularPaymentEntry)regularPaymentIter.next();
				custodian = regularPaymentEntry.getUser();
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
					invoiceHeader.setStatus(ConstantStatus.PRELIMINARY);
					invoiceHeader.store();
				}
				if(regularPaymentEntry.getRegSpecType().equals(RegSpecConstant.CHECK)){
					
				}else{
				}
				
				calculateTime(new Date(regularPaymentEntry.getFrom().getTime()),
						new Date(regularPaymentEntry.getTo().getTime()));

				InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
				invoiceRecord.setInvoiceHeader(invoiceHeader);
				invoiceRecord.setInvoiceText(regularPaymentEntry.getNote());

				invoiceRecord.setProviderId(regularPaymentEntry.getProvider());
				invoiceRecord.setRuleText(regularPaymentEntry.getNote());
				invoiceRecord.setDays(days);
				invoiceRecord.setPeriodStartCheck(startPeriod.getDate());
				invoiceRecord.setPeriodEndCheck(endPeriod.getDate());
				invoiceRecord.setPeriodStartPlacement(startTime.getDate());
				invoiceRecord.setPeriodEndPlacement(endTime.getDate());
				invoiceRecord.setDateCreated(currentDate);
				invoiceRecord.setCreatedBy(BATCH_TEXT);
				invoiceRecord.setAmount(regularPaymentEntry.getAmount()*months);
				invoiceRecord.setAmountVAT(regularPaymentEntry.getVAT()*months);
				invoiceRecord.setVATType(regularPaymentEntry.getVatRegulationID());
				invoiceRecord.setRuleSpecType(regularPaymentEntry.getRegSpecType());

				invoiceRecord.setOwnPosting(regularPaymentEntry.getOwnPosting());
				invoiceRecord.setDoublePosting(regularPaymentEntry.getDoublePostin());
				invoiceRecord.store();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(postingDetail != null){
				createNewErrorMessage(postingDetail.getTerm(),"invoice.DBSetupProblem");
			}else{
				createNewErrorMessage("invoice.severeError","invoice.DBSetupProblem");
			}
		}
	}

	/**
	 * Finds existing payment reacord or creates a new payment record if needed, 
	 * and populates the values as needed. It also creates the payment header if needed
	 *  
	 * @return the created payment record
	 * @throws CreateException
	 * @throws IDOLookupException
	 */
	private PaymentRecord createPaymentRecord() throws CreateException, IDOLookupException {
		PaymentHeader paymentHeader;
		PaymentRecord paymentRecord;
	
		//Get the payment header
		try {
			paymentHeader = ((PaymentHeaderHome) IDOLookup.getHome(PaymentHeader.class)).
					findBySchoolCategorySchoolPeriod(school,childcareCategory,currentDate);
		} catch (FinderException e) {
			//If No header found, create it
			paymentHeader = (PaymentHeader) IDOLookup.create(PaymentHeader.class);
			paymentHeader.setSchoolID(school);
			paymentHeader.setSchoolCategoryID(childcareCategory);
			if(categoryPosting.getProviderAuthorization()){
				paymentHeader.setStatus(ConstantStatus.BASE);
			} else {
				paymentHeader.setStatus(ConstantStatus.PRELIMINARY);
			}
		}

		//Update or create the payment record
		try {
			paymentRecord = ((PaymentRecordHome) IDOLookup.getHome(PaymentRecord.class)).
					findByPaymentHeaderAndRuleSpecType(paymentHeader,postingDetail.getRuleSpecType());
			//If it already exists, just update the contents.
			paymentRecord.setPlacements(paymentRecord.getPlacements()+1);
			paymentRecord.setTotalAmount(paymentRecord.getTotalAmount()+postingDetail.getAmount()*months);
			paymentRecord.setTotalAmountVAT(paymentRecord.getTotalAmountVAT()+postingDetail.getVat()*months);
		} catch (FinderException e1) {
			//It didn't exist, so we create it
			paymentRecord = (PaymentRecord) IDOLookup.create(PaymentRecord.class);
			//Set all the values for the payment record
			paymentRecord.setPaymentHeader(paymentHeader);
			if(categoryPosting.getProviderAuthorization()){
				paymentRecord.setStatus(ConstantStatus.BASE);
			} else {
				paymentRecord.setStatus(ConstantStatus.PRELIMINARY);
			}
			paymentRecord.setPeriod(startPeriod.getDate());
			paymentRecord.setPaymentText(postingDetail.getTerm());
			paymentRecord.setDateCreated(currentDate);
			paymentRecord.setCreatedBy(BATCH_TEXT);
			paymentRecord.setPlacements(1);
			paymentRecord.setPieceAmount(postingDetail.getAmount());
			paymentRecord.setTotalAmount(postingDetail.getAmount()*months);
			paymentRecord.setTotalAmountVAT(postingDetail.getVat()*months);
			paymentRecord.setRuleSpecType(postingDetail.getRuleSpecType());
			paymentRecord.setOwnPosting(ownPosting);
			paymentRecord.setDoublePosting(doublePosting);
			paymentRecord.setVATType(postingDetail.getVatRegulationID());
			paymentRecord.store();
		}
		return paymentRecord;
	}

	/**
	 * Code to set all the sibling order used to calculate the sibling discount for each contract.
	 * At the moment there is no sibling order column in the User object, so this function is not used.
	 * Instead the getSiblingOrder(ChildCareContract contract) is used.
	 * 
	 * This methode will probably not be used getSiblingOrder() will take care of the whole work
	 * 
	 * @param contractIter
	 */
	/*
	private void setSiblingOrder(Iterator contractIter){
		boolean found;
		TreeSet sortedSiblings;
		//Zero all sibling orders
		ChildCareContract contract;
		//Itterate through all contracts
		while(contractIter.hasNext()){
			contract = (ChildCareContract)contractIter.next();

			sortedSiblings = new TreeSet();
		
			SortableSibling sortableChild = new SortableSibling(contract.getChild());
			sortedSiblings.add(sortableChild);

			List parents = contract.getChild().getParentGroups();
			Iterator parentIter = parents.iterator();
			//Itterate through parents
			while(parentIter.hasNext()){
				User parent = (User)parentIter.next();
				Iterator siblingsIter = parent.getChildren();
				//Itterate through their kids
				Collection addr1Coll = contract.getChild().getAddresses();
				while(siblingsIter.hasNext())
				{
					User sibling = (User) siblingsIter.next();
					//If kids have same address add to collection
					found = false;
					Collection addr2Coll = sibling.getAddresses();
					Iterator addr1Iter = addr1Coll.iterator();
					while(addr1Iter.hasNext() && found==false){
						Address addr1 = (Address)addr1Iter.next();

						Iterator addr2Iter = addr2Coll.iterator();
						while(addr2Iter.hasNext() && found==false){
							Address addr2 = (Address)addr2Iter.next();
							if(addr1.getPrimaryKey().equals(addr2.getPrimaryKey())){
								found = true;
							}
						}
					}
					//Sort kids in age order
					if(found){
						SortableSibling sortableSibling = new SortableSibling(sibling);
						if(!sortedSiblings.contains(sortableSibling)){
							sortedSiblings.add(sortableSibling);
						}
					}
				}
			}
			// (JJ) Here we need to set the order to the User objects if allowed to create an extra col
			sortedSiblings.headSet(sortableChild).size();
		}
	}
	*/

	/**
	 * Calculates the sibling order for the child connected to a contract
	 * and also stores the order for all of its siblings.
	 * 
	 * @param contract
	 * @return the sibling order for the child connected to the contract
	 */
	private int getSiblingOrder(ChildCareContract contract) throws EJBException, SiblingOrderException, IDOLookupException, RemoteException, CreateException{
		UserBusiness userBus = (UserBusiness) IDOLookup.create(UserBusiness.class);
	
		//First see if the child already has been given a sibling order
		MemberFamilyLogic familyLogic = (MemberFamilyLogic) IDOLookup.create(MemberFamilyLogic.class);
		Integer order = (Integer)siblingOrders.get(contract.getChild().getPrimaryKey());
		if(order != null)
		{
			return order.intValue();	//Sibling order already calculated.
		}
	
		TreeSet sortedSiblings = new TreeSet();		//Container for the siblings that keeps them in sorted order
		Address childAddress = userBus.getUsersMainAddress(contract.getChild());

		//Add the child to the sibbling collection right away to make sure it will be in there
		//This should really happen in the main loop below as well, but this is done since
		//the realtionships seem to be a bit unreliable
		sortedSiblings.add(new SortableSibling(contract.getChild()));

		//Gather up a collection of the parents and their cohabitants
		Collection parents;		//Collection parents only hold the biological parents
		try {
			parents = familyLogic.getCustodiansFor(contract.getChild());
		} catch (NoCustodianFound e1) {
			throw new SiblingOrderException("No custodians found for the child.");
		}
		//Adults hold all the adults that could be living on the same address
		Collection adults = new ArrayList(parents);
		Iterator parentIter = parents.iterator();
		//Itterate through parents
		while(parentIter.hasNext()){
			User parent = (User)parentIter.next();
			try {
				adults.add(familyLogic.getCohabitantFor(parent));
			} catch (NoCohabitantFound e) {
			}
		}

		//Loop through all adults
		Iterator adultIter = adults.iterator();
		while(adultIter.hasNext()){
			User adult = (User)adultIter.next();
			Iterator siblingsIter = adult.getChildren();
			//Itterate through their kids
			while(siblingsIter.hasNext())
			{
				User sibling = (User) siblingsIter.next();
			
				//Check if the sibling has a valid contract of right type
				try {
					getChildCareContractHome().findValidContractByChild(((Integer)sibling.getPrimaryKey()).intValue(),currentDate);
					//If kids have same address add to collection
					Address siblingAddress = userBus.getUsersMainAddress(contract.getChild());
					if(childAddress.getPostalAddress().equals(siblingAddress.getPostalAddress()) &&
						childAddress.getCity().equals(siblingAddress.getCity()) && 
						childAddress.getPOBox().equals(siblingAddress.getPOBox())){

						SortableSibling sortableSibling = new SortableSibling(sibling);
						if(!sortedSiblings.contains(sortableSibling)){
							sortedSiblings.add(sortableSibling);
						}
					}
				} catch (FinderException e) {
					//If sibling doesn't have an address or contract, it won't be counted in the sibling order
					System.out.println("Warning: got a finder exception where it shouldn't happen");
					e.printStackTrace();
				}
			}
		}
	
		//Store the sorting order
		Iterator sortedIter = sortedSiblings.iterator();
		int orderNr = 1;
		while(sortedIter.hasNext()){
			SortableSibling sortableSibling = (SortableSibling)sortedIter.next();
			siblingOrders.put(sortableSibling.getSibling().getPrimaryKey(),new Integer(orderNr));
			orderNr++;
		}
	
		//Look up the order of the child that started the whole thing
		order = (Integer)siblingOrders.get(contract.getChild().getPrimaryKey());
		if(order != null)
		{
			return order.intValue();
		}
		//This should really never happen
		throw new SiblingOrderException("Could not find the sibling order.");
	}

	/**
	 * Creates a new error log message (to be used according to fonster 33 in C&P Req.Spec.)
	 *  
	 * @param related Description of related objects
	 * @param desc Description possible errors
	 */
	private void createNewErrorMessage(String related, String desc){
		try {
			BatchRunError error = (BatchRunError) IDOLookup.create(BatchRunError.class);
			error.setRelated(related);
			error.setDescription(desc);
			error.setOrder(errorOrder);
			errorOrder++;
		} catch (Exception e) {
			System.out.println("Exception so complicated that it wasn't even possible to create an error message in the log!");
			e.printStackTrace();
		}
	}

	/**
	 * Creates an invoice record with the specific descriptive text for the check.
	 * @param invoiceHeader
	 * @param header
	 * @param paymentRecord
	 * @return the created invoice record
	 * @throws PostingParametersException
	 * @throws PostingException
	 * @throws RemoteException
	 * @throws CreateException
	 * @throws MissingMandatoryFieldException
	 */
	private InvoiceRecord createInvoiceRecordForCheck(InvoiceHeader invoiceHeader, String header, PaymentRecord paymentRecord) throws PostingParametersException, PostingException, RemoteException, CreateException, MissingMandatoryFieldException{
		InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
		invoiceRecord.setInvoiceHeader(invoiceHeader);
		invoiceRecord.setInvoiceText(header);
		//set the reference to payment record (utbetalningsposten)
		invoiceRecord.setPaymentRecordId(paymentRecord);
		return createInvoiceRecordSub(invoiceRecord);
	}

	/**
	 * Creates an invoice record
	 * @param invoiceHeader
	 * @return the created invoice record
	 * @throws PostingParametersException
	 * @throws PostingException
	 * @throws RemoteException
	 * @throws CreateException
	 * @throws MissingMandatoryFieldException
	 */
	private InvoiceRecord createInvoiceRecord(InvoiceHeader invoiceHeader) throws PostingParametersException, PostingException, RemoteException, CreateException, MissingMandatoryFieldException{
		InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
		invoiceRecord.setInvoiceHeader(invoiceHeader);
		invoiceRecord.setInvoiceText(postingDetail.getTerm());
		return createInvoiceRecordSub(invoiceRecord);
	}

	/**
	 * Does all the work of creating an invoice record that is the same for both check 
	 * and non-check invoice records
	 * 
	 * @param invoiceRecord
	 * @return the created invoice record
	 * @throws CreateException
	 * @throws PostingParametersException
	 * @throws PostingException
	 * @throws RemoteException
	 * @throws MissingMandatoryFieldException
	 */
	private InvoiceRecord createInvoiceRecordSub(InvoiceRecord invoiceRecord) throws CreateException, PostingParametersException, PostingException, RemoteException, MissingMandatoryFieldException{
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

		//Set the posting strings
/*
		PostingBusiness postingBusiness = getPostingBusinessHome().create();
		PostingParameters parameters = postingBusiness.getPostingParameter(
			new Date(new java.util.Date().getTime()), childcare, check, 0, 0);

		ownPosting = parameters.getPostingString();
		Provider provider = new Provider(((Integer)contract.getApplication().getProvider().getPrimaryKey()).intValue());
		ownPosting = postingBusiness.generateString(ownPosting,provider.getOwnPosting(),currentDate);
		ownPosting = postingBusiness.generateString(ownPosting,categoryPosting.getAccount(),currentDate);
		postingBusiness.validateString(ownPosting,currentDate);
*/
		invoiceRecord.setOwnPosting(ownPosting);
/*	
		doublePosting = parameters.getDoublePostingString();
		doublePosting = postingBusiness.generateString(doublePosting,provider.getDoublePosting(),currentDate);
		doublePosting = postingBusiness.generateString(doublePosting,categoryPosting.getCounterAccount(),currentDate);
		postingBusiness.validateString(doublePosting,currentDate);
*/
		invoiceRecord.setDoublePosting(doublePosting);
		invoiceRecord.store();
	
		return invoiceRecord;
	}

	private void compilePostingStrings() throws CreateException, PostingParametersException, PostingException, RemoteException, MissingMandatoryFieldException{
		//Set the posting strings
		PostingBusiness postingBusiness = (PostingBusiness)IDOLookup.create(PostingBusiness.class);

		PostingParameters parameters = postingBusiness.getPostingParameter(
			new Date(new java.util.Date().getTime()), childcare, check, 0, 0);

		ownPosting = parameters.getPostingString();
		Provider provider = new Provider(((Integer)contract.getApplication().getProvider().getPrimaryKey()).intValue());
		ownPosting = postingBusiness.generateString(ownPosting,provider.getOwnPosting(),currentDate);
		ownPosting = postingBusiness.generateString(ownPosting,categoryPosting.getAccount(),currentDate);
		postingBusiness.validateString(ownPosting,currentDate);

		doublePosting = parameters.getDoublePostingString();
		doublePosting = postingBusiness.generateString(doublePosting,provider.getDoublePosting(),currentDate);
		doublePosting = postingBusiness.generateString(doublePosting,categoryPosting.getCounterAccount(),currentDate);
		postingBusiness.validateString(doublePosting,currentDate);
	}

	/**
	 * Calculates the amount of the month that has passed at the given date.
	 * @param date
	 * @return the amount (%) of the month that has passed
	 */
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

	//Getters to different commonly used objects
	private ChildCareContractHome getChildCareContractHome() throws RemoteException {
		return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
	}

	private InvoiceHeaderHome getInvoiceHeaderHome() throws RemoteException {
		return (InvoiceHeaderHome) IDOLookup.getHome(InvoiceHeader.class);
	}

	private InvoiceRecordHome getInvoiceRecordHome() throws RemoteException {
		return (InvoiceRecordHome) IDOLookup.getHome(InvoiceRecord.class);
	}

	private RegulationsBusiness getRegulationsBusiness() throws RemoteException {
		return (RegulationsBusiness) IBOLookup.getServiceInstance(iwc, RegulationsBusiness.class);
	}

	private RegularPaymentBusiness getRegularPaymentBusiness() throws RemoteException {
		return (RegularPaymentBusiness) IBOLookup.getServiceInstance(iwc, RegularPaymentBusiness.class);
	}
}