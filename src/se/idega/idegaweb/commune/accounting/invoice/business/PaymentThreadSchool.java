package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
import se.idega.idegaweb.commune.accounting.regulations.business.PaymentFlowConstant;
import se.idega.idegaweb.commune.accounting.regulations.business.RegulationsBusiness;
import se.idega.idegaweb.commune.accounting.regulations.business.RuleTypeConstant;
import se.idega.idegaweb.commune.accounting.regulations.data.ConditionParameter;
import se.idega.idegaweb.commune.accounting.regulations.data.PostingDetail;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.resource.business.ResourceBusiness;
import se.idega.idegaweb.commune.accounting.resource.data.Resource;
import se.idega.idegaweb.commune.accounting.school.data.Provider;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolManagementTypeHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Abstract class that holds all the logic that is common for the shool billing
 * 
 * @author Joakim
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public abstract class PaymentThreadSchool extends BillingThread{
	PaymentHeader paymentHeader;

	//This is a horrible solution... This class should not have to know the localization keys!!!	
	private static final String OPPEN_VERKSAMHET = "sch_type.school_type_oppen_verksamhet";
	private static final String FRITIDSKLUBB = "sch_type.school_type_fritidsklubb";
	
	public PaymentThreadSchool(Date month, IWContext iwc){
		super(month,iwc);
		currentDate = month;
	}
	
	public boolean isInDefaultCommune(User user) throws RemoteException, FinderException {
		Address address = getCommuneUserBusiness().getUsersMainAddress(user);
		Commune commmune = getCommuneHome().findByPrimaryKey(new Integer(address.getCommuneID()));
		return commmune.getIsDefault();
	}
	
	private CommuneHome getCommuneHome() throws RemoteException {
		return (CommuneHome) IDOLookup.getHome(Commune.class);
	}
									

	protected void contracts(){
		Collection regulationArray = new ArrayList();
		ArrayList conditions = new ArrayList();
//		boolean first;
//		ExportDataMapping categoryPosting;
		PostingDetail postingDetail;

		try {
			//Set the category parameter to ElementarySchool
			categoryPosting = (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).
					findByPrimaryKeyIDO(category.getPrimaryKey());

			RegulationsBusiness regBus = getRegulationsBusiness();
			
			String privateManagementType = (String)((SchoolManagementTypeHome) IDOLookup.getHome(SchoolManagementType.class)).findPrivateManagementType().getPrimaryKey();

			Iterator schoolIter = getSchoolHome().findAllInHomeCommuneByCategory(category).iterator();
			//Go through all elementary schools
			while(schoolIter.hasNext()){
				try{
					school = (School) schoolIter.next();
					System.out.println("About to create payments for school "+school.getName());
					Provider provider = new Provider(((Integer)school.getPrimaryKey()).intValue());
					//Only look at those not "payment by invoice"
					//Check if it is private or in Nacka
					if(school.getCommune().getIsDefault()||
							(school.getManagementType().getPrimaryKey().equals(privateManagementType)&&
							!provider.getPaymentByInvoice())){
						
						System.out.println("Getting regulations for school "+school.getName()+" with "
								+category.getCategory()
								+"  PaymentFlowConstant.OUT "+PaymentFlowConstant.OUT
								+"  "+currentDate.toString()
								+"  RuleTypeConstant.DERIVED "+RuleTypeConstant.DERIVED
								+"  condition "+conditions.size()+"  "+conditions.toString()
								);
						//Get all the rules for this contract
						regulationArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
							category.getCategory(),//The ID that selects barnomsorg in the regulation
							PaymentFlowConstant.OUT, 		//The payment flow is out
							currentDate,					//Current date to select the correct date range
							RuleTypeConstant.DERIVED,		//The conditiontype
							conditions						//The conditions that need to fulfilled
							);

						System.out.println("Got "+regulationArray.size()+" regulations for "+school.getName());
//						first = true;
						Iterator regulationIter = regulationArray.iterator();
						while(regulationIter.hasNext())
						{
							Regulation regulation = (Regulation)regulationIter.next();
							//NOTE this should be changed to use ...ByDateRange when changed to date range rathre than day by day calculation
							
							Iterator schoolClassMemberIter = getSchoolClassMemberHome().findBySchool(((Integer)school.getPrimaryKey()).intValue(),-1,category.getCategory(),currentDate).iterator();
//							Iterator contractIter = getChildCareContractHome().findValidContractByProvider(((Integer)school.getPrimaryKey()).intValue(),currentDate).iterator();
//							Iterator applicationIter = getChildCareApplicationHome().findApplicationsByProviderAndDate(((Integer)school.getPrimaryKey()).intValue(), 
//									((Integer)school.getPrimaryKey()).intValue(),currentDate).iterator();
							System.out.println("looking at regulation");
//							ChildCareContract contract = null;
							while(schoolClassMemberIter.hasNext()){
								SchoolClassMember schoolClassMember = null;
								try{
									System.out.println("looking at school class memeber");

//									ChildCareApplication application = (ChildCareApplication) applicationIter.next();

									schoolClassMember = (SchoolClassMember) schoolClassMemberIter.next();
									
									System.out.println("Found "+schoolClassMember.getStudent().getName());
									if( getCommuneUserBusiness().isInDefaultCommune(schoolClassMember.getStudent()) ){
//									if( isInDefaultCommune(schoolClassMember.getStudent()) ){
//										address.getCommuneID();
//										Header created in the createPaymentRecord()
	/*
										if(first){
											System.out.println("Creating payment header");
											paymentHeader = (PaymentHeader) IDOLookup.create(PaymentHeader.class);
											paymentHeader.setSchoolID(school);
											paymentHeader.setSchoolCategoryID(category);
											if(categoryPosting.getProviderAuthorization()){
												paymentHeader.setStatus(ConstantStatus.BASE);
											} else {
												paymentHeader.setStatus(ConstantStatus.PRELIMINARY);
											}
											first = false;
											paymentHeader.store();
										}
	*/
//										ChildCareContract contract = getChildCareContractHome().findApplicationByContract(((Integer)application.getPrimaryKey()).intValue());

										Date sDate= null;
										Date eDate=null;
										if(schoolClassMember.getRegisterDate()!=null)
										{
											sDate = new Date(schoolClassMember.getRegisterDate().getTime());
										}
										if(schoolClassMember.getRemovedDate()!=null)
										{
											eDate = new Date(schoolClassMember.getRemovedDate().getTime());
										}
										calculateTime(sDate,eDate);
										//Get the posting details for the contract
										postingDetail = regBus.getPostingDetailForPlacement(0.0f,schoolClassMember, regulation);
										RegulationSpecType regSpecType = getRegulationSpecTypeHome().
												findByRegulationSpecType(postingDetail.getRuleSpecType());
										String[] postings = getPostingBusiness().getPostingStrings(category, schoolClassMember.getSchoolType(), ((Integer)regSpecType.getPrimaryKey()).intValue(), provider,currentDate);
										System.out.println("about to create payment record");
										createPaymentRecord(postingDetail,postings[0],postings[1]);
										System.out.println("created payment record");
										
										//Find the oppen verksamhet and fritidsklubb
										int schoolYear = schoolClassMember.getSchoolYear().getSchoolYearAge();
										if(schoolYear<=3){
											Iterator typeIter = schoolClassMember.getSchoolClass().getSchool().getSchoolTypes().iterator();
											while (typeIter.hasNext()) {
												SchoolType schoolType = (SchoolType) typeIter.next();
												if(schoolType.getLocalizationKey().equalsIgnoreCase(OPPEN_VERKSAMHET)){
													ArrayList oppenConditions = new ArrayList();
													oppenConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION,OPPEN_VERKSAMHET));
													Collection regulationForTypeArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
														category.getCategory(),			//The ID that selects barnomsorg in the regulation
														PaymentFlowConstant.OUT, 		//The payment flow is out
														currentDate,						//Current date to select the correct date range
														RuleTypeConstant.DERIVED,		//The conditiontype
														conditions							//The conditions that need to fulfilled
														);
													Iterator regulationForTypeIter = regulationForTypeArray.iterator();
													while(regulationForTypeIter.hasNext())
													{
														regulation = (Regulation)regulationForTypeIter.next();
														postingDetail = regBus.getPostingDetailForPlacement(0.0f,schoolClassMember, regulation);
														regSpecType = getRegulationSpecTypeHome().
															findByRegulationSpecType(postingDetail.getRuleSpecType());
														createPaymentRecord(postingDetail,postings[0],postings[1]);
													}

													//TODO (JJ) Supposed to do something here... Waiting for info from Lotta
												}
											}
										} else if(schoolYear<=6){
											Iterator typeIter = schoolClassMember.getSchoolClass().getSchool().getSchoolTypes().iterator();
											while (typeIter.hasNext()) {
												SchoolType schoolType = (SchoolType) typeIter.next();
												if(schoolType.getLocalizationKey().equalsIgnoreCase(FRITIDSKLUBB)){
													//TODO (JJ) Supposed to do something here... Waiting for info from Lotta
													ArrayList oppenConditions = new ArrayList();
													oppenConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION,OPPEN_VERKSAMHET));
													Collection regulationForTypeArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
														category.getCategory(),			//The ID that selects barnomsorg in the regulation
														PaymentFlowConstant.OUT, 		//The payment flow is out
														currentDate,						//Current date to select the correct date range
														RuleTypeConstant.DERIVED,		//The conditiontype
														conditions							//The conditions that need to fulfilled
														);
													Iterator regulationForTypeIter = regulationForTypeArray.iterator();
													while(regulationForTypeIter.hasNext())
													{
														regulation = (Regulation)regulationForTypeIter.next();
														postingDetail = regBus.getPostingDetailForPlacement(0.0f,schoolClassMember, regulation);
														regSpecType = getRegulationSpecTypeHome().
															findByRegulationSpecType(postingDetail.getRuleSpecType());
														createPaymentRecord(postingDetail,postings[0],postings[1]);
													}
												}
											}
										}

										Iterator resourceIter = getResourceBusiness().getResourcePlacementsByMemberId((Integer)schoolClassMember.getStudent().getPrimaryKey()).iterator();
										while (resourceIter.hasNext()) {
											Resource resource = (Resource) resourceIter.next();
											ArrayList resourceConditions = new ArrayList();
											resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_RESOURCE,resource.getResourceName()));
											
											Collection regulationForResourceArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
												category.getCategory(),			//The ID that selects barnomsorg in the regulation
												PaymentFlowConstant.OUT, 		//The payment flow is out
												currentDate,						//Current date to select the correct date range
												RuleTypeConstant.DERIVED,		//The conditiontype
												resourceConditions				//The conditions that need to fulfilled
												);

											Iterator regulationForResourceIter = regulationForResourceArray.iterator();
											while(regulationForResourceIter.hasNext())
											{
												regulation = (Regulation)regulationForResourceIter.next();
												postingDetail = regBus.getPostingDetailForPlacement(0.0f,schoolClassMember, regulation);
												regSpecType = getRegulationSpecTypeHome().
													findByRegulationSpecType(postingDetail.getRuleSpecType());
												createPaymentRecord(postingDetail,postings[0],postings[1]);
											}
										}
									}
								}catch(NullPointerException e){
									e.printStackTrace();
									if(schoolClassMember != null){
										createNewErrorMessage(schoolClassMember.getStudent().getName(),"invoice.Child with no school type for school placement");
									}else{
										createNewErrorMessage("invoice.ContractCreation","invoice.nullpointer");
									}
								}
							}
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.DBError");
				} catch (FinderException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.CouldNotFindContractForSchool");
				} catch (CreateException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.CouldNotInsertIntoDatabase");
				} catch (PostingException e) {
					e.printStackTrace();
					createNewErrorMessage(school.getName(),"invoice.PostingString");
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.ContractCreation","invoice.DBError");
		} catch (FinderException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.ContractCreation","invoice.CouldNotFindSchoolCategory");
		} catch (EJBException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.ContractCreation","invoice.CouldNotFindHomeCommune");
		} catch (CreateException e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.ContractCreation","invoice.CouldNotFindHomeCommune");
		} catch (Exception e) {
			e.printStackTrace();
			createNewErrorMessage("invoice.ContractCreation","invoice.Exception");
		}
	}
	
	/**
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the Regular payments
	 */
	protected void regularPayment(){
		PostingDetail postingDetail = null;
		
		try {
			Iterator regularPaymentIter = getRegularPaymentBusiness().findRegularPaymentsForPeriode(startPeriod.getDate(), endPeriod.getDate()).iterator();
			//Go through all the regular payments
			while(regularPaymentIter.hasNext()){
				RegularPaymentEntry regularPaymentEntry = (RegularPaymentEntry)regularPaymentIter.next();
				postingDetail = new PostingDetail(regularPaymentEntry);
				createPaymentRecord(postingDetail,regularPaymentEntry.getOwnPosting(), regularPaymentEntry.getDoublePosting());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(postingDetail != null){
				createNewErrorMessage(postingDetail.getTerm(),"payment.DBSetupProblem");
			}else{
				createNewErrorMessage("payment.severeError","payment.DBSetupProblem");
			}
		}
	}
	
	/*
	 * Overridden function until billing is done by period instead of date (non-Javadoc)
	 * Now we always bill for the whole month...
	 * Just remove this function when changing to date range
	 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread#calculateTime(java.sql.Date, java.sql.Date)
	 */
	/**
	 * calculatest the number of days and months between the start and end date 
	 * and sets the local variables monts and days
	 * 
	 * @param start
	 * @param end
	 */
	protected void calculateTime(Date start, Date end){
		startTime = new IWTimestamp(startPeriod);
		startTime.setAsDate();
		//Then get end date
		endTime = new IWTimestamp(endPeriod);
		endTime.setAsDate();
		//calc the how many months are in the given time.
		months = 1.0f;
		days = IWTimestamp.getDaysBetween(startTime, endTime);
	}

	private SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}

	protected SchoolTypeHome getSchoolTypeHome() throws RemoteException {
		return (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
	}

	private CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}

	private ResourceBusiness getResourceBusiness() throws RemoteException {
		return (ResourceBusiness) IBOLookup.getServiceInstance(iwc, ResourceBusiness.class);
	}

	private SchoolHome getSchoolHome() throws RemoteException {
		return (SchoolHome) IDOLookup.getHome(School.class);
	}

	protected SchoolCategoryHome getSchoolCategoryHome() throws RemoteException {
		return (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
	}

	protected ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) IDOLookup.getHome(ChildCareApplication.class);
	}

}
