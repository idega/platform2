package se.idega.idegaweb.commune.accounting.invoice.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.family.business.NoCustodianFound;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.business.AccountingUtil;
import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.regulations.business.AgeBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.BruttoIncomeException;
import se.idega.idegaweb.commune.accounting.regulations.business.LowIncomeException;
import se.idega.idegaweb.commune.accounting.regulations.business.MissingConditionTypeException;
import se.idega.idegaweb.commune.accounting.regulations.business.MissingFlowTypeException;
import se.idega.idegaweb.commune.accounting.regulations.business.MissingRegSpecTypeException;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegSpecConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationException;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.TooManyRegulationsException;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionParameter;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecTypeHome;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.accounting.userinfo.business.SiblingOrderException;
import se.idega.idegaweb.commune.accounting.userinfo.business.UserInfoService;
import se.idega.idegaweb.commune.accounting.userinfo.data.DateOfBirthMissingException;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.EmploymentType;
import se.idega.idegaweb.commune.care.data.ProviderType;
import se.idega.idegaweb.commune.care.data.ProviderTypeHome;
import se.idega.util.ErrorLogger;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.User;
import com.idega.util.CalendarMonth;

/**
 * Holds most of the logic for the batchjob that creates the information that is
 * base for invoicing  and payment data, that is sent to external finance
 * system.
 * <p>
 * Last modified: $Date: 2004/12/02 12:39:08 $ by $Author: laddi $
 *
 * @author <a href="mailto:joakim@idega.is">Joakim Johnson</a>
 * @version $Revision: 1.155 $
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public class InvoiceChildcareThread extends BillingThread{
	
	private static final String HOURS_PER_WEEK = "t/v ";
	private static final String CHECK = "Check ";
	private static final String DAYS = " dagar";
	private ChildCareContract contract;
	private PostingDetail postingDetail;
	private Map siblingOrders = new HashMap();
	private Set incoiceEntryChildSet;
	
	public InvoiceChildcareThread(Date dateInMonth, IWContext iwc, School school, boolean testRun){
		super(dateInMonth,iwc,school,testRun);
	}
	
	public InvoiceChildcareThread(Date month, IWContext iwc){
		super(month,iwc);
	}
	
	/**
	 * The thread that does the acctual work on the batch process
	 * @see java.lang.Runnable#run()
	 */
	public void run(){
		try {
			category = ((SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class)).findChildcareCategory();
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).findByPrimaryKeyIDO(category.getPrimaryKey());
			
			createBatchRunLogger(category);
			if(getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(month,category.getCategory()) == 0){
				//Create all the billing info derrived from the contracts
				createBillingFromContracts();
				//Create all the billing info derrived from the regular invoices
				//regularInvoice();
				//Create all the billing info derrived from the regular payments
				createBillingFromRegularPayment();
				//VAT
				//calcVAT();
			}else{
				createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),
						getLocalizedString("invoice.Posts_with_status_L_or_H_already_exist","Posts with status L or H already exist"));
			}
		} catch (NotEmptyException e) {
			createNewErrorMessage(getLocalizedString("invoice.PaymentSchool","Payment school"), 
					getLocalizedString("invoice.Severe_MustFirstEmptyOldData","Severe. Must first empty old data"));
		} catch (Exception e) {
			//This is a spawned off thread, so we cannot report back errors to the browser, just log them
			e.printStackTrace();
			if (null != errorRelated) {
				errorRelated.append(e);
				createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),getLocalizedString("invoice.DBSetupProblem","Database setup problem"));
			}else{
				StringBuffer message = new StringBuffer();
				StackTraceElement[] stackTraceElement = e.getStackTrace();
				for(int i=0; i<stackTraceElement.length;i++){
					message.append(stackTraceElement[i].toString());
				}
				createNewErrorMessage(message.toString(),getLocalizedString("invoice.DBSetupProblem","Database setup problem"));
			}
		}
		batchRunLoggerDone();
		BatchRunSemaphore.releaseChildcareRunSemaphore();
		BatchRunQueue.BatchRunDone();
	}
	
	/**
	 * Sets the invoice receivere acccording to C&P rules
	 * @param contract
	 * @return User InvoiceReceiver
	 */
	private User getInvoiceReceiver(ChildCareContract contract){
		//First option is to set it to the invoice receiver according to the contract
		User invoiceReceiver = contract.getInvoiceReceiver();
		User child = contract.getChild();
		
		//If non is set in the contract, start looking for parents at the same address
		//Select the female if several are found
		if(invoiceReceiver == null){
			int femaleKey = -1;
			try {
				Gender female = ((GenderHome) IDOLookup.getHome(Gender.class)).getFemaleGender();
				femaleKey = ((Integer)female.getPrimaryKey()).intValue();
			} catch (Exception e2) {
				e2.printStackTrace();
				createNewErrorMessage(errorRelated,getLocalizedString("invoice.CouldNotFindPrimaryKeyForFemaleGender","Could not find primary key for female gender"));
			}
			
			try {
				UserBusiness userBus = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
				FamilyLogic familyLogic = (FamilyLogic) IBOLookup.getServiceInstance(iwc, FamilyLogic.class);
				Collection custodians;		//Collection parents only hold the biological parents
				custodians = familyLogic.getCustodiansFor(child);
				Iterator custIter = custodians.iterator();
				while(custIter.hasNext()){
					User adult = (User)custIter.next();
					Address childAddress = userBus.getUsersMainAddress(child);
					Address custodianAddress = userBus.getUsersMainAddress(adult);
					if(childAddress!=null && custodianAddress!=null){
						if (childAddress.isEqualTo (custodianAddress)) {
							if(invoiceReceiver == null || adult.getGenderID() == femaleKey){
								invoiceReceiver = adult;
							}
						}
					}else{
						if(childAddress==null){
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.ChildAddressNotSet","Child address not set"));
						}
						if(custodianAddress==null){
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.CustodianAddressNotSet","Custodian address not set"));
						}
					}
				}
				if(invoiceReceiver!=null){
					contract.setInvoiceReceiver(invoiceReceiver);
					contract.store();
				}
			} catch (NoCustodianFound e1) {
				//Poor child
			} catch (RemoteException e) {
				e.printStackTrace();
				errorRelated.append(e);
				createNewErrorMessage(errorRelated,getLocalizedString("invoice.RemoteExceptionFindingCustodianForChild","Remote Exception finding custodian for child"));
			}
		}
		//If no invoice receiver is set in contract and no fitting custodian found,  
		//just set the owner of the contract and create a warning
		if(invoiceReceiver == null){
			contract.setInvoiceReceiver(contract.getApplication().getOwner());
			contract.store();
			createNewErrorMessage(errorRelated,getLocalizedString("invoice.InvoiceReceiverNotSetAndNoCustodianAtSameAddressFound_UsingContractOwner",
					"Invoice receiver not set and no custodian at same address found. Using contract owner"));
		}
		errorRelated.append(getLocalizedString("invoice.InvoiceReceiver","Invoice receiver")+":"+invoiceReceiver);
		return invoiceReceiver;
	}
	
	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the childcare contracts
	 */
	private void createBillingFromContracts() throws NotEmptyException{
		//Collection contractArray = new ArrayList();
		Collection regulationArray = new ArrayList();
		User custodian;
		//		Age age;
		String hours;
		PlacementTimes placementTimes = null;
		long totalSum;
		InvoiceRecord invoiceRecord, subventionToReduce;
		int highestOrderNr;
		
		try {
			if (hasPlacements()) {
				throw new NotEmptyException(getLocalizedString("invoice.must_first_empty_old_data","Must first empty old data"));
			}
			Collection contractArray = null;
			if(getSchool()!=null){
				contractArray = getChildCareContractHome().findByDateRangeAndProviderWhereStatusActive(startPeriod.getDate(), endPeriod.getDate(),getSchool());
			}
			else{
				contractArray = getChildCareContractHome().findByDateRangeWhereStatusActive(startPeriod.getDate(), endPeriod.getDate());
			}
			log.info("# of contracts: "+contractArray.size());
			Iterator contractIter = contractArray.iterator();
			errorOrder = 0;
			final Commune	homeCommune = getCommuneHome ().findDefaultCommune ();
			final ProviderType communeProviderType
					= getProviderTypeHome ().findCommuneType ();
			//Loop through all contracts
			while(contractIter.hasNext())
			{
				try{
					contract = (ChildCareContract)contractIter.next();

					errorRelated = new ErrorLogger();
					try {
						errorRelated.append(getLocalizedString("invoice.ChildcareContract","Childcare Contract")+":"+contract.getPrimaryKey());
						errorRelated.append(getLocalizedString("invoice.ContractStart","Contract Start")+":"+contract.getValidFromDate()+"; Contract End: "+(null == contract.getTerminatedDate() ? "-" : ""+contract.getTerminatedDate()));
						errorRelated.append(getLocalizedString("invoice.Child","Child")+":"+contract.getChild().getName()+"; P#: "+contract.getChild().getPersonalID());
					} catch (NullPointerException e) {
						e.printStackTrace ();
					}

					//Moved up for better logging
					//Get all the parameters needed to select the correct contract
					SchoolClassMember schoolClassMember = contract.getSchoolClassMember();
					User child = null;
					try{
						child = schoolClassMember.getStudent();
					}catch (NullPointerException e){
						errorRelated.append(getLocalizedString("invoice.PlacementIdInContract","Placement id in contract")+":"+ contract.getSchoolClassMemberId ());
						throw new NoSchoolClassMemberException("");
					}
					//Fetch invoice receiver
					custodian = getInvoiceReceiver(contract);
					//Get school
					final School school = contract.getApplication ().getProvider ();
					errorRelated.append(getLocalizedString("invoice.School","School")+":"+school.getName(),1);
					//Get school type
					SchoolType schoolType = schoolClassMember.getSchoolType();
					String childcareType = null;
					try {
						childcareType =schoolType.getLocalizationKey();
					}catch (NullPointerException e){
						throw new NoSchoolTypeException("");
					}
					errorRelated.append(getLocalizedString("invoice.SchoolType","SchoolType")+":"+schoolType.getName());
					
					// check if this is either inside commune or private childcare
					final Provider provider = new Provider (school);
					final ProviderType providerType = provider.getProviderType ();
					final Commune commune = school.getCommune ();
					if (providerType.equals (communeProviderType)
							&& !commune.equals (homeCommune)) {
						throw new CommuneChildcareOutsideHomeCommuneException ();
					}
					//Check if both provider and child is outside home commune
					UserBusiness userBus = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
					if(!commune.equals(homeCommune) && userBus.getUsersMainAddress(child).getCommuneID() != ((Integer)homeCommune.getPrimaryKey()).intValue()){
						throw new NotDefaultCommuneException(getLocalizedString("invoice.School","School")+":"+provider.getSchool().getName()+
								"; "+getLocalizedString("invoice.Student","Student")+":"+child.getName());
					}
					
					// if provider has payment by invoice set, then ignore this silently
					if (provider.getPaymentByInvoice ()) {
						log.info ("Provider " + school.getName ()
								+ " has payment by invoice, school class member "
								+ schoolClassMember.getPrimaryKey ()
								+ " will be ignored in childcare batch.");
						continue;
					}

					// **Get or create the invoice header
					InvoiceHeader invoiceHeader;
					try{
						invoiceHeader = getInvoiceHeaderHome().findByCustodianAndMonth(custodian,month);
					} catch (FinderException e) {
						//No header was found so we have to create it
						invoiceHeader = getInvoiceHeaderHome().create();
						//Fill in all the field available at this times
						invoiceHeader.setSchoolCategory(category);
						invoiceHeader.setPeriod(startPeriod.getDate());
						//Custodian is not correct label. Should be invoice receiver
						invoiceHeader.setCustodianId(((Integer)custodian.getPrimaryKey()).intValue());
						invoiceHeader.setDateCreated(currentDate);
						invoiceHeader.setCreatedBy(BATCH_TEXT);
						invoiceHeader.setStatus(ConstantStatus.PRELIMINARY);
						invoiceHeader.store();
					}
					
					// **Calculate how big part of time period this contract is valid for
					placementTimes = calculateTime(contract.getValidFromDate(), contract.getTerminatedDate());
					
					totalSum = 0;
					subventionToReduce = null;
					highestOrderNr = -1;
					
					//Get the check for the contract
					RegulationsBusiness regBus = getRegulationsBusiness();
					hours = contract.getCareTime();
					
					AgeBusiness ageBusiness = (AgeBusiness) IBOLookup.getServiceInstance(iwc, AgeBusiness.class);
					int ageInYears = ageBusiness.getChildAge(contract.getChild().getPersonalID(), startPeriod.getDate());
					
					ArrayList conditions = new ArrayList();
					errorRelated.append(getLocalizedString("invoice.Hours","Hours")+": "+hours);
					errorRelated.append(getLocalizedString("invoice.Age","Age")+":"+ageInYears+" "+getLocalizedString("invoice.years","years"));
					
					conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION,childcareType));
					conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_HOURS,new Integer(hours)));
					conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_AGE_INTERVAL,new Integer(ageInYears)));
					EmploymentType employmentType = contract.getEmploymentType();
					if(employmentType!= null){
						conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_EMPLOYMENT,employmentType.getPrimaryKey()));
						errorRelated.append(getLocalizedString("invoice.EmploymentType","EmploymentType")+":"+employmentType.getLocalizationKey());
					}
