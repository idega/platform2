package se.idega.idegaweb.commune.accounting.invoice.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.export.data.ExportDataMapping;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeader;
import se.idega.idegaweb.commune.accounting.invoice.data.PaymentRecord;
import se.idega.idegaweb.commune.accounting.invoice.data.RegularPaymentEntry;
import se.idega.idegaweb.commune.accounting.posting.business.PostingException;
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
import se.idega.idegaweb.commune.accounting.regulations.data.ProviderType;
import se.idega.idegaweb.commune.accounting.regulations.data.ProviderTypeHome;
import se.idega.idegaweb.commune.accounting.regulations.data.Regulation;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.resource.business.ResourceBusiness;
import se.idega.idegaweb.commune.accounting.resource.data.ResourceClassMember;
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
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Commune;
import com.idega.core.location.data.CommuneHome;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Abstract class that holds all the logic that is common for the shool billing
 * 
 * Last modified: $Date: 2003/12/16 15:53:13 $ by $Author: staffan $
 *
 * @author <a href="mailto:joakim@idega.com">Joakim Johnson</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.59 $
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public abstract class PaymentThreadSchool extends BillingThread {
	Logger log = Logger.getLogger(this.getClass().getName());
	PaymentHeader paymentHeader;

	//This is a horrible solution... This class should not have to know the localization keys!!!	
	private static final String OPPEN_VERKSAMHET = "sch_type.school_type_oppen_verksamhet";
	private static final String FRITIDSKLUBB = "sch_type.school_type_fritidsklubb";
    
	public PaymentThreadSchool(Date month, IWContext iwc) {
		super(month, iwc);
		currentDate = month;
	}
    
	public boolean isInDefaultCommune(User user) throws RemoteException, FinderException {
		Address address = getCommuneUserBusiness().getUsersMainAddress(user);
		Commune commmune = getCommuneHome().findByPrimaryKey(new Integer(address.getCommuneID()));
		return commmune.getIsDefault();
	}

	/**
	 * 
	 *
	 */
	protected void contracts() throws NotEmptyException {
		School school;
		
		try {
			timerStart();
			//Set the category parameter to ElementarySchool
			categoryPosting = getCategoryPosting();
			ProviderTypeHome providerTypeHome = (ProviderTypeHome) IDOLookup.getHome(ProviderType.class);
			ProviderType providerType = providerTypeHome.findPrivateType();
			if (hasPlacements()) {
				throw new NotEmptyException("invoice.must_first_empty_old_data");
			}
            
			int privateType = ((Integer) providerType.getPrimaryKey()).intValue();
            
			RegulationsBusiness regBus = getRegulationsBusiness();
			
			//Go through all elementary schools
			for (Iterator i = getSchools().iterator(); i.hasNext();) {
				dispTime("Enter main loop");
				try {
					school = (School) i.next();
					System.err.print ("### school = " + school.getName ());
					errorRelated = new StringBuffer("School "+school.getName());
					Provider provider = new Provider(((Integer) school.getPrimaryKey()).intValue());
					//Only look at those not "payment by invoice"
					//Check if it is private or in Nacka
					final boolean schoolIsInDefaultCommune = school.getCommune().getIsDefault();
					System.err.print ("; inDefaultCommune = " + schoolIsInDefaultCommune);
					final boolean schoolIsPrivate = provider.getProviderTypeId() == privateType;
					System.err.print ("; isPrivate = " + schoolIsPrivate);
					System.err.print ("; paymentByInvoice = " + provider.getPaymentByInvoice());
					if ((schoolIsInDefaultCommune || schoolIsPrivate) && !provider.getPaymentByInvoice()) {
						System.err.println ("; investigate school = yes");
						for (Iterator j = getSchoolClassMembers(school).iterator(); j.hasNext();) {
							try{
								SchoolClassMember schoolClassMember = (SchoolClassMember) j.next();
								createPaymentForSchoolClassMember (regBus, provider, schoolClassMember, schoolIsInDefaultCommune && !schoolIsPrivate);
							} catch(NullPointerException e){
								e.printStackTrace();
								createNewErrorMessage("invoice.PaymentSchool","invoice.nullpointer");
							}
							catch (MissingFlowTypeException e) {
								e.printStackTrace();
								if(errorRelated!=null)
								{
									createNewErrorMessage(errorRelated.toString(), "invoice.ErrorFindingFlowType");
								}else{
									createNewErrorMessage("invoice.school", "invoice.ErrorFindingFlowType");
								}
							}
							catch (MissingConditionTypeException e) {
								e.printStackTrace();
								if(errorRelated!=null)
								{
									createNewErrorMessage(errorRelated.toString(), "invoice.ErrorFindingConditionType");
								}else{
									createNewErrorMessage("invoice.school", "invoice.ErrorFindingConditionType");
								}
							}
							catch (MissingRegSpecTypeException e) {
								e.printStackTrace();
								if(errorRelated!=null)
								{
									createNewErrorMessage(errorRelated.toString(), "invoice.ErrorFindingRegSpecType");
								}else{
									createNewErrorMessage("invoice.school", "invoice.ErrorFindingRegSpecType");
								}
							}
							catch (TooManyRegulationsException e) {
								e.printStackTrace();
								if(errorRelated!=null)
								{
									createNewErrorMessage(errorRelated.toString(), "invoice.ErrorFindingTooManyRegulations");
								}else{
									createNewErrorMessage("invoice.school", "invoice.ErrorFindingTooManyRegulations");
								}
							}
						}
					}else{
						System.err.println ("; investigate school = no");
						log.info("School "+school.getName()+" is not in home commune and not private or gets payment by invoice");
					}
				} catch (RemoteException e) {
					e.printStackTrace();
					if(errorRelated!=null)
					{
						createNewErrorMessage(errorRelated.toString(), "invoice.DBError_Creating_contracts_for_school");
					}else{
						createNewErrorMessage("invoice.school", "invoice.DBError_Creating_contracts_for_school");
					}
				} catch (FinderException e) {
					e.printStackTrace();
					if(errorRelated!=null)
					{
						createNewErrorMessage(errorRelated.toString(), "invoice.CouldNotFindContractForSchool");
					}else{
						createNewErrorMessage("invoice.school", "invoice.CouldNotFindContractForSchool");
					}
				} catch (CreateException e) {
					e.printStackTrace();
					if(errorRelated!=null)
					{
						createNewErrorMessage(errorRelated.toString(), "invoice.CouldNotInsertIntoDatabase");
					}else{
						createNewErrorMessage("invoice.school", "invoice.CouldNotInsertIntoDatabase");
					}
				} catch (PostingException e) {
					e.printStackTrace();
					if(errorRelated!=null)
					{
						createNewErrorMessage(errorRelated.toString(), "invoice.PostingString");
					}else{
						createNewErrorMessage("invoice.school", "invoice.PostingString");
					}
				} catch (RegulationException e) {
					e.printStackTrace();
					if(errorRelated!=null)
					{
						createNewErrorMessage(errorRelated.toString(), "invoice.RegulationException");
					}else{
						createNewErrorMessage("invoice.Regulation", "invoice.RegulationException");
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
					java.io.StringWriter sw = new java.io.StringWriter ();
					e.printStackTrace (new java.io.PrintWriter (sw, true));
					errorRelated.append ("</br>" + sw + "</br>");
					if (errorRelated.length () > 900) errorRelated = new StringBuffer (errorRelated.substring (1, 900));
					createNewErrorMessage(errorRelated.toString(), "invoice.NullpointerException");
				}
				dispTime("Getting regulations for resource");
				timerStart();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			if(errorRelated!=null)
			{
				createNewErrorMessage(errorRelated.toString(), "invoice.Severe_DBError");
			}else{
				createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_DBError");
			}
		} catch (FinderException e) {
			e.printStackTrace();
			if(errorRelated!=null)
		{
			createNewErrorMessage(errorRelated.toString(), "invoice.Severe_CouldNotFindSchoolCategory");
		}else{
			createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_CouldNotFindSchoolCategory");
		}
		} catch (EJBException e) {
			e.printStackTrace();
			if(errorRelated!=null)
			{
				createNewErrorMessage(errorRelated.toString(), "invoice.Severe_CouldNotFindHomeCommune");
			}else{
				createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_CouldNotFindHomeCommune");
			}
		} catch (CreateException e) {
			e.printStackTrace();
			if(errorRelated!=null)
			{
				createNewErrorMessage(errorRelated.toString(), "invoice.Severe_CouldNotFindHomeCommune");
			}else{
				createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_CouldNotFindHomeCommune");
			}
		} catch (IDOException e) {
			e.printStackTrace();
			if(errorRelated!=null)
			{
				createNewErrorMessage(errorRelated.toString(), "invoice.Severe_IDOException");
			}else{
				createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_IDOException");
			}
		}
	}
	
	private void createPaymentForSchoolClassMember(RegulationsBusiness regBus, Provider provider, SchoolClassMember schoolClassMember, boolean schoolIsInDefaultCommuneAndNotPrivate) 
			throws FinderException, EJBException, PostingException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		if (null != schoolClassMember.getStudent()) {
			errorRelated.append("Student "+schoolClassMember.getStudent().getName()+"<br>");
			dispTime("Found " + schoolClassMember.getStudent().getName());
		}
		System.err.print ("&&& member id = " + schoolClassMember.getPrimaryKey ());
		System.err.print ("; pid = " + schoolClassMember.getStudent ().getPersonalID ());
		System.err.print ("; name = " + schoolClassMember.getStudent ().getName ());
		final boolean placementIsInPeriod = isPlacementInPeriod(schoolClassMember);
		System.err.print ("; placementIsInPeriod = " + placementIsInPeriod);
		final boolean userIsInDefaultCommune = getCommuneUserBusiness().isInDefaultCommune(schoolClassMember.getStudent());
		System.err.print ("; userIsInDefaultCommune = " + userIsInDefaultCommune);
		System.err.print ("; schoolIsInDefaultCommuneAndNotPrivate = " + schoolIsInDefaultCommuneAndNotPrivate);
		if (placementIsInPeriod  && (userIsInDefaultCommune || schoolIsInDefaultCommuneAndNotPrivate)) {
			System.err.println ("; create payment = yes");
			ArrayList conditions = getConditions (schoolClassMember);
			School school = schoolClassMember.getSchoolClass().getSchool();
			errorRelated.append(
			   "Category "+ category.getCategory()+"<br>"
			   + "PaymentFlowConstant.OUT "+ PaymentFlowConstant.OUT+"<br>"
			   + "Date "+ currentDate.toString()+"<br>"
			   + "RuleTypeConstant.DERIVED "+ RuleTypeConstant.DERIVED+"<br>"
			   + "#conditions "+ conditions.size()+"<br>"
			   );
			//Get the check
			PostingDetail postingDetail = regBus.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(
				category.getCategory(),		//The ID that selects barnomsorg in the regulation
				PaymentFlowConstant.OUT, 	//The payment flow is out
				currentDate, 					//Current date to select the correct date range
				RuleTypeConstant.DERIVED,	//The conditiontype
				RegSpecConstant.CHECK,		//The ruleSpecType shall be Check
				conditions, 					//The conditions that need to fulfilled
				0, 								//Sent in to be used for "Specialutrakning"
				null);							//Sent in to be used for "Specialutrakning"
			RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
			String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
			PlacementTimes placementTimes = getPlacementTimes(schoolClassMember);
			final PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
			createInvoiceRecord (record, schoolClassMember, postingDetail, placementTimes);
			SchoolType schoolType = null;
			//Find the oppen verksamhet and fritidsklubb
			try {
				int schoolYear = Integer.parseInt(schoolClassMember.getSchoolYear().getName());
                
				if (schoolYear <= 3) {
					for (Iterator i = getSchoolTypes(schoolClassMember).iterator(); i.hasNext();) {
						schoolType = (SchoolType) i.next();
						if (schoolType.getLocalizationKey().equalsIgnoreCase(OPPEN_VERKSAMHET)) {
							createPaymentsForOppenVerksamhet(regBus, provider, schoolClassMember, conditions, placementTimes);
						}
					}
				}else if (schoolYear <= 6) {
					for (Iterator i = getSchoolTypes(schoolClassMember).iterator(); i.hasNext();) {
						schoolType = (SchoolType) i.next();
						if (schoolType.getLocalizationKey().equalsIgnoreCase(FRITIDSKLUBB))
							createPaymentsForFritidsklubb(regBus, provider, schoolClassMember, conditions, placementTimes);
					}
				}
			} catch (NumberFormatException e) {
				//That's OK I only want years 1-6
			} catch (IDORelationshipException e) {
				e.printStackTrace();
				if(errorRelated!=null)
				{
					createNewErrorMessage(errorRelated.toString(), "invoice.DBRelationshipError");
				}else{
					createNewErrorMessage(createRelatedString(category,schoolClassMember.getSchoolType(),school,schoolClassMember.getStudent()), "invoice.DBRelationshipError");
				}
			}
			//Get all the resources for the child
			Collection resources = getResourceBusiness().getResourcePlacementsByMemberId((Integer) schoolClassMember.getPrimaryKey());
			log.info("Found "+resources.size()+" resources for "+schoolClassMember.getStudent().getName());
			for (Iterator i = resources.iterator(); i.hasNext();) {
				ResourceClassMember resource = (ResourceClassMember) i.next();
				try {
					createPaymentsForResource(regBus, provider, schoolClassMember, conditions, resource);
				} catch (NullPointerException e) {
					e.printStackTrace();
					errorRelated = new StringBuffer ();
					User user = null != schoolClassMember ? schoolClassMember.getStudent () : null;
					String studentInfo = null != user ? user.getName () + " " + user.getPersonalID () : "";
					errorRelated.append ("Student = " + studentInfo + "<br/>");
					errorRelated.append ("Conditions = " + conditions + "<br/>");
					errorRelated.append ("Resource = " + resource + "<br/>");
					java.io.StringWriter sw = new java.io.StringWriter ();
					e.printStackTrace (new java.io.PrintWriter (sw, true));
					errorRelated.append (sw + "</br>");
					if (errorRelated.length () > 900) errorRelated = new StringBuffer (errorRelated.substring (1, 900));
					createNewErrorMessage(errorRelated.toString (),"invoice.createPaymentsForResourceError");
				}
			}
		} else System.err.println ("; create payment = no");

	}

	private PlacementTimes getPlacementTimes(SchoolClassMember schoolClassMember) {
		Date sDate = null;
		Date eDate = null;
		if (schoolClassMember.getRegisterDate() != null) {
			sDate = new Date(schoolClassMember.getRegisterDate().getTime());
		}
		if (schoolClassMember.getRemovedDate() != null) {
			eDate = new Date(schoolClassMember.getRemovedDate().getTime());
		}
		PlacementTimes placementTimes = calculateTime(sDate, eDate);
		return placementTimes;
	}

	private boolean isPlacementInPeriod(SchoolClassMember schoolClassMember) {
		IWTimestamp placementStart = null;
		IWTimestamp placementEnd = null;
		if (schoolClassMember.getRegisterDate() != null) {
			placementStart = new IWTimestamp(schoolClassMember.getRegisterDate().getTime());
		}
		if (schoolClassMember.getRemovedDate() != null) {
			placementEnd = new IWTimestamp(schoolClassMember.getRemovedDate().getTime());
		}
		IWTimestamp periodStart = new IWTimestamp(startPeriod);
		startPeriod.setAsDate();
		IWTimestamp periodEnd = new IWTimestamp(endPeriod);
		endPeriod.setAsDate();
		final boolean placementIsInPeriod = !placementStart.isLaterThan (periodEnd) && (null == placementEnd || !periodStart.isLaterThan (placementEnd));
		return placementIsInPeriod;
	}

	private void createPaymentsForResource(RegulationsBusiness regBus, Provider provider, SchoolClassMember schoolClassMember, ArrayList conditions, ResourceClassMember resource)
			throws EJBException, FinderException, PostingException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		final Date startDate = resource.getStartDate();
		final Date endDate = resource.getEndDate ();
		final PlacementTimes placementTimes = calculateTime (startDate, endDate);
		School school = schoolClassMember.getSchoolClass().getSchool();
		Collection resourceConditions = new ArrayList();
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, schoolClassMember.getSchoolType().getLocalizationKey()));
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_RESOURCE, resource.getResource().getResourceName()));
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_SCHOOL_YEAR, schoolClassMember.getSchoolYear().getName()));        
		Collection regulationForResourceArray =
			regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
				category.getCategory(),	//The ID that selects barnomsorg in the regulation
				PaymentFlowConstant.OUT,	//The payment flow is out
				currentDate,					//Current date to select the correct date range
				RuleTypeConstant.DERIVED,//The conditiontype
				RegSpecConstant.RESOURCE,
				resourceConditions			//The conditions that need to fulfilled
			);
		dispTime("Getting regulations for resource of size " + regulationForResourceArray.size());		
		for (Iterator i = regulationForResourceArray.iterator(); i.hasNext();) {
			try {
				Regulation regulation = (Regulation) i.next();
				PostingDetail postingDetail = regBus.getPostingDetailForPlacement(0.0f, schoolClassMember, regulation, currentDate, conditions);
				RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
				String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
				PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
				createInvoiceRecord (record, schoolClassMember, postingDetail, placementTimes, startDate, endDate);
			} catch (BruttoIncomeException e) {
				//Who cares!!!
			} catch (LowIncomeException e) {
				
			}
		}
	}

	private void createPaymentsForFritidsklubb(
			RegulationsBusiness regBus,Provider provider,SchoolClassMember 
			schoolClassMember,ArrayList conditions, PlacementTimes placementTimes)
			throws FinderException, PostingException, EJBException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		School school = schoolClassMember.getSchoolClass().getSchool();
		ArrayList oppenConditions = new ArrayList();
		oppenConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, FRITIDSKLUBB));
		Collection regulationForTypeArray =
			regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
			category.getCategory(),		//The ID that selects barnomsorg in the regulation
			PaymentFlowConstant.OUT,	//The payment flow is out
			currentDate,					//Current date to select the correct date range
			RuleTypeConstant.DERIVED,	//The conditiontype
			null, oppenConditions		//The conditions that need to fulfilled
		);
		for (Iterator i = regulationForTypeArray.iterator(); i.hasNext();) {
			try {
				Regulation regulation = (Regulation) i.next();
				PostingDetail postingDetail =
					regBus.getPostingDetailForPlacement(0.0f, schoolClassMember, regulation, currentDate, conditions);
				RegulationSpecType regSpecType =
					getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
				String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
				PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
				System.out.println("created payment info for fritidsklubb " + schoolClassMember.getStudent().getName());
				createInvoiceRecord (record, schoolClassMember, postingDetail, placementTimes);
			} catch (BruttoIncomeException e) {
				//Who cares!!!
			} catch (LowIncomeException e) {

			}
		}
	}
	
	private void createPaymentsForOppenVerksamhet(
			RegulationsBusiness regBus, Provider provider, SchoolClassMember 
			schoolClassMember, ArrayList conditions, PlacementTimes placementTimes)
			throws FinderException, PostingException, EJBException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		ArrayList oppenConditions = new ArrayList();
		School school = schoolClassMember.getSchoolClass().getSchool();
		
		oppenConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, OPPEN_VERKSAMHET));
		Collection regulationForTypeArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
			category.getCategory(),			//The ID that selects barnomsorg in the regulation
			PaymentFlowConstant.OUT,		//The payment flow is out
			currentDate,						//Current date to select the correct date range
			RuleTypeConstant.DERIVED,		//The conditiontype
			null, 
			oppenConditions			//The conditions that need to fulfilled
		);
		for (Iterator i = regulationForTypeArray.iterator(); i.hasNext();) {
			try {
				Regulation regulation = (Regulation) i.next();
				PostingDetail postingDetail =
					regBus.getPostingDetailForPlacement(0.0f, schoolClassMember, regulation, currentDate, conditions);
				RegulationSpecType regSpecType =
					getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
				String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
				PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
				createInvoiceRecord (record, schoolClassMember, postingDetail, placementTimes);
			} catch (BruttoIncomeException e) {
				//Who cares!!!
			} catch (LowIncomeException e) {

			}
		}
	}
    
	/**
	 * Used to create a description of every entity that is related 
	 * @param cat
	 * @param type
	 * @param sch
	 * @param child
	 * @return
	 */
	protected String createRelatedString(SchoolCategory cat, SchoolType type, School sch, User child){
		StringBuffer ret = new StringBuffer();
		if(cat!=null){
			ret.append(cat.getCategory());
		}
		if(type!=null){
			if(ret.length()!=0){
				ret.append("<br>");
			}
			ret.append(type.getName());
		}
		if(sch!=null){
			if(ret.length()!=0){
				ret.append("<br>");
			}
			ret.append(sch.getName());
		}
		if(child!=null){
			if(ret.length()!=0){
				ret.append("<br>");
			}
			ret.append(child.getName());
		}
		return ret.toString();
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
				log.info("Looking at reg Payment "+regularPaymentEntry.getPrimaryKey());
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
	 * Creates all the invoice headers, invoice records, payment headers and payment records
	 * for the Regular payments
	 */
/*	protected void regularPayment() {
		PostingDetail postingDetail = null;
		try {
			//Go through all the regular payments
			for (Iterator i = getRegularPayments().iterator(); i.hasNext();) {
				RegularPaymentEntry regularPaymentEntry = (RegularPaymentEntry) i.next();
				postingDetail = new PostingDetail(regularPaymentEntry);
				PlacementTimes placementTimes = calculateTime(regularPaymentEntry.getFrom(), regularPaymentEntry.getTo());
				createPaymentRecord(postingDetail, regularPaymentEntry.getOwnPosting(), regularPaymentEntry.getDoublePosting(), placementTimes.getMonths(), regularPaymentEntry.getSchool());
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (postingDetail != null) {
				createNewErrorMessage(postingDetail.getTerm(), "payment.DBSetupProblem");
			} else {
				createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			}
		}
	}
*/
	private ArrayList getConditions(SchoolClassMember schoolClassMember) {
		ArrayList conditions = new ArrayList();
		conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, schoolClassMember.getSchoolType().getLocalizationKey()));
		conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_SCHOOL_YEAR, schoolClassMember.getSchoolYear().getName()));
		errorRelated.append("SchoolType "+schoolClassMember.getSchoolType().getName()+"<br>");
		errorRelated.append("School Year "+schoolClassMember.getSchoolYear().getName()+"<br>");
		int studyPathId = schoolClassMember.getStudyPathId();
		if(studyPathId!=-1){
			conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_STUDY_PATH,new Integer(studyPathId)));
			errorRelated.append("Study path "+schoolClassMember.getStudyPathId()+"<br>");
		}
		return conditions;
	}

	//Temporary code to meassure the performance of the batchrun
	long start, stop, time;
	private void timerStart() {
		start = System.currentTimeMillis();
		stop = start;
	}
    
	private void dispTime(String s) {
		long t = System.currentTimeMillis();
		long tt;
		tt = t - start;
		time = t - stop;
		log.info(s + "  total time:" + (tt / 1000f) + "  from last stop, time:" + (time / 1000f));
		stop = t;
	}

	private ExportDataMapping getCategoryPosting() throws FinderException, IDOLookupException, EJBException {
		return (ExportDataMapping) IDOLookup.getHome(ExportDataMapping.class).findByPrimaryKeyIDO(category.getPrimaryKey());
	}

	private Collection getSchools() throws IDOLookupException, EJBException, FinderException, CreateException, RemoteException {
		return getSchoolHome().findAllByCategory(category);
	}

	private Collection getSchoolClassMembers(School school) throws FinderException, RemoteException, EJBException {
		return getSchoolClassMemberHome().findBySchool(((Integer) school.getPrimaryKey()).intValue(), -1, category.getCategory(), currentDate);
	}

	private Collection getSchoolTypes(SchoolClassMember schoolClassMember) throws IDORelationshipException {
		return schoolClassMember.getSchoolClass().getSchool().getSchoolTypes();
	}

	private String[] getPostingStrings(Provider provider, SchoolClassMember schoolClassMember, RegulationSpecType regSpecType) throws PostingException, RemoteException, EJBException {
		return getPostingBusiness().getPostingStrings(category, schoolClassMember.getSchoolType(), ((Integer) regSpecType.getPrimaryKey()).intValue(), provider, currentDate,((Integer)schoolClassMember.getSchoolYear().getPrimaryKey ()).intValue());
	}

	private CommuneHome getCommuneHome() throws RemoteException {
		return (CommuneHome) IDOLookup.getHome(Commune.class);
	}
    
	protected Collection getRegularPayments() throws RemoteException {
		return getRegularPaymentBusiness().findRegularPaymentsForPeriode(startPeriod.getDate(), endPeriod.getDate());
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
