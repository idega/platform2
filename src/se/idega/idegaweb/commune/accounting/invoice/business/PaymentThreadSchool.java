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
import se.idega.util.ErrorLogger;

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
 * Last modified: $Date: 2004/01/04 20:16:13 $ by $Author: joakim $
 *
 * @author <a href="mailto:joakim@idega.com">Joakim Johnson</a>
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.80 $
 * 
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadElementarySchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.PaymentThreadHighSchool
 * @see se.idega.idegaweb.commune.accounting.invoice.business.BillingThread
 */
public abstract class PaymentThreadSchool extends BillingThread {
	Logger log = Logger.getLogger(this.getClass().getName());
	PaymentHeader paymentHeader;

	//This is a horrible solution... This class should not have to know the
	// localization keys!!!
	private static final String OPPEN_VERKSAMHET = "sch_type.school_type_oppen_verksamhet";
	private static final String FRITIDSKLUBB = "sch_type.school_type_fritidsklubb";

	public PaymentThreadSchool(Date month, IWContext iwc) {
		super(month, iwc);
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
//			for (int i = 0; i < 1; i++) {
				dispTime("Enter main loop");
				try {
					school = (School) i.next();
//					school = getSchoolHome().findByPrimaryKey(new Integer(8));
					errorRelated = new ErrorLogger();
					errorRelated.append("School " + school.getName(),1);
					Provider provider = new Provider(((Integer) school.getPrimaryKey()).intValue());
					//Only look at those not "payment by invoice"
					//Check if it is private or in Nacka
					final boolean schoolIsInDefaultCommune = school.getCommune().getIsDefault();
					final boolean schoolIsPrivate = provider.getProviderTypeId() == privateType;
					errorRelated.logToConsole();
					if ((schoolIsInDefaultCommune || schoolIsPrivate) && !provider.getPaymentByInvoice()) {
						ErrorLogger tmpErrorRelated = new ErrorLogger(errorRelated.toString());
						Collection pupils = getSchoolClassMembers(school);
						Iterator j = pupils.iterator();
//						for (Iterator j = getSchoolClassMembers(school).iterator(); j.hasNext();) {
//						System.out.println("Found " + pupils.size() + " class members");
						for (; j.hasNext();) {
							try {
								errorRelated = new ErrorLogger(tmpErrorRelated);
								SchoolClassMember schoolClassMember = (SchoolClassMember) j.next();
								errorRelated.append("SchoolClassMemeber: "+schoolClassMember.getPrimaryKey());
								createPaymentForSchoolClassMember(regBus, provider, schoolClassMember, schoolIsInDefaultCommune && !schoolIsPrivate);
							}
							catch (NullPointerException e) {
								e.printStackTrace();
								createNewErrorMessage("invoice.PaymentSchool", "invoice.nullpointer");
							}
							catch (MissingFlowTypeException e) {
								e.printStackTrace();
								createNewErrorMessage(errorRelated, "invoice.ErrorFindingFlowType");
							}
							catch (MissingConditionTypeException e) {
								e.printStackTrace();
								createNewErrorMessage(errorRelated, "invoice.ErrorFindingConditionType");
							}
							catch (MissingRegSpecTypeException e) {
								e.printStackTrace();
								createNewErrorMessage(errorRelated, "invoice.ErrorFindingRegSpecType");
							}
							catch (TooManyRegulationsException e) {
								e.printStackTrace();
								errorRelated.append("Regulations found:"+e.getRegulationNamesString());
								createNewErrorMessage(errorRelated,"invoice.ErrorFindingTooManyRegulations");
							}
							catch (PostingException e) {
								e.printStackTrace();
								createNewErrorMessage(errorRelated, "invoice.PostingString");
							}
							catch (RegulationException e) {
								e.printStackTrace();
								createNewErrorMessage(errorRelated, "invoice.RegulationException");
							}
							catch (RemoteException e) {
								e.printStackTrace();
								createNewErrorMessage(errorRelated, "invoice.RemoteException");
							}
							catch (FinderException e) {
								e.printStackTrace();
								createNewErrorMessage(errorRelated, "invoice.FinderException");
							}
							catch (EJBException e) {
								e.printStackTrace();
								createNewErrorMessage(errorRelated, "invoice.EJBException");
							}
							catch (CreateException e) {
								e.printStackTrace();
								createNewErrorMessage(errorRelated, "invoice.CreateException");
							}
						}
					}else{
						log.info("School "+school.getName()+" is not in home commune and not private or gets payment by invoice");
					}
				}
				catch (RemoteException e) {
					e.printStackTrace();
					if (errorRelated != null) {
						createNewErrorMessage(errorRelated, "invoice.DBError_Creating_contracts_for_school");
					}
					else {
						createNewErrorMessage("invoice.school", "invoice.DBError_Creating_contracts_for_school");
					}
				}
				catch (FinderException e) {
					e.printStackTrace();
					if (errorRelated != null) {
						createNewErrorMessage(errorRelated, "invoice.CouldNotFindContractForSchool");
					}
					else {
						createNewErrorMessage("invoice.school", "invoice.CouldNotFindContractForSchool");
					}
				}
				catch (NullPointerException e) {
					e.printStackTrace();
					java.io.StringWriter sw = new java.io.StringWriter();
					e.printStackTrace(new java.io.PrintWriter(sw, true));
					errorRelated.append("</br>" + sw + "</br>");
					if (errorRelated.toString().length() > 900)
						errorRelated = new ErrorLogger(errorRelated.toString().substring(1, 900));
					createNewErrorMessage(errorRelated, "invoice.NullpointerException");
				}
				dispTime("Getting regulations for resource");
				timerStart();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				createNewErrorMessage(errorRelated, "invoice.Severe_DBError");
			}
			else {
				createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_DBError");
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				createNewErrorMessage(errorRelated, "invoice.Severe_CouldNotFindSchoolCategory");
			}
			else {
				createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_CouldNotFindSchoolCategory");
			}
		}
		catch (EJBException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				createNewErrorMessage(errorRelated, "invoice.Severe_CouldNotFindHomeCommune");
			}
			else {
				createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_CouldNotFindHomeCommune");
			}
		}
		catch (CreateException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				createNewErrorMessage(errorRelated, "invoice.Severe_CouldNotFindHomeCommune");
			}
			else {
				createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_CouldNotFindHomeCommune");
			}
		}
		catch (IDOException e) {
			e.printStackTrace();
			if (errorRelated != null) {
				createNewErrorMessage(errorRelated, "invoice.Severe_IDOException");
			}
			else {
				createNewErrorMessage("invoice.PaymentSchool", "invoice.Severe_IDOException");
			}
		}
	}
	
	private void createPaymentForSchoolClassMember(RegulationsBusiness regBus, Provider provider, SchoolClassMember schoolClassMember, boolean schoolIsInDefaultCommuneAndNotPrivate) 
			throws FinderException, EJBException, PostingException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		if (null != schoolClassMember.getStudent()) {
			errorRelated.append("Student "+schoolClassMember.getStudent().getName());
			dispTime("Found " + schoolClassMember.getStudent().getName());
		}
		errorRelated.logToConsole();
		final boolean placementIsInPeriod = isPlacementInPeriod(schoolClassMember);
		final boolean userIsInDefaultCommune = getCommuneUserBusiness().isInDefaultCommune(schoolClassMember.getStudent());
		final boolean placementIsInValidGroup = schoolClassMember.getSchoolClass().getValid();
		if (placementIsInValidGroup && placementIsInPeriod
				&& (userIsInDefaultCommune || schoolIsInDefaultCommuneAndNotPrivate)) {
			ArrayList conditions = getConditions(schoolClassMember, provider);
			School school = schoolClassMember.getSchoolClass().getSchool();
			errorRelated.append("Category " + category.getCategory() + "<br>" + "PaymentFlowConstant.OUT " + PaymentFlowConstant.OUT + "<br>" + "Date " + calculationDate.toString() + "<br>" + "RuleTypeConstant.DERIVED " + RuleTypeConstant.DERIVED + "<br>" + "#conditions " + conditions.size() + "<br>");
			//Get the check
			PostingDetail postingDetail = regBus.getPostingDetailByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(), /*The ID that selects barnomsorg in the regulation */ 
							PaymentFlowConstant.OUT, //The payment flow is out
							calculationDate, //Current date to select the correct date range
							RuleTypeConstant.DERIVED, //The conditiontype
							RegSpecConstant.CHECK, //The ruleSpecType shall be Check
							conditions, //The conditions that need to fulfilled
							0, //Sent in to be used for "Specialutrakning"
							null); //Sent in to be used for "Specialutrakning"
			RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
			String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
			PlacementTimes placementTimes = getPlacementTimes(schoolClassMember);
			log.info("About to create payment record");
			final PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
			log.info("About to create invoice record");
			createInvoiceRecord(record, schoolClassMember, postingDetail, placementTimes);
			log.info("Done creating invoice record");
			SchoolType schoolType = null;
			//Find the oppen verksamhet and fritidsklubb
			try {
				int schoolYear = Integer.parseInt(schoolClassMember.getSchoolYear().getName());

				if (schoolYear <= 3) {
					for (Iterator i = getSchoolTypes(schoolClassMember).iterator(); i.hasNext();) {
						schoolType = (SchoolType) i.next();
						if (schoolType.getLocalizationKey().equalsIgnoreCase(OPPEN_VERKSAMHET)) {
							createPaymentsForOppenVerksamhet(regBus, provider, schoolClassMember, conditions, placementTimes, schoolType);
							break;
						}
					}
				}
				else if (schoolYear <= 6) {
					for (Iterator i = getSchoolTypes(schoolClassMember).iterator(); i.hasNext();) {
						schoolType = (SchoolType) i.next();
						if (schoolType.getLocalizationKey().equalsIgnoreCase(FRITIDSKLUBB)) {
							createPaymentsForFritidsklubb(regBus, provider, schoolClassMember, conditions, placementTimes, schoolType);
							break;
						}
					}
				}
			}
			catch (NumberFormatException e) {
				//That's OK I only want years 1-6
			}
			catch (IDORelationshipException e) {
				e.printStackTrace();
				if (errorRelated != null) {
					createNewErrorMessage(errorRelated, "invoice.DBRelationshipError");
				}
				else {
					createNewErrorMessage(createRelatedString(category, schoolClassMember.getSchoolType(), school, schoolClassMember.getStudent()), "invoice.DBRelationshipError");
				}
			}
			//Get all the resources for the child
			Collection resources = getResourceBusiness().getResourcePlacementsByMemberId((Integer) schoolClassMember.getPrimaryKey());
			log.info("Found " + resources.size() + " resources for " + schoolClassMember.getStudent().getName());
			ErrorLogger tmpErrorRelated = new ErrorLogger(errorRelated.toString());
			for (Iterator i = resources.iterator(); i.hasNext();) {
				ResourceClassMember resource = (ResourceClassMember) i.next();
				errorRelated = new ErrorLogger(tmpErrorRelated);
				try {
					createPaymentsForResource(regBus, provider, schoolClassMember, conditions, resource);
				}
				catch (NullPointerException e) {
					e.printStackTrace();
//					errorRelated = new ErrorLogger();
					User user = null != schoolClassMember ? schoolClassMember.getStudent() : null;
					String studentInfo = null != user ? user.getName() + " " + user.getPersonalID() : "";
					errorRelated.append("Student = " + studentInfo);
					errorRelated.append("Conditions = " + conditions);
					errorRelated.append("Resource = " + resource);
					java.io.StringWriter sw = new java.io.StringWriter();
					e.printStackTrace(new java.io.PrintWriter(sw, true));
					errorRelated.append(sw.toString());
//					if (errorRelated.toString().length() > 900)
//						errorRelated = new StringBuffer(errorRelated.substring(1, 900));
					createNewErrorMessage(errorRelated, "invoice.createPaymentsForResourceError");
				}
			}
		}
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
		final boolean placementIsInPeriod = !placementStart.isLaterThan(periodEnd) && (null == placementEnd || !periodStart.isLaterThan(placementEnd));
		return placementIsInPeriod;
	}

	private void createPaymentsForResource(RegulationsBusiness regBus, Provider provider, SchoolClassMember schoolClassMember, ArrayList conditions, ResourceClassMember resource) throws EJBException, FinderException, PostingException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		final Date startDate = resource.getStartDate();
		final Date endDate = resource.getEndDate();
		final PlacementTimes placementTimes = calculateTime(startDate, endDate);
		School school = schoolClassMember.getSchoolClass().getSchool();
		Collection resourceConditions = new ArrayList();
		errorRelated.append("SchoolType "+schoolClassMember.getSchoolType().getName());
		errorRelated.append("Resource "+resource.getResource().getResourceName());
		errorRelated.append("SchoolYear "+schoolClassMember.getSchoolYear().getName());
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, schoolClassMember.getSchoolType().getLocalizationKey()));
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_RESOURCE, resource.getResource().getResourceName()));
		resourceConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_SCHOOL_YEAR, schoolClassMember.getSchoolYear().getName()));
		Collection regulationForResourceArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(
			category.getCategory(),
			PaymentFlowConstant.OUT,
			calculationDate,
			RuleTypeConstant.DERIVED,
			RegSpecConstant.RESOURCE, resourceConditions
			);
		errorRelated.append("# of Regulations "+regulationForResourceArray.size());
		dispTime("Getting regulations for resource of size " + regulationForResourceArray.size());
		for (Iterator i = regulationForResourceArray.iterator(); i.hasNext();) {
			try {
				Regulation regulation = (Regulation) i.next();
				errorRelated.append("Regulation "+regulation.getName());
				PostingDetail postingDetail = regBus.getPostingDetailForPlacement(0.0f, schoolClassMember, regulation, calculationDate, conditions, placementTimes);
				RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
				String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
				PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
				createInvoiceRecord(record, schoolClassMember, postingDetail, placementTimes, startDate, endDate);
			}
			catch (BruttoIncomeException e) {
				//Who cares!!!
			}
			catch (LowIncomeException e) {

			}
		}
	}

	private void createPaymentsForFritidsklubb(RegulationsBusiness regBus, Provider provider, SchoolClassMember schoolClassMember, ArrayList conditions, PlacementTimes placementTimes, SchoolType schoolType) throws FinderException, PostingException, EJBException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		School school = schoolClassMember.getSchoolClass().getSchool();
		ArrayList oppenConditions = new ArrayList();
		oppenConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, FRITIDSKLUBB));
		Collection regulationForTypeArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(),
			PaymentFlowConstant.OUT,
			calculationDate,
			RuleTypeConstant.DERIVED,
			null, oppenConditions
		);
		if(regulationForTypeArray.size()!=1){
			errorRelated.append("Number of regulations found for fritidsklubb "+regulationForTypeArray.size());
			createNewErrorMessage(errorRelated, "invoice.NumberOfRegulationsForFritidsklubbNotCorrect");
		}
		for (Iterator i = regulationForTypeArray.iterator(); i.hasNext();) {
			try {
				Regulation regulation = (Regulation) i.next();
				PostingDetail postingDetail = regBus.getPostingDetailForPlacement(0.0f, schoolClassMember, regulation, calculationDate, conditions, placementTimes);
//				RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
				RegulationSpecType regSpecType = regulation.getRegSpecType();
				errorRelated.append("RegSpecType from regulation "+regulation.getRegSpecType(),1);
				errorRelated.append("RegSpecType from posting detail"+((RegulationSpecType)getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType())).getLocalizationKey(),1);
				if(!regulation.getRegSpecType().getLocalizationKey().equalsIgnoreCase(((RegulationSpecType)getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType())).getLocalizationKey())){
					createNewErrorMessage(errorRelated, "invoice.WarningConflictingRegSpecTypesGivenForFritidsKlubb");
				}