//					errorRelated.append("RuleTypeConstant.DERIVED:"+RuleTypeConstant.DERIVED);
//					errorRelated.append("RegSpecConstant.CHECK:"+RegSpecConstant.CHECK);
					
					postingDetail = regBus.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(
							category.getCategory(),		//The ID that selects barnomsorg in the regulation
							PaymentFlowConstant.OUT, 	//The payment flow is out
							calculationDate,					//Current date to select the correct date range
							RuleTypeConstant.DERIVED,	//The conditiontype
							RegSpecConstant.CHECK,		//The ruleSpecType shall be Check
							conditions,						//The conditions that need to fulfilled
							totalSum,						//Sent in to be used for "Specialutrakning"
							contract, null);						//Sent in to be used for "Specialutrakning"
					
					if(postingDetail == null){
						throw new RegulationException("reg_exp_no_results","No regulations found.");
					}
					
					RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(RegSpecConstant.CHECK);
					errorRelated.append(getLocalizedString("invoice.RegelSpecTyp","Regel Spec Typ")+": "+regSpecType);
					
					String[] postings = getPostingBusiness().getPostingStrings(
							category, schoolClassMember.getSchoolType(), ((Integer)regSpecType.getPrimaryKey()).intValue(), provider,calculationDate);
					String[] checkPost = getPostingBusiness().getPostingStrings(
							category, schoolClassMember.getSchoolType(), ((Integer)getRegulationSpecTypeHome().findByRegulationSpecType(RegSpecConstant.CHECKTAXA).getPrimaryKey()).intValue(), provider,calculationDate);
					PaymentRecord paymentRecord = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);			//MUST create payment record first, since it is used in invoice record
					createVATPaymentRecord(paymentRecord, postingDetail,placementTimes.getMonths(),school,schoolClassMember.getSchoolType(),schoolClassMember.getSchoolYear());
								
					// **Create the invoice record
					invoiceRecord = createInvoiceRecordForCheck(invoiceHeader, 
							CHECK+school.getName(),contract.getChild().getFirstName()+", "+hours+" "+HOURS_PER_WEEK+ placementTimes.getDays()+DAYS, paymentRecord, 
							checkPost[0], checkPost[1], placementTimes, school, contract);
						
 					totalSum = AccountingUtil.roundAmount(postingDetail.getAmount()*placementTimes.getMonths());
					int siblingOrder;
 					try{
						siblingOrder = getSiblingOrder(contract, siblingOrders);
					} catch (SiblingOrderException e) {
						e.printStackTrace();
						errorRelated.append(e.getMessage());
						errorRelated.append(e);
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.CouldNotGetSiblingOrder","Could not get sibling Order"));
						siblingOrder = 1;
					} catch (DateOfBirthMissingException e){
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.CouldNotGetSiblingOrder","Could not get sibling Order")+
								". "+getLocalizedString("invoice.MissingDateOfBirth","Missing date of birth"));
						siblingOrder = 1;
					}
					conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_SIBLING_NR,
									new Integer(siblingOrder)));
					errorRelated.append(getLocalizedString("invoice.SiblingOrderSetTo","Sibling order set to")+":"+siblingOrder+
							" "+getLocalizedString("invoice.for","for")+" "+schoolClassMember.getStudent().getName());
						
					//Get all the rules for this contract
					regulationArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
							category.getCategory(),//The ID that selects barnomsorg in the regulation
							PaymentFlowConstant.IN, 			//The payment flow is out
							startPeriod.getDate(),							//Current date to select the correct date range
							RuleTypeConstant.DERIVED,			//The conditiontype
							null,
							conditions								//The conditions that need to fulfilled
							);
						
					ErrorLogger tmpErrorRelated = new ErrorLogger(errorRelated);
					Iterator regulationIter = regulationArray.iterator();
					while(regulationIter.hasNext())
					{
						errorRelated = new ErrorLogger(tmpErrorRelated);
						try {
							Regulation regulation = (Regulation)regulationIter.next();
							errorRelated.append(getLocalizedString("invoice.Regulation","Regulation")+":"+regulation.getName());
							postingDetail = regBus.getPostingDetailForContract(
									totalSum,
									contract,
									regulation,
									startPeriod.getDate(),
									conditions,
									placementTimes);
							if(postingDetail==null){
								throw new RegulationException("reg_exp_no_results", "No regulation match conditions");
							}
								
							errorRelated.append(getLocalizedString("invoice.PostingDetail","Posting detail")+":"+postingDetail);
							// **Create the invoice record
							//maybe get these strings from the postingDetail instead.
							postingDetail.setRuleSpecType(regulation.getRegSpecType().getLocalizationKey());		//This is a patch, Pallis func should probably return the right one in the first place.
							errorRelated.append(getLocalizedString("invoice.InvoiceHeader","InvoiceHeader")+":"+invoiceHeader.getPrimaryKey());
							//						RegulationSpecType regulationSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
							postings = getPostingBusiness().getPostingStrings(category, schoolClassMember.getSchoolType(), ((Integer)regulation.getRegSpecType().getPrimaryKey()).intValue(), provider,calculationDate);

							// if this is a discounted subvention, then amount is allready
							// divided by the number of days
							final boolean isDiscount = 0.0f != regulation.getDiscount ()
									|| 0.0f != regulation.getMaxAmountDiscount();
							invoiceRecord = createInvoiceRecord(invoiceHeader, postings[0], "", placementTimes, school, contract,isDiscount);
							
							//Need to store the subvention row, so that it can be adjusted later if needed
							//							if(postingDetail.getRuleSpecType().equalsIgnoreCase(RegSpecConstant.SUBVENTION) || regulation.getRegSpecType().getLocalizationKey().equalsIgnoreCase(RegSpecConstant.SUBVENTION)){
							if(postingDetail.getOrderID()>highestOrderNr){
								highestOrderNr = postingDetail.getOrderID();
								subventionToReduce = invoiceRecord;
							}
							totalSum += invoiceRecord.getAmount ();
						}
						catch (BruttoIncomeException e) {
							//Who cares!!!
						}
						catch (LowIncomeException e) {
							//No low income registered...
						}
						catch (CreateException e1) {
							e1.printStackTrace();
							errorRelated.append(e1);
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.CreateException","Create exception"));
						}
						catch (RegulationException e1) {
							e1.printStackTrace();
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingRegulationWhenItWasExpected","Error finding regulation when it was expected"));
						}
						catch (PostingException e1) {
							e1.printStackTrace();
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.PostingException","PostingException"));
						}
						catch (RemoteException e1) {
							e1.printStackTrace();
							errorRelated.append(e1);
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.RemoteException","RemoteException"));
						}
						catch(MissingConditionTypeException e) {
							e.printStackTrace();
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingConditionType","ErrorFindingConditionType"));
						}
						catch (MissingFlowTypeException e) {
							e.printStackTrace();
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingFlowType","ErrorFindingFlowType"));
						}
						catch (MissingRegSpecTypeException e) {
							e.printStackTrace();
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingRegSpecType","ErrorFindingRegSpecType"));
						}
						catch (TooManyRegulationsException e) {
							e.printStackTrace();
							errorRelated.append(getLocalizedString("invoice.RegulationsFound","Regulations found")+":"+e.getRegulationNamesString());
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.TooManyRegulationsFoundForQuery","Too many regulations found for query"));
						}
					}
					//Make sure that the sum is not less than 0
					ErrorLogger errorRelated = new ErrorLogger(tmpErrorRelated);
					errorRelated.append(getLocalizedString("invoice.TotalSumIs","Total sum is")+":"+totalSum);
					if(totalSum<0){
						if(subventionToReduce!=null){
							errorRelated.append(getLocalizedString("invoice.SumTooLow_ChangingSubventionFrom","Sum too low, changing subvention from")+
									" "+subventionToReduce.getAmount()+" "+getLocalizedString("invoice.To","to")+" "+(subventionToReduce.getAmount()-totalSum));
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.Info_SubventionChangedToMakeSumZero","Info: Subvention changed to make sum zero"));
							subventionToReduce.setAmount(subventionToReduce.getAmount()-totalSum);
							subventionToReduce.store();
						} else {
							errorRelated.append(getLocalizedString("invoice.SumTooLow_ButNoSubventionFound","Sum too low, but no subvention found."));
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.noSubventionFoundAndSumLessThanZero","No subvention found and sum less than zero"));
						}
					}
					createRegularInvoiceForChild(child,schoolClassMember,custodian,invoiceHeader,placementTimes,totalSum);
					
				}catch (CommuneChildcareOutsideHomeCommuneException e1) {
//					errorRelated.append(e1);
					createNewErrorMessage(errorRelated,getLocalizedString("invoice.CommuneChildcareOutsideHomeCommune","Commune childcare outside home commune"));
				}catch (NoSchoolClassMemberException e1) {
					errorRelated.append(e1);
					createNewErrorMessage(errorRelated,getLocalizedString("invoice.SchoolClassMemberNotSetForContract","School class member not set for contract"));
				}catch (NoSchoolTypeException e1) {
					errorRelated.append(e1);
					createNewErrorMessage(errorRelated,getLocalizedString("invoice.NoSchooltypeFound","No schooltype found"));
				}catch (NullPointerException e1) {
					e1.printStackTrace();
					if(errorRelated != null){
						errorRelated.append(e1);
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.ReferenceErrorPossiblyNullInPrimaryKeyInDB","Reference error possibly null in primary key in DB"));
					} else{
						createNewErrorMessage(contract.getChild().getName(),getLocalizedString("invoice.ReferenceErrorPossiblyNullInPrimaryKeyInDB","Reference error possibly null in primary key in DB"));
					}
				}catch (RegulationException e1) {
					e1.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingCheckRegulation","Error finding check regulation"));
					} else{
						createNewErrorMessage(contract.getChild().getName(),getLocalizedString("invoice.ErrorFindingCheckRegulation","Error finding check regulation"));
					}
				} catch (PostingException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.PostingParameterIncorrectlyFormatted","Posting parameter incorrectly formatted"));
					} else{
						createNewErrorMessage(contract.getChild().getName(),getLocalizedString("invoice.PostingParameterIncorrectlyFormatted","Posting parameter incorrectly formatted"));
					}
				} catch (CreateException e) {
					e.printStackTrace();
					if(errorRelated != null){
						errorRelated.append(e);
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.DBProblem","DB Problem"));
					} else{
						createNewErrorMessage(contract.getChild().getName(),getLocalizedString("invoice.DBProblem","DB Problem"));
					}
				} catch (EJBException e) {
					e.printStackTrace();
					if(errorRelated != null){
						errorRelated.append(e);
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.EJBException","EJB Exception"));
					} else{
						createNewErrorMessage(contract.getChild().getName(),getLocalizedString("invoice.EJBException","EJB Exception"));
					}
				}
				catch (MissingFlowTypeException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingFlowType","Error finding flow type"));
					} else{
						createNewErrorMessage(contract.getChild().getName(),getLocalizedString("invoice.ErrorFindingFlowType","Error finding flow type"));
					}
				}
				catch (MissingConditionTypeException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingConditionType","Error finding condition type"));
					} else{
						createNewErrorMessage(contract.getChild().getName(),getLocalizedString("invoice.ErrorFindingConditionType","Error finding condition type"));
					}
				}
				catch (MissingRegSpecTypeException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingRegSpecType","Error finding reg spec type"));
					} else{
						createNewErrorMessage(contract.getChild().getName(),getLocalizedString("invoice.ErrorFindingRegSpecType","Error finding reg spec type"));
					}
				}
				catch (TooManyRegulationsException e) {
					e.printStackTrace();
					errorRelated.append(getLocalizedString("invoice.RegulationsFound","Regulations found")+":"+e.getRegulationNamesString());
					if(errorRelated != null){
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.ErrorFindingTooManyRegulations","Error finding too many regulations"));
					} else{
						createNewErrorMessage(contract.getChild().getName(),getLocalizedString("invoice.ErrorFindingTooManyRegulations","Error finding too many regulations"));
					}
				} catch (RemoteException e) {
					e.printStackTrace();
					createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),getLocalizedString("invoice.RemoteException","Remote exception"));
				} catch (FinderException e) {
					e.printStackTrace();
					createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),getLocalizedString("invoice.NoContractsFound","No contracts found"));
				} catch (NotDefaultCommuneException e) {
					e.printStackTrace();
					createNewErrorMessage(errorRelated, getLocalizedString("invoice.BothStudentAndSchoolOutsideDefaultCommmune","Both student and school outside default commmune"));
				}
				if(!running){
					return;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),getLocalizedString("invoice.RemoteException","Remote exception"));
		} catch (FinderException e) {
			e.printStackTrace();
			createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),getLocalizedString("invoice.NoContractsFound","No contracts found"));
		} catch (EJBException e) {
			e.printStackTrace();
			createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),getLocalizedString("invoice.EJBException","EJB Exception"));
		} catch (IDOException e) {
			e.printStackTrace();
			createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"),getLocalizedString("invoice.IDOException","IDO Exception"));
		}
	}
	
	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the Regular payments
	 */
	private void createRegularInvoiceForChild(User child,SchoolClassMember classMember,User custodian,InvoiceHeader invoiceHeader,PlacementTimes pTimes, long totalSum){
		int days = pTimes.getDays();
		int childId = ((Number)child.getPrimaryKey()).intValue();
		RegularInvoiceEntry regularInvoiceEntry=null;
		boolean hasBeenHandled = haveInvoiceEntriesBeenHandledForChild(child);
		if(!hasBeenHandled){
			try {
				Collection regularInvoices = getRegularInvoiceBusiness().findRegularInvoicesForPeriodAndChildAndCategoryExceptLowincome(startPeriod.getDate(),endPeriod.getDate(),childId,category.getPrimaryKey().toString());
				Iterator regularInvoiceIter = regularInvoices.iterator();
				//Go through all the regular invoices
				while(regularInvoiceIter.hasNext()){
					try{
						//User custodian = null;
						//InvoiceHeader invoiceHeader = null;
						
						regularInvoiceEntry = (RegularInvoiceEntry)regularInvoiceIter.next();
						
						ErrorLogger errorRelated = new ErrorLogger(getLocalizedString("invoice.RegularInvoiceEntryID","Regular invoice entry ID")+":"+regularInvoiceEntry.getPrimaryKey());
						
						//Get the child and then look up the custodian
						//childID = regularInvoiceEntry.getChildId();
						
						errorRelated.append(getLocalizedString("invoice.Child","Child")+":"+childId);
						//MemberFamilyLogic familyLogic = (MemberFamilyLogic) IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
						//User child = (User) IDOLookup.findByPrimaryKey(User.class, new Integer(childID));
						errorRelated.append(getLocalizedString("invoice.ChildName","Child name")+":"+child.getName());
						/*Iterator custodianIter = familyLogic.getCustodiansFor(child).iterator();
							while (custodianIter.hasNext() ){//&& invoiceHeader == null) {
							custodian = (User) custodianIter.next();
							//try{
							//invoiceHeader = getInvoiceHeaderHome().findByCustodianID(((Integer)custodian.getPrimaryKey()).intValue());
							custodianID = ((Integer)custodian.getPrimaryKey()).intValue();
							errorRelated.append("Parent "+custodianID+"<br>");
							//} catch (FinderException e) {
							//That's OK, just keep looking
							//}
							}*/
						if(invoiceHeader==null){
							//					try{
							//						invoiceHeader = getInvoiceHeaderHome().findByCustodianID(custodianID);
							//					} catch (FinderException e) {
							//No header was found so we have to create it
							invoiceHeader = getInvoiceHeaderHome().create();
							//Fill in all the field available at this times
							invoiceHeader.setSchoolCategory(category);
							invoiceHeader.setPeriod(startPeriod.getDate());
							invoiceHeader.setCustodian(custodian);
							invoiceHeader.setDateCreated(currentDate);
							invoiceHeader.setCreatedBy(BATCH_TEXT);
							invoiceHeader.setStatus(ConstantStatus.PRELIMINARY);
							invoiceHeader.store();
							createNewErrorMessage(errorRelated.toString(),"invoice.CouldNotFindCustodianForRegularInvoice");
						}
						errorRelated.append(getLocalizedString("invoice.Note","Note")+":"+regularInvoiceEntry.getNote());
						
						PlacementTimes placementTimes = calculateTime(contract.getValidFromDate(), contract.getTerminatedDate());
						
						InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
						invoiceRecord.setInvoiceHeader(invoiceHeader);
						invoiceRecord.setInvoiceText(regularInvoiceEntry.getNote());
						invoiceRecord.setSchoolClassMember(classMember);
						
						invoiceRecord.setProvider(regularInvoiceEntry.getSchool());
						invoiceRecord.setRuleText(regularInvoiceEntry.getNote());
						invoiceRecord.setDays(days);

						invoiceRecord.setNotes(regularInvoiceEntry.getNote());
						
						invoiceRecord.setPeriodStartCheck(placementTimes.getFirstCheckDay().getDate());
						invoiceRecord.setPeriodEndCheck(placementTimes.getLastCheckDay().getDate());
						invoiceRecord.setPeriodStartPlacement(contract.getValidFromDate());
						invoiceRecord.setPeriodEndPlacement(contract.getTerminatedDate());
						
						invoiceRecord.setPeriodStartCheck(placementTimes.getFirstCheckDay().getDate());
						invoiceRecord.setPeriodEndCheck(placementTimes.getLastCheckDay().getDate());
						invoiceRecord.setPeriodStartPlacement(regularInvoiceEntry.getFrom());
						invoiceRecord.setPeriodEndPlacement(regularInvoiceEntry.getTo());
						invoiceRecord.setDateCreated(currentDate);
						invoiceRecord.setCreatedBy(BATCH_TEXT);
						long amount = AccountingUtil.roundAmount(regularInvoiceEntry.getAmount());
						float vat = regularInvoiceEntry.getVAT ();
						totalSum += amount;
						if(totalSum<0){
							errorRelated.append(getLocalizedString("invoice.PreviousSum","Previous sum")+":"+amount+" "+
									getLocalizedString("invoice.changedTo","changed to")+" "+(amount-totalSum));
							createNewErrorMessage(errorRelated,getLocalizedString("invoice.SumLessThanZeroForRegularInvoiceRecord",
									"Sum less than zero for regular invoice record"));
							final long newAmount = amount - totalSum;
							if (0 != amount) {
								vat *= (float) newAmount / (float) amount;
							}
							amount = newAmount;
						}
						invoiceRecord.setAmount(amount);
						invoiceRecord.setVATRuleRegulation(regularInvoiceEntry.getVatRuleRegulationId());
						//invoiceRecord.setAmountVAT (AccountingUtil.roundAmount(vat));
						invoiceRecord.setAmountVAT (0);
						invoiceRecord.setRegSpecType(regularInvoiceEntry.getRegSpecType());
						
						invoiceRecord.setOwnPosting(regularInvoiceEntry.getOwnPosting());
						invoiceRecord.setDoublePosting(regularInvoiceEntry.getDoublePosting());
						invoiceRecord.store();
						markInvoiceEntriesHandledForChild(child);
						
					} catch (RemoteException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.RemoteException","Remote Exception"));
					} catch (CreateException e) {
						e.printStackTrace();
						createNewErrorMessage(errorRelated,getLocalizedString("invoice.CreateException","CreateException"));
					}
					if(!running){
						return;
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				createNewErrorMessage(getLocalizedString("invoice.RegularInvoices","RegularInvoices"),
						getLocalizedString("invoice.RemoteException","Remote Exception"));
			} catch (FinderException e) {
				e.printStackTrace();
				createNewErrorMessage(getLocalizedString("invoice.RegularInvoices","RegularInvoices"),
						getLocalizedString("invoice.FinderException","Finder Exception"));
			}
		}
	}	
	
	/**
	 * @param invoiceRecord
	 * @param child
	 */
	private void markInvoiceEntriesHandledForChild(User child) {
		Object childPK = child.getPrimaryKey();
		getIncoiceEntryChildSet().add(childPK);
	}
	
	/**
	 * @param regularInvoiceEntry
	 * @param child
	 * @return
	 */
	private boolean haveInvoiceEntriesBeenHandledForChild(User child) {
		Object childPK = child.getPrimaryKey();
		return getIncoiceEntryChildSet().contains(childPK);
	}
	
	private Set getIncoiceEntryChildSet(){
		if(incoiceEntryChildSet==null){
			incoiceEntryChildSet=new HashSet();
		}
		return incoiceEntryChildSet;
	}
	
	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the Regular payments
	 */
	private void createBillingFromRegularPayment() {
		PostingDetail postingDetail = null;
		PlacementTimes placementTimes = null;
		School school;
		
		try {
			Iterator regularPaymentIter = null;
			if(getSchool()!=null){
				regularPaymentIter = getRegularPaymentBusiness().findRegularPaymentsForPeriodeAndSchool(month.getFirstDateOfMonth(),month.getLastDateOfMonth(), getSchool()).iterator();
			}
			else{
				regularPaymentIter = getRegularPaymentBusiness().findRegularPaymentsForPeriodeAndCategory(startPeriod.getDate(), category).iterator();
			}
			//Go through all the regular payments
			while (regularPaymentIter.hasNext()) {
				RegularPaymentEntry regularPaymentEntry = (RegularPaymentEntry) regularPaymentIter.next();
				ErrorLogger errorRelated = new ErrorLogger(getLocalizedString("invoice.RegularPaymentEntryID","Regular payment entry ID")+":"+regularPaymentEntry.getPrimaryKey());
				errorRelated.append(getLocalizedString("invoice.Placing","Placing")+":"+regularPaymentEntry.getPlacing());
				errorRelated.append(getLocalizedString("invoice.Amount","Amount")+":"+regularPaymentEntry.getAmount());
//				errorRelated.append("School: "+regularPaymentEntry.getSchool());
				postingDetail = new PostingDetail(regularPaymentEntry);
				school = regularPaymentEntry.getSchool();
				placementTimes = calculateTime(regularPaymentEntry.getFrom(),regularPaymentEntry.getTo());
				try {
					PaymentRecord paymentRecord = createPaymentRecord(postingDetail, regularPaymentEntry.getOwnPosting(), regularPaymentEntry.getDoublePosting(), placementTimes.getMonths(), school, regularPaymentEntry.getNote());
					createVATPaymentRecord(paymentRecord,postingDetail,placementTimes.getMonths(),school,regularPaymentEntry.getSchoolType(),null);
				} catch (IDOLookupException e) {
					createNewErrorMessage(regularPaymentEntry.toString(), getLocalizedString("invoice.IDOLookupException","IDOLookup Exception"));
					e.printStackTrace();
				} catch (CreateException e) {
					createNewErrorMessage(regularPaymentEntry.toString(), getLocalizedString("invoice.CreateException","Create Exception"));
					e.printStackTrace();
				}
				if(!running){
					return;
				}
			}
		}catch (FinderException e) {
			e.printStackTrace();
			if (postingDetail != null) {
				createNewErrorMessage(postingDetail.getTerm(), getLocalizedString("invoice.FinderException","Finder Exception"));
			}
			else {
				createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"), getLocalizedString("invoice.FinderException","Finder Exception"));
			}
		} catch (IDOLookupException e) {
			createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"), getLocalizedString("invoice.IDOLookupException","IDOLookup Exception"));
			e.printStackTrace();
		} catch (RemoteException e) {
			createNewErrorMessage(getLocalizedString("invoice.severeError","Severe error"), getLocalizedString("invoice.RemoteException","Remote Exception"));
			e.printStackTrace();
		}
	}
	
	/**
	 * Calculates the sibling order for the child connected to a contract
	 * and also stores the order for all of its siblings.
	 * 
	 * @param contract
	 * @return the sibling order for the child connected to the contract
	 */
	
	private int getSiblingOrder(ChildCareContract contract, Map siblingOrders) throws EJBException, RemoteException, SiblingOrderException{
		User contractChild = contract.getChild ();	
		UserInfoService userInfo = (UserInfoService) IBOLookup.getServiceInstance(iwc, UserInfoService.class);
		return userInfo.getSiblingOrder(contractChild, siblingOrders, new CalendarMonth (startPeriod));
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
	private InvoiceRecord createInvoiceRecordForCheck(InvoiceHeader invoiceHeader, String header, String text2,
													  PaymentRecord paymentRecord, String ownPosting, String doublePosting, PlacementTimes placementTimes, School school, ChildCareContract contract) 
		throws RemoteException, CreateException{
		InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
		invoiceRecord.setInvoiceHeader(invoiceHeader);
		invoiceRecord.setInvoiceText(header);
		invoiceRecord.setInvoiceText2(text2);
//		errorRelated.append("Created invoice for check Invoiceheader: "+invoiceHeader.getPrimaryKey());
		//set the reference to payment record (utbetalningsposten)
		invoiceRecord.setPaymentRecord(paymentRecord);
		return createInvoiceRecordSub(invoiceRecord, ownPosting, doublePosting, placementTimes, school, contract, false);
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
	private InvoiceRecord createInvoiceRecord(InvoiceHeader invoiceHeader, String ownPosting, String doublePosting, PlacementTimes placementTimes, School school, ChildCareContract contract, boolean isDiscount) 
		throws RemoteException, CreateException{
		InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
		invoiceRecord.setInvoiceHeader(invoiceHeader);
		invoiceRecord.setInvoiceText(postingDetail.getTerm());
		return createInvoiceRecordSub(invoiceRecord, ownPosting, doublePosting, placementTimes, school, contract, isDiscount);
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
	private InvoiceRecord createInvoiceRecordSub(InvoiceRecord invoiceRecord, String ownPosting, String doublePosting, PlacementTimes placementTimes, School school, ChildCareContract contract, boolean isDiscount) 
		throws RemoteException{
		invoiceRecord.setProvider(school);
		invoiceRecord.setSchoolClassMember(contract.getSchoolClassMember());
		invoiceRecord.setRuleText(postingDetail.getTerm());
		invoiceRecord.setDays(placementTimes.getDays());
		invoiceRecord.setPeriodStartCheck(placementTimes.getFirstCheckDay().getDate());
		invoiceRecord.setPeriodEndCheck(placementTimes.getLastCheckDay().getDate());
		invoiceRecord.setPeriodStartPlacement(contract.getValidFromDate());
		invoiceRecord.setPeriodEndPlacement(contract.getTerminatedDate());
		invoiceRecord.setDateCreated(currentDate);
		invoiceRecord.setCreatedBy(BATCH_TEXT);
		invoiceRecord.setAmount(AccountingUtil.roundAmount(postingDetail.getAmount()*(isDiscount ? 1.0f : placementTimes.getMonths())));
		//invoiceRecord.setAmountVAT(AccountingUtil.roundAmount(postingDetail.getVATPercent()*invoiceRecord.getAmount ()/100.0f));
		invoiceRecord.setAmountVAT (0);
		invoiceRecord.setVATRuleRegulation(postingDetail.getVatRuleRegulationId());
		invoiceRecord.setOrderId(postingDetail.getOrderID());
		invoiceRecord.setSchoolType(contract.getSchoolClassMember().getSchoolType());
		//		errorRelated.append("Order ID = "+postingDetail.getOrderID());
		RegulationSpecTypeHome regSpecTypeHome = (RegulationSpecTypeHome) IDOLookup.getHome(RegulationSpecType.class);
		try {
			RegulationSpecType regSpecType = regSpecTypeHome.findByRegulationSpecType(postingDetail.getRuleSpecType());
			invoiceRecord.setRegSpecType(regSpecType);
		} catch (Exception e) {
			e.printStackTrace ();
		}
		
		//Set the posting strings
		invoiceRecord.setOwnPosting(ownPosting);
		invoiceRecord.setDoublePosting(doublePosting);
		invoiceRecord.setChildCareContract(contract);
		invoiceRecord.store();
		
		return invoiceRecord;
	}

	/**
	 * Do nothing
	 */
	protected void setStudyPath(SchoolClassMember schoolClassMember, ArrayList conditions){
	}
	
	private CommuneHome getCommuneHome() throws RemoteException {
		return (CommuneHome) IDOLookup.getHome (Commune.class);
	}

	private ProviderTypeHome getProviderTypeHome() throws RemoteException {
		return (ProviderTypeHome) IDOLookup.getHome (ProviderType.class);
	}
}
