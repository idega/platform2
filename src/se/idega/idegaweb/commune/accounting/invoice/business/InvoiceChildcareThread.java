package se.idega.idegaweb.commune.accounting.invoice.business;

import is.idega.idegaweb.member.business.MemberFamilyLogic;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.ConstantStatus;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.InvoiceRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularInvoiceEntry;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.accounting.posting.business.MissingMandatoryFieldException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.posting.business.PostingParametersException;
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
import se.idega.idegaweb.commune.accounting.userinfo.business.UserInfoService;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.EmploymentType;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.Age;

/**
 * Holds most of the logic for the batchjob that creates the information that is base for invoicing 
 * and payment data, that is sent to external finance system.
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public class InvoiceChildcareThread extends BillingThread{

	private static final String HOURS_PER_WEEK = "t/v";		//Localize this text in the user interface
	private static final String CHECK = "Check ";
	private static final String DAYS = "dagar";
	private ChildCareContract contract;
	private PostingDetail postingDetail;
	private Map siblingOrders = new HashMap();

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

			if(getPaymentRecordHome().getCountForMonthCategoryAndStatusLH(startPeriod.getDate(),category.getCategory()) == 0){
				createBatchRunLogger(category);
				//Create all the billing info derrived from the contracts
				contracts();
				//Create all the billing info derrived from the regular invoices
				regularInvoice();
				//Create all the billing info derrived from the regular payments
				regularPayment();
				//VAT
				calcVAT();
				batchRunLoggerDone();
			}else{
				createNewErrorMessage("invoice.severeError","invoice.Posts_with_status_L_or_H_already_exist");
				batchRunLoggerDone();
			}
		} catch (NotEmptyException e) {
			createNewErrorMessage("invoice.severeError", "invoice.Severe_MustFirstEmptyOldData");
			batchRunLoggerDone();
			e.printStackTrace();
		} catch (Exception e) {
			//This is a spawned off thread, so we cannot report back errors to the browser, just log them
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.DBSetupProblem");
			batchRunLoggerDone();
		}
	}
	
	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the childcare contracts
	 */
	private void contracts() throws NotEmptyException{
		Collection contractArray = new ArrayList();
		Collection regulationArray = new ArrayList();
		User custodian;
		Age age;
		int hours;
		PlacementTimes placementTimes = null;
		float totalSum;
		InvoiceRecord invoiceRecord, subvention;
		School school;

		try {
			if (hasPlacements()) {
				throw new NotEmptyException("invoice.must_first_empty_old_data");
			}

			contractArray = getChildCareContractHome().findByDateRangeWhereStatusActive(startPeriod.getDate(), endPeriod.getDate());
			log.info("# of contracts = "+contractArray.size());
			Iterator contractIter = contractArray.iterator();
			errorOrder = 0;

			//Loop through all contracts
			while(contractIter.hasNext())
			{
				contract = (ChildCareContract)contractIter.next();
				errorRelated = new StringBuffer();
				errorRelated.append("ChildcareContract "+contract.getPrimaryKey()+"<br>");
				errorRelated.append("Contract "+contract.getContractID()+"<br>");
				// **Fetch invoice receiver
				custodian = contract.getApplication().getOwner();
				errorRelated.append("Custodian "+custodian.getName()+"<br>");
				//**Fetch the reference at the provider
				school = contract.getApplication().getProvider();
				errorRelated.append("School "+school.getName()+"<br>");
				log.info("School = "+school);
				// **Get or create the invoice header
				InvoiceHeader invoiceHeader;
				try{
					try{
						invoiceHeader = getInvoiceHeaderHome().findByCustodian(custodian);
					} catch (FinderException e) {
						//No header was found so we have to create it
						invoiceHeader = getInvoiceHeaderHome().create();
						//Fill in all the field available at this times
						invoiceHeader.setSchoolCategory(category);
						invoiceHeader.setPeriod(startPeriod.getDate());
						invoiceHeader.setCustodianId(((Integer)custodian.getPrimaryKey()).intValue());
						invoiceHeader.setDateCreated(currentDate);
						invoiceHeader.setCreatedBy(BATCH_TEXT);
						invoiceHeader.setStatus(ConstantStatus.PRELIMINARY);
						System.out.println("Store Invoice Header with Category '"+invoiceHeader.getSchoolCategoryID());
						System.out.println("and custodian "+invoiceHeader.getCustodianId());
						System.out.println("Databean: "+invoiceHeader);
						invoiceHeader.store();
					}
					errorRelated.append("InvoiceHeader "+invoiceHeader.getPrimaryKey()+"<br>");
				
					// **Calculate how big part of time period this contract is valid for
					placementTimes = calculateTime(contract.getValidFromDate(), contract.getTerminatedDate());
	
					totalSum = 0;
					subvention = null;
					//
					//Get the check for the contract
					//
					RegulationsBusiness regBus = getRegulationsBusiness();
				
					//Get all the parameters needed to select the correct contract
					SchoolClassMember schoolClassMember = contract.getSchoolClassMmeber();
					System.out.println("Contract id "+contract.getPrimaryKey());
					System.out.println("SchoolClassMmeberid "+schoolClassMember.getPrimaryKey());
					SchoolType schoolType = schoolClassMember.getSchoolType();
					String childcareType =schoolType.getLocalizationKey();
					errorRelated.append("SchoolType "+schoolType.getName()+"<br>");
					errorRelated.append("Child "+contract.getChild().getName()+"<br>");

					//childcare = ((Integer)schoolClassMember.getSchoolType().getPrimaryKey()).intValue();
					hours = contract.getCareTime();
					age = new Age(contract.getChild().getDateOfBirth());
					ArrayList conditions = new ArrayList();
					errorRelated.append("Hours "+contract.getCareTime()+"<br>");
					errorRelated.append("Age "+contract.getChild().getDateOfBirth()+"<br>");
					
					conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION,childcareType));
					conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_HOURS,new Integer(hours)));
					conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_AGE_INTERVAL,new Integer(age.getYears())));
					String employment = "";
					EmploymentType employmentType = contract.getEmploymentType();
					if(employmentType!= null){
						conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_EMPLOYMENT,employmentType.getPrimaryKey()));
						employment = employmentType.getLocalizationKey();
						errorRelated.append("EmploymentType "+employment+"<br>");
					}
					log.info("\n School type: "+childcareType+
						"\n Hours "+hours+
						"\n Years "+age.getYears()+
						"\n Emplyment "+employment);
					//Select a specific row from the regulation, given the following restrictions
					log.info("Getting posting detail for:\n" +
						"  Category:"+category.getCategory()+"\n"+
						"  PaymentFlowConstant.OUT:"+PaymentFlowConstant.OUT+"\n"+
						"  currentDate:"+currentDate+"\n"+
						"  RuleTypeConstant.DERIVED:"+RuleTypeConstant.DERIVED+"\n"+
						"  RegSpecConstant.CHECK:"+RegSpecConstant.CHECK+"\n"+
						"  conditions:"+conditions.size()+"\n"+
						"  totalSum:"+totalSum+"\n"+
						"  contract:"+contract.getPrimaryKey()+"\n"
						);
					errorRelated.append("Category:"+category.getCategory()+"<br>"+
						"PaymentFlowConstant.OUT:"+PaymentFlowConstant.OUT+"<br>"+
						"currentDate:"+currentDate+"\n"+
						"RuleTypeConstant.DERIVED:"+RuleTypeConstant.DERIVED+"<br>"+
						"RegSpecConstant.CHECK:"+RegSpecConstant.CHECK+"<br>"+
						"conditions:"+conditions.size()+"<br>"+
						"totalSum:"+totalSum+"<br>"+
						"contract:"+contract.getPrimaryKey()+"<br>");
						postingDetail = regBus.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(
							category.getCategory(),		//The ID that selects barnomsorg in the regulation
							PaymentFlowConstant.OUT, 	//The payment flow is out
							currentDate,					//Current date to select the correct date range
							RuleTypeConstant.DERIVED,	//The conditiontype
							RegSpecConstant.CHECK,		//The ruleSpecType shall be Check
							conditions,						//The conditions that need to fulfilled
							totalSum,						//Sent in to be used for "Specialutrakning"
							contract);						//Sent in to be used for "Specialutrakning"

						if(postingDetail == null){
						throw new RegulationException("reg_exp_no_results","No regulations found.");
					}
					System.out.println("RuleSpecType to use: "+postingDetail.getTerm());
		
					Provider provider = new Provider(((Integer)contract.getApplication().getProvider().getPrimaryKey()).intValue());
					RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(RegSpecConstant.CHECK);
					errorRelated.append("Regel Spec Typ "+regSpecType+"<br>");
					
					String[] postings = getPostingBusiness().getPostingStrings(
						category, schoolClassMember.getSchoolType(), ((Integer)regSpecType.getPrimaryKey()).intValue(), provider,currentDate);
					String[] checkPost = getPostingBusiness().getPostingStrings(
						category, schoolClassMember.getSchoolType(), ((Integer)getRegulationSpecTypeHome().findByRegulationSpecType(RegSpecConstant.CHECKTAXA).getPrimaryKey()).intValue(), provider,currentDate);
					log.info("About to create payment record check");
					PaymentRecord paymentRecord = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);			//MUST create payment record first, since it is used in invoice record
					log.info("created payment record, Now creating invoice record");
					// **Create the invoice record
					invoiceRecord = createInvoiceRecordForCheck(invoiceHeader, 
							CHECK+school.getName(),contract.getChild().getFirstName()+", "+hours+" "+HOURS_PER_WEEK+ placementTimes.getDays()+DAYS, paymentRecord, 
							checkPost[0], checkPost[1], placementTimes, school, contract);
					log.info("created invoice record");

 					totalSum = postingDetail.getAmount()*placementTimes.getMonths();
					int siblingOrder = getSiblingOrder(contract, siblingOrders);
					conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_SIBLING_NR,
							new Integer(siblingOrder)));
					log.info(" Sibling order set to: "+siblingOrder+" for "+schoolClassMember.getStudent().getName());

					//Get all the rules for this contract
					regulationArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
						category.getCategory(),//The ID that selects barnomsorg in the regulation
						PaymentFlowConstant.IN, 			//The payment flow is out
						startPeriod.getDate(),							//Current date to select the correct date range
						RuleTypeConstant.DERIVED,			//The conditiontype
						null,
						conditions								//The conditions that need to fulfilled
						);

					String tmpErrorRelated = errorRelated.toString();
					log.info("Found "+regulationArray.size()+" regulations that apply.");
					Iterator regulationIter = regulationArray.iterator();
					while(regulationIter.hasNext())
					{
						errorRelated = new StringBuffer(tmpErrorRelated);
						try {
							Regulation regulation = (Regulation)regulationIter.next();
							errorRelated.append("Regel "+regulation.getName()+"<br>");
							log.info("regulation "+regulation.getName());
							postingDetail = regBus.getPostingDetailForContract(
								totalSum,
								contract,
								regulation,
								startPeriod.getDate(),
								conditions);
								
							if(postingDetail==null){
								log.warning("Posting detail is null!"+
								"\n tot sum "+totalSum+
								"\n contract "+contract.getPrimaryKey()+
								"\n regulation "+regulation.getName()+
								"\n start period "+startPeriod.toString()+
								"\n # of conditions"+conditions.size());
								throw new RegulationException("reg_exp_no_results", "No regulation match conditions");
							}

							errorRelated.append("Posting detail "+postingDetail+"<br>");
							// **Create the invoice record
							//TODO (JJ) get these strings from the postingDetail instead.
							System.out.println("Regspectyp: "+postingDetail.getRuleSpecType());
							System.out.println("Regspectyp: "+regulation.getRegSpecType().getLocalizationKey());
							postingDetail.setRuleSpecType(regulation.getRegSpecType().getLocalizationKey());		//TODO (JJ) This is a patch, Pallis func should probably return the right one in the first place.
							System.out.println("InvoiceHeader "+invoiceHeader.getPrimaryKey());
	//						RegulationSpecType regulationSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
							postings = getPostingBusiness().getPostingStrings(category, schoolClassMember.getSchoolType(), ((Integer)regulation.getRegSpecType().getPrimaryKey()).intValue(), provider,currentDate);
							invoiceRecord = createInvoiceRecord(invoiceHeader, postings[0], "", placementTimes, school, contract);
	
							//Need to store the subvention row, so that it can be adjusted later if needed					
							if(postingDetail.getRuleSpecType()== RegSpecConstant.SUBVENTION){
								subvention = invoiceRecord;
							}
							totalSum += postingDetail.getAmount()*placementTimes.getMonths();
						}
						catch (BruttoIncomeException e) {
							//Who cares!!!
						}
						catch (LowIncomeException e) {
							//No low income registered...
						}
						catch (CreateException e1) {
							e1.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.CreateException");
						}
						catch (RegulationException e1) {
							e1.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.ErrorFindingRegulationWhenItWasExpected");
						}
						catch (PostingParametersException e1) {
							e1.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.PostingParametersException");
						}
						catch (PostingException e1) {
							e1.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.PostingException");
						}
						catch (RemoteException e1) {
							e1.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.RemoteException");
						}
						catch (MissingMandatoryFieldException e1) {
							e1.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.MissingMandatoryFieldException");
						}
						catch(MissingConditionTypeException e) {
							e.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.ErrorFindingConditionType");
						}
						catch (MissingFlowTypeException e) {
							e.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.ErrorFindingFlowType");
						}
						catch (MissingRegSpecTypeException e) {
							e.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.ErrorFindingRegSpecType");
						}
						catch (TooManyRegulationsException e) {
							e.printStackTrace();
							createNewErrorMessage(errorRelated.toString(),"invoice.TooManyRegulationsFoundForQuery");
						}
					}
					//Make sure that the sum is not less than 0
					log.info("Total sum is:"+totalSum);
					if(totalSum<0){
						if(subvention!=null){
							log.info("Sum too low, changing subvention from "+subvention.getAmount()+"...to "+subvention.getAmount()+totalSum);
							subvention.setAmount(subvention.getAmount()+totalSum);
							subvention.store();
						} else {
							log.info("Sum too low, but no subvention found. Creating error message");
							createNewErrorMessage(errorRelated.toString(),"invoice.noSubventionFoundAndSumLessThanZero");
						}
					}
				}catch (NullPointerException e1) {
					e1.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.ReferenceErrorPossiblyNullInPrimaryKeyInDB");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.ReferenceErrorPossiblyNullInPrimaryKeyInDB");
					}
				}catch (RegulationException e1) {
					e1.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.ErrorFindingCheckRegulation");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.ErrorFindingCheckRegulation");
					}
				} catch(MissingMandatoryFieldException e){
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.MissingMandatoryFieldInPostingParameter");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.MissingMandatoryFieldInPostingParameter");
					}
				} catch (PostingParametersException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.ErrorInPostingParameter");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.ErrorInPostingParameter");
					}
				} catch (PostingException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.PostingParameterIncorrectlyFormatted");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.PostingParameterIncorrectlyFormatted");
					}
				} catch (CreateException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.DBProblem");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.DBProblem");
					}
				} catch (EJBException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.EJBError");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.EJBError");
					}
				} catch (UserInfoService.SiblingOrderException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated + " " + e.getMessage (),"invoice.CouldNotGetSiblingOrder");
					} else{
						createNewErrorMessage(contract.getChild().getName() + " " + e.getMessage (),"invoice.CouldNotGetSiblingOrder");
					}
				}
				catch (MissingFlowTypeException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.ErrorFindingFlowType");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.ErrorFindingFlowType");
					}
				}
				catch (MissingConditionTypeException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.ErrorFindingConditionType");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.ErrorFindingConditionType");
					}
				}
				catch (MissingRegSpecTypeException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.ErrorFindingRegSpecType");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.ErrorFindingRegSpecType");
					}
				}
				catch (TooManyRegulationsException e) {
					e.printStackTrace();
					if(errorRelated != null){
						createNewErrorMessage(errorRelated.toString(),"invoice.ErrorFindingTooManyRegulations");
					} else{
						createNewErrorMessage(contract.getChild().getName(),"invoice.ErrorFindingTooManyRegulations");
					}
				} catch (RemoteException e) {
					e.printStackTrace();
					createNewErrorMessage("invoice.severeError","invoice.SeriousErrorBatchrunTerminated");
				} catch (FinderException e) {
					e.printStackTrace();
					createNewErrorMessage("invoice.severeError","invoice.NoContractsFound");
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.SeriousErrorBatchrunTerminated");
		} catch (FinderException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.NoContractsFound");
		} catch (EJBException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.EJBException");
		} catch (IDOException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.severeError","invoice.IDOException");
		}
	}
	
	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the Regular payments
	 */
	private void regularInvoice(){
		int childID;
		PlacementTimes placementTimes = null;
		
		RegularInvoiceEntry regularInvoiceEntry=null;
		try {
			Iterator regularInvoiceIter = getRegularInvoiceBusiness().findRegularInvoicesForPeriodeAndCategory(startPeriod.getDate(), category).iterator();
			//Go through all the regular invoices
			while(regularInvoiceIter.hasNext()){
				try{
					User custodian = null;
					InvoiceHeader invoiceHeader = null;
					int custodianID = -1;
					
					regularInvoiceEntry = (RegularInvoiceEntry)regularInvoiceIter.next();
					StringBuffer errorRelated = new StringBuffer("RegularInvoiceEntry ID "+regularInvoiceEntry.getPrimaryKey()+"<br>");
					
					//Get the child and then look up the custodian
					childID = regularInvoiceEntry.getUserID();
					errorRelated.append("Child "+childID+"<br>");
					MemberFamilyLogic familyLogic = (MemberFamilyLogic) IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
					User child = (User) IDOLookup.findByPrimaryKey(User.class, new Integer(childID));
					errorRelated.append("Child name "+child.getName()+"<br>");
					Iterator custodianIter = familyLogic.getCustodiansFor(child).iterator();
					while (custodianIter.hasNext() && invoiceHeader == null) {
						custodian = (User) custodianIter.next();
						try{
							invoiceHeader = getInvoiceHeaderHome().findByCustodianID(((Integer)custodian.getPrimaryKey()).intValue());
							custodianID = ((Integer)custodian.getPrimaryKey()).intValue();
							errorRelated.append("Parent "+custodianID+"<br>");
						} catch (FinderException e) {
							//That's OK, just keep looking
						}
					}
					if(invoiceHeader==null){
//					try{
//						invoiceHeader = getInvoiceHeaderHome().findByCustodianID(custodianID);
//					} catch (FinderException e) {
						//No header was found so we have to create it
						invoiceHeader = getInvoiceHeaderHome().create();
						//Fill in all the field available at this times
						invoiceHeader.setSchoolCategory(category);
						invoiceHeader.setPeriod(startPeriod.getDate());
						invoiceHeader.setCustodianId(custodianID);
						invoiceHeader.setDateCreated(currentDate);
						invoiceHeader.setCreatedBy(BATCH_TEXT);
						invoiceHeader.setStatus(ConstantStatus.PRELIMINARY);
						invoiceHeader.store();
						createNewErrorMessage(errorRelated.toString(),"invoice.CouldNotFindCustodianForRegularInvoice");
					}
					errorRelated.append("Note "+regularInvoiceEntry.getNote()+"<br>");
				
					placementTimes = calculateTime(new Date(regularInvoiceEntry.getFrom().getTime()),
							new Date(regularInvoiceEntry.getTo().getTime()));

					InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
					invoiceRecord.setInvoiceHeader(invoiceHeader);
					invoiceRecord.setInvoiceText(regularInvoiceEntry.getNote());

					invoiceRecord.setProvider(regularInvoiceEntry.getSchool());
					invoiceRecord.setRuleText(regularInvoiceEntry.getNote());
					invoiceRecord.setDays(placementTimes.getDays());
					invoiceRecord.setPeriodStartCheck(startPeriod.getDate());
					invoiceRecord.setPeriodEndCheck(endPeriod.getDate());
					invoiceRecord.setPeriodStartPlacement(placementTimes.getFirstCheckDay().getDate());
					invoiceRecord.setPeriodEndPlacement(placementTimes.getLastCheckDay().getDate());
					invoiceRecord.setDateCreated(currentDate);
					invoiceRecord.setCreatedBy(BATCH_TEXT);
					invoiceRecord.setAmount(regularInvoiceEntry.getAmount()*placementTimes.getMonths());
					invoiceRecord.setAmountVAT(regularInvoiceEntry.getVAT()*placementTimes.getMonths());
					invoiceRecord.setVATType(regularInvoiceEntry.getVatRuleId());
					invoiceRecord.setRegSpecType(regularInvoiceEntry.getRegSpecType());

					invoiceRecord.setOwnPosting(regularInvoiceEntry.getOwnPosting());
					invoiceRecord.setDoublePosting(regularInvoiceEntry.getDoublePosting());
					invoiceRecord.store();
				} catch (RemoteException e) {
					e.printStackTrace();
					createNewErrorMessage(errorRelated.toString(),"invoice.DBSetupProblem");
				} catch (CreateException e) {
					e.printStackTrace();
					createNewErrorMessage(errorRelated.toString(),"invoice.DBSetupProblem");
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.RegularInvoices","invoice.CouldNotFindAnyRegularInvoicesTerminating");
		} catch (FinderException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.RegularInvoices","invoice.CouldNotFindAnyRegularInvoicesTerminating");
		}
	}

	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the Regular payments
	 */
	protected void regularPayment() {
		PostingDetail postingDetail = null;
		PlacementTimes placementTimes = null;
		School school;

		try {
			Iterator regularPaymentIter = getRegularPaymentBusiness().findRegularPaymentsForPeriodeAndCategory(startPeriod.getDate(), category).iterator();
			//Go through all the regular payments
			while (regularPaymentIter.hasNext()) {
				RegularPaymentEntry regularPaymentEntry = (RegularPaymentEntry) regularPaymentIter.next();
				StringBuffer errorRelated = new StringBuffer("RegularPaymentEntry ID "+regularPaymentEntry.getPrimaryKey()+"<br>");
				errorRelated.append("Placing "+regularPaymentEntry.getPlacing()+"<br>");
				errorRelated.append("Amount "+regularPaymentEntry.getAmount()+"<br>");
				errorRelated.append("School "+regularPaymentEntry.getSchool()+"<br>");
				postingDetail = new PostingDetail(regularPaymentEntry);
				school = regularPaymentEntry.getSchool();
				placementTimes = calculateTime(regularPaymentEntry.getFrom(),regularPaymentEntry.getTo());
				try {
					createPaymentRecord(postingDetail, regularPaymentEntry.getOwnPosting(), regularPaymentEntry.getDoublePosting(), placementTimes.getMonths(), school);
					log.info("Regular Payment" + errorRelated);
				} catch (IDOLookupException e) {
					createNewErrorMessage(regularPaymentEntry.toString(), "regularPayment.IDOLookup");
					e.printStackTrace();
				} catch (CreateException e) {
					createNewErrorMessage(regularPaymentEntry.toString(), "regularPayment.Create");
					e.printStackTrace();
				}
			}
		}catch (FinderException e) {
			e.printStackTrace();
			if (postingDetail != null) {
				createNewErrorMessage(postingDetail.getTerm(), "payment.DBSetupProblem");
			}
			else {
				createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			}
		} catch (IDOLookupException e) {
			createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			e.printStackTrace();
		} catch (RemoteException e) {
			createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			e.printStackTrace();
		}
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
			// Here we need to set the order to the User objects if allowed to create an extra col
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

	private int getSiblingOrder(ChildCareContract contract, Map siblingOrders) throws EJBException, UserInfoService.SiblingOrderException, IDOLookupException, RemoteException, CreateException{
			User contractChild = contract.getChild ();	
			UserInfoService userInfo = (UserInfoService) IBOLookup.getServiceInstance(iwc, UserInfoService.class);
			return userInfo.getSiblingOrder(contractChild, siblingOrders, startPeriod);
	}

/*
	private int getSiblingOrder(ChildCareContract contract) throws EJBException, SiblingOrderException, IDOLookupException, RemoteException, CreateException{
		UserBusiness userBus = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	
		//First see if the child already has been given a sibling order
		MemberFamilyLogic familyLogic = (MemberFamilyLogic) IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
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
			Iterator siblingsIter;
			try {
				siblingsIter = familyLogic.getChildrenFor(adult).iterator();
				//Itterate through their kids
				while(siblingsIter.hasNext())
				{
					User sibling = (User) siblingsIter.next();
			
					//Check if the sibling has a valid contract of right type
					try {
						getChildCareContractHome().findValidContractByChild(((Integer)sibling.getPrimaryKey()).intValue(),startPeriod.getDate());
						//If kids have same address add to collection
						Address siblingAddress = userBus.getUsersMainAddress(sibling);
						if(childAddress.getPostalAddress().equals(siblingAddress.getPostalAddress()) &&
							childAddress.getCity().equals(siblingAddress.getCity()) &&
							childAddress.getStreetAddress().equals(siblingAddress.getStreetAddress())){

							SortableSibling sortableSibling = new SortableSibling(sibling);
							if(!sortedSiblings.contains(sortableSibling)){
								sortedSiblings.add(sortableSibling);
							}
						}
					} catch (FinderException e) {
						//If sibling don't have a childcare contract we just ignore it
					} catch (NullPointerException e) {
						//If sibling doesn't have an address or contract, it won't be counted in the sibling order
						createNewErrorMessage(contract.getChild().getName(),"invoice.ChildHasNoAddress");
					}
				}
			} catch (RemoteException e2) {
				e2.printStackTrace();
				createNewErrorMessage(contract.getChild().getName(),"invoice.DBError");
			} catch (NoChildrenFound e) {
				e.printStackTrace();
				createNewErrorMessage(contract.getChild().getName(),"invoice.NoChildrenFound");
			}
		}
	
		//Store the sorting order
		Iterator sortedIter = sortedSiblings.iterator();
		int orderNr = 1;
		while(sortedIter.hasNext()){
			SortableSibling sortableSibling = (SortableSibling)sortedIter.next();
			siblingOrders.put(sortableSibling.getSibling().getPrimaryKey(),new Integer(orderNr));
			log.info("Added child "+sortableSibling.getSibling()+" as sibling "+orderNr+" out of "+sortedSiblings.size());
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
*/
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
			throws PostingParametersException, PostingException, RemoteException, CreateException, MissingMandatoryFieldException{
		InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
		invoiceRecord.setInvoiceHeader(invoiceHeader);
		invoiceRecord.setInvoiceText(header);
		invoiceRecord.setInvoiceText2(text2);
		//set the reference to payment record (utbetalningsposten)
		invoiceRecord.setPaymentRecord(paymentRecord);
		return createInvoiceRecordSub(invoiceRecord, ownPosting, doublePosting, placementTimes, school, contract);
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
	private InvoiceRecord createInvoiceRecord(InvoiceHeader invoiceHeader, String ownPosting, String doublePosting, PlacementTimes placementTimes, School school, ChildCareContract contract) 
			throws PostingParametersException, PostingException, RemoteException, CreateException, MissingMandatoryFieldException{
		InvoiceRecord invoiceRecord = getInvoiceRecordHome().create();
		invoiceRecord.setInvoiceHeader(invoiceHeader);
		invoiceRecord.setInvoiceText(postingDetail.getTerm());
		return createInvoiceRecordSub(invoiceRecord, ownPosting, doublePosting, placementTimes, school, contract);
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
	private InvoiceRecord createInvoiceRecordSub(InvoiceRecord invoiceRecord, String ownPosting, String doublePosting, PlacementTimes placementTimes, School school, ChildCareContract contract) 
			throws CreateException, PostingParametersException, PostingException, RemoteException, MissingMandatoryFieldException{
		invoiceRecord.setProvider(school);
        //		invoiceRecord.setContractId(contract.getContractID());
		invoiceRecord.setSchoolClassMember(contract.getSchoolClassMmeber());
		invoiceRecord.setRuleText(postingDetail.getTerm());
		invoiceRecord.setDays(placementTimes.getDays());
		invoiceRecord.setPeriodStartCheck(placementTimes.getFirstCheckDay().getDate());
		invoiceRecord.setPeriodEndCheck(placementTimes.getLastCheckDay().getDate());
		invoiceRecord.setPeriodStartPlacement(contract.getValidFromDate());
		invoiceRecord.setPeriodEndPlacement(contract.getTerminatedDate());
		invoiceRecord.setDateCreated(currentDate);
		invoiceRecord.setCreatedBy(BATCH_TEXT);
		invoiceRecord.setAmount(postingDetail.getAmount()*placementTimes.getMonths());
		invoiceRecord.setAmountVAT(postingDetail.getVat()*placementTimes.getMonths());
		invoiceRecord.setVATType(postingDetail.getVatRegulationID());
		invoiceRecord.setOrderId(postingDetail.getOrderID());
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
		invoiceRecord.store();
	
		return invoiceRecord;
	}
	
	protected boolean hasInvoices() throws FinderException, IDOException, RemoteException, EJBException {
		return getInvoiceRecordHome().getPlacementCountForSchoolCategoryAndPeriod((String) category.getPrimaryKey(), currentDate) > 0;
	}

}