//				String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
				String[] postings =  getPostingBusiness().getPostingStrings(category, schoolType, ((Integer) regSpecType.getPrimaryKey()).intValue(), provider, calculationDate, ((Integer) schoolClassMember.getSchoolYear().getPrimaryKey()).intValue());
				
				PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
				errorRelated.append("created payment info for fritidsklubb " + schoolClassMember.getStudent().getName(),1);
				createInvoiceRecord(record, schoolClassMember, postingDetail, placementTimes);
			}
			catch (BruttoIncomeException e) {
				//Who cares!!!
			}
			catch (LowIncomeException e) {

			}
		}
	}

	private void createPaymentsForOppenVerksamhet(RegulationsBusiness regBus, Provider provider, SchoolClassMember schoolClassMember, ArrayList conditions, PlacementTimes placementTimes, SchoolType schoolType) throws FinderException, PostingException, EJBException, CreateException, RegulationException, MissingFlowTypeException, MissingConditionTypeException, MissingRegSpecTypeException, TooManyRegulationsException, RemoteException {
		ArrayList oppenConditions = new ArrayList();
		School school = schoolClassMember.getSchoolClass().getSchool();

		oppenConditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, OPPEN_VERKSAMHET));
		Collection regulationForTypeArray = regBus.getAllRegulationsByOperationFlowPeriodConditionTypeRegSpecType(category.getCategory(),
			PaymentFlowConstant.OUT, //The payment flow is out
			calculationDate, //Current date to select the correct date range
			RuleTypeConstant.DERIVED, //The conditiontype
			null, oppenConditions //The conditions that need to fulfilled
		);
		if(regulationForTypeArray.size()!=1){
			errorRelated.append("Number of regulations found for oppen verksamhet "+regulationForTypeArray.size());
			createNewErrorMessage(errorRelated, "invoice.NumberOfRegulationsForOppenVerksamhetNotCorrect");
		}
		for (Iterator i = regulationForTypeArray.iterator(); i.hasNext();) {
			try {
				Regulation regulation = (Regulation) i.next();
				PostingDetail postingDetail = regBus.getPostingDetailForPlacement(0.0f, schoolClassMember, regulation, calculationDate, conditions,placementTimes);
				RegulationSpecType regSpecType = getRegulationSpecTypeHome().findByRegulationSpecType(postingDetail.getRuleSpecType());
//				String[] postings = getPostingStrings(provider, schoolClassMember, regSpecType);
				String[] postings =  getPostingBusiness().getPostingStrings(category, schoolType, ((Integer) regSpecType.getPrimaryKey()).intValue(), provider, calculationDate, ((Integer) schoolClassMember.getSchoolYear().getPrimaryKey()).intValue());

				PaymentRecord record = createPaymentRecord(postingDetail, postings[0], postings[1], placementTimes.getMonths(), school);
				errorRelated.append("created payment info for Oppen verksamhet" + schoolClassMember.getStudent().getName(),1);
				createInvoiceRecord(record, schoolClassMember, postingDetail, placementTimes);
			}
			catch (BruttoIncomeException e) {
				//Who cares!!!
			}
			catch (LowIncomeException e) {

			}
		}
	}

	/**
	 * Used to create a description of every entity that is related
	 * 
	 * @param cat
	 * @param type
	 * @param sch
	 * @param child
	 * @return
	 */
	private String createRelatedString(SchoolCategory cat, SchoolType type, School sch, User child) {
		StringBuffer ret = new StringBuffer();
		if (cat != null) {
			ret.append(cat.getCategory());
		}
		if (type != null) {
			if (ret.length() != 0) {
				ret.append("<br>");
			}
			ret.append(type.getName());
		}
		if (sch != null) {
			if (ret.length() != 0) {
				ret.append("<br>");
			}
			ret.append(sch.getName());
		}
		if (child != null) {
			if (ret.length() != 0) {
				ret.append("<br>");
			}
			ret.append(child.getName());
		}
		return ret.toString();
	}

	/**
	 * Creates all the invoice headers, invoice records, payment headers and
	 * payment records for the Regular payments
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
				log.info("Looking at reg Payment " + regularPaymentEntry.getPrimaryKey());
				errorRelated = new ErrorLogger("RegularPaymentEntry ID " + regularPaymentEntry.getPrimaryKey());
				errorRelated.append("Placing " + regularPaymentEntry.getPlacing());
				errorRelated.append("Amount " + regularPaymentEntry.getAmount());
				errorRelated.append("School " + regularPaymentEntry.getSchool());
				postingDetail = new PostingDetail(regularPaymentEntry);
				school = regularPaymentEntry.getSchool();
				placementTimes = calculateTime(regularPaymentEntry.getFrom(), regularPaymentEntry.getTo());
				try {
					createPaymentRecord(postingDetail, regularPaymentEntry.getOwnPosting(), regularPaymentEntry.getDoublePosting(), placementTimes.getMonths(), school);
					log.info("Regular Payment" + errorRelated);
				}
				catch (IDOLookupException e) {
					createNewErrorMessage(errorRelated, "regularPayment.IDOLookup");
					e.printStackTrace();
				}
				catch (CreateException e) {
					createNewErrorMessage(errorRelated, "regularPayment.Create");
					e.printStackTrace();
				}
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
			if (postingDetail != null) {
				createNewErrorMessage(postingDetail.getTerm(), "payment.DBSetupProblem");
			}
			else {
				createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			}
		}
		catch (IDOLookupException e) {
			createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			e.printStackTrace();
		}
		catch (RemoteException e) {
			createNewErrorMessage("payment.severeError", "payment.DBSetupProblem");
			e.printStackTrace();
		}
	}

	/**
	 * Creates all the invoice headers, invoice records, payment headers and
	 * payment records for the Regular payments
	 */
	/*
	 * protected void regularPayment() { PostingDetail postingDetail = null; try {
	 * //Go through all the regular payments for (Iterator i =
	 * getRegularPayments().iterator(); i.hasNext();) { RegularPaymentEntry
	 * regularPaymentEntry = (RegularPaymentEntry) i.next(); postingDetail = new
	 * PostingDetail(regularPaymentEntry); PlacementTimes placementTimes =
	 * calculateTime(regularPaymentEntry.getFrom(), regularPaymentEntry.getTo());
	 * createPaymentRecord(postingDetail, regularPaymentEntry.getOwnPosting(),
	 * regularPaymentEntry.getDoublePosting(), placementTimes.getMonths(),
	 * regularPaymentEntry.getSchool()); } } catch (Exception e) {
	 * e.printStackTrace(); if (postingDetail != null) {
	 * createNewErrorMessage(postingDetail.getTerm(), "payment.DBSetupProblem"); }
	 * else { createNewErrorMessage("payment.severeError",
	 * "payment.DBSetupProblem"); } } }
	 */
	private ArrayList getConditions(SchoolClassMember schoolClassMember, Provider provider) {
		ArrayList conditions = new ArrayList();
		conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_OPERATION, schoolClassMember.getSchoolType().getLocalizationKey()));
		conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_SCHOOL_YEAR, schoolClassMember.getSchoolYear().getName()));
		conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_STADSBIDRAG, new Boolean(provider.getStateSubsidyGrant())));
		errorRelated.append("SchoolType " + schoolClassMember.getSchoolType().getName());
		errorRelated.append("School Year " + schoolClassMember.getSchoolYear().getName());
		errorRelated.append("StateSubsidyGrant " + provider.getStateSubsidyGrant());
		int studyPathId = schoolClassMember.getStudyPathId();
		if (studyPathId != -1) {
			conditions.add(new ConditionParameter(RuleTypeConstant.CONDITION_ID_STUDY_PATH, new Integer(studyPathId)));
			errorRelated.append("Study path " + schoolClassMember.getStudyPathId());
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
		return getSchoolClassMemberHome().findBySchool(((Integer) school.getPrimaryKey()).intValue(), -1, category.getCategory(), calculationDate);
	}

	private Collection getSchoolTypes(SchoolClassMember schoolClassMember) throws IDORelationshipException {
		return schoolClassMember.getSchoolClass().getSchool().getSchoolTypes();
	}

	private String[] getPostingStrings(Provider provider, SchoolClassMember schoolClassMember, RegulationSpecType regSpecType) throws PostingException, RemoteException, EJBException {
		return getPostingBusiness().getPostingStrings(category, schoolClassMember.getSchoolType(), ((Integer) regSpecType.getPrimaryKey()).intValue(), provider, calculationDate, ((Integer) schoolClassMember.getSchoolYear().getPrimaryKey()).intValue());
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
